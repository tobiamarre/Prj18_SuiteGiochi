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
	public SudokuModel getSolution() {
//		if (hasSolution != null) {
//			return solution;
//		}
		init();
		
		// dove andremo a segnare le celle che abbiamo riempito
		
		// depth first, pre-order traversal dell'albero dei riempimenti
		// della griglia
		while (celleVuote.size() > 0) {
			int cella = celleVuote.pop();
			
//			System.out.println(modelFromMatrix(matrix));
//			System.out.println("provo a riempire " + cella(position));
			
			// cerchiamo di riempire la cella identificata da position
			// ne calcoliamo l'annotazione corrispondente unendo i value esclusi perché già presenti
			// sulla stessa riga/colonna/blocco (annotazioniColonna etc)
			int colonna = colonna(cella);
			int blocco = blocco(cella);
			int riga = riga(cella);
			int annotazione = 
					annotazioniColonne[colonna] | 
					annotazioniBlocchi[blocco] | 
					annotazioniRighe[riga] |
					// annotazioniPosition: abbiamo già tentato queste strade, e siamo dovuti tornare indietro
					// meglio evitare di ripetere gli stessi errori
					annotazioniCelle[cella];
			

//			System.out.println("annotazione: " +Integer.toBinaryString(annotazione));
//			System.out.println("value scelto: " +cifra(value));
			
			if (annotazione == 511) {
				// se annotazione è zero, allora non è possibile scrivere nulla sulla cella
				if (celleRiempite.size() == 0) {
					// se è la prima cella che scriviamo (i.e. per trovare una soluzione
					// dovremmo cambiare il problema)
					hasSolution = false;
					return null;	// allora il problema non ammette soluzione
				}
				// backtrack:
				// prendiamo l'ultima cella che avevamo scritto, per cancellarla
				int cellaPrec = celleRiempite.pop();
				
				// aggiorniamo le annotazioni di righe blocchi e colonne
				int ValuePrec = matrix[cellaPrec];
				annotazioniColonne[colonna(cellaPrec)] ^= ValuePrec;
				annotazioniBlocchi[blocco(cellaPrec)] ^= ValuePrec;
				annotazioniRighe[riga(cellaPrec)] ^= ValuePrec;
				
				// l'annotazionePosition (i value già tentati) della cella che stavamo cercando di riempire perde di significato, 
				// dal momento che cambieremo pendingPosition, che sta nella parte di matrix che la precede (nell'ordine di riempimento)
				annotazioniCelle[cella] = 0;
				
				// cancelliamo il value che avevamo scritto
				// matrix[prevPosition] = 0;	(in realtà non è necessario farlo davvero: se esiste una soluzione ci scriveremo sopra un altro valore comunque; mentre se non esiste una soluzione non ha importanza cosa ci resta scritto)
				
				// reinseriamo le due celle nella stack di quelle da riempire (nello stesso ordine in cui le abbiamo trovate)
				celleVuote.add(cella);
				celleVuote.add(cellaPrec);
			}
			else {
				// riempiamo la cella dandole la cifra più piccola fra quelle disponibili
				// (corrispondente al bit OFF meno significativo presente nell'annotazione)
				
				int randomShift = r.nextInt(9);
				int value = annotazione * 0b1000000001 >> randomShift;
				value = value+1 & ~value & (0x3ffff >> randomShift);
				value <<= randomShift;
				
				if (value > 256) {
					value >>= 9;
				}				matrix[cella] = value;
				
				// segniamo sulla sua colonna/riga/blocco
				annotazioniColonne[colonna] ^= value;
				annotazioniBlocchi[blocco] ^= value;
				annotazioniRighe[riga] ^= value;
				
				// segniamo che abbiamo tentato questa strada, nel caso dovessimo tornare indietro
				annotazioniCelle[cella] ^= value;
				
				// segniamo che abbiamo riempito questa cella
				celleRiempite.add(cella);
			}
		}
		// se siamo giunti qua significa che abbiamo riempito tutta la matrice in modo coerente
		
		hasSolution = true;
		return modelFromMatrix(matrix);
	}



	
	
}
