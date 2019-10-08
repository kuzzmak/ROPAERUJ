package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BitVector {
	
	private int numberOfBits;
	protected List<Boolean> bits;
	
	public BitVector(Random rand, int numberOfBits) {
		this.bits = new ArrayList<>();
		this.numberOfBits = numberOfBits;
		for(int i = 0; i < numberOfBits; i++) {
			bits.add(rand.nextBoolean());
		}
	}
	
	public BitVector(List<Boolean> bits) {
		this.bits = new ArrayList<>(bits);
		this.numberOfBits = bits.size();
	}
	
	public BitVector(int n) {
		StringBuilder sb = new StringBuilder(Integer.toBinaryString(n));
		this.bits = new ArrayList<>();
		this.numberOfBits = sb.length();
		for(int i = 0; i < this.numberOfBits; i++) {
			if(sb.charAt(i) == '1') {
				this.bits.add(true);
			}else {
				this.bits.add(false);
			}
		}
	}

	public boolean get(int index) {
		return this.bits.get(index);
	}
	
	public int getSize() {
		return this.numberOfBits;
	}
	
	public MutableBitVector copy() {
		return new MutableBitVector(bits);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for(int index = 0; index < this.numberOfBits; index++) {
			if(bits.get(index)) {
				sb.append("1");
			}else {
				sb.append(0);
			}
		}
		return sb.toString();
	}
}


