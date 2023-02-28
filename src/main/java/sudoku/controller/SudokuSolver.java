package sudoku.controller;

import sudoku.model.SudokuModel;

public abstract class SudokuSolver {
	
	SudokuModel problem;
	
	public abstract void setProblem(SudokuModel newProblem);
	
	public abstract boolean hasSolution();
	
	public abstract SudokuModel getSolution();
	
}
