package hr.fer.zemris.optjava.dz9;

import java.util.List;

import hr.fer.zemris.optjava.dz9.Functions.IFunction;

public interface MOOPProblem {
	
	public double[] evaluate(double[] point);
	public int getDimension();
	public void add(IFunction f);
	public List<IFunction> getObjectiveFunctions();

}
