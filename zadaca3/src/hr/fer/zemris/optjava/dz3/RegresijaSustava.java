package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;
import java.util.Random;

public class RegresijaSustava {
	
	public static void main(String[] args) {

		Random rand = new Random();
		// broj varijabli
		int n = 6;
		String path = "02-zad-prijenosna.txt";
//		int[] nBits = new int[] {30,30,30,30,30,30};
		int[] nBits = new int[] {10,10,10,10,10,10};
 		double[] mins = new double[] {-5,-5,-5,-5,-5,-5};
		double[] maxs = new double[] {8,8,8,8,8,8};
		double[] deltas = new double[] {0.1,0.1,0.1,0.1,0.1,0.1};
		SingleObjectiveSolution solution;
		IDecoder<SingleObjectiveSolution> decoder = new NaturalBinaryDecoder(mins, maxs, nBits, n);
		INeighbourhood<SingleObjectiveSolution> neighbourhood = new BitVectorNeighbourhood();
		// pocetno rjesenje
		int numOfBits = 0;
		for(int i = 0; i < nBits.length; i++) {
			numOfBits += nBits[i];
		}
		BitVectorSolution startWith = new BitVectorSolution(numOfBits);
		
		// optimizacijaks funkcija
		IFunction function = new OptimizationFunction(path);
		startWith.randomize(rand);
		boolean minimize = true;
		
		double alpha = 0.99d;
		double tInitial = 1000;
		int innerLimit = 1500;
		int outerLimit = 5000;
		
		ITempSchedule schedule = new GeometricTempSchedule(alpha, tInitial, innerLimit, outerLimit);
	//	double[][] matrixData = function.getMatrixData();
	//	for(int i = 0; i < 20; i++) {
	//		for(int j = 0; j < 6; j++) {
	//			System.out.printf("%f ", matrixData[i][j]);
	//		}
	//		System.out.println();
	//	}
		
		SimulatedAnnealing<SingleObjectiveSolution> alg = 
				new SimulatedAnnealing<SingleObjectiveSolution>(decoder, neighbourhood, schedule, startWith, function, minimize, rand);
		
		solution = alg.run();
		double[] sol = decoder.decode(solution);
		
		System.out.println(Arrays.toString(sol));
	
	
	}
	
}
