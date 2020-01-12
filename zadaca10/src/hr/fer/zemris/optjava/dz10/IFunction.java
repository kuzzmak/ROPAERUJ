package hr.fer.zemris.optjava.dz10;

public interface IFunction {
	
	Double[] getConstraints();
	double valueAt(Double[] point, boolean minimize);
	int getDimension();
}
