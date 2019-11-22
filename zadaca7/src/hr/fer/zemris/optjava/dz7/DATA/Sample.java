package hr.fer.zemris.optjava.dz7.DATA;

import java.util.Arrays;

public class Sample{

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

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(!(obj instanceof Sample)) return false;
		Sample s = (Sample)obj;
		for(int i = 0; i < s.y.length; i++) {
			if(this.x[i] != s.x[i]) return false;
			if(this.y[i] != s.y[i]) return false;
		}
		return true;
	}
}
