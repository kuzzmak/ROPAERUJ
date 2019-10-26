package hr.fer.zemris.optjava.dz4.part1;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class GeneticAlgorithm {

	public static Random rand = new Random();
	// varijable za random inicijalizaciju pocetnih rjesenja
	public static int min = -4;
	public static int max = 8;
	public static boolean minimize = true;
	// parametar za BLA-x
	public static double alpha = 0.05;
	// broj varijabli
	public static int N = 6;
	// velicina populaicije
	public static int VEL_POP = 10;
	// minimalna greska nakon koje staje algoritam
	public static double minErr = 1;
	// broj generacija nakon kojih staje algoritam
	public static int numOfGenerations = 50000;
	
	// parametar za mutacij rjesenja
	public static double sigma = 0.03;
	// 
	public static String pathToFunction = "02-zad-prijenosna.txt";
	// funkcija koja se optimizira
	public static IFunction function = new OptimizationFunction(pathToFunction);
	// dekoder rjese
	public static IDecoder<SingleObjectiveSolution> decoder = new PassThroughDecoder();
	
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
	
	public static void main(String[] args) {
		
		int iteration = 0;
		TreeMap<SingleObjectiveSolution, Double> population = Util.makePopulation();
		ISelection<SingleObjectiveSolution> roulette = new RouletteWheelSelection();
		
		while (function.valueAt(decoder.decode(population.firstKey())) > minErr && iteration < numOfGenerations) {
			
			System.out.println("it: " + iteration++ + " curr best: " + population.firstKey() + " " + function.valueAt(decoder.decode(population.firstKey())));
			TreeMap<SingleObjectiveSolution, Double> offspring = new TreeMap<>(comp.reversed());
			Iterator<Map.Entry<SingleObjectiveSolution, Double>> iter = population.entrySet().iterator();
			
			// prva dva najbolja roditelja idu u novu generaciju
			Map.Entry<SingleObjectiveSolution, Double> next = iter.next();
			offspring.put(next.getKey(), next.getValue());
			next = iter.next();
			offspring.put(next.getKey(), next.getValue());

			if(iteration == 50000) {
				Util.populationPrint(population);
				break;
			}
			
			while(offspring.size() < population.size() + 2) {

				SingleObjectiveSolution parent1 = roulette.select(population, rand);
				SingleObjectiveSolution parent2 = roulette.select(population, rand);
				while(parent1 == parent2) {
					parent2 = roulette.select(population, rand);
				}
				
				SingleObjectiveSolution child1 = BLXa(parent1, parent2);
				child1.setValue(function.valueAt(decoder.decode(child1)), minimize);
				SingleObjectiveSolution child2 = BLXa(parent1, parent2);
				child2.setValue(function.valueAt(decoder.decode(child2)), minimize);
					
				Util.mutate(child1);
				Util.mutate(child2);

				offspring.put(child1, child1.getFitness());
				offspring.put(child2, child2.getFitness());
			}
			
			offspring.remove(offspring.lastKey());
			offspring.remove(offspring.lastKey());
			
			population = offspring;
			
		}

	}
}
