package sudoku.controller;

import java.util.Random;

import sudoku.model.SimpleStack;
import sudoku.model.SudokuModel;

public class RandomSolver extends InverseLexicographicSolver {

	Random r = new Random();
	
	public RandomSolver(SudokuModel problem) {
		this.problem = problem;
	}
	
	public RandomSolver() {
		this.problem = new SudokuModel();
	}

	@Override
	public SudokuModel getSolution() {
//		if (hasSolution != null) {
//			return solution;
//		}

		initMatrix();
		initAnnotazioni();
		initPositionVuote();
		
		SimpleStack pendingPositions = new SimpleStack(positionVuote.size());
		pendingPositions.addAll(positionVuote);
		
		SimpleStack filledPositions = new SimpleStack(positionVuote.size());
		
		while (pendingPositions.size() > 0) {			
			int position = pendingPositions.pop();
			
//				System.out.println(modelFromMatrix(matrix));
//				System.out.println("provo a riempire " + cella(position));
			
			int colonna = position & 15;
			int blocco = (position >> 2) & 15;
			int riga = position >> 4;
			int annotazione = 
					annotazioniColonne[colonna] | 
					annotazioniBlocchi[blocco] | 
					annotazioniRighe[riga] |
					annotazioniPosition[position];
			
			int randomShift = r.nextInt(9);
			// mi rendo conto che non è chiarissimo
			
			int value = annotazione * 0b1000000001 >> randomShift;
			value = value+1 & ~value & (0x3ffff >> randomShift);
			value <<= randomShift;
			
			if (value > 256) {
				value >>= 9;
			}
			matrix[position] = value;

//				System.out.println("annotazione: " +Integer.toBinaryString(annotazione));
//				System.out.println("value scelto: " +cifra(value));
			
			if (value == 0) {
				if (filledPositions.size() == 0) {
					hasSolution = false;
					return null;	// il problema non ammette soluzione
				}
				int prevPosition = filledPositions.pop();
				int prevValue = matrix[prevPosition];
				matrix[prevPosition] = 0;
				annotazioniColonne[prevPosition & 15] ^= prevValue;
				annotazioniBlocchi[(prevPosition >> 2) & 15] ^= prevValue;
				annotazioniRighe[prevPosition >> 4] ^= prevValue;
				annotazioniPosition[position] = 0;
				pendingPositions.add(position);
				pendingPositions.add(prevPosition);
			}
			else {
				annotazioniColonne[colonna] ^= value;
				annotazioniBlocchi[blocco] ^= value;
				annotazioniRighe[riga] ^= value;
				annotazioniPosition[position] ^= value;
				filledPositions.add(position);
			}
			
		}
		hasSolution = true;
		return modelFromMatrix(matrix);
	}
	

	
	
}
