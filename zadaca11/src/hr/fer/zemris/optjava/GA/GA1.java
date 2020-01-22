package hr.fer.zemris.optjava.GA;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import hr.fer.zemris.optjava.GrayScaleImage.GrayScaleImage;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class GA1 {

	private String pathToTemplate;
	private int Np;
	private int populationSize;
	private int maxIterations;
	private double minError;
	private String pathToGeneratedPicture;
	private int firstN;
	
	private double p;
	
	// jedinka za gasenje dretvi
	GASolution<int[]> PILL = new IntSolution(new int[] {});

	
	/**
	 * @param pathToTemplate staza do originalne PNG slike
	 * @param np broj pravokutnika
	 * @param populationSize velicina populacije
	 * @param maxIterations broj iteracija
	 * @param minError minimalni -fitnes
	 * @param pathToParameterFile staza do txt datoteke za ispis optimalnih parametara
	 * @param pathToGeneratedPicture staza do lokacije spremanje generirane slike
	 */
	public GA1(String pathToTemplate, int np, int populationSize, int maxIterations, double minError,
			String pathToGeneratedPicture, int firstN, double p) {
		super();
		this.pathToTemplate = pathToTemplate;
		this.Np = np;
		this.populationSize = populationSize;
		this.maxIterations = maxIterations;
		this.minError = minError;
		this.pathToGeneratedPicture = pathToGeneratedPicture;
		this.firstN = firstN;
		this.p = p;
	}

	public GASolution<int[]> run() {

		IRNG rng = RNG.getRNG();
		
		int solutionSize = 1 + 5 * this.Np;

		List<GASolution<int[]>> population = Util.makePopulation(populationSize, solutionSize, rng);

		// broj dretvi koji ce se stvoriti
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
				
				// dohvat evaluatora pojedine dretve
				Evaluator evaluator = ((EVOThread)Thread.currentThread()).getEvaluator();
				
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

		GrayScaleImage template = new GrayScaleImage(Util.width, Util.height);
		try {
			template = GrayScaleImage.load(new File(this.pathToTemplate));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// pokretanje dretvi
		for (int i = 0; i < cores; i++) {
			EVOThread thread = new EVOThread(job);
			thread.setEvaluator(new Evaluator(template.duplicate()));
			thread.start();
		}

		while (currentIteration < this.maxIterations && currentBestError > this.minError) {

			unprocessedQueue.addAll(population);

			List<GASolution<int[]>> processedPopulation = new ArrayList<>();

			while (processedPopulation.size() != population.size()) {
				if (processedQueue.peek() != null) {
					processedPopulation.add(processedQueue.poll());
				}
			}
			
			Util.sort(processedPopulation);
			
			currentBestError = -processedPopulation.get(0).fitness;
			
			// brisanje zadnjih firstN jedinki
			for(int i = 0; i < firstN; i++) {
				processedPopulation.remove(processedPopulation.size() - 1);
			}

			List<GASolution<int[]>> offspring = new ArrayList<>();
			
			// dodavanje prvih firstN jedinki u populaciju offspring
			offspring.addAll(processedPopulation.stream().limit(firstN).collect(Collectors.toList()));
			
			while (offspring.size() < this.populationSize + this.firstN) {
				
				GASolution<int[]> parent1 = Util.select(processedPopulation, rng);
				GASolution<int[]> parent2 = Util.select(processedPopulation, rng);

				while (parent1 == parent2) {
					parent2 = Util.select(processedPopulation, rng);
				}

				GASolution<int[]> child1 = Util.cross(parent1, parent2, rng);
				GASolution<int[]> child2 = Util.cross(parent1, parent2, rng);

				Util.mutate(child1, rng, p);
				Util.mutate(child2, rng, p);
				
				offspring.add(child1);
				offspring.add(child2);
			}

			System.out.println("current iter: " + currentIteration + ", minerr: " + currentBestError);
			
			population = new ArrayList<>(offspring);
			currentIteration++;
		}

		// gasenje pokrenutih dretvi
		for (int i = 0; i < cores; i++) {
			unprocessedQueue.add(PILL);
		}

		Util.sort(population);
		
		GASolution<int[]> best = population.get(0);
		
		// spremanje slike
		Evaluator evaluator = new Evaluator(template);
		GrayScaleImage im = new GrayScaleImage(Util.width, Util.height);
		evaluator.draw(best, im);
		
		File saveTo = new File(this.pathToGeneratedPicture);
		try {
			im.save(saveTo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return population.get(0);
	}

}
