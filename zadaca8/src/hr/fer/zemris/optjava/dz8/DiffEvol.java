package hr.fer.zemris.optjava.dz8;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz8.NN.NeuralNet;

public class DiffEvol {

	double F = 0.5;
	private static int maxIterations;
	private static double minError;
	
	private List<double[]> population;
	private double[] populationErrors;
	
	private static NeuralNet net;
	
	public DiffEvol(int maxIterations, double minError, NeuralNet net) {
		DiffEvol.maxIterations = maxIterations;
		DiffEvol.minError = minError;
		DiffEvol.net = net;
		
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
	
	
	public static evaluatePopulation() {
		
	}
	
}
