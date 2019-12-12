package hr.fer.zemris.optjava.dz8;

import java.util.Arrays;

public class Sample{

	private double[] x;
	private double y;
	
	public Sample(double[] x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double[] getX() {
		return x;
	}


	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append(Arrays.toString(this.x)).append(" : ").append(this.y);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(x);
		long temp;
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sample other = (Sample) obj;
		if (!Arrays.equals(x, other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
}

