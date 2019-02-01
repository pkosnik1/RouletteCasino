package com.roulette.utils;

import com.roulette.units.Board;
import com.roulette.units.BoardField;
import com.roulette.units.EBetType;
import com.roulette.units.EFiledColor;
import com.roulette.units.Player;

public class FieldsGetter {

	public static BoardField[] getFields(Board board, EBetType betType, int param1, int param2) {
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

		BoardField[] fields = null;
		int k = 0;
		switch (betType) {
		case STRIGHTUP:
			fields = new BoardField[1];
			fields[0] = board.getFieldById(param1);
			break;
		case SPLIT:
			fields = new BoardField[2];
			fields[0] = board.getFieldById(param1);
			fields[1] = board.getFieldById(param1 + param1 + param2);
			break;
		case STREET:
			fields = new BoardField[3];
			fields[0] = board.getFieldById(--param1 * 3 + 1);
			fields[1] = board.getFieldById(--param1 * 3 + 2);
			fields[2] = board.getFieldById(--param1 * 3 + 3);
			break;
		case CORNER:
			fields = new BoardField[4];
			fields[0] = board.getFieldById(--param1 * 3 + (1 + --param2));
			fields[1] = board.getFieldById(--param1 * 3 + (2 + --param2));
			fields[2] = board.getFieldById(param1 * 3 + (1 + --param2));
			fields[3] = board.getFieldById(param1 * 3 + (2 + --param2));
			break;
		case FIVE:
			fields = new BoardField[5];
			fields[0] = board.getFieldById(1);
			fields[1] = board.getFieldById(2);
			fields[2] = board.getFieldById(3);
			fields[3] = board.getFieldById(4);
			fields[4] = board.getFieldById(5);
			break;
		case SIXLINE:
			fields = new BoardField[6];
			if (param1 == 0 || param2 != 0)
				throw new RuntimeException("Some parameter configuration issue");
			--param1;
			fields[0] = board.getFieldById(param1 * 3 + 1);
			fields[1] = board.getFieldById(param1 * 3 + 2);
			fields[2] = board.getFieldById(param1 * 3 + 3);
			++param1;
			fields[3] = board.getFieldById(param1 * 3 + 1);
			fields[4] = board.getFieldById(param1 * 3 + 2);
			fields[5] = board.getFieldById(param1 * 3 + 3);
			break;
		case COLUMN1:
			fields = new BoardField[12];
			for (int i = 0; i < 12; i++)
				fields[i] = board.getFieldById((i) * 3 + 1);
			break;
		case COLUMN2:
			fields = new BoardField[12];
			for (int i = 0; i < 12; i++)
				fields[i] = board.getFieldById((i) * 3 + 2);
			break;
		case COLUMN3:
			fields = new BoardField[12];
			for (int i = 0; i < 12; i++)
				fields[i] = board.getFieldById((i) * 3 + 3);
			break;
		case DOZEN1:
			fields = new BoardField[12];
			for (int i = 0; i < 12; i++)
				fields[i] = board.getFieldById(0 + i + 1);
			break;
		case DOZEN2:
			fields = new BoardField[12];
			for (int i = 0; i < 12; i++)
				fields[i] = board.getFieldById(12 + i + 1);
			break;
		case DOZEN3:
			fields = new BoardField[12];
			for (int i = 0; i < 12; i++)
				fields[i] = board.getFieldById(24 + i + 1);
			break;
		case EVEN:
			fields = new BoardField[18];
			k = 0;
			for (int i = 0; i < 36; i++) {
				if (i % 2 == 0)
					fields[k++] = board.getFieldById(i + 1);
			}
			break;
		case ODD:
			fields = new BoardField[18];
			k = 0;
			for (int i = 0; i < 36; i++) {
				if (i % 2 != 0)
					fields[k++] = board.getFieldById(i + 1);
			}
			break;
		case HALF1:
			fields = new BoardField[18];
			for (int i = 0; i < 18; i++)
				fields[i] = board.getFieldById(i + 1);
			break;
		case HALF2:
			fields = new BoardField[18];
			for (int i = 18; i < 36; i++)
				fields[i - 18] = board.getFieldById(i + 1);
			break;
		case RED:
			fields = new BoardField[18];
			k = 0;
			for (int i = 0; i < 36; i++) {
				if (board.getFieldById(i + 1).getColor() == EFiledColor.RED)
					fields[k++] = board.getFieldById(i + 1);
			}
			break;
		case BLACK:
			fields = new BoardField[18];
			k = 0;
			for (int i = 0; i < 36; i++) {
				if (board.getFieldById(i + 1).getColor() == EFiledColor.BLACK)
					fields[k++] = board.getFieldById(i + 1);

			}
			break;
		default:
			break;
		}
		return fields;

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

	public static BoardField[] getFields(Player player, EBetType betType, int[] params) {
		Board board = player.getPlay().getBoard();
		switch (betType) {
		case STRIGHTUP:
			return FieldsGetter.getFields(board, EBetType.STRIGHTUP, params[0], params[1]);
		case SPLIT:
			return FieldsGetter.getFields(board, EBetType.SPLIT, params[0], params[1]);
		case STREET:
			return FieldsGetter.getFields(board, EBetType.STREET, params[0], params[1]);
		case CORNER:
			return FieldsGetter.getFields(board, EBetType.CORNER, params[0], params[1]);
		case FIVE:
			return FieldsGetter.getFields(board, EBetType.FIVE, params[0], params[1]);
		case SIXLINE:
			return FieldsGetter.getFields(board, EBetType.SIXLINE, params[0], params[1]);
		case COLUMN1:
			return FieldsGetter.getFields(board, EBetType.COLUMN1, 0, 0);
		case COLUMN2:
			return FieldsGetter.getFields(board, EBetType.COLUMN2, 0, 0);
		case COLUMN3:
			return FieldsGetter.getFields(board, EBetType.COLUMN3, 0, 0);
		case DOZEN1:
			return FieldsGetter.getFields(board, EBetType.DOZEN1, 0, 0);
		case DOZEN2:
			return FieldsGetter.getFields(board, EBetType.DOZEN2, 0, 0);
		case DOZEN3:
			return FieldsGetter.getFields(board, EBetType.DOZEN3, 0, 0);
		case EVEN:
			return FieldsGetter.getFields(board, EBetType.EVEN, 0, 0);
		case ODD:
			return FieldsGetter.getFields(board, EBetType.ODD, 0, 0);
		case BLACK:
			return FieldsGetter.getFields(board, EBetType.BLACK, 0, 0);
		case RED:
			return FieldsGetter.getFields(board, EBetType.RED, 0, 0);
		case HALF2:
			return FieldsGetter.getFields(board, EBetType.HALF2, 0, 0);
		case HALF1:
			return FieldsGetter.getFields(board, EBetType.HALF1, 0, 0);
		default:
			return null;
		}
	}
}
