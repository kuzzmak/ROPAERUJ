package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class MAIN2 {

	public static double[][] distances;
	public static double[][] heuristics;
	public static double[][] pheromone;
	public static double[][] probabilities;
	public static int numberOfCities;
	public static List<City> cities;

	static List<Ant> population;

	// vjerojatnost rekreiranja optimalnog puta
	public static double p = 0.8;
	public static double mi;
	public static double a;
	// duljina optimalne rute dobivena greedy algoritmom
	public static double cStar;
	// maksimalna razina feromona
	public static double tauMax;
	// minimalna razina feromona
	public static double tauMin;

	public static Ant best;

	public static void main(String[] args) {

		Random rand = new Random();

//		String path = "C:\\Users\\kuzmi\\Desktop\\att48.tsp\\";
		String path = "C:\\Users\\kuzmi\\Desktop\\bays29.tsp";
		TSPConfiguration conf = new TSPConfiguration(path);

		distances = conf.getDistances();
		heuristics = conf.getHeuristics();
		pheromone = conf.getPheremone();
		cities = conf.getCitiesList();
		probabilities = new double[conf.getNumOfCities()][conf.getNumOfCities()];
		numberOfCities = conf.getNumOfCities();

		mi = (numberOfCities - 1) / (numberOfCities * (-1 + Math.pow(CONSTANTS.p, -1. / numberOfCities)));
		a = mi * numberOfCities;
		GreedyAlgorithm alg = new GreedyAlgorithm(cities);
		cStar = alg.run();
		tauMax = 1 / (CONSTANTS.ro * cStar);
		tauMin = tauMax / a;

		best = new Ant(numberOfCities);

		// mapa gdje su kljucevi indeksi gradova, a vrijednosti su liste s
		// indeksima k najblizih gradova
		Map<Integer, List<City>> kNearest = kNearestCities();

		int worstIter = 0;
		
		for (int iter = 0; iter < CONSTANTS.numOfIterations; iter++) {

			population = new ArrayList<>();

			for (int k = 0; k < CONSTANTS.populationSize; k++) {

				List<City> citiesShuffled = new ArrayList<>(cities);
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

					if (nearestCities.size() != 0) {
						double[] nextCityProbabilities = new double[nearestCities.size()];
						double sum = 0;
						for (int index = 0; index < nearestCities.size(); index++) {
							nextCityProbabilities[index] = Math.pow(
									pheromone[route.get(i - 1).getIndex()][nearestCities.get(index).getIndex()],
									CONSTANTS.alpha)
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

					} else {

						double[] nextCityProbabilities = new double[remaining.size()];
						double sum = 0;
						for (int index = 0; index < remaining.size(); index++) {

							nextCityProbabilities[index] = Math.pow(
									pheromone[route.get(i - 1).getIndex()][remaining.get(index).getIndex()],
									CONSTANTS.alpha)
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
				Ant ant = new Ant(route);
				if (ant.pathDistence < best.pathDistence) {
					best = ant;
				}else {
					worstIter++;
				}
				if(worstIter > 500) {
					resetPheromone();
				}
				population.add(ant);
			}

			evaporatePheromone();
			updatePheromone(false);

			System.out.println("iter: " + iter + " " + "best: " + best.pathDistence);

		}
		
		int[] temp = best.getIndexes();
		int index = 0;
		for(int i = 0; i < temp.length; i++) {
			if(temp[i] == 0) index = i;
		}
		int[] temp2 = Arrays.copyOfRange(temp, 0, index);
		int[] temp3 = Arrays.copyOfRange(temp, index, temp.length - 1);
		
		int[] optimalPath = IntStream.concat(Arrays.stream(temp3), Arrays.stream(temp2)).toArray();
		
		
		System.out.println(Arrays.toString(optimalPath));
	}

	/**
	 * Funkcija za isparavanje feromona s bridova
	 */
	public static void evaporatePheromone() {
		for (int i = 0; i < numberOfCities; i++) {
			for (int j = 0; j < numberOfCities; j++) {
				if (pheromone[i][j] * (1 - CONSTANTS.ro) > tauMin) {
					pheromone[i][j] *= (1 - CONSTANTS.ro);
				} else {
					pheromone[i][j] = tauMin;
				}
			}
		}
	}

	/**
	 * Funkcija za azuriranje feromona na bridovima
	 */
	public static void updatePheromone(boolean onlyBest) {

		// ako feromon ostavlja samo najbolji
		if (onlyBest) {
			for (int j = 0; j < best.getIndexes().length - 1; j++) {
				int from = best.getIndexes()[j];
				int to = best.getIndexes()[j + 1];
				if (pheromone[from][to] + 1. / best.pathDistence < tauMax) {
					pheromone[from][to] += 1. / best.pathDistence;
				} else {
					pheromone[from][to] = tauMax;
				}
			}
		} else {
			// uzima se samo prvih firstAnts mrava
			for (int i = 0; i < CONSTANTS.firstAnts; i++) {

				Ant a = population.get(i);

				for (int j = 0; j < a.getIndexes().length - 1; j++) {
					int from = a.getIndexes()[j];
					int to = a.getIndexes()[j + 1];
					if (pheromone[from][to] + 1 / a.pathDistence < tauMax) {
						pheromone[from][to] += 1 / a.pathDistence;
					}else {
						pheromone[from][to] = tauMax;
					}
				}
			}
		}
	}

	/**
	 * Funkcija za pronalazak k najblizih gradova koji se vec ne nalaze u posjecenim
	 * gradovima
	 * 
	 * @param citiesUnshuffled
	 * @return mapa
	 */
	public static Map<Integer, List<City>> kNearestCities() {

		// mapa u kojoj su kljucevi indeksi gradova, a vrijednosti
		// su liste s indeksima najblizih gradova
		Map<Integer, List<City>> knearest = new TreeMap<>();

		for (int i = 0; i < numberOfCities; i++) {

			List<City> nearestCities = new ArrayList<>();
			// sve udaljenosti od trenutnog grada pa do ostalih
			double[] distancesFromCity = distances[i].clone();
			Arrays.sort(distancesFromCity);

			for (int j = 0; j < CONSTANTS.k; j++) {

				double value = distancesFromCity[j + 1];
				// pronalazak indeksa grada
				for (int l = 0; l < distancesFromCity.length; l++) {
					if (value == distances[i][l]) {
						nearestCities.add(cities.get(l));
						break;
					}
				}
			}
			knearest.put(i, nearestCities);
		}
		return knearest;
	}
	
	public static void resetPheromone() {
		double[][] phromone = new double[numberOfCities][numberOfCities];
		for(int i = 0; i < pheromone.length; i++) {
			for(int j = 0; j < pheromone.length; j++) {
				pheromone[i][j] = tauMax;
			}
		}
	}
}
