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

	protected ArrayList<Integer> positionVuote;		// position delle celle da riempire. L'ordine determina la soluzione trovata
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
	protected int[] annotazioniPosition;	
		
	
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

		initMatrix();
		initAnnotazioni();
		initPositionVuote();
		
		// le celle ancora da riempire
		SimpleStack pendingPositions = new SimpleStack(positionVuote.size());
		pendingPositions.addAll(positionVuote);
		
		// dove andremo a segnare le celle che abbiamo riempito
		SimpleStack filledPositions = new SimpleStack(positionVuote.size());
		
		// depth first, pre-order traversal dell'albero dei riempimenti
		// della griglia
		while (pendingPositions.size() > 0) {
			int position = pendingPositions.pop();
			
//			System.out.println(modelFromMatrix(matrix));
//			System.out.println("provo a riempire " + cella(position));
			
			// cerchiamo di riempire la cella identificata da position
			// ne calcoliamo l'annotazione corrispondente unendo i value esclusi perché già presenti
			// sulla stessa riga/colonna/blocco (annotazioniColonna etc)
			int colonna = position & 15;
			int blocco = (position >> 2) & 15;
			int riga = position >> 4;
			int annotazione = 
					annotazioniColonne[colonna] | 
					annotazioniBlocchi[blocco] | 
					annotazioniRighe[riga] |
					// annotazioniPosition: abbiamo già tentato queste strade, e siamo dovuti tornare indietro
					// meglio evitare di ripetere gli stessi errori
					annotazioniPosition[position];
			

//			System.out.println("annotazione: " +Integer.toBinaryString(annotazione));
//			System.out.println("value scelto: " +cifra(value));
			
			if (annotazione == 0) {
				// se annotazione è zero, allora non è possibile scrivere nulla sulla cella
				if (filledPositions.size() == 0) {
					// se è la prima cella che scriviamo (i.e. per trovare una soluzione
					// dovremmo cambiare il problema)
					hasSolution = false;
					return null;	// allora il problema non ammette soluzione
				}
				// backtrack:
				// prendiamo l'ultima cella che avevamo scritto, per cancellarla
				int prevPosition = filledPositions.pop();
				
				// aggiorniamo le annotazioni di righe blocchi e colonne
				int prevValue = matrix[prevPosition];
				annotazioniColonne[prevPosition & 15] ^= prevValue;
				annotazioniBlocchi[(prevPosition >> 2) & 15] ^= prevValue;
				annotazioniRighe[prevPosition >> 4] ^= prevValue;
				
				// l'annotazionePosition (i value già tentati) della cella che stavamo cercando di riempire perde di significato, 
				// dal momento che cambieremo pendingPosition, che sta nella parte di matrix che la precede (nell'ordine di riempimento)
				annotazioniPosition[position] = 0;
				
				// cancelliamo il value che avevamo scritto
				// matrix[prevPosition] = 0;	(in realtà non è necessario farlo davvero: se esiste una soluzione ci scriveremo sopra un altro valore comunque; mentre se non esiste una soluzione non ha importanza cosa ci resta scritto)
				
				// reinseriamo le due celle nella stack di quelle da riempire (nello stesso ordine in cui le abbiamo trovate)
				pendingPositions.add(position);
				pendingPositions.add(prevPosition);
			}
			else {
				// riempiamo la cella dandole la cifra più piccola fra quelle disponibili
				// (corrispondente al bit OFF meno significativo presente nell'annotazione)
				int value = primoValueDisponibile[annotazione];
				matrix[position] = value;
				
				// segniamo sulla sua colonna/riga/blocco
				annotazioniColonne[colonna] ^= value;
				annotazioniBlocchi[blocco] ^= value;
				annotazioniRighe[riga] ^= value;
				
				// segniamo che abbiamo tentato questa strada, nel caso dovessimo tornare indietro
				annotazioniPosition[position] ^= value;
				
				// segniamo che abbiamo riempito questa cella
				filledPositions.add(position);
			}
		}
		// se siamo giunti qua significa che abbiamo riempito tutta la matrice in modo coerente
		
		hasSolution = true;
		return modelFromMatrix(matrix);
	}

	protected void initMatrix() {
		matrix = new int[0xAB];
		for (int cella = 0; cella < 81; cella++) {
			matrix[position(cella)] = value(problem.getMatrice()[cella]);
		}
	}
	protected void initAnnotazioni() {
		annotazioniColonne = new int[0xB];
		annotazioniBlocchi = new int[0xB];
		annotazioniRighe = new int[0xB];		// max colonna, blocco, riga: 0b1010 = 0xA
		annotazioniPosition = new int[0xAB];	// max position: 0b1010_1010 = 0xAA
		for (int cella = 0; cella < 81; cella++) {
			int positionCella = position(cella);
			int value = value(problem.getMatrice()[cella]);
			annotazioniColonne[colonna(positionCella)] |= value;
			annotazioniBlocchi[blocco(positionCella)] |= value;
			annotazioniRighe[riga(positionCella)] |= value;
		}
	}
	protected void initPositionVuote() {
		positionVuote = new ArrayList<>();
		for (int i = 0; i < 81; i++) {
			if (problem.getMatrice()[i] == 0) positionVuote.add(position(i));
		}
	}
	protected SudokuModel modelFromMatrix(int[] matrice) {
		int[] matriceModel = new int[81];
		for (int i = 0; i < 81; i++) {
			matriceModel[i] = cifra(matrice[position(i)]);
		}
		return new SudokuModel(matriceModel);
	}
	
	static int value(int cifra) {	//	0->0, e poi 1->1, 2->4, 3->8, 4->16, 5->32 etc.
		return (1 << cifra) >> 1;
	}
	static final int[] tableCifre = new int[] { 1, 2, 3, 6, 4, 10, 7, 12, 16, 5, 9, 11, 15, 8, 14, 13};
	static int cifra(int value) {	// bitscan con sequenza di De Bruijn (0x9AF) (inversa di value(cifra) )
		return value == 0 ? 0 : tableCifre[(0x9AF * value >> 12) & 15];
	}
	
	static int position(int cella) {	// scomposizine dell'indice della cella nelle sue coordinate in (Z/3Z)^4
		return (((cella/27)%3) << 2) + (((cella/9)%3) << 0) + (((cella/3)%3) << 4) + (((cella/1)%3) << 6);
	}
	static int cella(int position) {	// inversa di position(cella)
		return 27*((position >> 2) & 3) + 9*((position >> 0) & 3) + 3*((position >> 4) & 3) + 1*((position >> 6) & 3);
	}

	static int colonna(int position) {
		return position & 15;
	}
	static int blocco(int position) {
		return (position >> 2) & 15;
	}
	static int riga(int position) {
		return position >> 4;
	}
	
	static int primoValueDisponibile(int annotazione) { // bit OFF meno significativo (troncato a 9 bit)
		return annotazione+1 & ~annotazione & 511;
	}
	static int[] primoValueDisponibile;
	static {
		primoValueDisponibile = new int[512];
		for (int annotazione = 0; annotazione < 512; annotazione++) {
			primoValueDisponibile[annotazione] = primoValueDisponibile(annotazione);
		}
	}
	
	
	
	
}