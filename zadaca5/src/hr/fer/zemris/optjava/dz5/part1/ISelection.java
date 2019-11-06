package hr.fer.zemris.optjava.dz5.part1;

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.TreeMap;

public interface ISelection {
	BitVector select(LinkedHashMap<BitVector, Double> population, Random rand);
}
