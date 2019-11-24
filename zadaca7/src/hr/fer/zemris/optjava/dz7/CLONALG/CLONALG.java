package hr.fer.zemris.optjava.dz7.CLONALG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.NN.NeuralNet;

public class CLONALG {

	// funkcija koja se optimira
	private static NeuralNet net;
	// velicina populacije
	static int numberOfAntigenes;
	// broj novih antitijela koji se ubacuje u populaciju
	int d;
	// velicina antigena
	private int antigeneSize;
	// za odredjivanje velicine populacije klonova
	double beta;
	// najveci broj dozvoljenih iteracija
	private int maxIterations;
	// konstanta za reguliranje vjerojatnosti mutacije
	private double ro = 0.5;
	// minimalna prihvatljiva greska
	private double minError;
	
	private List<Antigene> population;
	// sluzi za pracenje maksimalnog afiniteta odredjene populacije

	private static Random rand;

	public CLONALG(NeuralNet net, int numberOfAntigenes, int d, double beta, int maxIterations, double minError) {
		CLONALG.net = net;
		CLONALG.numberOfAntigenes = numberOfAntigenes;
		this.d = d;
		this.antigeneSize = CLONALG.net.getNumOfWeights();
		this.beta = beta;
		this.maxIterations = maxIterations;
		this.minError = minError;
		CLONALG.rand = new Random();
		this.population = initializePopulation();
	}

	public double[] run() {

		int iteration = 0;
		// najbolja greska trenutne populacije
		double error = Double.MAX_VALUE;
		
		while (iteration < this.maxIterations && error > minError) {

			evaluatePopulation(this.population);
			List<Antigene> clones = makeClones();
			mutate(clones);
			evaluatePopulation(clones);
			pickAntigenes(clones);
			if (iteration % 50 == 0) insertNewAntigenes(clones);
			this.population = clones;
			error = population.get(0).getFunctionValue();
			System.out.println("iter: " + iteration + ", minerr: " + population.get(0).getFunctionValue());
			iteration++;

		}
		return population.get(0).getValue();
	}

	/**
	 * Funkcija za inicijalizaciju populacije
	 * 
	 * @return lista antigena
	 */
	private static List<Antigene> initializePopulation() {

		List<Antigene> population = new ArrayList<>();
		int sizeOfAntigene = net.getNumOfWeights();

		for (int j = 0; j < numberOfAntigenes; j++) {

			double[] value = new double[sizeOfAntigene];
			for (int i = 0; i < sizeOfAntigene; i++) {
				value[i] = rand.nextGaussian();
			}
			population.add(new Antigene(value));
		}

		return population;
	}

	/**
	 * Funkcija za azuriranje vrijednosti funkcije i afiniteta pojedinog antigena
	 * 
	 * @param population populacija koja se vrednuje
	 */
	public static void evaluatePopulation(List<Antigene> population) {
		double totalAff = 0;
		for (int i = 0; i < population.size(); i++) {
			net.setWeights(population.get(i).getValue());
			// vrijednost greske u danom polozaju
			double functionValue = net.calculateError();
			population.get(i).setFunctionValue(functionValue);
			// vrijednost afiniteta
			double affinity = 1. / functionValue;
			population.get(i).setAffinity(affinity);

			totalAff += affinity;
		}
		for (int i = 0; i < population.size(); i++) {
			double affinity = population.get(i).getAffinity() / totalAff;
			population.get(i).setAffinity(affinity);
		}
	}

	/**
	 * Funkcija za stvaranje klonova
	 * 
	 * Broj klonova ovisi o afinitetu pojedinog antigena
	 * Veci afinitet -> veci broj klonova
	 * 
	 * @return lista klonova
	 */
	public List<Antigene> makeClones() {
		
		List<Antigene> clones = new ArrayList<>();
		
		for (int i = 1; i < population.size() + 1; i++) {
			// broj klonova pojedinog antigena
			int numOfClones = (int) Math.floor(beta * population.size() / i);
			
			for (int j = 0; j < numOfClones; j++) {
				// kopija antigena
				Antigene ng = population.get(i - 1).copy();
				clones.add(ng);
			}
		}
		return clones;
	}

	/**
	 * Funkcija za mutaciju pojedinog antigena
	 * 
	 * @param clones populacija antigena koje se mutira
	 */
	public void mutate(List<Antigene> clones) {

		// koristi se za izracun broja mutacija na pojedinom antigenu
		double tau = -(numberOfAntigenes - 1) / Math.log(1 - ro);
		
		for (int i = 0; i < clones.size(); i++) {

			double numOfMutations = 1 + numberOfAntigenes * (1 - Math.exp(-i / tau));
			// kopija vrijednosti antigena
			double[] value = clones.get(i).getValue().clone();
			for (int j = 0; j < numOfMutations; j++) {
				// indeks na kojem se radi mutacija
				int index = rand.nextInt(value.length);
				value[index] += rand.nextGaussian();
			}
			clones.get(i).setValue(value);
		}
	}

	/**
	 * Funkcija za biranje antigena koji ostaju nakon mutacija U ovom slucaju samo
	 * se izbace najgori
	 * 
	 * @param clones
	 */
	public void pickAntigenes(List<Antigene> clones) {
		Collections.sort(clones);
		while (clones.size() != numberOfAntigenes) {
			clones.remove(clones.size() - 1);
		}

	}

	/**
	 * Funkcija za unos novih antigena u populaciju Najgorih d se izbaci, a ubaci se
	 * d nasumicno generiranih
	 * 
	 * @param clones populacija klonova koju se ubacuju novi antigeni
	 */
	public void insertNewAntigenes(List<Antigene> clones) {
		for (int i = 0; i < d; i++) {
			clones.remove(clones.size() - 1);
			clones.add(new Antigene(antigeneSize));
		}
	}
}
