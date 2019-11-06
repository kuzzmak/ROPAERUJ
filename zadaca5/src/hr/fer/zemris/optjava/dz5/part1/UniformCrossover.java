package hr.fer.zemris.optjava.dz5.part1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UniformCrossover implements ICrossover {

	private static boolean[] byteTotBoolean(byte[] bits) {

		boolean[] result = new boolean[bits.length];

		for (int i = 0; i < result.length; i++) {

			if (bits[i] == 1) {
				result[i] = true;
			} else
				result[i] = false;
		}
		return result;
	}

	private static byte[] booleanToByte(boolean[] bool) {

		byte[] bits = new byte[bool.length];

		for (int i = 0; i < bits.length; i++) {

			if (bool[i]) {
				bits[i] = 1;
			} else
				bits[i] = 0;
		}
		return bits;
	}

	private static BitVector logicalOr(BitVector b1, BitVector b2) {

		boolean[] result = new boolean[b1.size()];
		byte[] bits1 = b1.getBits();
		byte[] bits2 = b2.getBits();
		
		boolean[] bool1 = byteTotBoolean(bits1);
		boolean[] bool2 = byteTotBoolean(bits2);

		for (int i = 0; i < result.length; i++) {
			result[i] = bool1[i] || bool2[i];
		}
		return new BitVector(booleanToByte(result));
	}
	
	private static BitVector logicalAnd(BitVector b1, BitVector b2) {

		boolean[] result = new boolean[b1.size()];
		byte[] bits1 = b1.getBits();
		byte[] bits2 = b2.getBits();
		
		boolean[] bool1 = byteTotBoolean(bits1);
		boolean[] bool2 = byteTotBoolean(bits2);

		for (int i = 0; i < result.length; i++) {
			result[i] = bool1[i] && bool2[i];
		}
		return new BitVector(booleanToByte(result));
	}
	
	private static BitVector logicalXor(BitVector b1, BitVector b2) {

		boolean[] result = new boolean[b1.size()];
		byte[] bits1 = b1.getBits();
		byte[] bits2 = b2.getBits();
		
		boolean[] bool1 = byteTotBoolean(bits1);
		boolean[] bool2 = byteTotBoolean(bits2);

		for (int i = 0; i < result.length; i++) {
			result[i] = bool1[i] ^ bool2[i];
		}
		return new BitVector(booleanToByte(result));
	}
	
	private static BitVector logicalNot(BitVector b1) {
		boolean[] result = new boolean[b1.size()];
		byte[] bits1 = b1.getBits();
		boolean[] bool1 = byteTotBoolean(bits1);
		
		for (int i = 0; i < result.length; i++) {
			result[i] = !bool1[i];
		}
		return new BitVector(booleanToByte(result));
	}
	
	@Override
	public List<BitVector> cross(BitVector parent1, BitVector parent2, Random rand) {
		
		List<BitVector> children = new ArrayList<>();
		BitVector parentOne = parent1.newLikeThis();
		BitVector parentTwo = parent2.newLikeThis();
		BitVector randomBitVector = new BitVector(parent1.size());
		randomBitVector.randomize(rand);
		
		parentOne = logicalOr(logicalAnd(parent1, parent2), logicalAnd(randomBitVector, logicalXor(parent1, parent2)));
		parentTwo = logicalOr(logicalAnd(parent1, parent2), logicalAnd(logicalNot(randomBitVector), logicalXor(parent1, parent2)));
		
		children.add(parentOne);
		children.add(parentTwo);
		return children;
	}
}
