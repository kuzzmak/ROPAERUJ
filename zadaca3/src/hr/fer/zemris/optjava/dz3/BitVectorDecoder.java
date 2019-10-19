package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;

public abstract class BitVectorDecoder implements IDecoder<SingleObjectiveSolution>{

	private double[] mins;
	private double[] maxs;
	private int[] nBits;
	private int n;
	private int totalBits = 0;
	
	public BitVectorDecoder(double[] mins, double[] maxs, int[] nBits, int n) {
		this.mins = mins;
		this.maxs = maxs;
		this.nBits = nBits;
		this.n = n;
		
		for(int i = 0; i < nBits.length; i++) {
			totalBits += nBits[i];
		}
	}
	
	public BitVectorDecoder(double min, double max, int nBits, int n) {
		this.n = n;
		this.mins = new double[n];
		this.maxs = new double[n];
		this.nBits = new int[n];
		Arrays.fill(this.mins, min);
		Arrays.fill(this.maxs, max);
		Arrays.fill(this.nBits, nBits);
	}

	public int getTotalBits() {
		return totalBits;
	}
	
	public int getDimensions() {
		return n;
	}
	
	public int[] getnBits() {
		return nBits;
	}
	
	public int getN() {
		return n;
	}

	public double[] getMins() {
		return mins;
	}

	public double[] getMaxs() {
		return maxs;
	}
}
