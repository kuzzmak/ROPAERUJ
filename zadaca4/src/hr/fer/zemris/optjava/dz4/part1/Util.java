package hr.fer.zemris.optjava.dz4.part1;

import java.util.Map;
import java.util.TreeMap;

public class Util {

	/**
	 * Funkcija za inicijalno stvaranje populacije rjesenja
	 * 
	 * @param function funkcija za koju se generiraju rjesenja
	 * @param decoder dekoder rjesenja
	 * @return mapa populacije
	 */
	public static TreeMap<SingleObjectiveSolution, Double> makePopulation() {

		TreeMap<SingleObjectiveSolution, Double> population = new TreeMap<>(GeneticAlgorithm.comp.reversed());

		for (int i = 0; i < GeneticAlgorithm.VEL_POP; i++) {
			DoubleArraySolution temp = new DoubleArraySolution(GeneticAlgorithm.N);
			temp.randomize(GeneticAlgorithm.rand, GeneticAlgorithm.min, GeneticAlgorithm.max);
			temp.setValue(GeneticAlgorithm.function.valueAt(GeneticAlgorithm.decoder.decode(temp)), GeneticAlgorithm.minimize);
			population.put(temp, temp.getFitness());
		}
		
		return population;
	}
	
	/**
	 * Funkcija za normalizaciju vjerojatnosti odabira pojedinog rjesenja
	 * 
	 * @param population populacija jedinki iz koje se bira
	 */
	public static void normalize(TreeMap<SingleObjectiveSolution, Double> population) {
		double sum = 0;
		for (Map.Entry<SingleObjectiveSolution, Double> entry : population.entrySet()) {
			sum += entry.getValue();
		}
		for (Map.Entry<SingleObjectiveSolution, Double> entry : population.entrySet()) {
			population.put(entry.getKey(), entry.getValue() / sum);
		}
	}

	/**
	 * Funkcija za mutaciju pojedinog rjesenja
	 * 
	 * @param child rjesenje koje se mutira
	 */
	public static void mutate(SingleObjectiveSolution child) {

		for (int i = 0; i < GeneticAlgorithm.N; i++) {
			((DoubleArraySolution) child).values[i] += GeneticAlgorithm.rand.nextGaussian() * GeneticAlgorithm.sigma;
		}
		child.setValue(GeneticAlgorithm.function.valueAt(GeneticAlgorithm.decoder.decode(child)), GeneticAlgorithm.minimize);
	}
	
	/**
	 * Funkcija za ispis cjelokupne trenutne populacije
	 * 
	 * @param population mapa populacije za ispis
	 */
	public static void populationPrint(TreeMap<SingleObjectiveSolution, Double> population) {
		for(Map.Entry<SingleObjectiveSolution, Double> entry: population.entrySet()) {
			System.out.println(entry.getKey() + "  " + entry.getValue());
		}
		System.out.println();
	}
}
