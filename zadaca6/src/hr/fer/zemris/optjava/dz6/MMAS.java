package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MMAS {

	// matrica udaljenosti izmedju dva grada
	public static double[][] distances;
	// matrica heuristike izmedju dva grada
	public static double[][] heuristics;
	// matrica feromona izmedju dva grada
	public static double[][] pheromone;
	// ukupan broj gradova
	public static int numberOfCities;
	// lista svih gradova iz datoteke
	public static List<City> cities;
	// jedna populacija mrava
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
	// najbolji mrav sveukupno
	public static Ant best;
	// broj potencijalnih susjeda
	protected static int k;
	// velicina populacije
	private int populationSize;
	// maksimalan broj iteracija
	private int numOfIterations;
	
	TSPConfiguration conf;
	public static Random rand;
	
	public MMAS(String path, int k, int populationSize, int numOfIterations) {
		MMAS.k = k;
		this.populationSize = populationSize;
		this.numOfIterations = numOfIterations;
		this.conf = new TSPConfiguration(path);
		
		MMAS.distances = conf.getDistances();
		MMAS.heuristics = conf.getHeuristics();
		MMAS.pheromone = conf.getPheremone();
		MMAS.numberOfCities = conf.getNumOfCities();
		MMAS.cities = conf.getCitiesList();
		MMAS.rand = new Random();
		new GreedyAlgorithm(cities);
		MMAS.mi = (numberOfCities - 1) / (numberOfCities * (-1 + Math.pow(CONSTANTS.p, -1. / numberOfCities)));
		MMAS.a = mi * numberOfCities;
		MMAS.cStar = GreedyAlgorithm.run();
		MMAS.tauMax = 1 / (CONSTANTS.ro * cStar);
		MMAS.tauMin = tauMax / a;
		MMAS.best = new Ant(numberOfCities);
	}
	

	public int[] run() {

		// mapa gdje su kljucevi indeksi gradova, a vrijednosti su liste s
		// indeksima k najblizih gradova
		Map<Integer, List<City>> kNearest = Util.kNearestCities();

		// brojac koliko je proslo iteracija bez novog najboljeg mrava
		int worstIter = 0;
		
		for (int iter = 0; iter < numOfIterations; iter++) {

			population = new ArrayList<>();

			for (int k = 0; k < populationSize; k++) {

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
					
					// micanje susjednih gradova koji su vec posjeceni
					nearestCities.removeAll(route);

					// ako lista susjeda nije prazna, odaberemo jedan grad
					if (nearestCities.size() != 0) {
					
						City c = Util.selectCity(nearestCities, route, i);

						route.add(c);

					} else {

						// ako je lista susjeda prazna, odaberemo jedan grad iz neposjecenih
						City c = Util.selectCity(remaining, route, i);
						route.add(c);

					}
				}
				// novi mrav sa svojom rutom
				Ant ant = new Ant(route);
				if (ant.pathDistence < best.pathDistence) {
					best = ant;
				}else {
					worstIter++;
				}
				if(worstIter > 1000) {
					Util.resetPheromone();
				}
				population.add(ant);
			}

			Util.evaporatePheromone();
			Util.updatePheromone(false);
		
		}
		return Util.makeSolution(best.getIndexes());
	}
}
