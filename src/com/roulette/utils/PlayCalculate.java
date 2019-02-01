package com.roulette.utils;

import java.util.ArrayList;
import java.util.Arrays;

import com.roulette.units.Bet;
import com.roulette.units.BoardBall;
import com.roulette.units.BoardField;
import com.roulette.units.EBallState;
import com.roulette.units.EBetType;
import com.roulette.units.Play;
import com.roulette.units.Player;

public class PlayCalculate {

	public static void calculateScore(Play play) {
		int ballDock = -1;
		BoardBall ball = play.getBoard().getBall();
		// verify state
		if (ball.getState() != EBallState.DOCKED || ball.getDockNumber() == -1) {
			throw new RuntimeException("Incorrect state of ball");
		} else {
			ballDock = ball.getDockNumber();
		}
		// for each playeer
		for (Player player : Play.getPlayers()) {
			// for each bets of player
			ArrayList<Bet> bets = play.getPlayerBets().get(player);
			if (bets != null && bets.size() > 0) {
				for (Bet bet : bets) {
					// check each fields
					ArrayList<BoardField> filds = new ArrayList<BoardField>(Arrays.asList(bet.getFields()));
					for (BoardField boardField : filds) {
						if (boardField.getNumber() == ballDock) {
							int winning = calculateWinning(bet.getValue(), bet.getBetType());
							player.increaseSaldo(winning);
							// turn back suspended bet value
							player.increaseSaldo(bet.getValue());
							// go to next bet
							break;
						}
					}
				}
				// clear bets
				play.getPlayerBets().get(player).clear();
			}
		}

	}

	private static int calculateWinning(int value, EBetType betType) {
		return betType.getMultiplier() * value + value;
	}
}
