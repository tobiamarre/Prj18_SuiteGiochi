package sudoku.controller;

import java.util.ArrayList;
import java.util.Collections;

import sudoku.model.SudokuModel;
import sudoku.model.SudokuPuzzle;

public class SudokuGenerator {

		
	public void generateProblem() {
		solution = new RandomSolver().getSolution();
		problem = solution.clone();
		
		// remove sevens
		for (int i = 0; i < 81; i++) {
			if (problem.getMatrix()[i] == 7) {
				problem.getMatrix()[i] = 0;
			}
		}
		
		SudokuSolver ls = new LexicographicSolver();
		SudokuSolver ils = new AntiLexicographicSolver();
		
		ArrayList<Integer> filledCells = new ArrayList<>();
		for (int i = 0; i < 81; i++) {
			if (problem.getMatrix()[i] != 0) {
				filledCells.add(i);
			}
		}
		
		boolean continua = true;
		while (continua) {
			continua = false;
			Collections.shuffle(filledCells);
			for (int i = 0; i < filledCells.size(); i++) {
				int cell = filledCells.get(i);
				int cellValue = problem.getMatrix()[cell];
				problem.getMatrix()[cell] = 0;
				
//				System.out.println("-----------------------------\ncontrollo se questo ha una sol unica:");
//				System.out.println(problem);
				
				ls.setProblem(problem);
				ils.setProblem(problem);
				
//				System.out.println("ls dice \n" + ls.getSolution());
//				System.out.println("ils dice \n" + ils.getSolution());
				
				if (ls.getSolution().equals(ils.getSolution())) {
					filledCells.remove(i);
					continua = true;
					break;
				}
				problem.getMatrix()[cell] = cellValue;
				
			}
		}
		
		
	}

	
	
	SudokuModel solution;
	SudokuModel problem;

	public SudokuPuzzle getPuzzle() {
		if (problem == null) {
			generateProblem();
		}

		return new SudokuPuzzle(problem, solution);
	}
	
	
}


