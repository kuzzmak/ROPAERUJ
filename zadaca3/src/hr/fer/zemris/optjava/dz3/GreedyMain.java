package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;
import java.util.Random;

public class GreedyMain {
	public static void main(String[] args) {
		
		Random rand = new Random();
		// broj varijabli
		int n = 6;
		String path = "02-zad-prijenosna.txt";
		int[] nBits = new int[] {20,20,20,20,20,20};
		double[] mins = new double[] {-5,-5,-5,-5,-5,-5};
		double[] maxs = new double[] {8,8,8,8,8,8};
		double[] deltas = new double[] {0.1,0.1,0.1,0.1,0.1,0.1};
		SingleObjectiveSolution solution;
		IDecoder<SingleObjectiveSolution> decoder = new GreyBinaryDecoder(mins, maxs, nBits, n);
		INeighbourhood<SingleObjectiveSolution> neighbourhood = new BitVectorNeighbourhood();
		// pocetno rjesenje
		BitVectorSolution startWith = new BitVectorSolution(n * 20);
		
		// optimizacijaks funkcija
		IFunction function = new OptimizationFunction(path);
		startWith.randomize(rand);
		boolean minimize = true;
		
		
		
//		double[][] matrixData = function.getMatrixData();
//		for(int i = 0; i < 20; i++) {
//			for(int j = 0; j < 6; j++) {
//				System.out.printf("%f ", matrixData[i][j]);
//			}
//			System.out.println();
//		}
		
		GreedyAlgorithm<SingleObjectiveSolution> alg = 
				new GreedyAlgorithm<SingleObjectiveSolution>(decoder, neighbourhood, startWith, function, minimize);
		
		solution = alg.run();
		double[] sol = decoder.decode(solution);
		
		System.out.println(Arrays.toString(sol));
		
		
		
	}
}
