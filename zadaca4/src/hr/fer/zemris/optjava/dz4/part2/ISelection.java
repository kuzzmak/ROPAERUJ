package hr.fer.zemris.optjava.dz4.part2;

import java.util.List;
import java.util.Random;

public interface ISelection {
	Chromosome select(List<Chromosome> cList, Random rand, int numberOfSelections, boolean best);
}
