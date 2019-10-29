package hr.fer.zemris.optjava.dz4.part1;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Util {
	// konstante za pocetnu inicjalizaciju rjesenja
	public static int min = -10;
	public static int max = 10;
	// minimiziramo li ili maksimiziramo funkciju
	public static boolean minimize = true;
	// konstanta kod blx-a krizanja
	public static double alpha = 0.2d;

	/**
	 * Funkcija za inicijalno stvaranje populacije rjesenja
	 * 
	 * @param function funkcija za koju se generiraju rjesenja
	 * @param decoder  dekoder rjesenja
	 * @return mapa populacije
	 */
	public static TreeMap<SingleObjectiveSolution, Double> makePopulation() {

		TreeMap<SingleObjectiveSolution, Double> population = new TreeMap<>(Util.comp.reversed());

		for (int i = 0; i < GenAlg.populationSize; i++) {
			DoubleArraySolution temp = new DoubleArraySolution(GenAlg.n);
			temp.randomize(GenAlg.rand, min, max);
			temp.setValue(GenAlg.function.valueAt(GenAlg.decoder.decode(temp)), minimize);
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

		for (int i = 0; i < GenAlg.n; i++) {
			((DoubleArraySolution) child).values[i] += GenAlg.rand.nextGaussian() * GenAlg.sigma;
		}
		child.setValue(GenAlg.function.valueAt(GenAlg.decoder.decode(child)), minimize);
	}

	/**
	 * Funkcija za ispis cjelokupne trenutne populacije
	 * 
	 * @param population mapa populacije za ispis
	 */
	public static void populationPrint(TreeMap<SingleObjectiveSolution, Double> population) {
		for (Map.Entry<SingleObjectiveSolution, Double> entry : population.entrySet()) {
			System.out.println(entry.getKey() + "  " + entry.getValue());
		}
		System.out.println();
	}

	/**
	 * Funkcija za krizanje dva kromosoma roditelja
	 * 
	 * @param parent01 prvi roditelj
	 * @param parent02 drugi roditelj
	 * @param rand 
	 * @return dobiveno dijete
	 */
	public static SingleObjectiveSolution BLXa(SingleObjectiveSolution parent01, SingleObjectiveSolution parent02,
			Random rand) {

		DoubleArraySolution parent1 = (DoubleArraySolution) parent01;
		DoubleArraySolution parent2 = (DoubleArraySolution) parent02;

		DoubleArraySolution child = parent1.duplicate();

		int childLength = child.values.length;
		for (int i = 0; i < childLength; i++) {

			double di = Math.abs(parent1.values[i] - parent2.values[i]);

			double lower = Math.min(parent1.values[i], parent2.values[i]) - alpha * di;
			double upper = Math.max(parent1.values[i], parent2.values[i]) + alpha * di;

			double u = rand.nextDouble() * (upper - lower) + lower;

			child.values[i] = u;

		}

		return child;
	}

	// takav komparator da su rjesenja s boljim fitnesom uvijek na pocetku mape
	public static Comparator<SingleObjectiveSolution> comp = new Comparator<SingleObjectiveSolution>() {

		@Override
		public int compare(SingleObjectiveSolution o1, SingleObjectiveSolution o2) {
			if (o1.getFitness() > o2.getFitness())
				return 1;
			if (o1.getFitness() < o2.getFitness())
				return -1;
			else
				return 0;
		}
	};
}
