package hr.fer.zemris.optjava.dz4.part1;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class GeneticAlgorithm {

	static Random rand = new Random();
	// varijable za random inicijalizaciju pocetnih rjesenja
	static int min = -10;
	static int max = 10;
	static boolean minimize = true;
	static double alpha = 0.5;

	// broj varijabli
	static int N = 6;

	private static int VEL_POP = 10;
	private static double minErr = 5;
	private static int numOfGenerations;
	ISelection selection;
	private static double sigma = 0.03;

	
	
	static String pathToFunction = "02-zad-prijenosna.txt";
	static IFunction function = new OptimizationFunction(pathToFunction);
	static IDecoder<SingleObjectiveSolution> decoder = new PassThroughDecoder();
	
	// takav komparator da su rjesenja s boljim fitnesom uvijek na pocetku mape
	static Comparator<SingleObjectiveSolution> comp = new Comparator<SingleObjectiveSolution>() {

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


	public static void normalize(TreeMap<SingleObjectiveSolution, Double> population) {
		double sum = 0;
		for (Map.Entry<SingleObjectiveSolution, Double> entry : population.entrySet()) {
			sum += entry.getValue();
		}
		for (Map.Entry<SingleObjectiveSolution, Double> entry : population.entrySet()) {
			population.put(entry.getKey(), entry.getValue() / sum);
		}
	}

	public static TreeMap<SingleObjectiveSolution, Double> makePopulation(IFunction function,
			IDecoder<SingleObjectiveSolution> decoder) {

		TreeMap<SingleObjectiveSolution, Double> population = new TreeMap<>(comp.reversed());

		for (int i = 0; i < VEL_POP; i++) {
			DoubleArraySolution temp = new DoubleArraySolution(N);
			temp.randomize(rand, min, max);
			temp.setValue(function.valueAt(decoder.decode(temp)), minimize);
			population.put(temp, temp.getFitness());
		}
		
		return population;
	}

	static SingleObjectiveSolution rouletteWheelSelection(TreeMap<SingleObjectiveSolution, Double> offspring, Random rand) {
		
		double minFitness = Double.MAX_VALUE;
		double meanFitness = 0;
		Iterator<Map.Entry<SingleObjectiveSolution, Double>> iter = offspring.entrySet().iterator();
		
		while(iter.hasNext()) {
			Map.Entry<SingleObjectiveSolution, Double> entry = iter.next();
			meanFitness += entry.getValue();
			if(entry.getValue() < minFitness) minFitness = entry.getValue();
		}
		meanFitness /= N;
		
		iter = offspring.entrySet().iterator();
		TreeMap<SingleObjectiveSolution, Double> tempOffspring = new TreeMap<>(comp.reversed());
		
		for(Map.Entry<SingleObjectiveSolution, Double> entry: offspring.entrySet()) {
			tempOffspring.put(entry.getKey(), (entry.getValue() - minFitness) / (N * (meanFitness - minFitness)));
		}
		
		normalize(tempOffspring);
		System.out.println("offspring");
		populationPrint(tempOffspring);
		SingleObjectiveSolution selected = null;
		
		double randFit = rand.nextDouble();
		
		double total = 0;
		for (Map.Entry<SingleObjectiveSolution, Double> entry : tempOffspring.entrySet()) {
			total += entry.getValue();
			if(total > randFit) {
//				System.out.println(total + " " + randFit);
				selected = entry.getKey();
				selected.setValue(function.valueAt(decoder.decode(entry.getKey())), minimize);
				break;
			}
		}
		return selected;
	}
	
	public static SingleObjectiveSolution BLXa(SingleObjectiveSolution parent01,
										SingleObjectiveSolution parent02) {
		
		
		DoubleArraySolution parent1 = (DoubleArraySolution)parent01;
		DoubleArraySolution parent2 = (DoubleArraySolution)parent02;
		
		DoubleArraySolution child = parent1.duplicate();

		int childLength = child.values.length;
		for(int i = 0; i < childLength; i++) {
			
			double di = Math.abs(parent1.values[i] - parent2.values[i]);
			
			double lower = Math.min(parent1.values[i], parent2.values[i]) - alpha * di;
			double upper = Math.max(parent1.values[i], parent2.values[i]) + alpha * di;
			
			double u = rand.nextDouble() * (upper - lower) + lower;
			
			child.values[i] = u;
			
		}
		
		return child;
	}
	
	
	public static void mutate(SingleObjectiveSolution child) {
		
		for(int i = 0; i < N; i++) {
			((DoubleArraySolution)child).values[i] += rand.nextGaussian() * sigma;
		}
		child.setValue(function.valueAt(decoder.decode(child)), minimize);
	}
	
	public static void populationPrint(TreeMap<SingleObjectiveSolution, Double> population) {
		for(Map.Entry<SingleObjectiveSolution, Double> entry: population.entrySet()) {
			System.out.println(entry.getKey() + "  " + entry.getValue());
		}
		System.out.println();
	}
	public static void main(String[] args) {

		
		int iteration = 0;
		TreeMap<SingleObjectiveSolution, Double> population = makePopulation(function, decoder);

		
		while (function.valueAt(decoder.decode(population.firstKey())) > minErr) {
			
			System.out.println("it: " + iteration++ + " curr best: " + population.firstKey() + " " + function.valueAt(decoder.decode(population.firstKey())));
			TreeMap<SingleObjectiveSolution, Double> offspring = new TreeMap<>(comp.reversed());
			Iterator<Map.Entry<SingleObjectiveSolution, Double>> iter = population.entrySet().iterator();
			
			// prva dva najbolja roditelja idu u novu generaciju
			Map.Entry<SingleObjectiveSolution, Double> next = iter.next();
			offspring.put(next.getKey(), next.getValue());
			next = iter.next();
			offspring.put(next.getKey(), next.getValue());

			while(offspring.size() < population.size() + 2) {
				populationPrint(population);
				System.out.println();
				SingleObjectiveSolution parent1 = rouletteWheelSelection(population, rand);
				SingleObjectiveSolution parent2 = rouletteWheelSelection(population, rand);
				while(parent1 == parent2) {
					parent2 = rouletteWheelSelection(population, rand);
				}
				
				SingleObjectiveSolution child1 = BLXa(parent1, parent2);
				child1.setValue(function.valueAt(decoder.decode(child1)), minimize);
				SingleObjectiveSolution child2 = BLXa(parent1, parent2);
				child2.setValue(function.valueAt(decoder.decode(child2)), minimize);
					
				mutate(child1);
				mutate(child2);
//				System.out.println("child1: " + child1.toString());
//				System.out.println("child2: " + child2.toString());
				offspring.put(child1, child1.getFitness());
				offspring.put(child2, child2.getFitness());
				
			}
			
			offspring.remove(offspring.lastKey());
			offspring.remove(offspring.lastKey());
			
			population = offspring;
			
		}

	}
}
