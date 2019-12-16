package hr.fer.zemris.optjava.dz8.Evaluator;

import hr.fer.zemris.optjava.dz8.Data.Dataset;
import hr.fer.zemris.optjava.dz8.TDNN.NeuralNet;

public class TDNNEvaluator implements IEvaluator {
	
	private static NeuralNet net;
	
	public TDNNEvaluator(int[] architecture, Dataset data) {
		TDNNEvaluator.net = new NeuralNet(architecture, data);
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

	@Override
	public double[] getContext() {
		return null;
	}

}
