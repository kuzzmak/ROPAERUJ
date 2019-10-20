package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;
import java.util.Random;

public class DoubleArrayUnifNeighbourhood implements INeighbourhood<DoubleArraySolution>{
	private double[] deltas;
	private Random rand;
	
	public DoubleArrayUnifNeighbourhood(double[] deltas) {
		super();
		this.deltas = deltas;
		this.rand = new Random();
	}

	@Override
	public DoubleArraySolution randomNeighbour(DoubleArraySolution obj) {
		
		double[] solution = obj.values;
		DoubleArraySolution nextNeighbour = new DoubleArraySolution(solution.length);
		
		for(int i = 0; i < solution.length; i++) {
			double upper = solution[i] + deltas[i];
			double lower = solution[i] - deltas[i];
			solution[i] = rand.nextDouble() * (upper - lower) + lower;
		}
		
		nextNeighbour.setValues(solution);
		return nextNeighbour;
	}
	
	public static void main(String[] args) {
//		double[] solution = new double[] {1,2,3};
//		double[] deltas = new double[] {0.2,0.2,0.2};
//		Random rand = new Random();
//		for(int i = 0; i < solution.length; i++) {
//			double upper = solution[i] + deltas[i];
//			double lower = solution[i] - deltas[i];
//			solution[i] = rand.nextDouble() * (upper - lower) + lower;
//		}
//		System.out.println(Arrays.toString(solution));
	}
	
}
