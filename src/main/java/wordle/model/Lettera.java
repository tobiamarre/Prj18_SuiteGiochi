package wordle.model;

public class Lettera implements Comparable<Lettera> {
	
	int GRIGIO = 0;
	int VERDE = 1;
	int GIALLO = 2;

	public String carattere;
	public int colore;
	public int posizione;
	
	public Lettera(String carattere, int colore, int posizione) {
		this.carattere = carattere;
		this.colore = colore;
		this.posizione = posizione;
	}
	public Lettera(char carattere, int colore, int posizione) {
		this.carattere = String.valueOf(carattere);
		this.colore = colore;
		this.posizione = posizione;
	}
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof Lettera)) {
			return false;
		}
		
		Lettera other = (Lettera) obj;
		
		boolean stessoCarattere =  this.carattere.equals(other.carattere);
		boolean stessaPosizione = this.posizione == other.posizione;
		
		return stessoCarattere && stessaPosizione;
	}
	@Override
	public String toString() {
		return "Lettera [carattere=" + carattere + ", colore=" + (colore == VERDE ? "verde" : colore == GIALLO ? "giallo" : "grigio") + ", posizione=" + posizione + "]";
	}
	@Override
	public int compareTo(Lettera o) {
		return this.posizione - o.posizione;
	}
	@Override
	public int hashCode() {
		return carattere.hashCode() * 31 + posizione;
	}

	
	
	
}
