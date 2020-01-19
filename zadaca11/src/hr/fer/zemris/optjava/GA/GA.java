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

		ConcurrentLinkedQueue<GASolution<int[]>> unprocessedQueue = new ConcurrentLinkedQueue<>();
		ConcurrentLinkedQueue<GASolution<int[]>> processedQueue = new ConcurrentLinkedQueue<>();

		Runnable job = new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (unprocessedQueue.peek() != null) {
						GASolution<int[]> solution = unprocessedQueue.poll();
						if(solution == PILL) break;
						System.out.println(solution);
						evaluator.evaluate(solution);
						processedQueue.add(solution);
					}
				}
			}
		};

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

//			Util.sort(processedPopulation);

//			for(int j = 0; j < processedPopulation.size(); j++) {
//				System.out.println(processedPopulation.get(j).fitness);
//			}

//		}
		
		for(int i = 0; i < cores; i++) {
			unprocessedQueue.add(PILL);
		}

		return population.get(0);
	}

}
