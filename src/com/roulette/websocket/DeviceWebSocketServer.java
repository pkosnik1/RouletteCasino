package com.roulette.websocket;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.roulette.units.Bet;
import com.roulette.units.EBetType;
import com.roulette.units.Player;

@ApplicationScoped
@ServerEndpoint("/actions")
public class DeviceWebSocketServer {
	@Inject
	private DeviceSessionHandler sessionHandler;

	@OnOpen
	public void open(Session session) {
		sessionHandler.addSession(session);
	}

	@OnClose
	public void close(Session session) {
		sessionHandler.removeSession(session);
	}

	@OnError
	public void onError(Throwable error) {
		Logger.getLogger(DeviceWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
			JsonObject jsonMessage = reader.readObject();

			if ("addPlayer".equals(jsonMessage.getString("action"))) {
				Player player = WebSocketUtil.getPlayerBySession(session);
				// fetch json
				String nick = jsonMessage.getString("nick");
				String key = jsonMessage.getString("key");
				String budget = jsonMessage.getString("budget");
				//
				if (nick == null)
					sessionHandler.errMessage(session, "Player nick cannot be null");
				if (key == null)
					sessionHandler.errMessage(session, "Player key cannot be null");
				if (budget == null)
					sessionHandler.errMessage(session, "Player budget cannot be null");
				//
				boolean isNew = false;
				boolean isUsed = false;
				//
				// check if any other session has the same nick
				Player playerToCompare = WebSocketUtil.getPlayerByNick(nick);
				if (playerToCompare != null) {
					isUsed = true; // nick is already used by someone
					if (playerToCompare.getSession().equals(session)) {
						isUsed = false; // it is my session so reset status
					}
				}
				// if palyer has not been found
				if (player == null) {
					// create new player
					if (!isUsed) {
						player = new Player();
						isNew = true;
					} else {
						// fetch player by pair nick and key
						if (playerToCompare != null && playerToCompare.getKey().equals(key)) {
							player = WebSocketUtil.getPlayerByNick(nick);
						} else {
							sessionHandler.errMessage(session, "Nick is already used");
							return;
						}
					}
				}
				//
				if (player != null) {
					player.setNick(nick);
					player.setBudgete(Integer.parseInt(budget));
					player.setSession(session);
					player.setKey(key);
					if (isNew)
						sessionHandler.addPlayer(player);
				} else {
					sessionHandler.errMessage(session, "Player has not been found");
				}
			}

			if ("addBet".equals(jsonMessage.getString("action"))) {
				Player player = WebSocketUtil.getPlayerBySession(session);
				if (player != null) {
					try {
						int[] params = new int[2];
						int cnt = 0;
						String fieldsString = jsonMessage.getString("fields");
						if (!fieldsString.isEmpty() && fieldsString != null) {
							String[] fieldsList = fieldsString.split("#");
							for (String s : fieldsList) {
								int fieldInt = Integer.parseInt(s);
								params[cnt++] = fieldInt;
							}
						}
						EBetType betType = EBetType.valueOf(jsonMessage.getString("type"));
						int betValue = Integer.parseInt(jsonMessage.getString("value"));
						// create new bet
						new Bet(player, params, betType, betValue);
						// publish bets
						sessionHandler.publishPlayers();
					} catch (Exception e) {
						sessionHandler.errMessage(session, "Error " + e.getMessage());
					}
				} else {
					sessionHandler.errMessage(session, "Player has not been defined yet or session is ended");
				}

			}
		}
	}
}