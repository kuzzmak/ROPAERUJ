package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class Util {

	/**
	 * Funkcija za inicijalizaciju pocetne populacije mrava
	 * 
	 * @param populationSize velicina populacije
	 * @param antSize velicina mrava / kolicina gradova
	 * @return lista mrava
	 */
	public static List<Ant> initializePopulation(int populationSize, int antSize){
		List<Ant> population = new ArrayList<>();
		for(int i = 0; i < populationSize; i++) {
			population.add(new Ant(antSize));
		}
		return population;
	}
	
	/**
	 * Funkcija za odabir grada medju susjedima(ako vise ima susjeda
	 * koji nisu u vec posjecenim gradivima) ili nekog grada koji jos nije 
	 * posjecen medju preostalima
	 * 
	 * @param cities lista gradova
	 * @param route lista vec posjecenih gradova
	 * @param i redak matrice
	 * @return odabran grad
	 */
	public static City selectCity(List<City> cities, List<City> route, int i) {
		
		// polje vjerojatnosti prelaska u neki od gradova
		double[] nextCityProbabilities = new double[cities.size()];
		double sum = 0;
		for (int index = 0; index < cities.size(); index++) {

			nextCityProbabilities[index] = Math.pow(
					MMAS.pheromone[route.get(i - 1).getIndex()][cities.get(index).getIndex()],
					CONSTANTS.alpha)
					* MMAS.heuristics[route.get(i - 1).getIndex()][cities.get(index).getIndex()];
			sum += nextCityProbabilities[index];

		}
		// normalizacija
		for (int j = 0; j < nextCityProbabilities.length; j++) {
			nextCityProbabilities[j] /= sum;
		}

		double prob = MMAS.rand.nextDouble();
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
		return cities.get(selected);
	}
	
	
	/**
	 * Funkcija za ispis rjesenja od prvog grada navedenog u datoteci
	 * 
	 */
	public static int[] makeSolution(int[] indexes) {
		
		int index = 0;
		for(int i = 0; i < indexes.length; i++) {
			if(indexes[i] == 0) index = i;
		}
		int[] second = Arrays.copyOfRange(indexes, 0, index);
		int[] first = Arrays.copyOfRange(indexes, index, indexes.length - 1);
		
		int[] optimalPath = IntStream.concat(Arrays.stream(first), Arrays.stream(second)).toArray();
		
		for(int i = 0; i < optimalPath.length; i++) {
			optimalPath[i] += 1;
		}
		
		return optimalPath;
	}
	
	
	/**
	 * Funkcija za vracanje feromona na pocetnu razinu
	 * 
	 */
	public static void resetPheromone() {
		double[][] initialPheromone = new double[MMAS.numberOfCities][MMAS.numberOfCities];
		for(int i = 0; i < MMAS.pheromone.length; i++) {
			for(int j = 0; j < MMAS.pheromone.length; j++) {
				initialPheromone[i][j] = MMAS.tauMax;
			}
		}
		MMAS.pheromone = initialPheromone;
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

		for (int i = 0; i < MMAS.numberOfCities; i++) {

			List<City> nearestCities = new ArrayList<>();
			// sve udaljenosti od trenutnog grada pa do ostalih
			double[] distancesFromCity = MMAS.distances[i].clone();
			Arrays.sort(distancesFromCity);

			for (int j = 0; j < MMAS.k; j++) {

				double value = distancesFromCity[j + 1];
				// pronalazak indeksa grada
				for (int l = 0; l < distancesFromCity.length; l++) {
					if (value == MMAS.distances[i][l]) {
						nearestCities.add(MMAS.cities.get(l));
						break;
					}
				}
			}
			knearest.put(i, nearestCities);
		}
		return knearest;
	}
	
	
	/**
	 * Funkcija za azuriranje feromona na bridovima
	 */
	public static void updatePheromone(boolean onlyBest) {

		// ako feromon ostavlja samo najbolji
		if (onlyBest) {
			for (int j = 0; j < MMAS.best.getIndexes().length - 1; j++) {
				int from = MMAS.best.getIndexes()[j];
				int to = MMAS.best.getIndexes()[j + 1];
				if (MMAS.pheromone[from][to] + 1. / MMAS.best.pathDistence < MMAS.tauMax) {
					MMAS.pheromone[from][to] += 1. / MMAS.best.pathDistence;
				} else {
					MMAS.pheromone[from][to] = MMAS.tauMax;
				}
			}
		} else {
			// uzima se samo prvih firstAnts mrava
			
			Collections.sort(MMAS.population);
			
			for (int i = 0; i < CONSTANTS.firstAnts; i++) {

				Ant a = MMAS.population.get(i);

				for (int j = 0; j < a.getIndexes().length - 1; j++) {
					int from = a.getIndexes()[j];
					int to = a.getIndexes()[j + 1];
					if (MMAS.pheromone[from][to] + 1 / a.pathDistence < MMAS.tauMax) {
						MMAS.pheromone[from][to] += 1 / a.pathDistence;
					}else {
						MMAS.pheromone[from][to] = MMAS.tauMax;
					}
				}
			}
		}
	}
	

	/**
	 * Funkcija za isparavanje feromona s bridova
	 */
	public static void evaporatePheromone() {
		for (int i = 0; i < MMAS.numberOfCities; i++) {
			for (int j = 0; j < MMAS.numberOfCities; j++) {
				if (MMAS.pheromone[i][j] * (1 - CONSTANTS.ro) > MMAS.tauMin) {
					MMAS.pheromone[i][j] *= (1 - CONSTANTS.ro);
				} else {
					MMAS.pheromone[i][j] = MMAS.tauMin;
				}
			}
		}
	}
}
