package com.roulette.tests;

import java.util.Arrays;
import java.util.List;

import com.roulette.units.Board;
import com.roulette.units.BoardField;
import com.roulette.units.EBetType;
import com.roulette.units.EFiledColor;
import com.roulette.units.Play;
import com.roulette.utils.FieldsGetter;
import com.roulette.utils.PlayPrinter;

public class Test03 {

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

		// generate play
		Play play = new Play(board);
		PlayPrinter printer = new PlayPrinter();
		printer.printPlayState(play);
		// STRIGHTUP(35, 1), //
		// SPLIT(17, 2), // to calculate
		// STREET(11, 3), //
		// CORNER(8, 4), //
		// FIVE(6, 5), //
		// SIXLINE(5, 6), //
		// COLUMN1(2, 12), COLUMN2(2, 12), COLUMN3(2, 12), //
		// DOZEN1(2, 12), DOZEN2(2, 12), DOZEN3(2, 12), //
		// EVEN(1, 18), ODD(1, 18), //
		// HALF1(1, 18), HALF2(1, 18), //
		// RED(1, 18), BLUE(1, 18);
		BoardField[] fields;
		System.out.println();
		System.out.println("SIXLINE1");
		fields = FieldsGetter.getFields(board, EBetType.SIXLINE, 1, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println("SIXLINE2");
		fields = FieldsGetter.getFields(board, EBetType.SIXLINE, 2, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println("SIXLINE3");
		fields = FieldsGetter.getFields(board, EBetType.SIXLINE, 3, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println();
		System.out.println("COLUMN1");
		fields = FieldsGetter.getFields(board, EBetType.COLUMN1, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println();
		System.out.println("COLUMN2");
		fields = FieldsGetter.getFields(board, EBetType.COLUMN2, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println();
		System.out.println("COLUMN3");
		fields = FieldsGetter.getFields(board, EBetType.COLUMN3, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println();
		System.out.println("DOZEN1");
		fields = FieldsGetter.getFields(board, EBetType.DOZEN1, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println();
		System.out.println("DOZEN2");
		fields = FieldsGetter.getFields(board, EBetType.DOZEN2, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println();
		System.out.println("DOZEN3");
		fields = FieldsGetter.getFields(board, EBetType.DOZEN3, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println();
		System.out.println("EVEN");
		fields = FieldsGetter.getFields(board, EBetType.EVEN, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println();
		System.out.println("ODD");
		fields = FieldsGetter.getFields(board, EBetType.ODD, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println();
		System.out.println("BLACK");
		fields = FieldsGetter.getFields(board, EBetType.BLACK, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println("RED");
		fields = FieldsGetter.getFields(board, EBetType.RED, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println();
		System.out.println("Half2");
		fields = FieldsGetter.getFields(board, EBetType.HALF2, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}
		System.out.println("Half1");
		fields = FieldsGetter.getFields(board, EBetType.HALF1, 0, 0);
		for (BoardField boardField : fields) {
			System.out.println(boardField.toString());
		}

	}
}
