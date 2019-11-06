package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SASEGASA implements IOptAlgorithm {
	
	private String path;
	private int populationSize;
	private int numOfSubpopulations;
	private static IFunction function;
	
	public SASEGASA(String path, int populationSize, int numOfSubpopulations) {
		super();
		this.path = path;
		this.populationSize = populationSize;
		this.numOfSubpopulations = numOfSubpopulations;
		this.function = new OptimizaitonFunction(path);
	}

	public static List<Chromosome> initializePopulation(int populationSize){
		
		int n = function.getN();
		List<Chromosome> population = new ArrayList<>();
		
		while(population.size() < populationSize) {
			Chromosome c = new Chromosome(n);
			c.shuffle();
			c.setFitness(function.valueAt(c));
			if(!population.contains(c)) population.add(c);
		}
		return population;
	}
	
	
	@Override
	public Chromosome run() {
		
		int maxIterations = 1000;
		double maxSelPress = 20;
		float mutationRate = 0.05f;
		ICrossover crossover = new OX2Crossover(function.getN());
		IMutation mutation = new MutationSwap(mutationRate);
		
		List<Chromosome> population = initializePopulation(populationSize);
		
		OffspringSelectionAlgorithm alg = new OffspringSelectionAlgorithm(
				function, 
				function.getN(), 
				maxIterations, 
				maxSelPress, 
				crossover, 
				mutation);
		
		while(numOfSubpopulations > 0) {
			
			int subPopSize = populationSize / numOfSubpopulations;
			
			int index = 0;
			List<Chromosome> newPopulation = new ArrayList<>();
			for(int i = 0; i < numOfSubpopulations; i++) {
				newPopulation.addAll(alg.run(population.subList(index, index + subPopSize)));
				index += subPopSize;
			}
			if(index != populationSize) {
				newPopulation.addAll(population.subList(index, populationSize));
			}
			
			population = newPopulation;
			numOfSubpopulations--;
		}
		
		Collections.sort(population);
		return population.get(0);
	}
	
	
	public static void main(String[] args) {
		
		String path = "nug25";
		int populationSize = 100;
		int numOfSubpopulations = 10;
		IOptAlgorithm alg = new SASEGASA(path, populationSize, numOfSubpopulations);
				
		Chromosome solution = alg.run();
		System.out.println(solution);
	
	}
}
