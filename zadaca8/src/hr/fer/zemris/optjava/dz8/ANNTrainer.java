package hr.fer.zemris.optjava.dz8;

import java.util.Random;

import hr.fer.zemris.optjava.dz8.NN.NeuralNet;

public class ANNTrainer {
	
	static Random rand = new Random();
	

	public static void main(String[] args) {
		
		String path = "data\\08-Laser-generated-data.txt";
					  
		int window = 8;
		int numOfSamples = 600;
		
		Dataset dataset = new Dataset(path, window, numOfSamples);
		Dataset.load();
				
		int[] architecture = new int[] {window,10,5,1};
		NeuralNet net = new NeuralNet(architecture, dataset);
		
		int populationSize = 40;
		int maxIterations = 500;
		double minError = 0.02;
		double Cr = 0.5;
		ICrossover crossover = new UniformCrossover(Cr);
		double minVal = -1;
		double maxVal = 1;
		
		DiffEvol alg = new DiffEvol(populationSize, maxIterations, minError, net, crossover, minVal, maxVal);
		
		double[] solution = alg.run();
		
	}
}
