package hr.fer.zemris.numeric;

public class f1b implements IHFunction{

	@Override
	public int numOfVariables() {
		return 2;
	}

	@Override
	public double valueAt(double[] v) {
		return Math.pow(v[0], 2) + Math.pow(v[1] - 1, 2);
	}

	@Override
	public double[] gradient(double[] v) {
		double[] gradient = new double[numOfVariables()];
		gradient[0] = 2 * v[0];
		gradient[1] = 2 * (v[1] - 1);
		return gradient;
	}

	@Override
	public double[][] hessian(double[] vector) {
		return new double[][] {{2,0}, {0,2}};
	}

}
