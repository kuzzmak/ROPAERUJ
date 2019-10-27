package hr.fer.zemris.optjava.dz4.part2;

public class Stick implements Comparable<Stick>{

	private int length;
	
	public Stick(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return String.valueOf(length);
	}

	@Override
	public int compareTo(Stick arg0) {
		return Integer.compare(this.length, arg0.length);
	}

	public int getLength() {
		return length;
	}
	
	
}
