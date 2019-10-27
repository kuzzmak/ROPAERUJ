package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Population {

	/**
	 * Funkcija za stvaranje pocetne populacije
	 * 
	 * @param populationSize velicina populacije
	 * @param sticks lista stapova
	 * @param binSize velicina spremnika
	 * @return populacija kromosoma
	 */
	public static List<Chromosome> makePopulation(int populationSize, List<Stick> sticks, int binSize) {

		List<Chromosome> population = new ArrayList<>();

		int counter = 0;

		while (counter <= populationSize) {
			
			// nakon svake iteracije se random izmijesaju stapovi
			Collections.shuffle(sticks);
			population.add(new Chromosome(sticks, binSize));
			counter++;
		}
		return population;
	}
}
