package hr.fer.zemris.optjava.dz8.Evaluator;

import hr.fer.zemris.optjava.dz8.Data.Dataset;
import hr.fer.zemris.optjava.dz8.ElmanNN.ElmanNeuralNet;

public class ElmanEvaluator implements IEvaluator {

	private ElmanNeuralNet net;
	
	public ElmanEvaluator(int[] architecture, Dataset data) {
		this.net = new ElmanNeuralNet(architecture, data);
	}
	
	@Override
	public double evaluate(double[] x) {
		
		net.setWeights(x);
		return net.calculateError();
	}

	@Override
	public int getNumOfWeights() {
		return net.getNumOfWeights();
	}

}
