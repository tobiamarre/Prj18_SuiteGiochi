package sudoku.model;

import java.util.Collection;

public class SimpleStack {
	private int[] items;
	private int size;
	
	public SimpleStack(int maxSize) {
		this.items = new int[maxSize];
		this.size = 0;
	}
	
	public void add(int newItem) {
		items[size++] = newItem;
	}
	
	public void addAll(int[] newItems) {
		for (int item : newItems) {
			items[size++] = item;
		}
	}

	public void addAll(Collection<Integer> newItems) {
		for (Integer item : newItems) {
			items[size++] = item;		
		}
	}
	
	public int pop() {
		return items[--size];
	}
	
	public int size() {
		return size;
	}
	
	@Override
	public String toString() {
		String out = "[";
		for (int item : items) {
			out += item + ", ";
		}
		out += "]";
		return out;
	}
}
