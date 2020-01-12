package hr.fer.zemris.optjava.dz10;

import java.util.List;

public interface MOOPProblem {
	
	public Double[] evaluate(Double[] point);
	public int getDimension();
	public void add(IFunction f);
	public List<IFunction> getObjectiveFunctions();

}
