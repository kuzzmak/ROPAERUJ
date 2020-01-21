package hr.fer.zemris.optjava.GA;

import java.util.List;

import hr.fer.zemris.optjava.GrayScaleImage.GrayScaleImage;

public class GA2 {
	
	private String pathToTemplate;
	private int Np;
	private int populationSize;
	private int maxIterations;
	private double minError;
	private String pathToParameterFile;
	private String pathToGeneratedPicture;
	
	private int height = 133;
	private int width = 200;
	
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
		Np = np;
		this.populationSize = populationSize;
		this.maxIterations = maxIterations;
		this.minError = minError;
		this.pathToParameterFile = pathToParameterFile;
		this.pathToGeneratedPicture = pathToGeneratedPicture;
	}
		
	public GASolution<int[]> run(){
		
		int currentIter = 0;
		double currentError = Double.MAX_VALUE;
		int solutionSize = 1 + 5 * this.Np;
		
		
		List<GASolution<int[]>> population = Util.makePopulation(this.populationSize, solutionSize, rng);
		
		
		
		
	}
	

}
