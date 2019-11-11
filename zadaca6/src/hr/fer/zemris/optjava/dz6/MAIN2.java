package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class MAIN2 {

	public static double[][] distances;
	public static double[][] heuristics;
	public static double[][] pheromone;
	public static double[][] probabilities;
	public static int numberOfCities;

	public static double ro = 0.5;
	public static int firstAnts = 2;

	public static double beta = 2.5;
	public static double alpha = 1;

	public static int numOfIterations = 500;

	public static int populationSize = 28;
	static List<Ant> population;
	
	public static Ant best;
	
	public static void main(String[] args) {

		Random rand = new Random();

//		String path = "C:\\Users\\kuzmi\\Desktop\\att48.tsp\\";
		String path = "C:\\Users\\kuzmi\\Desktop\\bays29.tsp";
		TSPConfiguration conf = new TSPConfiguration(path);

		distances = conf.getDistances();
		heuristics = conf.getHeuristics();
		pheromone = conf.getPheremone();
		probabilities = new double[conf.getNumOfCities()][conf.getNumOfCities()];
		numberOfCities = conf.getNumOfCities();
		best = new Ant(numberOfCities);
		
		
		// lista gradova
		List<City> citiesUnshuffled = conf.getCitiesList();
		
		// mapa gdje su kljucevi indeksi gradova, a vrijednosti su liste s
		// indeksima k najblizih gradova
		Map<Integer, List<City>> kNearest = kNearestCities(citiesUnshuffled);

		for (int iter = 0; iter < numOfIterations; iter++) {
			population = new ArrayList<>();
			
			for (int k = 0; k < populationSize; k++) {

				List<City> citiesShuffled = new ArrayList<>(citiesUnshuffled);
				Collections.shuffle(citiesShuffled);
				
				// konstruirana ruta
				List<City> route = new ArrayList<>();
				route.add(citiesShuffled.get(0));
				
				// neposjeceni gradovi
				List<City> remaining = new ArrayList<>(citiesShuffled);

				// ---------------------------------------------------------------
				// pocetak za svakog pojedinog mrava
				// ---------------------------------------------------------------
				for (int i = 1; i < conf.getNumOfCities() - 1; i++) {

					// micemo sve posjecene gradove
					remaining.removeAll(route);
					List<City> nearestCities = kNearest.get(route.get(i - 1).getIndex());
					
					nearestCities.removeAll(route);
					
					
					if(nearestCities.size() != 0) {
						double[] nextCityProbabilities = new double[nearestCities.size()];
						double sum = 0;
						for(int index = 0; index < nearestCities.size(); index++) {
//							System.out.println("index " + citiesUnshuffled.get(i - 1).getIndex());
							nextCityProbabilities[index] = Math
									.pow(pheromone[route.get(i - 1).getIndex()][nearestCities.get(index).getIndex()], alpha)
									* heuristics[route.get(i - 1).getIndex()][nearestCities.get(index).getIndex()];
							sum += nextCityProbabilities[index];
							
						}
						
						for (int j = 0; j < nextCityProbabilities.length; j++) {
							nextCityProbabilities[j] /= sum;
						}
						
						double prob = rand.nextDouble();
						// varijabla u koju se sumiraju vjerojatnosi sve dok se ne preskoci prob
						double probSum = 0;
						// indeks izabranog grada u preostalim gradovima
						int selected = 0;
						for (int j = 0; j < nextCityProbabilities.length; j++) {
							probSum += nextCityProbabilities[j];
							if (probSum > prob) {
								selected = j;
								break;
							}
						}
						
						route.add(nearestCities.get(selected));
						
					}else {
						
						double[] nextCityProbabilities = new double[remaining.size()];
						double sum = 0;
						for(int index = 0; index < remaining.size(); index++) {
							
							nextCityProbabilities[index] = Math
									.pow(pheromone[route.get(i - 1).getIndex()][remaining.get(index).getIndex()], alpha)
									* heuristics[route.get(i - 1).getIndex()][remaining.get(index).getIndex()];
							sum += nextCityProbabilities[index];
							
						}
						for (int j = 0; j < nextCityProbabilities.length; j++) {
							nextCityProbabilities[j] /= sum;
						}
						
						double prob = rand.nextDouble();
						// varijabla u koju se sumiraju vjerojatnosi sve dok se ne preskoci prob
						double probSum = 0;
						// indeks izabranog grada u preostalim gradovima
						int selected = 0;
						for (int j = 0; j < nextCityProbabilities.length; j++) {
							probSum += nextCityProbabilities[j];
							if (probSum > prob) {
								selected = j;
								break;
							}
						}
						route.add(remaining.get(selected));
						
					}
					
					
					
					
					
					
				}
				// novi mrav sa svojom rutom
				Ant a = new Ant(route);
				a.evaluate();
				if(a.pathDistence < best.pathDistence) best = a;
//				System.out.println(a);
//				System.out.println(a.pathDistence);
				population.add(a);
			}
			
			Collections.sort(population);
			
			evaporatePheromone();
			updatePheromone();
			if(population.get(0).pathDistence < best.pathDistence) best = population.get(0);
			System.out.println("iter: " + iter + " " + "best: " + best.pathDistence);

		}
	}

	/**
	 * Funkcija za isparavanje feromona s bridova
	 */
	public static void evaporatePheromone() {
		for (int i = 0; i < numberOfCities; i++) {
			for (int j = 0; j < numberOfCities; j++) {
				pheromone[i][j] *= (1 - ro);
			}
		}
	}

	/**
	 * Funkcija za azuriranje feromona na bridovima
	 */
	public static void updatePheromone() {
		// uzima se samo prvih firstAnts mrava
		for (int i = 0; i < firstAnts; i++) {

			Ant a = population.get(i);

			for (int j = 0; j < a.getIndexes().length - 1; j++) {
				int from = a.getIndexes()[j];
				int to = a.getIndexes()[j + 1];
				pheromone[from][to] += 1 / a.pathDistence;
			}
		}
	}
	
	public static Map<Integer, List<City>> kNearestCities(List<City> citiesUnshuffled) {
		
		Map<Integer, List<City>> knearest = new TreeMap<>();
		
		for(int i = 0; i < numberOfCities; i++) {
			
			List<City> nearestCities = new ArrayList<>();
			// sve udaljenosti od trenutnog grada pa do ostalih
			double[] distancesFromCity = distances[i].clone();
			Arrays.sort(distancesFromCity);
			
			for(int j = 0; j < CONSTANTS.k; j++) {
				
				double value = distancesFromCity[j + 1];
				
				for(int l = 0; l < distancesFromCity.length; l++) {
					if(value == distances[i][l]) {
						nearestCities.add(citiesUnshuffled.get(l));
						break;
					}
				}
			}
			knearest.put(i, nearestCities);
		}
		return knearest;
	}
}
