package com.roulette.units;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.roulette.utils.PlayCalculate;
import com.roulette.websocket.DeviceWebSocketServer;

public class Play {
	private static final int SHOUTTIME = 3 * 10 * 1000;// 30s
	private static final long IDLEMIN = 1;// 2 miniute of idle time
	private static Play instance;
	private static Board board;
	private static volatile BoardBall ball;

	private static List<Player> players = new ArrayList<Player>();
	private static List<Player>  splayers = Collections.synchronizedList(players);
	
	private ArrayList<Bet> bets = new ArrayList<Bet>();
	private Map<BoardField, ArrayList<Bet>> fieldBets = new HashMap<BoardField, ArrayList<Bet>>();
	private Map<Player, ArrayList<Bet>> playerBets = new HashMap<Player, ArrayList<Bet>>();
	

	public static Play getInstance() {
		if (instance == null) {
			instance = new Play();
		}
		return instance;
	}

	private Play() {
		Play.board = new Board();
		Play.ball = board.getBall();
		startManager();
	}

	private void startManager() {
		Runnable r = () -> {
			try {
				while (true) {
					if (ball != null) {
						ball.setState(EBallState.ROLL);
						// rolling tiem
						Random rand = new Random();
						int rollTime = rand.nextInt(5);
						Thread.sleep((int) (1000 * rollTime + 3000)); // at least 3s
						throwBall();
						calculateScore();
						disposeIdlePlayer();
						// info
						Logger.getLogger(DeviceWebSocketServer.class.getName()).log(Level.INFO, null,
								"Ball number " + ball.getDockNumber());
						//
					}
					Thread.sleep((int) (SHOUTTIME));
				}
			} catch (InterruptedException e) {
				Logger.getLogger(DeviceWebSocketServer.class.getName()).log(Level.SEVERE, "Play manager issue", e);
				// dispose instance
				instance = null;
			}
		};
		Thread t = new Thread(r);
		t.start();

	}

	private void disposeIdlePlayer() {
		LocalDateTime action;
		LocalDateTime now = LocalDateTime.now();
		for (Player player : getPlayers()) {
			action = player.getAction();
			if (action != null) {
				Duration time = Duration.between(action, now);
				if (time != null) {
					if (time.getSeconds() / 60 > IDLEMIN)
						userDispose(player);
				}
			}
		}
	}

	private void userDispose(Player player) {
		splayers.remove(player);
		playerBets.remove(player);
		player = null;
	}

	// only for testing purpose
	public Play(Board board) {
		Play.board = board;
	}

	public static void addPlayer(Player player) {
		player.setPlay(Play.getInstance());
		splayers.add(player);
	}

	public void removePlayer(Player player) {
		splayers.remove(player);
	}

	public synchronized void addBet(Player player, BoardField[] fields, EBetType betType, int value) throws Exception {
		Bet bet = new Bet(player, fields, betType, value);
		addBet(bet);
	}

	public synchronized void addBet(Bet bet) throws Exception {
		bets.add(bet);
		// add bets per field
		for (BoardField boardField : bet.getFields()) {
			if (fieldBets.get(boardField) == null) {
				fieldBets.put(boardField, new ArrayList<Bet>());
			}
			fieldBets.get(boardField).add(bet);
		}
		// add bets per player
		if (playerBets.get(bet.getPlayer()) == null) {
			playerBets.put(bet.getPlayer(), new ArrayList<Bet>());
		}
		playerBets.get(bet.getPlayer()).add(bet);
	}

	public synchronized void throwBall() {
		BoardBall ball = getBoard().getBall();
		ball.roll();
		// this.setBall(ball);
	}

	public synchronized void calculateScore() {
		PlayCalculate.calculateScore(this);
	}

	public Map<BoardField, ArrayList<Bet>> getFieldBets() {
		return fieldBets;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		Play.board = board;
	}

	public static List<Player> getPlayers() {
		return players;
	}

	public List<Bet> getBets() {
		return bets;
	}

	public void setBets(ArrayList<Bet> bets) {
		this.bets = bets;
	}

	public Map<Player, ArrayList<Bet>> getPlayerBets() {
		return playerBets;
	}

	public BoardBall getBall() {
		return ball;
	}

}