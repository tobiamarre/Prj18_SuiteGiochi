package sudoku.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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
	
	public String getJsonString() {
		JSONObject out = new JSONObject();
		out.append("problem", this.problem.getMatrice());
		out.append("solution", this.solution.getMatrice());
		return out.toString();
	}
	
}
