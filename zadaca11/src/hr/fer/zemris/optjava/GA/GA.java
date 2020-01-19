package hr.fer.zemris.optjava.GA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class GA {

	private int populationSize;
	private int solutionSize;
	private int maxIterations;
	private double minError;
	private Evaluator evaluator;
	
	GASolution<int[]> PILL = new IntSolution(new int[] {});

	public GA(int populationSize, int solutionSize, int maxIterations, double minError, Evaluator evaluator) {
		this.populationSize = populationSize;
		this.solutionSize = solutionSize;
		this.maxIterations = maxIterations;
		this.minError = minError;
		this.evaluator = evaluator;
	}

	public void evaluate() {

	}

	public GASolution<int[]> run() {

		IRNG rng = RNG.getRNG();

		List<GASolution<int[]>> population = Util.makePopulation(populationSize, solutionSize, rng);

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
						if(solution == PILL) break;
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

//		while(currentIteration < this.maxIterations && currentBestError > this.minError) {

		unprocessedQueue.addAll(population);

		List<GASolution<int[]>> processedPopulation = new ArrayList<>();

		while (processedPopulation.size() != this.populationSize) {
			if (processedQueue.peek() != null) {
				processedPopulation.add(processedQueue.poll());
			}
		}
			Util.sort(processedPopulation);

//		}
		
		GASolution<int[]> sol = Util.select(processedPopulation, rng);
		System.out.println(sol);
		
		// gasenje pokrenutih dretvi
		for(int i = 0; i < cores; i++) {
			unprocessedQueue.add(PILL);
		}

		return population.get(0);
	}

}
