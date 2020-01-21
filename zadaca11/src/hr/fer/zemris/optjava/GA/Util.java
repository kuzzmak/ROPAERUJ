package hr.fer.zemris.optjava.GA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hr.fer.zemris.optjava.rng.IRNG;

public class Util {

	// vjerojatnost mutacije
	private static double p = 0.05;
	
	private static final int width = 200;
	private static final int height = 133;

//	private static double alpha = 0.02;

	/**
	 * Funkcija za stvaranje inicijalne populacije
	 * 
	 * @param populationSize velicina populacije
	 * @param solutionSize velicina pojedine jedinke
	 * @param rng generator nasumicniih brojeva
	 * @param width sirina slike	
	 * @param height visina slike
	 * @return lista jedinki
	 */
	public static List<GASolution<int[]>> makePopulation(int populationSize, int solutionSize, IRNG rng) {

		List<GASolution<int[]>> population = new ArrayList<>();

		for (int i = 0; i < populationSize; i++) {

			IntSolution is = new IntSolution(new int[solutionSize]);

			// boja			
			is.data[0] = rng.nextInt(-128, 127);
			
			for (int j = 1; j < solutionSize - 1; j += 5) {
				// sirina slike
				is.data[j] = rng.nextInt(0, width);
				// visina slike
				is.data[j + 1] = rng.nextInt(0, height);
				// broj koraka u x smjeru
				is.data[j + 2] = rng.nextInt(0, is.data[j]);
				// broj koraka u y smjeru
				is.data[j + 3] = rng.nextInt(0, is.data[j + 1]);
				// boja piksela
				is.data[j + 4] = rng.nextInt(-128, 127);
			}
			population.add(is);
		}
		return population;
	}

	/**
	 * Funkcija za mutiranje pojedine jedinke
	 * 
	 * @param solution jedinka koja se mutira
	 * @param rng generator slucajnih brojeva
	 * @param width sirina slike
	 * @param height visina slike
	 */
	public static void mutate(GASolution<int[]> solution, IRNG rng) {

		// boja
		if(p > rng.nextDouble()) {
			solution.data[0] += (int) (rng.nextGaussian() * rng.nextInt(-128, 127));
		}
		
		for (int i = 1; i < solution.data.length - 1; i += 5) {

			double rand = rng.nextGaussian();
			
			// odabir koji pravokutnik mutirati
			if (p > rng.nextDouble()) {
				
				double randWidth = rng.nextInt(0, width);
				if((solution.data[i] + rand * randWidth <= width) && (solution.data[i] + rand * randWidth >= 0)) {
					solution.data[i] += rand * randWidth;
				}
				
				double randHeight = rng.nextInt(0, height);
				if((solution.data[i + 1] + rand * randHeight <= height) && (solution.data[i + 1] + rand * randHeight >= 0)) {
					solution.data[i + 1] += rand * randHeight;
				}

				double randX = rng.nextInt(0, solution.data[i]);
				if(!(solution.data[i + 2] + rand * randX > width)) {
					solution.data[i + 2] += rand * randX;
				}
				
				double randY = rng.nextInt(0, solution.data[i + 1]);
				if(!(solution.data[i + 3] + rand * randY > width)) {
					solution.data[i + 3] += rand * randY;
				}
				
				double randColor = rng.nextInt(-128, 127);
				if(!(solution.data[i + 4] + rand * randColor > 127 || solution.data[i + 4] + rand * randColor < -128)) {
					solution.data[i + 4] += rand * randColor;
				}
			}
		}
	}

//	public static GASolution<int[]> BLXa(GASolution<int[]> parent1, GASolution<int[]> parent2, IRNG rng) {
//
//		IntSolution child = (IntSolution) parent1.duplicate();
//
//		int childLength = child.size();
//
//		for (int i = 0; i < childLength; i++) {
//
//			double di = Math.abs(parent1.data[i] - parent2.data[i]);
//
//			double lower = Math.min(parent1.data[i], parent2.data[i]) - alpha * di;
//			double upper = Math.max(parent1.data[i], parent2.data[i]) + alpha * di;
//
//			int u = (int) (rng.nextDouble(lower, upper));
//
//			child.data[i] = u;
//
//		}
//		return child;
//	}
	
	/**
	 * Funkcija za krizanje dviju jedinki
	 * 
	 * @param parent1 prva jedinka
	 * @param parent2 druga jedinka
	 * @param rng generator slucajnih brojeva
	 * @return dijete dobiveno krizanjem
	 */
	public static GASolution<int[]> cross(GASolution<int[]> parent1, GASolution<int[]> parent2, IRNG rng) {
		
		IntSolution child = (IntSolution) parent1.duplicate();
		
		child.data[0] = rng.nextDouble() >= 0.5 ? parent1.data[0] : parent2.data[0];
		
		for(int i = 1; i < child.size() - 1; i += 5) {
			
			// odabire li se pravokutnik iz drugog roditelja
			double p = rng.nextDouble();
			
			if(p <= 0.5){
				child.data[i] = parent2.data[i];
				child.data[i + 1] = parent2.data[i + 1];
				child.data[i + 2] = parent2.data[i + 2];
				child.data[i + 3] = parent2.data[i + 3];
				child.data[i + 4] = parent2.data[i + 4];
			}
		}
		return child;
	}

	/**
	 * Funkcija za sortiranje populacije prema fitnesu
	 * 
	 * @param population populacija koja se sortira
	 */
	public static void sort(List<GASolution<int[]>> population) {

		// komparator koji usporedjuje po fitnesu
		Collections.sort(population, new Comparator<GASolution<int[]>>() {

			@Override
			public int compare(GASolution<int[]> arg0, GASolution<int[]> arg1) {

				double f0 = arg0.fitness;
				double f1 = arg1.fitness;

				if (f0 > f1)
					return 1;
				if (f1 > f0)
					return -1;
				return 0;
			}
		}.reversed());
	}

	/**
	 * Funkcija za odabir jedinke pomocu proporcionalne selekcije
	 * 
	 * @param population populacija iz koje se bira jedinka
	 * @param rng generator slucajnih brojeva
	 * @return odabrana jedinka
	 */
	public static GASolution<int[]> select(List<GASolution<int[]>> population, IRNG rng) {

		// lista za fitnes jedinki iz popuation
		List<Double> populationFitness = new ArrayList<>();

		population.stream().forEach(x -> populationFitness.add(x.fitness));

		// odredjivanje najmanjeg fitnesa
		double min = populationFitness.stream().mapToDouble(x -> x).min().getAsDouble();

		// oduzimanje najmanjeg fitnesa od svih ostalih kako bi se dobili pozitivni fitnesi
		for (int i = 0; i < populationFitness.size(); i++) {
			populationFitness.set(i, populationFitness.get(i) - min);
		}

		// ukupna suma svih fitnesa
		double sum = populationFitness.stream().mapToDouble(x -> x).sum();
		
		// normalizacija fitnesa na zbroj 1
		for (int i = 0; i < populationFitness.size(); i++) {
			populationFitness.set(i, populationFitness.get(i) / sum);
		}
		
		double p = rng.nextDouble();
		double cumSum = 0;
		
		// odabir jedinke
		for(int i = 0; i < populationFitness.size(); i++) {
			cumSum += populationFitness.get(i);
			if(cumSum > p) {
				return population.get(i);
			}
		}
		return population.get(0);
	}
	
}
