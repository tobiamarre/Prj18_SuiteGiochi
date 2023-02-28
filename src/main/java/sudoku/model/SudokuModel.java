package sudoku.model;

public class SudokuModel {
	int[] matrice;
	
	public SudokuModel() {
		this.matrice = new int[81];
	}
	
	public SudokuModel(int[] matrice) {
		this.matrice = matrice;
	}
	
	public SudokuModel(int[] values, int[] positions) {
		this.matrice = new int[81];
		for (int i = 0; i < positions.length; i++) {
			matrice[positions[i]] = values[i];
		}
	}
	
	public int[] getMatrice() {
		return matrice;
	}
	
	@Override
	public SudokuModel clone() {
		return new SudokuModel(matrice.clone());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SudokuModel)) {
			return false;
		}
		SudokuModel other = (SudokuModel)obj;

		for (int i = 0; i < 81; i++) {
			if (matrice[i] != other.matrice[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < 81; i++) {
			hash *= 317;
			hash += matrice[i];
		}
		return hash;
	}

	@Override
	public String toString() {
		String out = "";
		out += "-------------------------\n";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int h = 0; h < 3; h++) {
					out += "| ";
					for (int k = 0; k < 3; k++) {
						int cifraCella = matrice[27*i + 9*j + 3*h + k];
						out += cifraCella == 0 ? "  " : cifraCella + " ";
					}	
				}
				out += "|\n";
			}
			out += "-------------------------\n";
		}
		return out;
	}
}
