package hr.fer.zemris.optjava.dz7.NN;

public class Sigmoid implements IActivationFunction {

	public Sigmoid() {
		
	}
	
	@Override
	public double valueAt(double value) {
		return 1. / (1 + Math.exp(-value)); 
	}

}
