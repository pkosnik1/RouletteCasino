package com.roulette.units;

// Straight Up, numer pojedynczy – 35:1
// Split Bet, dwa numery – 17:1
// Street Bet, trzy numery – 11:1
// Corner Bet, cztery numery – 8:1
// Five Bet, pierwsze piêæ numerów (0, 00, 1, 2, 3) – 6:1
// Six Line Bet, linia – 5:1
// Column Bet, kolumna – 2:1
// Dozen Bet, tuziny: 1 – 12, 13 – 24, 25 – 36 – 2:1
// Even, Odd, parzyste, nieparzyste – 1:1
// 1st 18, 2nd 18, numery 1 – 18 i 19 – 36 – 1:1
// Red, Black – czerwone, czarne – 1:1
public enum EBetType {
	STRIGHTUP(35, 1), //
	SPLIT(17, 2), // to calculate
	STREET(11, 3), //
	CORNER(8, 4), //
	FIVE(6, 5), //
	SIXLINE(5, 6), //
	COLUMN1(2, 12), COLUMN2(2, 12), COLUMN3(2, 12), //
	DOZEN1(2, 12), DOZEN2(2, 12), DOZEN3(2, 12), //
	EVEN(1, 18), ODD(1, 18), //
	HALF1(1, 18), HALF2(1, 18), //
	BLACK(1, 18), RED(1, 18);

	private final int multiplier; // multiplicator
	private final int fieldsCount; // multiplicator

	EBetType(int multiplier, int fieldsCount) {
		this.multiplier = multiplier;
		this.fieldsCount = fieldsCount;
	}

	public int getMultiplier() {
		return this.multiplier;
	}

	public int getFieldsCount() {
		return this.fieldsCount;
	}
}
