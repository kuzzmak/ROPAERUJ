package hr.fer.zemris.optjava.dz6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPConfiguration {

	// put do datoteke zadatka
	private String path;
	// udaljenosti svaka dva grada
	private double[][] distances;
	// heuristicka funkcija 1 / udaljenost
	private double[][] heuristics;
	// tragovi feromona po bridovima
	private double[][] pheremone;
	// ukupan broj gradova
	private int numOfCities;
	// ime problema koji se rjesava
	private String problemName;
	// lista gradova nakon ucitavanja iz datoteke
	private List<City> citiesList;
	
	private static double beta = 2;

	public TSPConfiguration(String path) {
		this.path = path;
		this.citiesList = new ArrayList<>();
		load();
	}

	public void load() {

		try (BufferedReader br = new BufferedReader(new FileReader(this.path))) {

			String line = br.readLine().trim();
			// uzima se ime problema
			this.problemName = line.split(":")[1].trim();

			int numOfRows = 0;
			while (line != null && !line.startsWith("EOF")) {
				if (Character.isDigit(line.charAt(0))) {

					String[] row = line.split("\\s+");
					City c = new City(Double.parseDouble(row[1]), Double.parseDouble(row[2]), numOfRows);
					citiesList.add(c);

					numOfRows++;
				}
				line = br.readLine().trim();

			}

			this.numOfCities = numOfRows;

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.distances = new double[this.numOfCities][this.numOfCities];
		this.heuristics = new double[this.numOfCities][this.numOfCities];
		this.pheremone = new double[this.numOfCities][this.numOfCities];

		// racunanje udaljenosti izmedju svaka dva grada
		for (int i = 0; i < this.numOfCities; i++) {

			City c = citiesList.get(i);

			for (int j = 0; j < this.numOfCities; j++) {
				this.pheremone[i][j] = 1. / 3000;
				this.distances[i][j] = City.euclideanDistanceTo(c, citiesList.get(j));
				if(this.distances[i][j] != 0) {
					this.heuristics[i][j] = Math.pow(1. / this.distances[i][j], beta); 
				}else {
					this.heuristics[i][j] = 0;
				}
			}
		}
	}

	public int getNumOfCities() {
		return numOfCities;
	}

	public String getProblemName() {
		return problemName;
	}

	public List<City> getCitiesList() {
		return citiesList;
	}

	public double[][] getDistances() {
		return distances;
	}
	
	public double[][] getHeuristics() {
		return heuristics;
	}
	
	public double[][] getPheremone() {
		return pheremone;
	}

//	public static void main(String[] args) {
//
//		String path = "C:\\Users\\kuzmi\\Desktop\\att48.tsp\\";
//		TSPConfiguration conf = new TSPConfiguration(path);
//		conf.load();
//		List<City> cities = conf.getCitiesList();
//		for(City c: cities) {
//			System.out.println(c);
//		}
//
////		double[][] distances = conf.getDistances();
////		for (int i = 0; i < conf.numOfCities; i++) {
////			for (int j = 0; j < conf.numOfCities; j++) {
////				System.out.printf("%5.2f ", distances[i][j]);
////			}
////			System.out.println();
////		}
//
//	}

}
