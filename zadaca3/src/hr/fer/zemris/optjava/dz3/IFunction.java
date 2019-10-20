package hr.fer.zemris.optjava.dz3;

import Jama.Matrix;

public interface IFunction {
	double valueAt(double[] v);
	Matrix getMatrixData();
}
