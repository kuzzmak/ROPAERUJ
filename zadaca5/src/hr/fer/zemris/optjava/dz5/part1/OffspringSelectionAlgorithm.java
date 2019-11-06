package hr.fer.zemris.optjava.dz5.part1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public class OffspringSelectionAlgorithm implements IOptAlgorithm {

	private int bitVectorSize = 50;

	private int maxIterations = 5000;
	private static int populationSize = 20;
	private double maxSelPress = 20;
	private double actSelPress = 1;
	private double succRatio = 0.5;
	private double compFactor = 0;
	private static float mutationRate = 0.05f;

	// broj k u k-turnirsokj selekciji
	public static int numberOfParticipants = 4;

	private static Random rand = new Random();

	public static IFunction function = new OptimizationFunction();

	public static LinkedHashMap<BitVector, Double> initializePopulation(int bitVectorSize) {

		LinkedHashMap<BitVector, Double> population = new LinkedHashMap<>();

		while (population.size() < populationSize) {
			BitVector b = new BitVector(bitVectorSize);
			b.randomize(rand);
			population.put(b, function.valueAt(b));
		}

		return population;
	}

	public static BitVector bestIndividual(LinkedHashMap<BitVector, Double> population) {

		BitVector best = population.keySet().toArray(new BitVector[0])[0];
		double bestFit = function.valueAt(best);

		for (Map.Entry<BitVector, Double> entry : population.entrySet()) {
			if (entry.getValue() > bestFit)
				best = entry.getKey();

		}
		return best;
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

		ISelection tournament = new TournamentSelection();
		ICrossover crossover = new UniformCrossover();

		double fact = 0.99d;

		int numOfIter = 0;

		LinkedHashMap<BitVector, Double> population = initializePopulation(bitVectorSize);

		while ((numOfIter < maxIterations) && (actSelPress < maxSelPress)) {
			System.out.println(numOfIter);
			LinkedHashMap<BitVector, Double> nextPop = new LinkedHashMap<>();
			LinkedHashMap<BitVector, Double> badChildrenPool = new LinkedHashMap<>();

			int poolChildrenCount = 0;
			int nextPopCount = 0;

			while ((nextPop.size() < population.size() * succRatio)
					&& (nextPop.size() + badChildrenPool.size() < population.size() * maxSelPress)) {

				// izbor roditelja
				BitVector parent1 = tournament.select(population, rand);
				BitVector parent2 = tournament.select(population, rand);
				// djeca dobivena krizanjem
				List<BitVector> children = crossover.cross(parent1, parent2, rand);
				
				BitVector child;
				if(function.valueAt(children.get(0)) > function.valueAt(children.get(1))) child = children.get(0);
				else child = children.get(1);
				
				// mutacija djeteta
				//mutation.mutate(child);
				double tFit = function.valueAt(parent2)
						+ Math.abs(function.valueAt(parent1) - function.valueAt(parent2)) * compFactor;

				if (function.valueAt(child) < tFit) {
					badChildrenPool.put(child, function.valueAt(child));
					poolChildrenCount++;
				} else {
					nextPop.put(child, function.valueAt(child));
					nextPopCount++;
//					}
				}

			}

			actSelPress = (nextPopCount + poolChildrenCount) / population.size();
			succRatio = (double) nextPopCount / (nextPopCount + poolChildrenCount);

			// ako nije niti jedno dijete lose dijete, da popunimo ostatak nove populacije
			// samo generiramo nove random vektore
			if (badChildrenPool.size() == 0) {
				while (nextPop.size() < populationSize) {
					BitVector b = new BitVector(bitVectorSize);
					b.randomize(rand);
					nextPop.put(b, function.valueAt(b));
					// System.out.println("random vektor");
				}
			} else {
				// ako je broj djece u badChildrenPoolu nedovoljan da se popuni
				// ostatak praznine u novoj populaciji
				if (badChildrenPool.size() <= populationSize - nextPop.size()) {
					// prvo dodamo sve iz badChildrenPoola
					for (Map.Entry<BitVector, Double> entry : badChildrenPool.entrySet()) {
						nextPop.put(entry.getKey(), entry.getValue());
					}
					// zatim popunimo ostatak praznog mjesta do pune populacije random vektorima
					while (nextPop.size() < populationSize) {
						BitVector b = new BitVector(bitVectorSize);
						b.randomize(rand);
						nextPop.put(b, function.valueAt(b));
					}

				}

				// slucaj kada u badChildrenPoolu ima vise djece no sto je potrebno
				// da se popuni nova populacija do kraja
				badChildrenPool = (LinkedHashMap<BitVector, Double>) sortByValue(badChildrenPool);
				Iterator<Map.Entry<BitVector, Double>> it = badChildrenPool.entrySet().iterator();
				
				while (nextPop.size() < populationSize) {
					Map.Entry<BitVector, Double> entry = it.next();
					nextPop.put(entry.getKey(), entry.getValue());
				}

			}

			compFactor = 1 - Math.pow(fact, numOfIter);
			population = nextPop;
			numOfIter++;
		}

		System.out.println("uvjet: " + (actSelPress < maxSelPress));
		System.out.println("actSelPress: " + actSelPress);
		System.out.println("population");
		for (Map.Entry<BitVector, Double> entry : population.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
		System.out.println();
		System.out.println("compFactor: " + compFactor);
		return bestIndividual(population);
	}
}
