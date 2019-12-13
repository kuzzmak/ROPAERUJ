package hr.fer.zemris.optjava.dz8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ANNTrainer {
	
	static Random rand = new Random();
	

	public static void main(String[] args) {
		Random rand = new Random();
		String path = "data\\08-Laser-generated-data.txt";
					  
		int window = 4;
		int numOfSamples = 600;
		
//		Dataset dataset = new Dataset(path, window, numOfSamples);
//		Dataset.load();
//		List<Sample> data = Dataset.getData();
//		Dataset.print();
		
		int populationSize = 10;
		int dimension = 5;
		double minVal = 0;
		double maxVal = 1;
		double Cr = 0.2;
		ICrossover mutation = new UniformCrossover(Cr);
		
		List<double[]> population = DiffEvol.makeInitialPopulation(populationSize, dimension, minVal, maxVal);
		
		double[] crossed = mutation.cross(population.get(0), population.get(1));
		System.out.println("mutant: " + Arrays.toString(population.get(0)));
		System.out.println("target: " + Arrays.toString(population.get(1)));
		System.out.println("crossed: " + Arrays.toString(crossed));
		
//		List<Integer> chosenIndexes = new ArrayList<>();
//		
//		int index = rand.nextInt(population.size());
//		double[] r0 = population.get(index);
//		chosenIndexes.add(index);
//		index = rand.nextInt(population.size());
//		while(chosenIndexes.contains(index)) {
//			index = rand.nextInt(population.size());
//		}
//		double[] r1 = population.get(index);
//		chosenIndexes.add(index);
//		index = rand.nextInt(population.size());
//		while(chosenIndexes.contains(index)) {
//			index = rand.nextInt(population.size());
//		}
//		double[] r2 = population.get(index);
//		for(int i = 0; i < population.size(); i++) {
//			System.out.println(Arrays.toString(population.get(i)));
//		}
//		System.out.println();
//		System.out.println(Arrays.toString(r0));
//		System.out.println(Arrays.toString(r1));
//		System.out.println(Arrays.toString(r2));
		
		
		
	}
}
