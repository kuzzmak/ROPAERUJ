package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;
import java.util.Random;

public class RegresijaSustava {

	public static void main(String[] args) {

		// broj varijabli
		int n = 6;
		// String path = "02-zad-prijenosna.txt";
		double min = -3;
		double max = 7;
		// broj bitova po varijabli za bitvector rjesenje
		int[] nBits = new int[] { 30, 30, 30, 30, 30, 30 };
		double[] mins = new double[n];
		double[] maxs = new double[n];
		Arrays.fill(mins, min);
		Arrays.fill(maxs, max);
		double[] deltas = new double[n];
		double delta = 1;
		Arrays.fill(deltas, delta);
	
		double alpha = 0.99d;
		double tInitial = 1000;
		int innerLimit = 2000;
		int outerLimit = 5000;

		// argmenti komandne linije
		String path = args[0];
		String solutionType = args[1];

		IDecoder<SingleObjectiveSolution> decoder;
		INeighbourhood<SingleObjectiveSolution> neighbourhood;
		SingleObjectiveSolution solution;
		ITempSchedule schedule;

		SingleObjectiveSolution startWith;

		IFunction function = new OptimizationFunction(path);

		boolean minimize = true;
		Random rand = new Random();

		// ----------------------------------------------------

		String[] temp = solutionType.split(":");
		schedule = new GeometricTempSchedule(alpha, tInitial, innerLimit, outerLimit);

		if (temp[0].equals("decimal")) {

			startWith = new DoubleArraySolution(n);
			((DoubleArraySolution) startWith).randomize(rand, mins, maxs);
			decoder = new PassThroughDecoder();
			neighbourhood = new DoubleArrayNormNeighbourhood(deltas);
		} else {
			decoder = new NaturalBinaryDecoder(mins, maxs, nBits, n);
			neighbourhood = new BitVectorNeighbourhood();
			int numOfBits = 0;
			for (int i = 0; i < nBits.length; i++) {
				numOfBits += nBits[i];
			}
			startWith = new BitVectorSolution(numOfBits);
		}

		SimulatedAnnealing<SingleObjectiveSolution> alg = new SimulatedAnnealing<SingleObjectiveSolution>(decoder,
				neighbourhood, schedule, startWith, function, minimize, rand);

		solution = alg.run();

		System.out.println(solution);
	}

}
