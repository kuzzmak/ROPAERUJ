package hr.fer.zemris.optjava.dz5.part1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnePointCrossover implements ICrossover {

	@Override
	public List<BitVector> cross(BitVector parent1, BitVector parent2, Random rand) {
		
		List<BitVector> children = new ArrayList<>();
		byte[] bits1 = new byte[parent1.size()];
		byte[] bits2 = new byte[parent2.size()];
		
		int index = rand.nextInt(parent1.size());
		
		
		for(int i = 0; i < index; i++) {
			bits1[i] = parent1.getBits()[i];
		}
		for(int i = index; i < bits1.length; i++) {
			bits1[i] = parent2.getBits()[i];
		}
	
		for(int i = 0; i < index; i++) {
			bits2[i] = parent2.getBits()[i];
		}
		for(int i = index; i < bits2.length; i++) {
			bits2[i] = parent1.getBits()[i];
		}
		
		children.add(new BitVector(bits1));
		children.add(new BitVector(bits2));
		return children;
	}
}
