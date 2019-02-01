package com.roulette.units;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
	private List<BoardField> fields = new ArrayList<BoardField>();
	private BoardBall ball;

	public BoardBall getBall() {
		return ball;
	}

	public Board() {
		this.ball = new BoardBall();
		init();
	}

	private void init() {
		List<Integer> myRed = Arrays.asList(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36);
		EFiledColor color;
		for (int i = 1; i <= 36; i++) {
			if (myRed.contains(i)) {
				color = EFiledColor.RED;
			} else {
				color = EFiledColor.BLACK;
			}
			try {
				addFields(new BoardField(i, color));
			} catch (Exception e) {
				// logger to add
				e.printStackTrace();
			}
		}
	}

	public void addFields(BoardField field) {
		fields.add(field);
	}

	public List<BoardField> getFields() {
		return fields;
	}

	public BoardField getFieldByRowPosition(int row, int column) {
		for (BoardField boardField : fields) {
			if (boardField.getRow() == row && boardField.getCol() == column)
				return boardField;
		}
		throw new RuntimeException("There is not fields [" + row + "," + column + "]");
	}

	public BoardField getFieldById(int fieldId) {
		for (BoardField boardField : fields) {
			if (boardField.getNumber() == fieldId)
				return boardField;
		}
		throw new RuntimeException("There is not fields [" + fieldId + "]");
	}

	@SuppressWarnings("unused")
	public BoardField[] getField(int... numbers) {
		int len = 0;
		for (int n : numbers)
			len += 1;

		BoardField[] boardFields = new BoardField[len];
		int i = 0;
		for (int n : numbers)
			boardFields[i++] = getFieldById(n);

		return boardFields;
	}
}
