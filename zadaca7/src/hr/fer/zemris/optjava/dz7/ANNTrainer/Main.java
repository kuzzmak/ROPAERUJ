package hr.fer.zemris.optjava.dz7.ANNTrainer;

import java.util.Arrays;

import hr.fer.zemris.optjava.dz7.NN.NeuralNet;

public class Main {

	public static void main(String[] args) {
		
		String path = "data\\07-iris-formatirano.data";
		Dataset data = new Dataset(path);
		
		int[] architecture = new int[] {4,5,3};
		NeuralNet nn = new NeuralNet(architecture, data);
		int populationSize = 40;
		PSO alg = new PSO(populationSize, nn.getNumOfWeights(), nn);
		
		
		nn.setWeights(alg.run());
	
		double[] prediction = nn.predict(new double[] {5.1,3.5,1.4,0.2});
		System.out.println("prediction1: " + Arrays.toString(prediction));
		prediction = nn.predict(new double[] {4.9,3.0,1.4,0.2});
		System.out.println("prediction2: " + Arrays.toString(prediction));
		prediction = nn.predict(new double[] {6.3,3.3,6.0,2.5});
		System.out.println("prediction3: " + Arrays.toString(prediction));
		
	}
}
