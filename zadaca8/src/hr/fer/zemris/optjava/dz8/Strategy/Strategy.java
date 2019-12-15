package hr.fer.zemris.optjava.dz8.Strategy;

import java.util.List;

public class Strategy {
	
	private static String baseVectorSelection;
	
	private static String linearCombinatinos;
	
	private static String crossover;
	
	private List<double[]> population;

	public Strategy(String strategy) {
		
		String[] temp = strategy.split("/");
		
		Strategy.baseVectorSelection = temp[1];
		Strategy.linearCombinatinos = temp[2];
		Strategy.crossover = temp[3];
	}
	
	public double[] getBaseVector() {
		
	}

	public void setPopulation(List<double[]> population) {
		this.population = population;
	}

	public static String getBaseVectorSelection() {
		return baseVectorSelection;
	}

	public static String getLinearCombinatinos() {
		return linearCombinatinos;
	}

	public static String getCrossover() {
		return crossover;
	}
	
	
}
