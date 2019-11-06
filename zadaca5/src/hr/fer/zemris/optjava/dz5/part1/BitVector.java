package hr.fer.zemris.optjava.dz5.part1;

import java.util.Arrays;
import java.util.Random;

public class BitVector implements Comparable<BitVector>{

	private byte[] bits;
	
	public BitVector(int n) {
		bits = new byte[n];
	}
	
	public BitVector(byte[] bits) {
		this.bits = bits;
	}
	
	public BitVector newLikeThis() {
		return new BitVector(this.bits.length);
	}
	
	public BitVector duplicate() {
		BitVector vec = new BitVector(this.bits.length);
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
		return Arrays.toString(bits);
	}
	
	public int size() {
		return this.bits.length;
	}

	@Override
	public int compareTo(BitVector o) {
		for(int i = 0; i < bits.length; i++) {
			if(this.bits[i] != o.bits[i]) return 1;
		}
		return 0;
	}
	
}
