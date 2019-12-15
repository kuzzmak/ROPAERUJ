package hr.fer.zemris.optjava.dz8;

import java.util.Random;

import hr.fer.zemris.optjava.dz8.Data.Dataset;
import hr.fer.zemris.optjava.dz8.ElmanNN.ElmanNeuralNet;
import hr.fer.zemris.optjava.dz8.Evaluator.IEvaluator;
import hr.fer.zemris.optjava.dz8.Evaluator.TDNNEvaluator;

public class ANNTrainer {
	
	static Random rand = new Random();
	

	public static void main(String[] args) {
		
		String path = "data\\08-Laser-generated-data.txt";
					  
		int window = 1;
		int numOfSamples = 600;
		
		Dataset data = new Dataset(path, window, numOfSamples);
		Dataset.load();
				
//		int[] architecture = new int[] {1,2,1};
//		
//		ElmanNeuralNet net = new ElmanNeuralNet(architecture, data);
//		
//		System.out.println(net.calculateError());
		
		int[] architecture = new int[] {window,10,1};
		IEvaluator evaluator = new TDNNEvaluator(architecture, data);
		String strategy = "DE/best/2/bin";
		int populationSize = 40;
		int maxIterations = 500;
		double Cr = 0.98;
		double minError = 0.02;
		double minVal = -1;
		double maxVal = 1;
		
		DiffEvol alg = new DiffEvol(evaluator, strategy, populationSize, maxIterations, Cr, minError, minVal, maxVal);
		
		double[] solution = alg.run();
		
		
	}
}
