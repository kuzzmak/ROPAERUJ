package hr.fer.zemris.optjava.dz3;

import java.util.Random;

public class BitVectorSolution extends SingleObjectiveSolution{

	byte[] bits;
	
	public BitVectorSolution(int n) {
		bits = new byte[n];
	}
	
	BitVectorSolution newLikeThis() {
		return new BitVectorSolution(this.bits.length);
	}
	
	BitVectorSolution duplicate() {
		BitVectorSolution vec = new BitVectorSolution(this.bits.length);
		for(int i = 0; i < this.bits.length; i++) {
			vec.bits[i] = this.bits[i];
		}
		return vec;
	}
	
	void randomize(Random rand) {
		// TODO
	}
}
