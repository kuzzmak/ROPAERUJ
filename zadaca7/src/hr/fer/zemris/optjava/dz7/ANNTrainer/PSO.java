package hr.fer.zemris.optjava.dz7.ANNTrainer;

import java.util.Random;

import hr.fer.zemris.optjava.dz7.NN.NeuralNet;

public class PSO {

	private static int populationSize;
	private static int particleSize;
	Particle[] population;
	
	private NeuralNet net;
	
	
	private double vmin = -5;
	private double vmax = 5;
	private double xmin = -5;
	private double xmax = 5;
	private double c1 = 2;
	private double c2 = 2;
	private int maxIterations = 10000;
	private double wmin = 0.4;
	private double wmax = 0.9;
	
	private Random rand;
	
	Particle globalBest;
	
	public PSO(int populationSize, int particleSize, NeuralNet net) {
		
		PSO.populationSize = populationSize;
		PSO.particleSize = particleSize;
		this.net = net;
		this.rand = new Random();
		population = this.initializePopulation();
		globalBest = this.findBest(population);
	}
	
	public void run() {
		
		//while(true) {
			
			for(int i = 0; i < population.length; i++) {
				// dohvat trenutne lokacije
				double[] currentLocation = population[i].getCurrentLocation();
				// slanje tezina u mrezu
				net.setWeights(currentLocation);
				// greska u trenutnoj lokaciji
				double value = net.calculateError();
				// ako je greska manja od trenutne postaje personalBests
				if(value < population[i].getBestValue()) {
					population[i].setBestValue(value);
					population[i].setPersonalBest(currentLocation);
				}
			}

			// ima li neka cestica u populaciji bolje rjesenje od najboljeg
			Particle bestInPopulation = findBest(population);
			if(bestInPopulation.getBestValue() < globalBest.getBestValue()) {
				globalBest = bestInPopulation;
			}
			
			
		//}
		
		
		
		
	}
	
	
	
	
	public static void main(String[] args) {
		
		String path = "data\\07-iris-formatirano.data";
		Dataset data = new Dataset(path);
		
		int[] architecture = new int[] {4,3,3};
		NeuralNet nn = new NeuralNet(architecture, data);
		
		PSO pso = new PSO(10, nn.getNumOfWeights(), nn);
		pso.run();
		Particle[] population = pso.population;
//		for(int i = 0; i < population.length; i++) {
//			System.out.println(population[i].getBestValue());
//		}
		
	}
	
	
	/**
	 * Funkcija za inicijalizaciju pocetne populacije
	 * 
	 * @return populacija cestica
	 */
	public Particle[] initializePopulation() {
		
		Particle[] population = new Particle[populationSize];
		
		for(int i = 0; i < populationSize; i++) {
			
			Particle p = new Particle(particleSize);
			double[] speed = new double[particleSize];
			double[] location = new double[particleSize];
			
			for(int j = 0; j < particleSize; j++) {
				speed[j] = rand.nextDouble() * (vmax - vmin) + vmin;
				location[j] = rand.nextDouble() * (xmax - xmin) + xmin;
			}
			
			p.setCurrentLocation(location);
			p.setCurrentSpeed(speed);
			p.setPersonalBest(location);
			
			population[i] = p;
		}
		return population;
	}
	
	/**
	 * Funkcija za pronalazak najbolje cestice u populaciji
	 * 
	 * @param population populacija cestica
	 * @return najbolja cestica
	 */
	public Particle findBest(Particle[] population) {
		
		Particle bestParticle = population[0];
		
		for(int i = 0; i < population.length; i++) {
			if(population[i].getBestValue() < bestParticle.getBestValue()) {
				bestParticle = population[i];
			}
		}
		return bestParticle;
	}
	
	/**
	 * Funkcija za izracn faktora inercije u odredjenoj iteraciji
	 * 
	 * @param iteration broj iteracije
	 * @return vrijednost faktora inercije
	 */
	public double inertiaFactor(int iteration) {
		if(iteration <= maxIterations) {
			return (double)(iteration / maxIterations) * (wmin - wmax) + wmax;
		}
		return wmin;
	}
}
