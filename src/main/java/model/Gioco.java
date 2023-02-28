package model;

public class Gioco {
	String nome;
	public Gioco(String nome) {
		this.nome = nome;
	}
	
	public String puntoElenco() {
		return "<li>" + nome + "</li>";
	}
	
	@Override
	public String toString() {
		return "Gioco [nome=" + nome.toUpperCase() + "]";
	}

	public String getNome() {
		return nome;
	}
	
}
