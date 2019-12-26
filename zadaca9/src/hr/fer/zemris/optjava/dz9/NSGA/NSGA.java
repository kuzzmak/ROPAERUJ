package hr.fer.zemris.optjava.dz9.NSGA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz9.MOOPProblem;

public class NSGA {

	// optimizacijski problem koji se rjesava
	private MOOPProblem problem;
	// velicina populacije
	private int populationSize;
	// paretove fronte
	private List<List<double[]>> fronts;
	
	// iznos dobrote pojedine jedinke za svaku od optimizacijskih funkcija
	private List<double[]> fitness;
	
	private Random rand;
	
	public NSGA(MOOPProblem problem, int populationSize) {
		this.problem = problem;
		this.populationSize = populationSize;
		
		this.rand = new Random();
	}
	
	public void run() {
		
		List<double[]> population = this.makePopulation();
		this.evaluatePopulation(population);
		
		
		
		
	}
	
	public List<double[]> makePopulation(){
		
		// lista stvorenih jedinki
		List<double[]> population = new ArrayList<>();
		// dimenzionalnost svake jedinke
		int dimension = problem.getDimension();
		
		for(int i = 0; i < this.populationSize; i++) {
			
			double[] point = new double[dimension];
			
			for(int j = 0; j < dimension; j++) {
				
				point[j] = rand.nextDouble();
			}
			
			population.add(point);
		}
		
		return population;
	}
	
	/**
	 * Funkcija za evaluaciju populacije 
	 *  
	 * @param population populacija koja se evaluira
	 */
	public void evaluatePopulation(List<double[]> population) {
		
		this.fitness = new ArrayList<>();
		
		for(int i = 0; i < this.populationSize; i++) {
			
			this.fitness.add(problem.evaluate(population.get(i)));
			
		}
	}
	
	
}
