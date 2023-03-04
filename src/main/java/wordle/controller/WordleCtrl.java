package wordle.controller;

import java.util.ArrayList;
import java.util.Objects;

import wordle.model.Lettera;
import wordle.model.Parola;

public class WordleCtrl {
	
	int GRIGIO = 0;
	int VERDE = 1;
	int GIALLO = 2;
	
	
	int tentativiMax;
	String stringaSoluzione;
	
	ArrayList<Parola> tentativi = new ArrayList<>();
	
	

	
	public void nuovoTentativo(String stringaTentativo) {
		
		Parola soluzione = new Parola(stringaSoluzione);
		Parola tentativo = new Parola(stringaTentativo);
		Parola tentativoColorato = new Parola();
		
		
		// lettere verdi
		for (Lettera lettera : tentativo) {
			if (soluzione.contains(lettera)) {	
				lettera.colore = VERDE;
				tentativoColorato.add(lettera);
			}
		}
		
		// lettere gialle
		tentativo.removeAll(tentativoColorato);
		soluzione.removeAll(tentativoColorato);

		for (Lettera letteraSoluzione : soluzione) {
			for (Lettera letteraTentativo : tentativo) {
				if (letteraSoluzione.carattere.equals(letteraTentativo.carattere) ) {
					letteraTentativo.colore = GIALLO;
					tentativoColorato.add(letteraTentativo);
					tentativo.remove(letteraTentativo);
					break;
				}
			}
		}
		
		// lettere grigie
		tentativoColorato.addAll(tentativo);
		
		
		tentativi.add(tentativoColorato);
		
		
		
		
		
		
	}
	
	public static void main(String[] args) {
		String[] elencoSoluzioni = new String[] {"acasac", "ssaaba"};	
		String[] elencoTentativi = new String[] {"ccicso", "sccccc", "cccscc", "cccsss", "sccsss"};	
	
		WordleCtrl ctrl = new WordleCtrl();
		
		for (String soluzione : elencoSoluzioni) {
			System.out.println("----------------------");
			System.out.println(soluzione);

			ctrl.stringaSoluzione = soluzione;
			for (String tentativo : elencoTentativi) {
				System.out.println();
				ctrl.nuovoTentativo(tentativo);
				System.out.println(soluzione);
				System.out.println(ctrl.tentativi.remove(ctrl.tentativi.size()-1));
			}
		}
		
	}
}
