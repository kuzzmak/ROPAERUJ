package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

	public static double[][] distances;
	public static double[][] heuristics;
	public static double[][] pheromone;
	public static double[][] probabilities;
	public static int numberOfCities;

	public static double ro = 0.9;
	public static int firstAnts = 3;

	public static double beta = 2;
	public static double alpha = 3;

	public static int numOfIterations = 500;

	public static int populationSize = 20;
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

		for (int iter = 0; iter < numOfIterations; iter++) {
			population = new ArrayList<>();
			
			for (int k = 0; k < populationSize; k++) {

				// lista gradova
				List<City> cities = conf.getCitiesList();
				Collections.shuffle(cities);
				// konstruirana ruta
				List<City> route = new ArrayList<>();
				route.add(cities.get(0));
				// neposjeceni gradovi
				List<City> remaining = new ArrayList<>(cities);

				// ---------------------------------------------------------------
				// pocetak za svakog pojedinog mrava
				// ---------------------------------------------------------------
				for (int i = 1; i < conf.getNumOfCities(); i++) {

					// micemo sve posjecene gradove
					remaining.removeAll(route);
					// polje s vjerojatnostima posjeta sljedeceg grada
					double[] nextCityProbabilities = new double[conf.getNumOfCities() - i];
					// varijabla za normalizaciju vjerojatnosti
					double sum = 0;

					for (int j = 0; j < remaining.size(); j++) {
						// izracun vjerojatnosti odlaska u pojedini grad
						nextCityProbabilities[j] = Math
								.pow(pheromone[cities.get(i - 1).getIndex()][remaining.get(j).getIndex()], alpha)
								* heuristics[cities.get(i - 1).getIndex()][remaining.get(j).getIndex()];

						sum += nextCityProbabilities[j];
					}
					// normalizacija vjeorojatnosti
					for (int j = 0; j < nextCityProbabilities.length; j++) {
						nextCityProbabilities[j] /= sum;
					}
					// nasumicna vjerojatnost
					double prob = rand.nextDouble();
					// varijabla u koju se sumiraju vjerojatnosi sve dok se ne preskoci prob
					double probSum = 0;
					// indeks izabranog grada
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
				// novi mrav sa svojom rutom
				Ant a = new Ant(route);
				a.evaluate();
//				System.out.println(a.pathDistence);
				population.add(a);
			}
			
			Collections.sort(population);
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
		System.out.println(best);
		System.out.println(best.pathDistence);
		for(int i = 0; i < heuristics.length; i++) {
			for(int j = 0; j < heuristics.length; j++) {
				System.out.printf("%f ", heuristics[i][j]);
			}
			System.out.println();
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
	 * Funkcija za azuriranje feromona s bridova
	 */
	public static void updatePheromone() {
		// uzima se samo prvih nekoliko mrava
		for (int i = 0; i < firstAnts; i++) {

			Ant a = population.get(i);

			for (int j = 0; j < a.getIndexes().length - 1; j++) {
				int from = a.getIndexes()[j];
				int to = a.getIndexes()[j + 1];
				pheromone[from][to] += 1 / a.pathDistence;
			}
		}
	}
}
