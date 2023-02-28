package sudoku.test;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import presentation.Sudoku;
import sudoku.controller.InverseLexicographicSolver;
import sudoku.controller.LexicographicSolver;
import sudoku.controller.RandomSolver;
import sudoku.controller.SudokuGenerator;
import sudoku.controller.SudokuSolver;

import sudoku.model.SudokuModel;

public class SudokuTest {

	public static void main(String[] args) {
		
		

		SudokuModel m = new SudokuModel(
		new int[] { 5, 3, 3, 8, 1, 2, 4, 5, 1, 3, 3, 6, 8, 1, 6, 8, 4, 2, 4, 5, 9 },
		new int[] { 4,13,19,21,24,25,27,37,41,42,48,51,52,53,54,55,61,71,74,78,79 }
		);
		
		
		LexicographicSolver l = new LexicographicSolver();
		LexicographicSolver il = new InverseLexicographicSolver();
		LexicographicSolver r = new RandomSolver();
//		l.setProblem(m);
//		il.setProblem(m);
//		r.setProblem(m);
//		
		long start = System.nanoTime();
		for (int i = 0; i < 1_000_000; i++) {
			r.getSolution();
			
			
		}
		System.out.println((double)(System.nanoTime() - start)/1_000_000_000);
		
		System.out.println(r.getSolution());
		
		
//		GENERA GRIGLIE RISOLTE
		
//		try {
//			PrintWriter pw = new PrintWriter(new File("sudokuGrids.txt"));
		
//			int[] hashes = new int[100_000];
//			for (int i = 0; i < 1_000_000; i++) {
//				SudokuModel s = new RandomSolver().getSolution();
//				pw.append(Integer.toString(i) + "\n");
//				pw.append(s.toString() + "\n");
//				System.out.println(i);
//				System.out.println(s);
//				hashes[i] = s.hashCode();
//			}
//			pw.close();
			
//			Arrays.sort(hashes);
//			int n = 1;
//			for (int i = 1; i < 10_000; i++) {
//				if (hashes[i] != hashes[i-1]) {
//					n++;
//				}
//			}
//			System.out.println(n);
//			System.out.println("fatto");
			
			
			
//		} catch (FileNotFoundException e) {
//			 TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
		
		
//		GENERA PROBLEMI MINIMALI
		

		
		
//		SudokuGenerator gen = new SudokuGenerator();
//		gen.initReduction();
//		gen.reduceProblem(4);
//		System.out.println("-------------------------\nresult:");
//		System.out.println(gen.getProblem());
//	
//		
//
//		
//		
//		SudokuModel sm = new SudokuModel(
//				new int[] { 5, 3, 3, 8, 1, 2, 4, 5, 1, 3, 3, 6, 8, 1, 6, 8, 4, 2, 4, 5, 9 },
//				new int[] { 4,13,19,21,24,25,27,37,41,42,48,51,52,53,54,55,61,71,74,78,79 }
//				);
//		System.out.println(sm);
//		
//		System.out.println(new LexicographicSolver(sm).getSolution());
//		SudokuGenerator sg = new SudokuGenerator();
//		
//		sg.generatePuzzles();
//		System.out.println(new SudokuGenerator().getPuzzles().get(0));
//		
//		
	}
}
