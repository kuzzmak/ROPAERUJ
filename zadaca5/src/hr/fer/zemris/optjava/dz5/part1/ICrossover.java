package hr.fer.zemris.optjava.dz5.part1;

import java.util.List;
import java.util.Random;

public interface ICrossover {
	List<BitVector> cross(BitVector parent1, BitVector parent2, Random rand);
}
