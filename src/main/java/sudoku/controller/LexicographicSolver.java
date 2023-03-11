package sudoku.controller;


import sudoku.model.SimpleStack;
import sudoku.model.SudokuModel;

public class LexicographicSolver extends SudokuSolver {
	
	// GLOSSARIO
	
	// cella: 			indice dell'array del model
	
	// cifra/digit:		numero da 0 a 9 presente nell'array del model (0 per le celle vuote)
	
	// value:			potenza di due compresa fra 1 e 2^8, oppure 0. corrisponde alle cifre del model
	//					0 --> 0; 1 ... 9 --> 2^0 ... 2^8
	
	// excludedValues:		intero con 9 bit significativi. Ciascun bit indica la disponibilità 
	//					del corrispondente value in una data cella. Bit acceso corrisponde a value non disponibile
	
	protected SudokuModel solution;
	protected Boolean hasSolution;

	protected SimpleStack celleVuote;		
	protected SimpleStack celleRiempite;
	protected int[] matrix;							
	// ogni volta che aggiungiamo una cifra alla griglia
	// andiamo a segnare il value su excludedValuesColonne, excludedValuesBlocchi e excludedValuesRighe,
	// agli indici corrispondenti alla cella su cui abbiamo scritto
	
	// quindi excludedValuesColonne/Blocchi/Righe contengono i value non disponibili perché già presenti
	// nella sezione cui appartiene la cella
	
	// i valori di questi tre array sono determinati esclusivamente dai value presenti in matrix
	
	// excludedValuesCelle invece conserva i value che possiamo 
	// escludere perché li abbiamo provati e recano a configurazioni non risolvibili, e andremo a compilarlo
	// man mano che traversiamo l'albero dei possibili riempimenti della griglia. Col procedere 
	// dell'algoritmo excludedValuesCelle conterrà sempre più value quand'anche dovessimo tornare alla stessa
	// configurazione di matrix
	
	protected int[] excludedValuesColonne;		
	protected int[] excludedValuesBlocchi;
	protected int[] excludedValuesRighe;		
	protected int[] excludedValuesCelle;	
		
	
	public LexicographicSolver(SudokuModel problem) {
		this.problem = problem;
		init();
		hasSolution = null;
	}
	
	public LexicographicSolver() {
		this.problem = new SudokuModel();
		init();
		hasSolution = null;
	}
	
	public void setProblem(SudokuModel newProblem) {
		problem = newProblem;
		init();
		hasSolution = null;
	}

	@Override
	public boolean hasSolution() {
		if (hasSolution != null) {
			return hasSolution();
		}
		return this.getSolution() != null;
	}
	
