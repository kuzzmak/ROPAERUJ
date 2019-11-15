package hr.fer.zemris.optjava.dz7.ANNTrainer;

import java.util.Arrays;

import hr.fer.zemris.optjava.dz7.NN.NeuralNet;

public class Main {

	public static void main(String[] args) {
		
		String path = "data\\07-iris-formatirano.data";
		Dataset data = new Dataset(path);
		
		int[] architecture = new int[] {1,5,3,1};
		NeuralNet nn = new NeuralNet(architecture, data);
		
		System.out.println(nn);
	}
}
