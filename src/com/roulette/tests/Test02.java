package com.roulette.tests;

import java.util.Arrays;
import java.util.List;

import com.roulette.units.Board;
import com.roulette.units.BoardField;
import com.roulette.units.EBetType;
import com.roulette.units.EFiledColor;
import com.roulette.units.Play;
import com.roulette.units.Player;
import com.roulette.utils.PlayPrinter;

public class Test02 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Board board = new Board();
		try {
			List<Integer> myRed = Arrays.asList(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36);
			EFiledColor color;
			for (int i = 1; i <= 36; i++) {
				if (myRed.contains(i)) {
					color = EFiledColor.RED;
				} else {
					color = EFiledColor.BLACK;
				}
				board.addFields(new BoardField(i, color));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// print board
		for (BoardField field : board.getFields()) {
			// System.out.println(field.toString());
		}
		// add users
		Player p1 = new Player("Joe", 1000);
		Player p2 = new Player("Steve", 1500);
		// generate play
		Play play = new Play(board);
		//
		Play.addPlayer(p1);
		Play.addPlayer(p2);
		// prepare bets;
		try {
			play.addBet(p1, getField(board, 1,2), EBetType.SPLIT, 10);
			play.addBet(p1, getField(board, 1,2,3), EBetType.STREET, 20);
			play.addBet(p2, getField(board, 6,9), EBetType.SPLIT, 35);
			play.addBet(p2, getField(board, 7,8), EBetType.SPLIT, 45);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		PlayPrinter printer = new PlayPrinter();
		printer.printPlayState(play);
		//
		System.out.println("\n\nBefore:\n");
		printer.printUserSaldo(play);
		//
		play.throwBall();
		System.out.println("\nThrowing ball ... (" + play.getBoard().getBall().getDockNumber() + ")\n");
		play.calculateScore();
		//
		System.out.println("After:\n");
		printer.printUserSaldo(play);
	}

	@SuppressWarnings("unused")
	public static BoardField[] getField(Board board, int... numbers) {
		int len = 0;
		for (int n : numbers)
			len += 1;

		BoardField[] boardFields = new BoardField[len];
		int i = 0;
		for (int n : numbers)
			boardFields[i++] = board.getFieldById(n);

		return boardFields;
	}
}
