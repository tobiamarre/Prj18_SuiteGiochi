package sudoku.controller;

import sudoku.model.SudokuModel;

public class InverseLexicographicSolver extends LexicographicSolver {

	
	
	
	@Override
	protected void initMatrix() {
		matrix = new int[0xAB];
		for (int cella = 0; cella < 81; cella++) {
			int cifra = 10 - problem.getMatrice()[cella];
			if (cifra == 10) {
				cifra = 0;
			}
			matrix[position(cella)] = value(cifra);
		}
	}

	@Override
	protected SudokuModel modelFromMatrix(int[] matrice) {
		int[] matriceModel = new int[81];
		for (int i = 0; i < 81; i++) {
			matriceModel[i] = 10 - cifra(matrice[position(i)]);
			if (matriceModel[i] == 10) {
				matriceModel[i] = 0;
			}
		}
		return new SudokuModel(matriceModel);
	}

	@Override
	protected void initAnnotazioni() {
		annotazioniColonne = new int[0xA +1];
		annotazioniBlocchi = new int[0xA +1];
		annotazioniRighe = new int[0xA +1];		// max colonna, blocco, riga: 0b1010 = 0xA
		annotazioniPosition = new int[0xAA +1];	// max position: 0b1010_1010 = 0xAA
		for (int cella = 0; cella < 81; cella++) {
			int position = position(cella);
			int cifra = 10 - problem.getMatrice()[cella];
			if (cifra == 10) {
				cifra = 0;
			}
			int value = value(cifra);
			annotazioniColonne[colonna(position)] |= value;
			annotazioniBlocchi[blocco(position)] |= value;
			annotazioniRighe[riga(position)] |= value;
		}
	}
	
	


	
	
	
	
}