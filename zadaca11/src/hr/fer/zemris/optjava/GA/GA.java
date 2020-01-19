package hr.fer.zemris.optjava.GA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class GA {

	private int populationSize;
	private int solutionSize;
	private int maxIterations;
	private double minError;
	private Evaluator evaluator;
	private int width;
	private int height;
	
	// broj najbojlih jedinki populacije koji se dodaje u sljedecu populaciju
	private int firstN = 1;

	GASolution<int[]> PILL = new IntSolution(new int[] {});

	public GA(int populationSize, int solutionSize, int maxIterations, double minError, Evaluator evaluator, int width, int height) {
		this.populationSize = populationSize;
		this.solutionSize = solutionSize;
		this.maxIterations = maxIterations;
		this.minError = minError;
		this.evaluator = evaluator;
		this.width = width;
		this.height = height;
	}

	public void evaluate() {

	}

	public GASolution<int[]> run() {

		IRNG rng = RNG.getRNG();

		List<GASolution<int[]>> population = Util.makePopulation(populationSize, solutionSize, rng, width, height);

		int cores = Runtime.getRuntime().availableProcessors();

		int currentIteration = 0;
		double currentBestError = Double.MAX_VALUE;

		// red jedinki koje nisu vrednovane
		ConcurrentLinkedQueue<GASolution<int[]>> unprocessedQueue = new ConcurrentLinkedQueue<>();
		// red jedinki koje su vrednovane
		ConcurrentLinkedQueue<GASolution<int[]>> processedQueue = new ConcurrentLinkedQueue<>();

		Runnable job = new Runnable() {

			@Override
			public void run() {
				while (true) {
					// ako postoji neka jedinka u redu, dohvaca se
					if (unprocessedQueue.peek() != null) {
						// dohvat prve jedinke iz reda
						GASolution<int[]> solution = unprocessedQueue.poll();
						// ako je dohvacena jedinka PILL, dretva se gasi
						if (solution == PILL)
							break;
						// dodjeljivanje fitnesa jedinki
						evaluator.evaluate(solution);

						processedQueue.add(solution);
					}
				}
			}
		};

		// pokretanje dretvi
		for (int i = 0; i < cores; i++) {
			EVOThread thread = new EVOThread(job);
			thread.start();
		}

		while (currentIteration < this.maxIterations && currentBestError > this.minError) {

			unprocessedQueue.addAll(population);

			List<GASolution<int[]>> processedPopulation = new ArrayList<>();

			while (processedPopulation.size() != this.populationSize) {
				if (processedQueue.peek() != null) {
					processedPopulation.add(processedQueue.poll());
				}
			}

			processedPopulation.remove(processedPopulation.size() - 1);
			processedPopulation.remove(processedPopulation.size() - 2);
			
			Util.sort(processedPopulation);

			List<GASolution<int[]>> offspring = new ArrayList<>();
			
			// dodavanje prvih firstN jedinki u populaciju offspring
			offspring.addAll(processedPopulation.stream().limit(firstN).collect(Collectors.toList()));
			
			while (offspring.size() < this.populationSize + 2) {
				
				GASolution<int[]> parent1 = Util.select(processedPopulation, rng);
				GASolution<int[]> parent2 = Util.select(processedPopulation, rng);

				while (parent1 == parent2) {
					parent2 = Util.select(processedPopulation, rng);
				}

				GASolution<int[]> child1 = Util.BLXa(parent1, parent2, rng);
				GASolution<int[]> child2 = Util.BLXa(parent1, parent2, rng);

				Util.mutate(child1, rng, this.width, this.height);
				Util.mutate(child2, rng, this.width, this.height);
				
				offspring.add(child1);
				offspring.add(child2);
			}

			System.out.println("current iter: " + currentIteration + ", minerr: " + currentBestError);
			
//			unprocessedQueue.addAll(offspring);
//			
//			processedPopulation = new ArrayList<>();
//			
//			while (processedPopulation.size() != this.populationSize + 2) {
//				if (processedQueue.peek() != null) {
//					processedPopulation.add(processedQueue.poll());
//				}
//			}
//			
//			Util.sort(processedPopulation);
//			
//			offspring.remove(offspring.size() - 1);
//			offspring.remove(offspring.size() - 2);
			
			population = new ArrayList<>(offspring);
			currentIteration++;
			
			currentBestError = -population.get(0).fitness;
		}

		// gasenje pokrenutih dretvi
		for (int i = 0; i < cores; i++) {
			unprocessedQueue.add(PILL);
		}

		return population.get(0);
	}

}
