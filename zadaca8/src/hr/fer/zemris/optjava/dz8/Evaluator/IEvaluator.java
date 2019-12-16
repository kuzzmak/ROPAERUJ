package hr.fer.zemris.optjava.dz8.Evaluator;

public interface IEvaluator {
	
	public double evaluate(double[] x);
	public int getNumOfWeights();
	public double[] getContext();
}
