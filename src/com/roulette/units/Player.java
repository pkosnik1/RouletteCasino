package com.roulette.units;

import java.time.LocalDateTime;

import javax.websocket.Session;

public class Player {

	private String nick;
	private int budgete;
	private Session session;
	private Play play;
	private String key;
	private LocalDateTime action;

	public Player() {
		this.play = Play.getInstance();
		action();
	}

	public Player(String nick, int budgete) {
		this();
		this.nick = nick;
		this.budgete = budgete;		
	}

	public Player(String nick, int budgete, Session session) {
		this(nick, budgete);
		setSession(session);
	}

	public void setNick(String nick) {
		this.nick = nick;
		action();
	}

	public void setBudgete(int budgete) {
		this.budgete = budgete;
		action();
	}

	public String getNick() {
		return nick;
	}

	public int getBudgete() {
		return budgete;
	}

	public void increaseSaldo(int winning) {
		this.budgete += winning;
		action();
	}

	public synchronized void setSession(Session session) {
		this.session = session;
		action();
	}

	public Session getSession() {
		return this.session;
	}

	public void setPlay(Play play) {
		this.play = play;
		action();
	}

	public Play getPlay() {
		return this.play;
	}

	public void setKey(String key) {
		this.key = key;
		action();
	}

	public String getKey() {
		return this.key;

	}

	public void action() {
		this.action = LocalDateTime.now();
	}

	public LocalDateTime getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return "Player [nick=" + nick + ", budgete=" + budgete + "]";
	}
}
