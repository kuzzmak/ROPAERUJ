package hr.fer.zemris.optjava.dz7.CLONALG;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.NN.NeuralNet;
import hr.fer.zemris.optjava.dz7.PSO.Dataset;

public class CLONALG {

	// funkcija koja se optimira
	private static NeuralNet net;
	// velicina populacije
	int numberOfAntigenes;
	// broj novih antitijela koji se ubacuje u populaciju
	int d;
	// velicina antigena
	private int antigeneSize;
	// za odredjivanje velicine populacije klonova
	double beta;
	// najveci broj dozvoljenih iteracija
	private int maxIterations = 20;

	private int c = 5;
	private double ro = 5;

	private List<Antigene> population;
	// sluzi za pracenje maksimalnog afiniteta odredjene populacije
	private static double maxAffinity = 0;

	private static Random rand;

	public CLONALG(NeuralNet net, int numberOfAntigenes, int d, double beta) {
		CLONALG.net = net;
		this.numberOfAntigenes = numberOfAntigenes;
		this.d = d;
		this.beta = beta;
		CLONALG.rand = new Random();
		this.population = initializePopulation(this.numberOfAntigenes);
	}

	public void run() {

		int iteration = 0;
		while (iteration < this.maxIterations) {

			System.out.println("prije evaluate");
			evaluatePopulation(this.population);
			System.out.println("poslije evaluate");

			// --------------
			// izabrati n antigena iz populacije koji se dalje kloniraju
			// -------------

			System.out.println("prije clones");
			List<Antigene> clones = makeClones();
			System.out.println("clones size: " + clones.size());
			System.out.println("poslije clones");
			mutate(clones);
			System.out.println("poslije mutate");
			evaluatePopulation(clones);
			System.out.println("poslije evaluate");
			insertNewAntigenes(clones);
			this.population = clones;
			System.out.println("population size: " + population.size());
			System.out.println(iteration);
			iteration++;

		}
		for(int i = 0; i < population.size(); i++) {
			System.out.println(population.get(i).getFunctionValue());
		}

	}

	/**
	 * Funkcija za inicijalizaciju populacije
	 * 
	 * @return lista antigena
	 */
	private static List<Antigene> initializePopulation(int numberOfAntigenes) {

		List<Antigene> population = new ArrayList<>();
		int sizeOfAntigene = net.getNumOfWeights();

		for (int j = 0; j < numberOfAntigenes; j++) {

			double[] value = new double[sizeOfAntigene];
			for (int i = 0; i < sizeOfAntigene; i++) {
				value[i] = rand.nextDouble();
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
			population.get(i).setFunctionValue(net.calculateError());
			population.get(i).setAffinity(1. / population.get(i).getFunctionValue());
			double affinity = population.get(i).getAffinity();
			totalAff += affinity;
			if (affinity > maxAffinity)
				maxAffinity = affinity;
		}
		for (int i = 0; i < population.size(); i++) {
			population.get(i).setAffinity(population.get(i).getAffinity() / totalAff);
		}
	}

	
	public List<Antigene> makeClones() {
		List<Antigene> clones = new ArrayList<>();
		for (int i = 1; i < population.size() + 1; i++) {
			int numOfClones = (int) Math.floor(beta * population.size() / i);
			for (int j = 0; j < numOfClones; j++) {
				clones.add(population.get(i - 1).copy());
			}
		}
		return clones;
	}

	
	public void mutate(List<Antigene> clones) {

		for (int i = 0; i < clones.size(); i++) {
			// vjerojatnost da se antigen mutira
			double mutationPossibility = 1. / ro * Math.exp(-clones.get(i).getAffinity());
			
			if (rand.nextDouble() < mutationPossibility) {
				// broj mutacija na antigenu
				int numOfMutations = (int) Math.abs(clones.get(i).getAffinity() - maxAffinity) * c * antigeneSize;
				
				double[] value = clones.get(i).getValue().clone();
				for(int j = 0; j < numOfMutations; j++) {
					int index = rand.nextInt(value.length);
					value[index] += rand.nextGaussian();
					
				}
				clones.get(i).setValue(value);
			}
		}
	}
	
	
	public void insertNewAntigenes(List<Antigene> clones) {
		for(int i = 0; i < d; i++) {
			clones.add(new Antigene(antigeneSize));
		}
	}

	
	public static void main(String[] args) {
		String path = "data\\07-iris-formatirano.data";
		Dataset data = new Dataset(path);

		int[] architecture = new int[] { 4, 5, 3 };
		NeuralNet nn = new NeuralNet(architecture, data);

		int populationSize = 10;
		CLONALG cl = new CLONALG(nn, populationSize, 2, 2);
		cl.run();
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
//		System.out.println((int)Math.floor(0.02 * 100 / 1));
//		Antigene ag2 = ag1.copy();
//		System.out.println(ag1);
//		System.out.println(ag2);

	}
}
