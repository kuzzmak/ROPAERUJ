package hr.fer.zemris.optjava.dz7.ANNTrainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.NN.NeuralNet;

public class PSO {

	private static int populationSize;
	private static int particleSize;
	Particle[] population;
	
	private NeuralNet net;
	
	private double vmin = -0.5;
	private double vmax = 0.5;
	private double xmin = -5;
	private double xmax = 5;
	private double c1 = 2.05;
	private double c2 = 2.05;
	private int maxIterations = 10000;
	private double wmin = 0.3;
	private double wmax = 0.9;
	//private double fi = c1 + c2;
	//private double K = 2. / (Math.abs(2 - fi - Math.sqrt(fi * fi - 4 * fi)));
	private double minError = 0.02;
	
	private Random rand;
	
	private Particle globalBest;
	
	public PSO(int populationSize, int particleSize, NeuralNet net) {
		
		PSO.populationSize = populationSize;
		PSO.particleSize = particleSize;
		this.net = net;
		this.rand = new Random();
		population = this.initializePopulation();
		globalBest = this.findBest(population);
	}
	
	public double[] run() {
		
		int iteration = 0;
		double error = Double.MAX_VALUE;
		
		while(iteration < maxIterations && error > minError) {
			
			for(int i = 0; i < populationSize; i++) {
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
			
			double inertiaFac = inertiaFactor(iteration);
		
			for(int i = 0; i < populationSize; i++) {
				
				Particle p = population[i];
				double[] currentSpeed = p.getCurrentSpeed().clone();
				double[] currentLocation = p.getCurrentLocation().clone();
				double rnd = rand.nextDouble();
				
				for(int j = 0; j < particleSize; j++) {
					
//					currentSpeed[j] = inertiaFac * currentSpeed[j] + 
//							c1 * rnd * (p.getPersonalBest()[j] - currentLocation[j]) + 
//							c2 * rnd * (globalBest.getPersonalBest()[j] - currentLocation[j]);
					
					currentSpeed[j] = inertiaFac * currentSpeed[j] + 
							c1 * rnd * (p.getPersonalBest()[j] - currentLocation[j]) + 
							c2 * rnd * (pickNeighbour(p, 4).getPersonalBest()[j] - currentLocation[j]);
					
					// slucaj prevelike ili premale brzine
					if(currentSpeed[j] > vmax) currentSpeed[j] = vmax;
					if(currentSpeed[j] < vmin) currentSpeed[j] = vmin;
					currentLocation[j] += currentSpeed[j];
					
				}

				population[i].setCurrentLocation(currentLocation);
				population[i].setCurrentSpeed(currentSpeed);
				
			}
			iteration++;
			error = globalBest.getBestValue();
			System.out.println("iteration: " + iteration + ", value: " + globalBest.getBestValue() + ", inertia: " + inertiaFac);
		}
		
		return globalBest.getPersonalBest();
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
	 * Funkcija za izracun faktora inercije u odredjenoj iteraciji
	 * 
	 * @param iteration broj iteracije
	 * @return vrijednost faktora inercije
	 */
	public double inertiaFactor(double iteration) {
		if(iteration <= maxIterations) {
			return iteration / maxIterations * (wmin - wmax) + wmax;
		}
		return wmin;
	}
	
	public Particle pickNeighbour(Particle p, int numOfNeighbours) {
		
		// udaljenosti od cestice p do svih ostalih
		double[] distances = new double[population.length];
		// udaljenosti koje sluze za usporedbu sa sortiranim udaljenostima
		// u svrhu dohvata indeksa cestice kojoj udaljenot odgovara
		double[] distancesUnshuffled = new double[population.length];
		
		// izracun euklidske udaljenosti
		for(int i = 0; i < population.length; i++) {
			for(int j = 0; j < particleSize; j++) {
				distances[i] += Math.pow(p.getCurrentLocation()[j] - population[i].getCurrentLocation()[j], 2);
			}
			distances[i] = Math.sqrt(distances[i]);
			distancesUnshuffled[i] = distances[i];
		}
		// sortiranje po vrijednostima uzlazno
		Arrays.parallelSort(distances);
		// polje najblizih susjeda
		Particle[] nearest = new Particle[numOfNeighbours];
		// pronalazak najblizih po indeksu
		for(int i = 0; i < numOfNeighbours; i++) {
			
			double dist = distances[i + 1];
			int index = 0;
			for(int j = 0; j < distancesUnshuffled.length; j++) {
				if(dist == distancesUnshuffled[j]) {
					index = j;
					break;
				}
			}
			nearest[i] = population[index];
		}
		
		Particle bestNeighbour = nearest[0];
		for(int i = 1; i < nearest.length; i++) {
			if(nearest[i].getBestValue() > bestNeighbour.getBestValue()) bestNeighbour = nearest[i];
		}
		return bestNeighbour;
	}
}
