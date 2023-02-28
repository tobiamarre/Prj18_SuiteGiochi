package model;

import java.util.Random;

public class Dado {

	Random r = new Random();
	public int lancio() {
		return r.nextInt(6) + 1;
	}
}
