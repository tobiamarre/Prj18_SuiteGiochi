package sudoku.controller;


public class InverseLexicographicSolver extends LexicographicSolver {

	
	

	@Override
	public int value(int cifra) {	//	0->0, e poi 1->1, 2->4, 3->8, 4->16, 5->32 etc.
		return (512 >> cifra) & 511;
	}// 00010 >> 1 -> 00001
	
	@Override
	public int cifra(int value) {	// bitscan con sequenza di De Bruijn (0x9AF) (inversa di value(cifra) )
		return value == 0 ? 0 : 10 - tableCifre[(0x9AF * value >> 12) & 15];
	}

	
	


	
	
	
	
}