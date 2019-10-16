package hr.fer.zemris.numeric;

public class f2b implements IHFunction{

	@Override
	public int numOfVariables() {
		return 2;
	}

	@Override
	public double valueAt(double[] v) {
		return Math.pow(v[0] - 1, 2) + 10 * Math.pow(v[1] - 2, 2);
	}

	@Override
	public double[] gradient(double[] v) {
		double[] gradient = new double[numOfVariables()];
		gradient[0] = 2 * (v[0] - 1);
		gradient[1] = 20 * (v[1] - 2);
		return gradient; 
	}

	@Override
	public double[][] hessian(double[] vector) {
		return new double[][] {{2,0}, {0,20}};
	}

	@Override
	public String toString() {
		return "2b";
	}

}
