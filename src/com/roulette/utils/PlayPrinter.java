package com.roulette.utils;

import java.util.ArrayList;

import com.roulette.units.Bet;
import com.roulette.units.Board;
import com.roulette.units.BoardField;
import com.roulette.units.Play;
import com.roulette.units.Player;

public class PlayPrinter {
	private static final int ROWPIX = 5 * 12;
	private static final int COlPIX = 10 * 3;
	private char[][] print = new char[ROWPIX][COlPIX];

	public void printUserSaldo(Play play) {
		for (Player player : Play.getPlayers()) {
			System.out.println(player.toString());
		}

	}

	public void printPlayState(Play play) {
		Board board = play.getBoard();
		// print board
		for (int i = 1; i <= 12; i++) {
			for (int j = 1; j <= 3; j++) {
				BoardField field = board.getFieldByRowPosition(i, j);
				// System.out.printf("%d , %d %n", i, j);
				new Cell(i, j, field, play.getFieldBets().get(field));
			}

		}
		for (int i = 0; i < ROWPIX; i++) {
			System.out.print("\n");
			for (int j = 0; j < COlPIX; j++) {
				System.out.print(print[i][j]);
			}
		}
	}

	class Cell {
		int number;
		ArrayList<Bet> bets;

		public Cell(int r, int c, BoardField field, ArrayList<Bet> bets) {
			this.number = field.getNumber();
			this.bets = bets;
			int row = r * 5 - 5;
			int col = c * 10 - 10;
			addLine(row, col, 0);// up
			addLine(row, col, 4);// down
			addCol(row, col, 0);// left
			addCol(row, col, 9);// right
			//
			char[] chars;
			int i = 0;
			// show field number
			chars = ("" + this.number).toCharArray();
			for (char d : chars) {
				print[row + 1][col + (++i)] = d;
				if (i > 3)
					return;
			}
			// show bets
			int sum = 0;
			if (this.bets != null) {
				for (Bet b : this.bets) {
					sum += b.getValue();
				}
				chars = ("" + sum).toCharArray();
				for (char d : chars) {
					print[row + 3][col + (++i)] = d;
					if (i > 9)
						return;
				}
			}
		}

		void addLine(int row, int col, int offset) {
			for (int i = 0; i < 10; i++) {
				print[row + offset][col + i] = '-';
			}
		}

		void addCol(int row, int col, int offset) {
			for (int i = 0; i < 5; i++) {
				print[row + i][col + offset] = '|';
			}
		}

	}

}
