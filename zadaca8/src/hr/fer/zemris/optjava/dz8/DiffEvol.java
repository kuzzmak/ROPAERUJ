package hr.fer.zemris.optjava.dz8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz8.Evaluator.IEvaluator;

public class DiffEvol {

	// evaluator za TDNN ili Elmanovu neuronsku mrezu
	private static IEvaluator evaluator;
	// string strategije
	private String strategy;
	// faktor skaliranja razlike vektor kod konstrukcije mutiranog vektora
	private static final double F = 0.5;
	
	private static final double K = 0.5 * (F + 1);
	// vjerojatnost odabira samo mutacije ili rekombinacije kod either-or 
	private double pf = 0.5;
	// velicina populacije
	private static int populationSize;
	// maksimalan broj iteracija
	private static int maxIterations;
	// vjerojatnost mutacije
	private double Cr;
	// minimalna greska nakon koje staje algoritam
	private static double minError;
	// lista jedinki, trenutna populacija
	private static List<double[]> population;
	// polje gresaka trenutne populacije
	private static double[] populationErrors;

	private static Random rand;
	// najbolja jedinka
	static double[] best;
	// najmanja greska do sada
	static double bestError = Double.MAX_VALUE;

	public DiffEvol(IEvaluator evaluator, String strategy, int populationSize, int maxIterations, double Cr,
			double minError, double minVal, double maxVal) {

		DiffEvol.evaluator = evaluator;
		this.strategy = strategy;
		DiffEvol.populationSize = populationSize;
		DiffEvol.maxIterations = maxIterations;
		this.Cr = Cr;
		DiffEvol.minError = minError;
		DiffEvol.populationErrors = new double[populationSize];
		this.population = makeInitialPopulation(populationSize, evaluator.getNumOfWeights(), minVal, maxVal);
		DiffEvol.rand = new Random();
	}

	public double[] run() {

		int iteration = 0;

		String[] strategyStrings = strategy.split("/");
		String baseVectorSelection = strategyStrings[1];
		String linearCombinations = strategyStrings[2];
		String crossoverType = strategyStrings[3];

		ICrossover crossover;

		// odabir krizanja
		if (crossoverType == "exp") {
			crossover = new ExponentialCrossover(Cr);
		} else {
			crossover = new UniformCrossover(Cr);
		}

		// sve dok je broj iteracije manji od maksimalnog ili greska najbolje 
		// jedinke veca od najmanje prihvatljive greske
		while (iteration < maxIterations && bestError > minError) {

			List<double[]> newPopulation = new ArrayList<>();
			// izracun greske svake jedinke populacije
			evaluatePopulation(population);

			for (int i = 0; i < populationSize; i++) {
				// vec odabrani indeksi u populaciji
				List<Integer> chosenIndexes = new ArrayList<>();

				// ciljni vektor
				double[] targetVector = population.get(i);
				chosenIndexes.add(i);

				// odabir baznog vektora
				double[] r0 = new double[evaluator.getNumOfWeights()];

				if (baseVectorSelection == "rand") {
					int index = rand.nextInt(population.size());
					while (chosenIndexes.contains(index)) {
						index = rand.nextInt(population.size());
					}
					r0 = population.get(index);
					chosenIndexes.add(index);
				} else {
					int index = findBest();
					r0 = population.get(index);
					chosenIndexes.add(index);
				}

				// odabir ostala dva vektora, nuzno da svi(r0, r1, r2 i target vector) budu razliciti
				int index = rand.nextInt(population.size());
				while (chosenIndexes.contains(index)) {
					index = rand.nextInt(population.size());
				}
				double[] r1 = population.get(index);
				chosenIndexes.add(index);

				index = rand.nextInt(population.size());
				while (chosenIndexes.contains(index)) {
					index = rand.nextInt(population.size());
				}
				double[] r2 = population.get(index);

				// vektor s kojim se usporedjuje target vector, odnosno jedan od njih
				// ide dalje u novu populaciju
				double[] trialVector = new double[targetVector.length];
				
				if(crossoverType == "either-or") {

					if (pf > rand.nextDouble()) {
						for (int j = 0; j < targetVector.length; j++) {

							trialVector[j] = r0[j] + F * (r1[j] - r2[j]);
						}
					} else {
						for (int j = 0; j < targetVector.length; j++) {

							trialVector[j] = r0[j] + K *(r1[j] + r2[j] - 2 * r0[j]);
						}
					}
					
				}else {
					
					double[] mutantVector = new double[targetVector.length];

					for(int j = 0; j < targetVector.length; j++) {
						
						mutantVector[j] = r0[j] + (F + 0.001 * (rand.nextDouble() - 0.5)) * (r1[j] - r2[j]);
					}
					
					trialVector = crossover.cross(mutantVector, targetVector);
				}
				
				// evaluacija target vectora, odnosno izracun greske
				double errorTargetVector = evaluator.evaluate(targetVector);
				// evaluacije trial vectora
				double errorTrialVector = evaluator.evaluate(trialVector);
				// bolji ide u sljedecu generaciju
				if (errorTrialVector < errorTargetVector) {
					newPopulation.add(trialVector);
				} else {
					newPopulation.add(targetVector);
				}
			}
			System.out.println("iter: " + iteration + ", error: " + bestError);
			population = newPopulation;
			iteration++;

		}
		// globalno najbolji je rjesenje
		return best;
	}

	/**
	 * Funkcija za stvaranje pocetne populacije jedinki
	 * 
	 * @param populationSize velicina populacije
	 * @param dimension      dimenzionalnost vektora
	 * @param minVal         minimalna vrijednost u intervalu
	 * @param maxVal         maksimalna vrijednost u intervalu
	 * @return lista vektora
	 */
	public static List<double[]> makeInitialPopulation(int populationSize, int dimension, double minVal,
			double maxVal) {

		List<double[]> population = new ArrayList<>();

		Random rand = new Random();

		for (int i = 0; i < populationSize; i++) {

			double[] weights = new double[dimension];

			for (int j = 0; j < dimension; j++) {

				weights[j] = minVal + rand.nextDouble() * (maxVal - minVal);

			}
			population.add(weights);
		}
		return population;
	}

	/**
	 * Funkcija za evaluaciju populacije
	 * 
	 * @param population lista jedinki koja se evaluira
	 */
	public static void evaluatePopulation(List<double[]> population) {

		for (int i = 0; i < populationSize; i++) {

			double error = evaluator.evaluate(population.get(i));
			// zapis u polje gresaka
			populationErrors[i] = error;

			if (error < bestError) {
				bestError = error;
				best = population.get(i);
			}

		}
	}

	/**
	 * Funkcija za dohvat indeksa najbolje jedinke u populaciji
	 * 
	 * @return najbolja jedinka ternutne populacije
	 */
	public int findBest() {

		double minErr = populationErrors[0];
		int index = 0;

		for (int i = 0; i < populationErrors.length; i++) {

			if (populationErrors[i] < minErr) {
				minErr = populationErrors[i];
				index = i;
			}
		}

		return index;
	}

}
