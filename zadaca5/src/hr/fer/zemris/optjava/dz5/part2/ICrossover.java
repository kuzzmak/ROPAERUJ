package hr.fer.zemris.optjava.dz5.part2;

import java.util.List;
import java.util.Random;

public interface ICrossover {
	List<Chromosome> cross(Chromosome c1, Chromosome c2, Random rand);
}
