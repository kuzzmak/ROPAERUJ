package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Main {

	public static double[][] distances;
	public static double[][] heuristics;
	public static double[][] pheromone;
	public static double[][] probabilities;
	public static int numberOfCities;

	public static double ro = 0.5;
	public static int firstAnts = 3;

	public static double beta = 2.5;
	public static double alpha = 1;

	public static int numOfIterations = 500;

	public static int populationSize = 40;
	static List<Ant> population;
	
	public static Ant best;
	
	public static void main(String[] args) {

		Random rand = new Random();

		String path = "C:\\Users\\kuzmi\\Desktop\\att48.tsp\\";
//		String path = "C:\\Users\\kuzmi\\Desktop\\bays29.tsp";
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
		Map<Integer, List<Integer>> kNearest = kNearestCities();
//		List<Integer> lb = kNearest.get(18);
//		for(Integer b: lb) {
//			System.out.println(cities.get(b));
//		}

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
				for (int i = 0; i < conf.getNumOfCities(); i++) {

					// micemo sve posjecene gradove
					remaining.removeAll(route);
					List<Integer> nearestCities = kNearest.get(route.get(i).getIndex());
					
					double[] nextCityProbabilities = new double[CONSTANTS.k];
					
					double sum = 0;
					for(int index = 0; index < CONSTANTS.k; index++) {
						
						if(!route.contains(citiesUnshuffled.get(nearestCities.get(index)))) {
							System.out.println("dap");
							nextCityProbabilities[index] = Math
									.pow(pheromone[citiesUnshuffled.get(i).getIndex()][citiesUnshuffled.get(nearestCities.get(index)).getIndex()], alpha)
									* heuristics[citiesUnshuffled.get(i).getIndex()][citiesUnshuffled.get(nearestCities.get(index)).getIndex()];
							sum += nextCityProbabilities[index];
						}else {
							System.out.println("sadrzi");
							
						}
						
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
					route.add(citiesUnshuffled.get(nearestCities.get(selected)));
					
					
					
					
//					// polje s vjerojatnostima posjeta sljedeceg grada
//					double[] nextCityProbabilities = new double[conf.getNumOfCities() - i];
//					// varijabla za normalizaciju vjerojatnosti
//					double sum = 0;
//
//					for (int j = 0; j < remaining.size(); j++) {
//						// izracun vjerojatnosti odlaska u pojedini grad
//						nextCityProbabilities[j] = Math
//								.pow(pheromone[cities.get(i - 1).getIndex()][remaining.get(j).getIndex()], alpha)
//								* heuristics[cities.get(i - 1).getIndex()][remaining.get(j).getIndex()];
//
//						sum += nextCityProbabilities[j];
//					}
//					// normalizacija vjeorojatnosti
//					for (int j = 0; j < nextCityProbabilities.length; j++) {
//						nextCityProbabilities[j] /= sum;
//					}
//					// nasumicna vjerojatnost
//					double prob = rand.nextDouble();
//					// varijabla u koju se sumiraju vjerojatnosi sve dok se ne preskoci prob
//					double probSum = 0;
//					// indeks izabranog grada u preostalim gradovima
//					int selected = 0;
//					for (int j = 0; j < nextCityProbabilities.length; j++) {
//						probSum += nextCityProbabilities[j];
//						if (probSum > prob) {
//							selected = j;
//							break;
//						}
//					}
					//route.add(remaining.get(selected));
				}
				// novi mrav sa svojom rutom
				Ant a = new Ant(route);
				a.evaluate();
//				System.out.println(a);
				System.out.println(a.pathDistence);
				population.add(a);
			}
			
			Collections.sort(population);
			
//			for(int i = 0; i < population.size(); i++) {
//				
//				System.out.println(population.get(i).pathDistence);
//				
//			}
			
//			System.out.println("best in pop: " + population.get(0).pathDistence);
//			for(int i = 0; i < pheromone.length; i++) {
//				for(int j = 0; j < pheromone.length; j++) {
//					System.out.printf("%f ", pheromone[i][j]);
//				}
//				System.out.println();
//			}
			evaporatePheromone();
			updatePheromone();
//			for(int i = 0; i < pheromone.length; i++) {
//				for(int j = 0; j < pheromone.length; j++) {
//					System.out.printf("%f ", pheromone[i][j]);
//				}
//				System.out.println();
//			}
			if(population.get(0).pathDistence < best.pathDistence) best = population.get(0);
			System.out.println("iter: " + iter + " " + "best: " + best.pathDistence);

		}
//		System.out.println(best);
//		System.out.println(best.pathDistence);
//		for(int i = 0; i < heuristics.length; i++) {
//			for(int j = 0; j < heuristics.length; j++) {
//				System.out.printf("%f ", heuristics[i][j]);
//			}
//			System.out.println();
//		}
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
	
	public static Map<Integer, List<Integer>> kNearestCities() {
		
		Map<Integer, List<Integer>> knearest = new TreeMap<>();
		
		for(int i = 0; i < numberOfCities; i++) {
			
			List<Integer> nearestIndexes = new ArrayList<>();
			// sve udaljenosti od trenutnog grada pa do ostalih
			double[] distancesFromCity = distances[i].clone();
			Arrays.sort(distancesFromCity);
			
			for(int j = 0; j < CONSTANTS.k; j++) {
				
				double value = distancesFromCity[j + 1];
				
				for(int l = 0; l < distancesFromCity.length; l++) {
					if(value == distances[i][l]) {
						nearestIndexes.add(l);
						break;
					}
				}
			}
			knearest.put(i, nearestIndexes);
		}
		return knearest;
	}
}
