package hr.fer.zemris.optjava.test;

import java.io.File;
import java.io.IOException;

import hr.fer.zemris.optjava.GA.Evaluator;
import hr.fer.zemris.optjava.GA.GA;
import hr.fer.zemris.optjava.GA.GASolution;
import hr.fer.zemris.optjava.GrayScaleImage.GrayScaleImage;

public class Test3 {

	public static void main(String[] args) throws IOException {

		File file = new File("picture/11-kuca-200-133.png");

		int width = 200;
		int height = 133;

		GrayScaleImage template = new GrayScaleImage(width, height);
		GrayScaleImage.load(file);
		
		int populationSize = 20;
		int Np = 100;
		int solutionSize = 1 + 5 * Np;
		int maxIterations = 100;
		double minError = 100;

//		Runnable job = new Runnable() {
//
//			@Override
//			public void run() {
//
//				IRNG rng = RNG.getRNG();
//
//				Evaluator evaluator = new Evaluator(template);
//				
//				List<GASolution<int[]>> population = Util.makePopulation(populationSize, solutionSize, rng);
//				
//				Util.evaluatePopulation(population, evaluator);
//				
//				System.out.println("prije sorta");
//				for(int i = 0; i < population.size(); i++) {
//					System.out.println(population.get(i).fitness);
//				}
//			
//				Util.sort(population);
//				
//				System.out.println("poslije sorta");
//				for(int i = 0; i < population.size(); i++) {
//					System.out.println(population.get(i).fitness);
//				}
//				
//				
//			}
//		};
//
//		EVOThread thread = new EVOThread(job);
//		thread.start();
		Evaluator evaluator = new Evaluator(template);
		GA ga = new GA(populationSize, solutionSize, maxIterations, minError, evaluator);
		GASolution<int[]> solution = ga.run();
		System.out.println(solution);
		int cores = Runtime.getRuntime().availableProcessors();
		System.out.println(cores);

	}

}
