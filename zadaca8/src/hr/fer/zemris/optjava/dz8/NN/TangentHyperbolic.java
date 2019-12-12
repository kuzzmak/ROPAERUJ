package hr.fer.zemris.optjava.dz8.NN;

public class TangentHyperbolic implements IActivationFunction {

	public TangentHyperbolic() {
		
	}
	
	@Override
	public double valueAt(double value) {
		return (Math.pow(Math.E, value) - Math.pow(Math.E, -value)) 
				/ (Math.pow(Math.E, value) + Math.pow(Math.E, -value));
	}

}
