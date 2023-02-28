package sudoku.controller;

import java.util.ArrayList;

import sudoku.model.SimpleStack;
import sudoku.model.SudokuModel;

public class LexicographicSolver extends SudokuSolver {
	
	// GLOSSARIO
	
	// cella: 			indice dell'array del model
	
	// cifra/digit:		numero da 0 a 9 presente nell'array del model (0 per le celle vuote)
	
	// position:		indice nell'array del solver. Si ricava dalla cella corrispondente
	//					rende più veloce il calcolo di colonna blocco e riga
	
	// value:			potenza di due compresa fra 1 e 2^8, oppure 0. corrisponde alle cifre del model
	//					0 --> 0; 1 ... 9 --> 2^0 ... 2^8
	
	// annotazione:		intero con 9 bit significativi. Ciascun bit indica la disponibilità 
	//					del corrispondente value in una data cella. Bit acceso corrisponde a value non disponibile
	
	// colonne:			0000 -> 1ª colonna; 				
	//					0001 -> 2ª colonna; 
	//					0010 -> 3ª colonna; (0011 si salta);
	//					0100 -> 4ª colonna;  
	//					0101 -> 5ª colonna; 
	//					0110 -> 6ª colonna; (0111 si salta);
	//					1000 -> 7ª colonna; 	
	//					1001 -> 8ª colonna; 
	//					1010 -> 9ª colonna.
	
	// blocchi:			enumerandoli da sinistra a destra e poi dall'alto verso il basso:
	//					0000 -> 1º blocco; 					
	//					0001 -> 2º blocco; 	
	//					0010 -> 3º blocco; (0011 si salta);
	//					0100 -> 4º blocco;  		
	//					0101 -> 5º blocco; 	
	//					0110 -> 6º blocco; (0111 si salta);	
	//					1000 -> 7º blocco; 
	//					1001 -> 8º blocco; 	
	//					1010 -> 9º blocco.
	
	// righe:			0000 -> 1ª riga;	 				
	//					0001 -> 2ª riga;    
	//					0010 -> 3ª riga; (0011 si salta);   
	//					0100 -> 4ª riga; 	
	//					0101 -> 5ª riga;    
	//					0110 -> 6ª riga; (0111 si salta);   
	//					1000 -> 7ª riga;	
	//					1001 -> 8ª riga;    
	//					1010 -> 9ª riga.
	
	// in questa maniera si può formare la position di 8bit 
	// concatenando i 4bit dell'indice di riga e i 4bit dell'indice di colonna
	
	// così l'indice di riga si legge sui primi 4 bit
	// l'indice di colonna sugli ultimi 4
	// e l'indice di blocco può leggere nei 4bit intermedi (si sovrappone a quelli di riga e colonna)
	
	// l'array su cui scriviamo la soluzione viene lungo 171 (90 slot vanno sprecati
	// ma ricavare gli indici di colonna blocco e riga risulta molto più economico)
	// praticamente lavoriamo come se avessimo 16 righe e 16 colonne
	
	protected SudokuModel solution;
	protected Boolean hasSolution;

	protected SimpleStack celleVuote;		// position delle celle da riempire. L'ordine determina la soluzione trovata
	protected SimpleStack celleRiempite;
	protected int[] matrix;							// array sul quale viene scritta la soluzione 
													// ciascuna position contiene un value. (in realtà per opportunità computazionale
													// l'array è più grande di quello del model, alcuni byte vengono sprecati)
	
	// ogni volta che aggiungiamo una cifra alla griglia (più precisamente, scriviamo un value su matrix)
	// andiamo a segnare il value su annotazioniColonne, annotazioniBlocchi e annotazioniRighe,
	// agli indici corrispondenti alla position su cui abbiamo scritto
	
	// quindi annotazioniColonne/Blocchi/Righe contiene i value non disponibili perché già presenti
	// nella sezione cui appartiene la position
	
	// i valori di questi tre array sono determinati esclusivamente dai value presenti nella matrix
	
	// annotazioniPosition invece conserva i value che possiamo 
	// escludere perché li abbiamo provati e recano a configurazioni non risolvibili, e andremo a compilarlo
	// man mano che traversiamo l'albero dei riempimenti possibili della griglia. Col procedere 
	// dell'algoritmo annotazioniPosition conterrà sempre più value quand'anche dovessimo tornare alla stessa
	// configurazione di matrix
	
	protected int[] annotazioniColonne;		
	protected int[] annotazioniBlocchi;
	protected int[] annotazioniRighe;		
	protected int[] annotazioniCelle;	
		
	
	public void setProblem(SudokuModel newProblem) {
		problem = newProblem;
		hasSolution = null;
	}
	
	public LexicographicSolver(SudokuModel problem) {
		this.problem = problem;
		hasSolution = null;
	}
	
	public LexicographicSolver() {
		this.problem = new SudokuModel();
		hasSolution = null;
	}

	@Override
	public boolean hasSolution() {
		return this.getSolution() != null;
	}
	
	/**
	 * Restituisce una soluzione di problem. Precisamente, quella che assegna ad ogni cella
	 * la cifra più piccola che può ospitare (con precedenza alle celle con indice più alto)
	 */
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
			
			if (annotazione == 511) {
				// se annotazione è 511, allora non è possibile scrivere nulla sulla cella
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
				int value = primoValueDisponibile[annotazione];
				matrix[cella] = value;
				
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

	protected void init() {
		
		annotazioniColonne = new int[9];
		annotazioniBlocchi = new int[9];
		annotazioniRighe = new int[9];	
		annotazioniCelle = new int[81];	
		celleVuote = new SimpleStack(81);
		matrix = problem.getMatrice().clone();
		
		for (int cella = 80; cella >= 0; cella--) {
			int value = value(matrix[cella]);
			
			annotazioniColonne[colonna(cella)] |= value;
			annotazioniBlocchi[blocco(cella)] |= value;
			annotazioniRighe[riga(cella)] |= value;
			
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
	
	static int primoValueDisponibile(int annotazione) { // bit OFF meno significativo (troncato a 9 bit)
		return annotazione+1 & ~annotazione & 511;
	}
	int[] primoValueDisponibile;
	{
		primoValueDisponibile = new int[512];
		for (int annotazione = 0; annotazione < 512; annotazione++) {
			primoValueDisponibile[annotazione] = primoValueDisponibile(annotazione);
		}
	}
	
	
	
	
}