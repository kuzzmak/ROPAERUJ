package hr.fer.zemris.optjava.dz3;

import java.util.Random;

public class DoubleArraySolution extends SingleObjectiveSolution{

	double[] values;
	
	
	public DoubleArraySolution(int value) {
		super();
		values = new double[value];
	}
	
	public DoubleArraySolution newLikeThis() {
		return new DoubleArraySolution(this.values.length);
	}

	public DoubleArraySolution duplicate() {
		DoubleArraySolution vec = new DoubleArraySolution(this.values.length);
		for(int i = 0; i < this.values.length; i++) {
			vec.values[i] = this.values[i];
		}
		return vec;
	} 
	
	public void randomize(Random rand, double[] v1, double[] v2) {
		// TODO
	}
}
