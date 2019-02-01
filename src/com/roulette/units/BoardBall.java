package com.roulette.units;

import java.time.LocalDateTime;
import java.util.Random;

import com.roulette.utils.RulletteConstant;

public class BoardBall {

	private volatile EBallState state;
	private int dockNumber = -1;
	private LocalDateTime timeOfStateChange;

	public BoardBall() {
		setState(EBallState.INIT);
	}

	public void roll() {
		Random rand = new Random();
		this.dockNumber = rand.nextInt(RulletteConstant.FILDNUMBER);
		setState(EBallState.DOCKED);
	}

	public EBallState getState() {
		return state;
	}

	public int getDockNumber() {
		return dockNumber;
	}

	public void setState(EBallState state) {
		setTimeOfStateChange(LocalDateTime.now());
		this.state = state;
		// if rolling set number to -1
		if (state.equals(EBallState.ROLL))
			this.dockNumber = -1;

	}

	public LocalDateTime getTimeOfStateChange() {
		return timeOfStateChange;
	}

	public void setTimeOfStateChange(LocalDateTime timeOfStateChange) {
		this.timeOfStateChange = timeOfStateChange;
	}

}
