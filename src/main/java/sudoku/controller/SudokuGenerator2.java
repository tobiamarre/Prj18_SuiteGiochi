package sudoku.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SudokuGenerator2 extends SudokuGenerator {

		
	public void generateProblem() {
		solution = new RandomSolver().getSolution();
		problem = solution.clone();
		
		SudokuSolver lowerSolver = new LexicographicSolver();
		SudokuSolver higherSolver = new AntiLexicographicSolver();
		
		List<List<Integer>> celleCifra = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			celleCifra.add(new ArrayList<>());
		}
		
		for (int cella = 0; cella < 81; cella++) {
			celleCifra.get(solution.getMatrix()[cella]).add(cella);
		}
		for (int i = 0; i < 10; i++) {
			Collections.shuffle(celleCifra.get(i));
		}
		
		List<Integer> cifre = new ArrayList<>();
		for (int cifra = 1; cifra < 10; cifra++) {
			cifre.add(cifra);
		}
		Collections.shuffle(cifre);
		
		for (int cifra : cifre) {
			for (int cella : celleCifra.get(cifra)) {

				problem.getMatrix()[cella] = 0;
				lowerSolver.setProblem(problem);
				higherSolver.setProblem(problem);
				if (lowerSolver.getSolution().equals(higherSolver.getSolution())) {
					continue;
				}
				problem.getMatrix()[cella] = cifra;
			}
		}
		
	
		
		
	}



	
}


