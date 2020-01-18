package hr.fer.zemris.optjava.GA;

import java.util.Arrays;

public class IntSolution extends GASolution<int[]> {

	public IntSolution(int[] data) {
		super();
		this.data = data;
	}
	
	public int size() {
		return this.data.length;
	}
	
	@Override
	public String toString() {
		return "IntSolution [data=" + Arrays.toString(data) + "]";
	}


	@Override
	public GASolution<int[]> duplicate() {
		return new IntSolution(this.data.clone());
	}

}
