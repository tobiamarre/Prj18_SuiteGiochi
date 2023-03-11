package sudoku.controller;


public class AntiLexicographicSolver extends LexicographicSolver {

	@Override
	public int value(int cifra) {	//	0->0, e poi 1->256, 2->128, 3->64, 4->32, 5->16 etc.
		return (512 >> cifra) & 511;
	}// 00010 >> 1 -> 00001
	
	@Override
	public int cifra(int value) {	// bitscan con sequenza di De Bruijn (0x9AF) (inversa di value(cifra) )
		return value == 0 ? 0 : 10 - tableCifre[(0x9AF * value >> 12) & 15];
	}

	
}