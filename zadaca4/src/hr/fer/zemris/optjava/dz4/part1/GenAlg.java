package hr.fer.zemris.optjava.dz4.part1;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class GenAlg implements IOptAlgorithm {

	public static int populationSize;
	// min greska nakon koje algoritam staje
	private double minError;
	// broj iteracija nakon kojeg algoritam staje
	private int maxIterations;
	// selekcija(tournament ili roulettewheel)
	private ISelection<SingleObjectiveSolution> selection;
	// stopa ucenja
	public static double sigma;
	
	public static Random rand;
	
	public static IDecoder<SingleObjectiveSolution> decoder;
	// funkcija koju se optimizira
	public static IFunction function;
	// broj varijabli
	public static int n = 6;
	
	
	public GenAlg(int populationSize, double minError, int maxIterations, ISelection<SingleObjectiveSolution> selection, double sigma) {
		this.populationSize = populationSize;
		this.minError = minError;
		this.maxIterations = maxIterations;
		this.selection = selection;
		this.sigma = sigma;
		this.rand = new Random();
		this.decoder = new PassThroughDecoder();
		this.function = new OptimizationFunction("02-zad-prijenosna.txt");
	}
	
	@Override
	public SingleObjectiveSolution run() {
		
		int iteration = 0;

		// pocetna populacija
		TreeMap<SingleObjectiveSolution, Double> population = Util.makePopulation();

		while (function.valueAt(decoder.decode(population.firstKey())) > this.minError && iteration < this.maxIterations) {

			System.out.println("it: " + iteration++ + " curr best: " + population.firstKey() + " "
					+ function.valueAt(decoder.decode(population.firstKey())));
			TreeMap<SingleObjectiveSolution, Double> offspring = new TreeMap<>(Util.comp.reversed());
			Iterator<Map.Entry<SingleObjectiveSolution, Double>> iter = population.entrySet().iterator();

			// prva dva najbolja roditelja idu u novu generaciju
			Map.Entry<SingleObjectiveSolution, Double> next = iter.next();
			offspring.put(next.getKey(), next.getValue());
			next = iter.next();
			offspring.put(next.getKey(), next.getValue());

			// stvaranje novih potomaka
			while (offspring.size() < population.size() + 2) {

				SingleObjectiveSolution parent1 = selection.select(population, rand);
				SingleObjectiveSolution parent2 = selection.select(population, rand);
				// ako su izabrani jednaki roditelji, bira se opet
				while (parent1 == parent2) {
					parent2 = selection.select(population, rand);
				}

				// krizanje djece
				SingleObjectiveSolution child1 = Util.BLXa(parent1, parent2, rand);
				child1.setValue(function.valueAt(decoder.decode(child1)), Util.minimize);
				SingleObjectiveSolution child2 = Util.BLXa(parent1, parent2, rand);
				child2.setValue(function.valueAt(decoder.decode(child2)), Util.minimize);

				// mutacija djece
				Util.mutate(child1);
				Util.mutate(child2);

				// dodavanje djece u set potomaka
				offspring.put(child1, child1.getFitness());
				offspring.put(child2, child2.getFitness());
			}

			// brisanje dva najgora potomka
			offspring.remove(offspring.lastKey());
			offspring.remove(offspring.lastKey());

			population = offspring;
		}
		
		return population.firstKey();
	}
}
