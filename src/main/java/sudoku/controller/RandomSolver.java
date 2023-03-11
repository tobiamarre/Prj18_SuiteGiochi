package sudoku.controller;

import java.util.Random;

public class RandomSolver extends LexicographicSolver {

	Random r = new Random();

	@Override
	int unValueAmmissibile(int annotazione) {
		int randomShift = r.nextInt(9);
		int value = annotazione * 0b1000000001 >> randomShift;
		value = value+1 & ~value;

		return ((value << randomShift) * 0b1000000001 >> 9) & 511;
	}



	
	
}
