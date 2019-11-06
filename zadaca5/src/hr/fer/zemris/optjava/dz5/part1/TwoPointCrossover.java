package hr.fer.zemris.optjava.dz5.part1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwoPointCrossover implements ICrossover {

	@Override
	public List<BitVector> cross(BitVector parent1, BitVector parent2, Random rand) {

		List<BitVector> children = new ArrayList<>();
		byte[] bits1 = new byte[parent1.size()];
		byte[] bits2 = new byte[parent2.size()];

		int index1 = rand.nextInt(parent1.size());
		int index2 = rand.nextInt(parent1.size());

		int from = Math.min(index1, index2);
		int to = Math.max(index1, index2);

		// prvo dijete
		for (int i = 0; i < from; i++) {
			bits1[i] = parent2.getBits()[i];
		}

		for (int i = from; i < to; i++) {
			bits1[i] = parent1.getBits()[i];
		}

		for (int i = to; i < parent1.size(); i++) {
			bits1[i] = parent2.getBits()[i];
		}
		
		// drugo dijete
		for (int i = 0; i < from; i++) {
			bits2[i] = parent1.getBits()[i];
		}

		for (int i = from; i < to; i++) {
			bits2[i] = parent2.getBits()[i];
		}

		for (int i = to; i < parent1.size(); i++) {
			bits2[i] = parent1.getBits()[i];
		}
			
		children.add(new BitVector(bits1));
		children.add(new BitVector(bits2));
		return children;
	}
}
