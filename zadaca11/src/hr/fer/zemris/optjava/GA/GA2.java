package hr.fer.zemris.optjava.GA;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import hr.fer.zemris.optjava.GrayScaleImage.GrayScaleImage;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class GA2 {
	
	private String pathToTemplate;
	private int Np;
	private int populationSize;
	private int maxIterations;
	private double minError;
	private String pathToParameterFile;
	private String pathToGeneratedPicture;
	
	private static final int height = 133;
	private static final int width = 200;

	private static double p = 0.05;
	private int firstN = 2;
	
	private ThreadLocal<GrayScaleImage> tlgsi = ThreadLocal.withInitial(() -> {return new GrayScaleImage(width, height);});

	/**
	 * @param pathToTemplate staza do originalne PNG slike
	 * @param np broj pravokutnika
	 * @param populationSize velicina populacije
	 * @param maxIterations broj iteracija
	 * @param minError minimalni -fitnes
	 * @param pathToParameterFile staza do txt datoteke za ispis optimalnih parametara
	 * @param pathToGeneratedPicture staza do lokacije spremanje generirane slike
	 */
	public GA2(String pathToTemplate, int np, int populationSize, int maxIterations, double minError,
			String pathToParameterFile, String pathToGeneratedPicture) {
		super();
		this.pathToTemplate = pathToTemplate;
		this.Np = np;
		this.populationSize = populationSize;
		this.maxIterations = maxIterations;
		this.minError = minError;
		this.pathToParameterFile = pathToParameterFile;
		this.pathToGeneratedPicture = pathToGeneratedPicture;
	}
		
	public GASolution<int[]> run(){
		
		// trenutna iteracija algoritma
		int currentIter = 0;
		// najbolja greska do sada
		double currentError = Double.MAX_VALUE;
		// velicina pojedine jedinke
		int solutionSize = 1 + 5 * this.Np;
		// broj djece koji svaki task generira
		int numOfChildren = 5;
		
		// staza do png slike kuce
		File file = new File(this.pathToTemplate);
		
		GrayScaleImage template = new GrayScaleImage(width, height);
		try {
			template = GrayScaleImage.load(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Evaluator evaluator = new Evaluator(template);
		
		// generator slucajnih brojeva
		IRNG rng = RNG.getRNG();
		
		// broj raspolozivih jezgri
		int nThreads = Runtime.getRuntime().availableProcessors();
		
		ExecutorService executorService = Executors.newFixedThreadPool(nThreads, new EvoThreadFactory());
		
		// inicijalna populacija
		List<GASolution<int[]>> population = Util.makePopulation(this.populationSize, solutionSize, rng);
		
		// inicijalna evaluacija populacije
		GrayScaleImage im = new GrayScaleImage(width, height);
		for(GASolution<int[]> solution: population) {
			evaluator.evaluate(solution, im);
		}
		
		// glavna petlja
		while(currentIter < this.maxIterations && currentError > this.minError) {
			
			List<GASolution<int[]>> children = new ArrayList<>();
			
			// dodavanje prvih firstN djece u populaciju u sljedecoj iteraciji
			children.addAll(population.stream().limit(firstN).collect(Collectors.toList()));
			
			// poslovi za obaviti
			List<Callable<List<GASolution<int[]>>>> tasks = new ArrayList<>();
			
			// dodavanje poslova 
			for(int i = 0; i < (this.populationSize + firstN) / numOfChildren; i++) {
				
				tasks.add(new Task(population, evaluator, p, numOfChildren, tlgsi));
			}
			
			// dohvacanje rezultata
			List<Future<List<GASolution<int[]>>>> results = new ArrayList<>();
			try {
				results = executorService.invokeAll(tasks);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			// stavljanje djece u listu djece 
			for(int i = 0; i < results.size(); i++) {
				try {
					children.addAll(results.get(i).get());
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			Util.sort(children);
			
			for(int i = 0; i < firstN; i++) {
				children.remove(children.size() - 1);
			}
			
			population = new ArrayList<>(children);
			
			// trenutna greska najbolje jedinke
			currentError = -population.get(0).fitness;
			
			System.out.println("current iter: " + currentIter + ", err: " + currentError);
			currentIter++;
		}
		
		// gasenje thread-poola
		executorService.shutdown();
		
		// najbolja jedinka
		GASolution<int[]> best = population.get(0);
		
		// spremanje najboljg rjesenja kao slike
		File saveTo = new File(this.pathToGeneratedPicture);
		evaluator.draw(best, im);
		try {
			im.save(saveTo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return best;
	}
}
