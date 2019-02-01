package com.roulette.websocket;

import javax.websocket.Session;

import com.roulette.units.Play;
import com.roulette.units.Player;

public class WebSocketUtil {
	public static synchronized Player getPlayerBySession(Session session) {
		for (Player player : Play.getPlayers()) {
			if (player.getSession() != null) {
				if (player.getSession() == session) {
					return player;
				}
			}
		}
		return null;
	}
	public static synchronized Player getPlayerByNick(String nick) {
		for (Player player : Play.getPlayers()) {
			if (player.getNick() != null) {
				if (player.getNick().equals(nick)) {
					return player;
				}
			}
		}
		return null;
	}
}
