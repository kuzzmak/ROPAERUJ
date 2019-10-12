package hr.fer.zemris.numeric;

public interface IHFunction extends IFunction{
	public double[][] hessian(double[] formula);
}
