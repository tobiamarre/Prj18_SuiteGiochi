package sudoku.controller;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.apache.tomcat.util.digester.ArrayStack;
import org.eclipse.jdt.internal.compiler.lookup.ProblemBinding;

import sudoku.model.SudokuModel;
import sudoku.model.SudokuPuzzle;

public class SudokuGenerator {

		
	public void generateProblem() {
		solution = new RandomSolver().getSolution();
		problem = solution.clone();
		
		// remove sevens
		for (int i = 0; i < 81; i++) {
			if (problem.getMatrice()[i] == 7) {
				problem.getMatrice()[i] = 0;
			}
		}
		
		SudokuSolver ls = new LexicographicSolver();
		SudokuSolver ils = new InverseLexicographicSolver();
		
		ArrayList<Integer> filledCells = new ArrayList<>();
		for (int i = 0; i < 81; i++) {
			if (problem.getMatrice()[i] != 0) {
				filledCells.add(i);
			}
		}
		
		boolean continua = true;
		while (continua) {
			continua = false;
			Collections.shuffle(filledCells);
			for (int i = 0; i < filledCells.size(); i++) {
				int cell = filledCells.get(i);
				int cellValue = problem.getMatrice()[cell];
				problem.getMatrice()[cell] = 0;
				
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
				problem.getMatrice()[cell] = cellValue;
				
			}
		}
		
		
	}

	private static Queue<Integer> filledCells(SudokuModel problem) {
		Queue<Integer> filledCells = new ArrayDeque<>();
		for (int i = 0; i < 81; i++) {
			if (problem.getMatrice()[i] != 0) {				
				filledCells.add(i);
			}
		}
		return filledCells;
	}
	
	private static int score(SudokuModel problem) {
		return -filledCells(problem).size();
	}
	
	SudokuModel solution;
	SudokuModel problem;
	SudokuModel hardestProblem;
	Stack<SudokuModel> nodes;
	private void initReduction() {
		solution = new RandomSolver().getSolution();
		problem = solution.clone();
		nodes = new Stack<>();
		nodes.push(problem);
	}
	



	public SudokuPuzzle getPuzzle() {
		if (problem == null) {
			generateProblem();
		}

		return new SudokuPuzzle(problem, solution);
	}
	
	
}


