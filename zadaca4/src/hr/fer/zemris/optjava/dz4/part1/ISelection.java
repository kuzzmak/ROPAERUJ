package hr.fer.zemris.optjava.dz4.part1;

import java.util.Random;
import java.util.TreeMap;

public interface ISelection<T> {
	T select(TreeMap<SingleObjectiveSolution, Double> offspring, Random rand);
}
