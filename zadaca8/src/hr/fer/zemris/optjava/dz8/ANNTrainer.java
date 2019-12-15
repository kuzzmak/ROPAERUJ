package hr.fer.zemris.optjava.dz8;

import java.util.Random;

import hr.fer.zemris.optjava.dz8.Data.Dataset;
import hr.fer.zemris.optjava.dz8.Evaluator.IEvaluator;
import hr.fer.zemris.optjava.dz8.Evaluator.TDNNEvaluator;
import hr.fer.zemris.optjava.dz8.Strategy.Strategy;

public class ANNTrainer {
	
	static Random rand = new Random();
	

	public static void main(String[] args) {
		
		String path = "data\\08-Laser-generated-data.txt";
					  
		int window = 8;
		int numOfSamples = 600;
		
		Dataset data = new Dataset(path, window, numOfSamples);
		Dataset.load();
				
		int[] architecture = new int[] {window,10,1};
		
		IEvaluator evaluator = new TDNNEvaluator(architecture, data);
		String strategy = "DE/rand/2/bin";
		int populationSize = 40;
		int maxIterations = 500;
		double Cr = 0.2;
		double minError = 0.02;
		double minVal = -1;
		double maxVal = 1;
		
		DiffEvol alg = new DiffEvol(evaluator, strategy, populationSize, maxIterations, Cr, minError, minVal, maxVal);
		
		double[] solution = alg.run();
		
		
	}
}
