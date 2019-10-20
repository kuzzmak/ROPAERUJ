package hr.fer.zemris.optjava.dz3;

import java.util.Random;

public class DoubleArrayNormNeighbourhood implements INeighbourhood<DoubleArraySolution>{

	private double[] deltas;
	private Random rand;
	
	public DoubleArrayNormNeighbourhood(double[] deltas) {
		this.deltas = deltas;
		rand = new Random();
	}
	
	@Override
	public DoubleArraySolution randomNeighbour(DoubleArraySolution obj) {
		double[] solution = obj.values;
		DoubleArraySolution nextNeighbour = new DoubleArraySolution(solution.length);
		
		for(int i = 0; i < solution.length; i++) {
			solution[i] = rand.nextGaussian() * deltas[i];
		}
		
		nextNeighbour.setValues(solution);
		return nextNeighbour;
	}

}
