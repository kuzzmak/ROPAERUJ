package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) {

//		IFunction function = new OptimizaitonFunction("nug25");
//		Chromosome c1 = new Chromosome(25);
//		c1.setFitness(function.valueAt(c1));
//		Chromosome c2 = new Chromosome(25);
//		c2.setFitness(function.valueAt(c2));
//		
//		List<Chromosome> list = new ArrayList<>();
//		list.add(c1);
//		list.add(c2);
//		Collections.sort(list);
//		for(int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i) + " " + list.get(i).getFitness());
//		}
//		Random rand = new Random();
//	
//		ICrossover cross = new POSCrossover(2);
//		
//		System.out.println(c1);
//		System.out.println(c2);
//		System.out.println(cross.cross(c1, c2, rand));

//		int populationSize = 100;
//		int numOfSubpopulations = 10;
//		IOptAlgorithm alg = new SASEGASA("nug25", populationSize, numOfSubpopulations);
//				
//		List<Chromosome> population = SASEGASA.initializePopulation(populationSize);
//		int subPopSize = populationSize / numOfSubpopulations;
//		
//		
//		IFunction function = new OptimizaitonFunction("nug25");
//		ICrossover cross = new OX2Crossover(function.getN());
//		IMutation mutation = new MutationSwap(0.05f);
//		List<Chromosome> population = new ArrayList<>();
//		int n = function.getN();
//		
//		while(population.size() < 100) {
//			Chromosome c = new Chromosome(n);
//			c.shuffle();
//			c.setFitness(function.valueAt(c));
//			if(!population.contains(c)) population.add(c);
//		}
//		System.out.println(population.size());
//		OffspringSelectionAlgorithm alg = new OffspringSelectionAlgorithm(function, 25, population, 100, (double)20, cross, mutation);
//		List<Chromosome> list = alg.run();
//		System.out.println(list);
	}

}
