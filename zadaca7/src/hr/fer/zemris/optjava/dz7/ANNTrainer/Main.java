package hr.fer.zemris.optjava.dz7.ANNTrainer;

import java.util.Arrays;

import hr.fer.zemris.optjava.dz7.NN.NeuralNet;

public class Main {

	public static void main(String[] args) {
		
		String path = "data\\07-iris-formatirano.data";
		Dataset data = new Dataset(path);
		
		int[] architecture = new int[] {4,3,3};
		NeuralNet nn = new NeuralNet(architecture, data);
		double[] weights = new double[nn.getNumOfWeights()];
		Arrays.fill(weights, -0.2);
		nn.setWeights(weights);
		System.out.println(nn.calculateError());
	
		
		
		
	}
}
