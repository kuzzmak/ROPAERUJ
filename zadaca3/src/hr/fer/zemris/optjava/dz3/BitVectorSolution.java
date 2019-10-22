package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;
import java.util.Random;

public class BitVectorSolution extends SingleObjectiveSolution{

	private byte[] bits;
	
	public BitVectorSolution(int n) {
		bits = new byte[n];
	}
	
	public BitVectorSolution(byte[] bits) {
		this.bits = bits;
	}
	
	public BitVectorSolution newLikeThis() {
		return new BitVectorSolution(this.bits.length);
	}
	
	public BitVectorSolution duplicate() {
		BitVectorSolution vec = new BitVectorSolution(this.bits.length);
		for(int i = 0; i < this.bits.length; i++) {
			vec.bits[i] = this.bits[i];
		}
		return vec;
	}
	
	public void randomize(Random random) {
		int n = bits.length;
		for (int i = 0; i < n; i++) {
			bits[i] = (byte) (random.nextBoolean() ? 1 : 0);
		}
	}
	
	public byte[] getBits() {
		return bits;
	}

	public void setBits(byte[] bits) {
		this.bits = bits;
	}

	@Override
	public String toString() {
		return "BitVectorSolution [bits=" + Arrays.toString(bits) + "]";
	}
	
	
}
