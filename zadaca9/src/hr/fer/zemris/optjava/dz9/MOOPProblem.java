package hr.fer.zemris.optjava.dz9;

import java.util.List;

public interface MOOPProblem {
	
	public double[] evaluate(double[] point);
	public int getDimension();
	public void add(IFunction f);
	public List<IFunction> getObjectiveFunctions();

}
