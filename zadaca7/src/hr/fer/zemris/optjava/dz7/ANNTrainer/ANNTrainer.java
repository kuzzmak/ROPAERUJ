package hr.fer.zemris.optjava.dz7.ANNTrainer;

import hr.fer.zemris.optjava.dz7.DATA.Dataset;
import hr.fer.zemris.optjava.dz7.NN.NeuralNet;
import hr.fer.zemris.optjava.dz7.PSO.PSO;

public class ANNTrainer {

	public static void main(String[] args) {
		
		String path = "data\\07-iris-formatirano.data";
		Dataset data = new Dataset(path);
		int[] architecture = new int[] {4,5,3};
		NeuralNet nn = new NeuralNet(architecture, data);
		int populationSize = 40;
		PSO alg = new PSO(populationSize, nn.getNumOfWeights(), nn);
		
		
		
		
	}
	
}
