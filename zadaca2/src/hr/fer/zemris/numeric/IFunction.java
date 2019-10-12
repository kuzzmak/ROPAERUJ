package hr.fer.zemris.numeric;

public interface IFunction {
	public int numOfVariables();
	public double valueAt(double[] vector);
	public double[] gradient (double[] vector);
}
