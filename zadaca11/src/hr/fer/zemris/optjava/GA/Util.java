package hr.fer.zemris.optjava.GA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hr.fer.zemris.optjava.rng.IRNG;

public class Util {

	// vjerojatnost mutacije
	private static double p = 0.05;
	
	private static double alpha = 0.2;
	
	
	public List<GASolution<int[]>> makePopulation(int populationSize, int solutionSize, IRNG rng){
		
		List<GASolution<int[]>> population = new ArrayList<>();
		
		
		for(int i = 0; i < populationSize; i++) {
			
			IntSolution is = new IntSolution(new int[solutionSize]);
			
			for(int j = 0; j < solutionSize; j++) {
				is.data[j] = rng.nextInt(-128, 127);
			}
			
			population.add(is);
		}
		
		return population;
	}
	
	public static void mutate(IntSolution solution, IRNG rng) {
		
		for(int i = 0; i < solution.data.length; i++) {
			
			if(p > rng.nextDouble()) {
				solution.data[i] += (int)(rng.nextDouble() * rng.nextInt(-128, 127)); 
			}
		}
	}
	
	public static GASolution<int[]> BLXa(GASolution<int[]> parent1, GASolution<int[]> parent2,
			IRNG rng) {

		IntSolution child = (IntSolution)parent1.duplicate();

		int childLength = child.size();
		
		for (int i = 0; i < childLength; i++) {

			double di = Math.abs(parent1.data[i] - parent2.data[i]);

			double lower = Math.min(parent1.data[i], parent2.data[i]) - alpha * di;
			double upper = Math.max(parent1.data[i], parent2.data[i]) + alpha * di;
			
			int u = (int)(rng.nextDouble(lower, upper));

			child.data[i] = u;

		}

		return child;
	}
	
	
	public void sort(List<GASolution<int[]>> population){
		
		Collections.sort(population, new Comparator<GASolution<int[]>>() {

			@Override
			public int compare(GASolution<int[]> arg0, GASolution<int[]> arg1) {

				double f0 = arg0.fitness;
				double f1 = arg1.fitness;

				if (f0 > f1)
					return 1;
				if (f1 > f0)
					return -1;
				return 0;
			}
		});
	}
	
//	public static GASolution<int[]> select(List<GASolution<int[]>> population){
//		
//	}
	
	
}
