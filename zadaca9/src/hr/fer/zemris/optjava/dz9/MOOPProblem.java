package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.Functions.IFunction;

public interface MOOPProblem {
	
	public double[] evaluate(double[] point);
	public int getDimension();
	public void add(IFunction f);

}
