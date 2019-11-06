package hr.fer.zemris.optjava.dz5.part2;

import java.util.Random;

public interface IMutation {
	Chromosome mutate(Chromosome c, Random rand);
}
