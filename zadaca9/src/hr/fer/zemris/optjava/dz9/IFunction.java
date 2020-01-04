package hr.fer.zemris.optjava.dz9;

public interface IFunction {
	
	double[] getConstraints();
	double valueAt(double[] point, boolean minimize);
	int getDimension();
}
