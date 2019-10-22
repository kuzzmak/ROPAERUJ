package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;
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
	
	public void randomize(Random rand, double min, double max) {
		for (int i = 0; i < this.values.length; i++) {
			values[i] = rand.nextDouble() * (max - min) + min;
		}
	}
	
	public void randomize(Random rand, double[] mins, double[] maxs) {
		for (int i = 0; i < values.length; i++) {
			values[i] = rand.nextDouble() * (maxs[i] - mins[i]) + mins[i];
		}
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "DoubleArraySolution [values=" + Arrays.toString(values) + "]";
	}
	
	
	
}
