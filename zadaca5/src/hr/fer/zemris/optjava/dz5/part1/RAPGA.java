package hr.fer.zemris.optjava.dz5.part1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class RAPGA implements IOptAlgorithm {

	private int bitVectorSize;

	private int maxIterations = 500;
	private static int maxEffort = 100;
	private static int minPopSize = 20;
	private static int maxPopSize = 100;
	private double maxSelPress = 20;
	private double actSelPress = 1;
	private double compFactor = 0;

	// broj k u k-turnirsokj selekciji
	public static int numberOfParticipants = 4;

	private static Random rand = new Random();

	public static IFunction function = new OptimizationFunction();
	
	public RAPGA(int n) {
		this.bitVectorSize = n;
	}

	/**
	 * Funkcija za inicijalizaciju pocetne populacije
	 * 
	 * @param bitVectorSize velicina svake jedike
	 * @return pocetna populacija
	 */
	public static LinkedHashMap<BitVector, Double> initializePopulation(int bitVectorSize) {

		LinkedHashMap<BitVector, Double> population = new LinkedHashMap<>();

		while (population.size() < (minPopSize + maxPopSize) / 2) {
			BitVector b = new BitVector(bitVectorSize);
			b.randomize(rand);
			population.put(b, function.valueAt(b));
		}

		return population;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue(Comparator.reverseOrder()));

		Map<K, V> result = new LinkedHashMap<>();
		for (Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

	@Override
	public BitVector run() {

		// selekcija jednog roditelja
		ISelection tournament = new TournamentSelection();
		// operator krizanja
		ICrossover crossover = new UniformCrossover();
		// broj koji se potencira kako bi compFactor rastao od 0 prema 1 (compFactor = 1 - Math.pow(fact, 2))
		double fact = 0.99d;
		// broj predjenih iteracija
		int iter = 0;

		LinkedHashMap<BitVector, Double> population = initializePopulation(bitVectorSize);
		// lista mutacija kojima se pojedino dijete mutira ne bi li se postiglo jos bolje dijete
		List<IMutation> mutationList = new ArrayList<>();
		mutationList.add(new Mutation(0.01f, rand));
		mutationList.add(new Mutation(0.03f, rand));
		mutationList.add(new Mutation(0.05f, rand));

		while ((iter < maxIterations) && (actSelPress < maxSelPress)) {

			// jedinke bolje od roditelja
			LinkedHashMap<BitVector, Double> nextPopulation = new LinkedHashMap<>();
			// jedinke losije od roditelja
			LinkedHashMap<BitVector, Double> pool = new LinkedHashMap<>();

			// trud ulozen u stvaranje nove generacije jedinki
			int effort = 0;
			while ((nextPopulation.size() < maxPopSize) && (effort < maxEffort)) {
				effort++;
				// izbor roditelja
				BitVector parent1 = tournament.select(population, rand);
				BitVector parent2 = population.keySet().toArray(new BitVector[0])[rand.nextInt(population.size())];
				// djeca dobivena krizanjem
				List<BitVector> children = crossover.cross(parent1, parent2, rand);

				double parent1Fit = function.valueAt(parent1);
				double parent2Fit = function.valueAt(parent2);
				// za svako dijete se provede niz gore navedenih mutacija
				for (BitVector child : children) {
					for (IMutation mutation : mutationList) {
						
						BitVector mutated = mutation.mutate(child);
						double mutatedFit = function.valueAt(mutated);
						double tFit = parent2Fit + Math.abs(parent2Fit - parent1Fit) * compFactor;
						
						if (mutatedFit < tFit) {
								pool.put(mutated, mutatedFit);
						} else {
							if (nextPopulation.size() < maxPopSize) {
								nextPopulation.put(mutated, mutatedFit);
							}else {
								nextPopulation = (LinkedHashMap<BitVector, Double>) sortByValue(nextPopulation);
								BitVector worsInNextPop = nextPopulation.keySet().toArray(new BitVector[0])[nextPopulation.size() - 1];
								if(mutatedFit > function.valueAt(worsInNextPop)) {
									nextPopulation.remove(worsInNextPop);
									nextPopulation.put(mutated, mutatedFit);
								}
							}
						}
					}
				}
			}

			actSelPress = (nextPopulation.size() + pool.size()) / population.size();

			// ako sljedeca populacija jos nije dovoljno velika, dodamo nove clanove iz poola
			if (nextPopulation.size() < minPopSize) {
				pool = (LinkedHashMap<BitVector, Double>) sortByValue(pool);
				Iterator<Map.Entry<BitVector, Double>> it = pool.entrySet().iterator();
				while (nextPopulation.size() < minPopSize) {
					Map.Entry<BitVector, Double> entry = it.next();
					nextPopulation.put(entry.getKey(), entry.getValue());
				}
			}
			compFactor = 1 - Math.pow(fact, iter);
			population = nextPopulation;
			iter++;
		}
		
		population = (LinkedHashMap<BitVector, Double>) sortByValue(population);
		return population.keySet().toArray(new BitVector[0])[0];
	}
}
