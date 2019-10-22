package hr.fer.zemris.optjava.dz3;

import java.util.Random;

public class DoubleArrayNormNeighbourhood implements INeighbourhood<SingleObjectiveSolution> {

	private double[] deltas;
	private Random rand;

	public DoubleArrayNormNeighbourhood(double[] deltas) {
		this.deltas = deltas;
		rand = new Random();
	}

	@Override
	public DoubleArraySolution randomNeighbour(SingleObjectiveSolution obj) {
		DoubleArraySolution solution = (DoubleArraySolution) obj.duplicate();
		//DoubleArraySolution nextNeighbour = new DoubleArraySolution(solution.values.length);

		float p = 0.1f;

		for (int i = 0; i < solution.values.length; i++) {
			if (rand.nextFloat() < p) {
				solution.values[i] += rand.nextGaussian() * deltas[i];
			}
		}

		//nextNeighbour.setValues(solution.values);
		return solution;
	}

}
