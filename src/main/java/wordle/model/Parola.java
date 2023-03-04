package wordle.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class Parola implements Collection<Lettera>{

	int GRIGIO = 0;
	int VERDE = 1;
	int GIALLO = 2;
	
	TreeSet<Lettera> lettere;
	
	public Parola(String strParola) {
		lettere = new TreeSet<>();
		
		for (int i = 0; i < strParola.length(); i++) {
			lettere.add(new Lettera(strParola.charAt(i), GRIGIO, i));
		}
	}
	
	public Parola() {
		lettere = new TreeSet<>();
	}
	
	
	public String toString() {
		String out = "";
		for (Lettera lettera : lettere) {
			out += lettera.carattere;
		}
		out += "\n";
		for (Lettera lettera : lettere) {
			out += lettera.colore == VERDE ? "V" : lettera.colore == GIALLO ? "G" : "_";
		}
		return out;
	}
	
	
	@Override
	public int size() {
		return lettere.size();
	}

	@Override
	public boolean isEmpty() {
		return lettere.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return lettere.contains(o);
	}

	@Override
	public Iterator<Lettera> iterator() {
		return lettere.iterator();
	}

	@Override
	public Object[] toArray() {
		return lettere.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return lettere.toArray(a);
	}

	@Override
	public boolean add(Lettera e) {
		return lettere.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return lettere.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return lettere.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Lettera> c) {		
		return lettere.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return lettere.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return lettere.retainAll(c);
	}

	@Override
	public void clear() {
		lettere = new TreeSet<>();
	}


	
}
