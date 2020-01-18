package hr.fer.zemris.optjava.GA;

public abstract class GASolution<T> implements Comparable<GASolution<T>> {
	
	protected T data;
	public double fitness;

	public GASolution() {
	}

	public T getData() {
		return data;
	}

	public abstract GASolution<T> duplicate();

	@Override
	public int compareTo(GASolution<T> o) {
		return -Double.compare(this.fitness, o.fitness);
	}
}
