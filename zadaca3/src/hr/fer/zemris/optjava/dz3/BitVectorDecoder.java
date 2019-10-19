package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;

public abstract class BitVectorDecoder implements IDecoder<SingleObjectiveSolution>{

	private double[] mins;
	private double[] maxs;
	private int[] bits;
	private int n;
	private int totalBits = 0;
	
	public BitVectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
		this.mins = mins;
		this.maxs = maxs;
		this.bits = bits;
		this.n = n;
		
		for(int i = 0; i < bits.length; i++) {
			totalBits += bits[i];
		}
	}
	
	public BitVectorDecoder(double min, double max, int nBits, int n) {
		this.n = n;
		this.mins = new double[n];
		this.maxs = new double[n];
		this.bits = new int[n];
		Arrays.fill(this.mins, min);
		Arrays.fill(this.maxs, max);
		Arrays.fill(this.bits, nBits);
	}

	public int getTotalBits() {
		return totalBits;
	}
	
	public int getDimensions() {
		return n;
	}
	
}
