package hr.fer.zemris.optjava.dz4.part1;

import Jama.Matrix;

public interface IFunction {
	double valueAt(double[] v);
	Matrix getMatrixData();
}
