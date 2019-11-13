package hr.fer.zemris.optjava.dz6;

import java.util.Arrays;

public class TSPSolver {

	public static void main(String[] args) {
		
		String path = args[0];
		int k = Integer.parseInt(args[1]);
		int populationSize = Integer.parseInt(args[2]);
		int numOfIterations = Integer.parseInt(args[3]);
		
		MMAS alg = new MMAS(path, k, populationSize, numOfIterations);
		System.out.println(Arrays.toString(alg.run()));
	}
}
