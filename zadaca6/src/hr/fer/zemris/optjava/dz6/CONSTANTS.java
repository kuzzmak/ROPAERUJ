package hr.fer.zemris.optjava.dz6;

public class CONSTANTS {
	
	// broj mrava koji ostavljaju feromone
	public static final int firstAnts = 3;
	// broj potencijalnih susjeda
	public static final int k = 5;
	// faktor u izrazu (1 - ro) koji mnozi razinu feromona
	public static final double ro = 0.05;
	// faktor koji potencira heuristicku funkciju
	public static double beta = 5;
	// faktor koji potencira feromone
	public static double alpha = 1;
	// broj iteracija
	public static int numOfIterations = 2000;
	// velicina populacije mrava
	public static int populationSize = 28;
	// vjerojatnost rekreiranja optimalnog rjesenja
	public static double p = 0.6;
	
}
