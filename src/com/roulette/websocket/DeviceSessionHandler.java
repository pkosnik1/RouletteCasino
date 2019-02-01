package com.roulette.websocket;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;

import com.roulette.units.Bet;
import com.roulette.units.BoardBall;
import com.roulette.units.BoardField;
import com.roulette.units.Play;
import com.roulette.units.Player;

@ApplicationScoped
public class DeviceSessionHandler {
	private static final int PLAYINFOTIME = 1000;
	private final Set<Session> sessions = new HashSet<>();
	private Thread playStateControler = null;

	public void addSession(Session session) {
		sessions.add(session);
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	public void addPlayer(Player player) {
		Play.addPlayer(player);
		//
		JsonObject addPlayerMessage = createAddPlayerMessage(player);
		sendToAllConnectedSessions(addPlayerMessage);
		//
		publishPlayers();
		// run controller
		runController();
	}

	private void runController() {
		if (playStateControler != null)
			if (!playStateControler.isAlive())
				playStateControler = null;
		if (playStateControler == null) {
			Runnable r = () -> {
				try {
					while (true) {
						// clearIdlePlayer();
						sendStateInfo();
						publishPlayers();
						publishPlayersBets();
						Thread.sleep((int) (PLAYINFOTIME));
					}
				} catch (InterruptedException e) {
				}
			};
			playStateControler = new Thread(r);
			playStateControler.start();
		}

	}

	private void publishPlayersBets() {
		for (Player player : Play.getPlayers()) {
			sendPlayerBets(player);
		}
	}

	private void sendStateInfo() {
		JsonObject stateMessage = createStateOfPlayMessage();
		sendToAllConnectedSessions(stateMessage);
	}

	private JsonObject createStateOfPlayMessage() {
		BoardBall ball = Play.getInstance().getBall();
		if (ball == null)
			return null;
		LocalDateTime now = LocalDateTime.now();
		long time = Duration.between(ball.getTimeOfStateChange(), now).getSeconds();
		JsonProvider provider = JsonProvider.provider();
		JsonObject addMessage = provider.createObjectBuilder().add("action", "stateInfo")
				.add("ballState", ball.getState().toString()).add("ballNumber", ball.getDockNumber())
				.add("ballTime", time).build();
		return addMessage;
	}

	private JsonObject createAddPlayerMessage(Player player) {
		JsonProvider provider = JsonProvider.provider();
		JsonObject addMessage = provider.createObjectBuilder().add("action", "addPlayer").add("nick", player.getNick())
				.add("budget", player.getBudgete()).build();
		return addMessage;
	}

	public void publishPlayers() {
		JsonObject playersMessage = createListOfPlayerMessage();
		sendToAllConnectedSessions(playersMessage);
	}

	private JsonObject createListOfPlayerMessage() {
		JsonProvider provider = JsonProvider.provider();
		JsonObjectBuilder jsonObjectBuilder = provider.createObjectBuilder();
		// add action
		jsonObjectBuilder.add("action", "playerList");
		// create array
		JsonArrayBuilder arrBuilder = provider.createArrayBuilder();
		for (Player player : Play.getPlayers()) {
			JsonObject playerJson = provider.createObjectBuilder().add("nick", player.getNick())
					.add("budget", player.getBudgete()).build();
			// add player to array
			arrBuilder.add(playerJson);
		}
		// put array to json
		jsonObjectBuilder.add("players", arrBuilder);
		//
		return jsonObjectBuilder.build();
	}

	public void sendPlayerBets(Player player) {
		JsonObject betsMessage = createListOfBetMessage(player);
		sendToConnectedSessions(player.getSession(), betsMessage);
	}

	public JsonObject createListOfBetMessage(Player player) {
		// return all list of bets
		JsonProvider provider = JsonProvider.provider();
		JsonObjectBuilder jsonObjectBuilder = provider.createObjectBuilder();
		// add action
		jsonObjectBuilder.add("action", "betList");
		// create array
		Play play = player.getPlay();
		JsonArrayBuilder arrBuilder = provider.createArrayBuilder();
		ArrayList<Bet> bets = play.getPlayerBets().get(player);
		if (bets != null && bets.size() > 0) {
			for (Bet b : bets) {
				// fields arr
				JsonArrayBuilder arrFields = provider.createArrayBuilder();
				for (BoardField bf : b.getFields()) {
					JsonObject fieldJson = provider.createObjectBuilder().add("field", bf.getNumber()).build();
					arrFields.add(fieldJson);
				}
				JsonObject betJson = provider.createObjectBuilder().add("type", b.getBetType().toString())
						.add("fields", arrFields).add("value", b.getValue()).build();
				// add player to array
				arrBuilder.add(betJson);
			}
		}
		// put array to json
		jsonObjectBuilder.add("bets", arrBuilder);
		//
		return jsonObjectBuilder.build();

	}

	private JsonObject createErrMessage(String message) {
		JsonProvider provider = JsonProvider.provider();
		JsonObject addMessage = provider.createObjectBuilder().add("action", "addMessage").add("message", message)
				.build();
		return addMessage;
	}

	public void errMessage(Session session, String string) {
		JsonObject errMessage = createErrMessage(string);
		sendToConnectedSessions(session, errMessage);
		Logger.getLogger(DeviceSessionHandler.class.getName()).log(Level.SEVERE, null, string);
	}

	private void sendToAllConnectedSessions(JsonObject message) {
		if (message == null)
			return;
		for (Session session : sessions) {
			sendToSession(session, message);
		}
	}

	private void sendToConnectedSessions(Session session, JsonObject message) {
		for (Session s : sessions) {
			if (s.equals(session))
				sendToSession(s, message);
		}

	}

	private void sendToSession(Session session, JsonObject message) {
		try {
			session.getBasicRemote().sendText(message.toString());
		} catch (IOException ex) {
			sessions.remove(session);
			Logger.getLogger(DeviceSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}