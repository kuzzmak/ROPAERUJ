package hr.fer.zemris.optjava.dz5.part1;

import java.util.Random;

public class Mutation implements IMutation {
	
	private float mutationRate;
	private Random rand;
	
	public Mutation(float mutationRate, Random rand) {
		this.mutationRate = mutationRate;
		this.rand = rand;
	}

	@Override
	public BitVector mutate(BitVector b) {

		byte[] bits = b.getBits();
		
		for(int i = 0; i < b.size(); i++) {
			if(mutationRate >= rand.nextFloat()) {
				bits[i] = (byte)(bits[i] == 1 ? 0 : 1);
			}
		}
		return new BitVector(bits);
	}
}
