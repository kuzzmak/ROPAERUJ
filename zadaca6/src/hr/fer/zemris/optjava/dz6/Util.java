package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.List;

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
	
	
}
