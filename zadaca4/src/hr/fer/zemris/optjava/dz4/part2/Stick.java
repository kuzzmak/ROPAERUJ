package hr.fer.zemris.optjava.dz4.part2;

public class Stick implements Comparable<Stick> {

	private int length;
	private final int stickIndex;
	private static int numOfSticks = 0;
	@Override
	public int hashCode() {
		return stickIndex;
	}


	public Stick(int length) {
		this.length = length;
		this.stickIndex = numOfSticks;
		numOfSticks++;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Stick stick = (Stick)obj;

		return stickIndex == stick.stickIndex;
	}

}
