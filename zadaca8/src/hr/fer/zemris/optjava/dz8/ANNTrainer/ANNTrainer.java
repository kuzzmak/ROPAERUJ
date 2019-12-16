package hr.fer.zemris.optjava.dz8.ANNTrainer;

import java.util.Random;

import hr.fer.zemris.optjava.dz8.Data.Dataset;
import hr.fer.zemris.optjava.dz8.ElmanNN.ElmanNeuralNet;
import hr.fer.zemris.optjava.dz8.Evaluator.ElmanEvaluator;
import hr.fer.zemris.optjava.dz8.Evaluator.IEvaluator;
import hr.fer.zemris.optjava.dz8.Evaluator.TDNNEvaluator;

public class ANNTrainer {
	
	static Random rand = new Random();
	

	public static void main(String[] args) {
		
		String path = "data\\08-Laser-generated-data.txt";
					  
		int window = 8;
		int numOfSamples = 600;
		
		Dataset data = new Dataset(path, window, numOfSamples);
		Dataset.load();
		
		int[] architecture = new int[] {window,5,1};
		
//		int[] architecture = new int[] {window,5,4,1};
		IEvaluator evaluator = new TDNNEvaluator(architecture, data);
//		IEvaluator evaluator = new ElmanEvaluator(architecture, data);
		String strategy = "DE/best/2/either-or";
		int populationSize = 100;
		int maxIterations = 500;
		double Cr = 0.98;
		double minError = 0.02;
		double minVal = -1;
		double maxVal = 1;
		
		DiffEvol alg = new DiffEvol(evaluator, strategy, populationSize, maxIterations, Cr, minError, minVal, maxVal);
		
		double[] solution = alg.run();
		System.exit(0);
		
	}
}
