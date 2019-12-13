package hr.fer.zemris.optjava.dz8;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz8.NN.NeuralNet;

public class DiffEvol {

	// faktor skaliranja razlike vektor kod konstrukcije mutiranog vektora
	private static final double F = 0.5;
	// vjerojatnost mutacije
	private static final double Cr = 0.05;
	// velicina populacije
	private static int populationSize;
	// maksimalan broj iteracija
	private static int maxIterations;
	// minimalna greska nakon koje staje algoritam
	private static double minError;
	// lista jedinki, trenutna populacija
	private List<double[]> population;
	// polje gresaka trenutne populacije
	private static double[] populationErrors;
	// neuronska mreza koja se uci
	private static NeuralNet net;
	
	private static Random rand;
	
	public DiffEvol(int populationSize, int maxIterations, double minError, NeuralNet net) {
		
		DiffEvol.populationSize = populationSize;
		DiffEvol.maxIterations = maxIterations;
		DiffEvol.minError = minError;
		DiffEvol.net = net;
		DiffEvol.populationErrors = new double[populationSize];
		DiffEvol.rand = new Random();
	}
	
	public double[] run() {
		
		int iteration = 0;
		
		// najbolja greska trenutne populacije
		double error = Double.MAX_VALUE;
		
		List<double[]> newPopulation = new ArrayList<>();
		
		while(iteration < maxIterations && error > minError) {
			
			for(int i = 0; i < populationSize; i++) {
				
				List<Integer> chosenIndexes = new ArrayList<>();
				
				// ciljni vektor
				double[] targetVector = population.get(i);
				chosenIndexes.add(i);
				
				int index = rand.nextInt(population.size());
				while(chosenIndexes.contains(index)) {
					index = rand.nextInt(population.size());
				}
				double[] r0 = population.get(index);
				chosenIndexes.add(index);
				
				index = rand.nextInt(population.size());
				while(chosenIndexes.contains(index)) {
					index = rand.nextInt(population.size());
				}
				double[] r1 = population.get(index);
				chosenIndexes.add(index);
				
				index = rand.nextInt(population.size());
				while(chosenIndexes.contains(index)) {
					index = rand.nextInt(population.size());
				}
				double[] r2 = population.get(index);
				
				double[] mutantVector = new double[targetVector.length];
				
				int jRand = rand.nextInt(targetVector.length);
				
				for(int j = 0; j < targetVector.length; j++) {
					
					mutantVector[j] = r0[j] + F * (r1[j] - r2[j]);
				}
				
				for(int j = 0; j < targetVector.length; j++) {
					
					
					
					
					
				}
				
				
				
			}
			
			
			
			
			
		}
		return newPopulation.get(0);
		
	}
	
	
	/**
	 * Funkcija za stvaranje pocetne populacije jedinki
	 * 
	 * @param populationSize velicina populacije
	 * @param dimension dimenzionalnost vektora
	 * @param minVal minimalna vrijednost u intervalu
	 * @param maxVal maksimalna vrijednost u intervalu
	 * @return lista vektora
	 */
	public static List<double[]> makeInitialPopulation(int populationSize, int dimension, double minVal, double maxVal){
		
		List<double[]> population = new ArrayList<>();
		
		Random rand = new Random();
		
		for(int i = 0; i < populationSize; i++) {
		
			double[] weights = new double[dimension];
			
			for(int j = 0; j < dimension; j++) {
				
				weights[j] = minVal + rand.nextDouble() * (maxVal - minVal);
				
			}
			population.add(weights);
		}
		return population;
	}
	
	/**
	 * Funkcija za evaluaciju populacije
	 * 
	 * @param population lista jedinki koja se evaluira
	 */
	public static void evaluatePopulation(List<double[]> population) {
		
		for(int i = 0; i < populationSize; i++) {
			// postavaljnje tezina mreze
			net.setWeights(population.get(i));
			// greska jedinke
			double error = net.calculateError();
			// zapis u polje gresaka
			populationErrors[i] = error;
			
		}
	}
	
	
	
}
