package hr.fer.zemris.optjava.dz9.Functions;

import java.util.Map;

public interface IFunction {
	
	double[] getConstraints();
	double valueAt(double[] point);
	int getDimension();
}
