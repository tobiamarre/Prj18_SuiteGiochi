package sudoku.model;

import java.util.ArrayList;

public class SudokuPuzzle {
	private SudokuModel problem;
	private SudokuModel solution;
	private ArrayList<Integer> fixedCells;
	
	public SudokuPuzzle(SudokuModel problem, SudokuModel solution) {
		this.problem = problem;
		this.solution = solution;
		this.fixedCells = new ArrayList<>();
		for (int cell = 0; cell < 81; cell++) {
			if (problem.getMatrice()[cell] != 0) {
				fixedCells.add(cell);
			}
		}
	}
	
	public SudokuModel getProblem() {
		return problem;
	}
	public SudokuModel getSolution() {
		return solution;
	}	
	
}
