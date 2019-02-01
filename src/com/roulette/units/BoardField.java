package com.roulette.units;

public class BoardField {

	private int number;
	private EFiledColor color;
	private int row;
	private int col;

	private int half;
	private int doze;
	private boolean isOdd;

	public BoardField(int number, EFiledColor color) throws Exception {
		this.number = number;
		this.color = color;
		setRow();
		setCol();
		setDoze();
		setHalf();
		setIsOdd();
	}

	public void setIsOdd() {
		if (this.number % 2 == 0) {
			isOdd = false;
			return;
		}
		isOdd = true;
	}

	private int calulate(int threshold) throws Exception {
		int increment = 0;
		for (int i = 1; i <= 36; i++) {
			if (i == this.number) {
				return (++increment);
			}
			if ((i % threshold) == 0) {
				increment++;
			}
		}
		throw new Exception("Value not calculated");
	}

	public void setRow() throws Exception {
		this.row = calulate(3);
	}

	public void setDoze() throws Exception {
		this.doze = calulate(12);
	}

	public void setHalf() throws Exception {
		this.half = calulate(18);
	}

	public void setCol() throws Exception {
		int lv_col = 1;
		for (int i = 1; i <= 36; i++) {
			if (i == this.number) {
				this.col = lv_col;
				return;
			}
			lv_col++;
			if ((i % 3) == 0) {
				lv_col = 1;
			}
		}
		throw new Exception("Column not found");
	}

	public int getNumber() {
		return number;
	}

	public EFiledColor getColor() {
		return color;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getHalf() {
		return half;
	}

	public int getDoze() {
		return doze;
	}

	public boolean isOdd() {
		return isOdd;
	}

	@Override
	public String toString() {
		return "BoardField [number=" + number + ", color=" + color + ", row=" + row + ", col=" + col + ", half=" + half
				+ ", doze=" + doze + ", isOdd=" + isOdd + "]";
	}
}
