package sudoku.controller;

import java.util.Random;

import sudoku.model.SimpleStack;
import sudoku.model.SudokuModel;

public class RandomSolver extends LexicographicSolver {

	Random r = new Random();
	
	public RandomSolver(SudokuModel problem) {
		this.problem = problem;
	}
	
	public RandomSolver() {
		this.problem = new SudokuModel();
	}

	@Override
	int unValueAmmissibile(int annotazione) {
		int randomShift = r.nextInt(9);
		int value = annotazione * 0b1000000001 >> randomShift;
		value = value+1 & ~value & (0x3ffff >> randomShift);
		value <<= randomShift;
		
		if (value > 256) {
			value >>= 9;
		}	
		return value;
	}



	
	
}