	/**
	 * Restituisce una soluzione di problem. Precisamente, quella che assegna ad ogni cella
	 * la cifra più piccola che può ospitare (con precedenza alle celle con indice più basso)
	 */
	@Override
	public SudokuModel getSolution() {		
		// depth first, pre-order traversal dell'albero dei riempimenti della griglia
		while (celleVuote.size() > 0) {
			int cella = celleVuote.pop();
			
			// cerchiamo di riempire la cella
			// ne calcoliamo l'excludedValues corrispondente unendo i value esclusi perché già presenti
			// sulla stessa riga/colonna/blocco
			int colonna = colonna(cella);
			int blocco = blocco(cella);
			int riga = riga(cella);
			int excludedValues = 
					excludedValuesColonne[colonna] | 
					excludedValuesBlocchi[blocco] | 
					excludedValuesRighe[riga] |
					// excludedValuesCelle: abbiamo già tentato queste strade, e siamo dovuti tornare indietro
					excludedValuesCelle[cella];
			
			if (excludedValues == 511) {	// 511 = 0b111111111
				// se excludedValues è 511, allora non è possibile scrivere nulla sulla cella
				if (celleRiempite.size() == 0) {
					// se è la prima cella che scriviamo (i.e. per trovare una soluzione
					// dovremmo cambiare il problema)
					hasSolution = false;
					return null;	// allora il problema non ammette soluzione
				}
				// backtrack:
				// prendiamo l'ultima cella che avevamo scritto, per cancellarla
				int cellaPrec = celleRiempite.pop();
				
				// aggiorniamo gli excludedValues di righe blocchi e colonne
				int ValuePrec = matrix[cellaPrec];
				excludedValuesColonne[colonna(cellaPrec)] ^= ValuePrec;
				excludedValuesBlocchi[blocco(cellaPrec)] ^= ValuePrec;
				excludedValuesRighe[riga(cellaPrec)] ^= ValuePrec;
				
				// l'excludedValuesCelle (i value già tentati) della cella che stavamo cercando di riempire perde di significato, 
				// dal momento che cambieremo cellaPrec, che sta nella parte di matrix che la precede (nell'ordine di riempimento)
				excludedValuesCelle[cella] = 0;
				
				// cancelliamo il value che avevamo scritto
				// matrix[cellaPrec] = 0;	(in realtà non è necessario farlo davvero: se esiste una soluzione ci scriveremo sopra un altro valore comunque; mentre se non esiste una soluzione non ha importanza cosa ci resta scritto)
				
				// reinseriamo le due celle nella stack di quelle da riempire (nello stesso ordine in cui le abbiamo trovate)
				celleVuote.add(cella);
				celleVuote.add(cellaPrec);
			}
			else {
				// riempiamo la cella dandole la cifra più piccola fra quelle disponibili
				// (corrispondente al bit OFF meno significativo presente nell'excludedValues)
				int value = unValueAmmissibile(excludedValues);
				matrix[cella] = value;
				
				// segniamo sulla sua colonna/riga/blocco
				excludedValuesColonne[colonna] ^= value;
				excludedValuesBlocchi[blocco] ^= value;
				excludedValuesRighe[riga] ^= value;
				
				// segniamo che abbiamo tentato questa strada, nel caso dovessimo tornare indietro
				excludedValuesCelle[cella] ^= value;
				
				// segniamo che abbiamo riempito questa cella
				celleRiempite.add(cella);
			}
		}
		// se siamo giunti qua significa che abbiamo riempito tutta la matrice in modo coerente
		
		hasSolution = true;
		return modelFromMatrix(matrix);
	}

	protected void init() {
		
		excludedValuesColonne = new int[9];
		excludedValuesBlocchi = new int[9];
		excludedValuesRighe = new int[9];	
		excludedValuesCelle = new int[81];	
		celleVuote = new SimpleStack(81);
		matrix = problem.getMatrix().clone();
		
		for (int cella = 80; cella >= 0; cella--) {
			int value = value(matrix[cella]);
			
			excludedValuesColonne[colonna(cella)] |= value;
			excludedValuesBlocchi[blocco(cella)] |= value;
			excludedValuesRighe[riga(cella)] |= value;
			
			if (value == 0) {
				celleVuote.add(cella);
			}
			else {				
				matrix[cella] = value;
			}
		}
		celleRiempite = new SimpleStack(81);
	}

	protected SudokuModel modelFromMatrix(int[] matrice) {
		int[] matriceModel = new int[81];
		for (int i = 0; i < 81; i++) {
			matriceModel[i] = cifra(matrice[i]);
		}
		return new SudokuModel(matriceModel);
	}
	
	public int value(int cifra) {	//	0->0, e poi 1->1, 2->4, 3->8, 4->16, 5->32 etc.
		return (1 << cifra) >> 1;
	}
	static final int[] tableCifre = new int[] { 1, 2, 3, 6, 4, 10, 7, 12, 16, 5, 9, 11, 15, 8, 14, 13};
	public int cifra(int value) {	// bitscan con sequenza di De Bruijn (0x9AF) (inversa di value(cifra) )
		return value == 0 ? 0 : tableCifre[((0x9AF * value >> 12)  & 15)];
	}

//	cella = (cella/27 %3) *27 + (cella/9 %3) *9 + (cella/3 %3) *3 + cella %3;
	
	static int colonna(int cella) {
		return cella % 9;
	}
	static int blocco(int cella) {
		return (cella/27) *3 + (cella/3 % 3); 
	}
	static int riga(int cella) {
		return cella / 9; 
	}
	int unValueAmmissibile(int excludedValues) { // bit OFF meno significativo 
		return excludedValues+1 & ~excludedValues;
	}
	
}