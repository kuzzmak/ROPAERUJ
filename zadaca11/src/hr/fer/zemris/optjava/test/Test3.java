package hr.fer.zemris.optjava.test;

import java.io.File;
import java.io.IOException;

import hr.fer.zemris.optjava.GA.Task;
import hr.fer.zemris.optjava.GrayScaleImage.GrayScaleImage;

public class Test3 {

	public static void main(String[] args) throws IOException {

		File file = new File("picture/11-kuca-200-133.png");

		int width = 200;
		int height = 133;

		GrayScaleImage template = new GrayScaleImage(width, height);
		template = GrayScaleImage.load(file);
		
		int populationSize = 100;
		int Np = 200;
		int solutionSize = 1 + 5 * Np;
		int maxIterations = 2000;
		double minError = 10;
		int firstN = 2;
		
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
////
////		EVOThread thread = new EVOThread(job);
////		thread.start();
//		Evaluator evaluator = new Evaluator(template);
//		GA1 ga = new GA1(populationSize, solutionSize, maxIterations, minError, firstN, evaluator, width, height);
//		GASolution<int[]> solution = ga.run();
//		
//		GrayScaleImage im = new GrayScaleImage(width, height);
//		evaluator.draw(solution, im);
//		
//		File saveTo = new File("generated.png");
//		im.save(saveTo);
//
//		System.out.println(solution);
//		IRNG rng = RNG.getRNG();
//		List<GASolution<int[]>> population = Util.makePopulation(populationSize, solutionSize, rng, width, height);
//		Util.mutate(population.get(0), rng, width, height);
		
	}

}
