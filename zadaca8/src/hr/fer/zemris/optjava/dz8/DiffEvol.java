package hr.fer.zemris.optjava.dz8;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz8.NN.NeuralNet;

public class DiffEvol {

	double F = 0.5;
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
	
	public DiffEvol(int populationSize, int maxIterations, double minError, NeuralNet net) {
		
		DiffEvol.populationSize = populationSize;
		DiffEvol.maxIterations = maxIterations;
		DiffEvol.minError = minError;
		DiffEvol.net = net;
		DiffEvol.populationErrors = new double[populationSize];
	}
	
	public double[] run() {
		
		int iteration = 0;
		
		// najbolja greska trenutne populacije
		double error = Double.MAX_VALUE;
		
		while(iteration < maxIterations && error > minError) {
			
			
			
			
			
		}
		
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
