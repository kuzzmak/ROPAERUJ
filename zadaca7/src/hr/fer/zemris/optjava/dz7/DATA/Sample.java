package hr.fer.zemris.optjava.dz7.DATA;

import java.util.Arrays;

public class Sample {

	private double[] x;
	private double[] y;
	
	public Sample(double[] x, double[] y) {
		this.x = x;
		this.y = y;
	}
	
	public double[] getX() {
		return x;
	}


	public double[] getY() {
		return y;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append(Arrays.toString(this.x)).append(" : ").append(Arrays.toString(this.y));
		return sb.toString();
	}
}
