package hr.fer.zemris.optjava.dz7.CLONALG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.NN.NeuralNet;
import hr.fer.zemris.optjava.dz7.PSO.Dataset;

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
	private int maxIterations = 200;
	// konstanta za reguliranje vjerojatnosti mutacije
	private double ro = 0.5;

	private List<Antigene> population;
	// sluzi za pracenje maksimalnog afiniteta odredjene populacije

	private static Random rand;

	public CLONALG(NeuralNet net, int numberOfAntigenes, int d, double beta) {
		CLONALG.net = net;
		CLONALG.numberOfAntigenes = numberOfAntigenes;
		this.d = d;
		this.antigeneSize = CLONALG.net.getNumOfWeights();
		this.beta = beta;
		CLONALG.rand = new Random();
		this.population = initializePopulation();
	}

	public Antigene run() {

		int iteration = 0;
		while (iteration < this.maxIterations) {

			evaluatePopulation(this.population);

			// --------------
			// izabrati n antigena iz populacije koji se dalje kloniraju
			// -------------

			List<Antigene> clones = makeClones();
			mutate(clones);
			evaluatePopulation(clones);
			pickAntigenes(clones);
			if (iteration % 50 == 0) insertNewAntigenes(clones);
			this.population = clones;
			System.out.println("iter: " + iteration + ", minerr: " + population.get(0).getFunctionValue());
			iteration++;

		}
		return population.get(0);
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

	public static void main(String[] args) {
		String path = "data\\07-iris-formatirano.data";
		Dataset data = new Dataset(path);

		int[] architecture = new int[] { 4, 5, 3, 3 };
		NeuralNet nn = new NeuralNet(architecture, data);

		int populationSize = 30;
		int d = 5;
		double beta = 5;
		CLONALG cl = new CLONALG(nn, populationSize, d, beta);
		nn.setWeights(cl.run().getValue());

		double[] prediction = nn.predict(new double[] { 5.1, 3.5, 1.4, 0.2 });
		System.out.println("prediction1: " + Arrays.toString(prediction));
		prediction = nn.predict(new double[] { 4.9, 3.0, 1.4, 0.2 });
		System.out.println("prediction2: " + Arrays.toString(prediction));
		prediction = nn.predict(new double[] { 6.3, 3.3, 6.0, 2.5 });
		System.out.println("prediction3: " + Arrays.toString(prediction));

//		List<Antigene> population = initializePopulation(populationSize);
//		for (int i = 0; i < population.size(); i++) {
//			System.out.println(population.get(i));
//		}
//		System.out.println();
//		evaluatePopulation(population);
//		for (int i = 0; i < population.size(); i++) {
//			System.out.println(population.get(i).getFunctionValue());
//		}
//		System.out.println();
//		for (int i = 0; i < population.size(); i++) {
//			System.out.println(population.get(i).getAffinity());
//		}

//		Antigene ag1 = new Antigene(3);
//		System.out.println(Arrays.toString(ag1.getValue()));
//		System.out.println((int)Math.floor(0.02 * 100 / 1));
//		Antigene ag2 = ag1.copy();
//		System.out.println(ag1);
//		System.out.println(ag2);

	}
}
