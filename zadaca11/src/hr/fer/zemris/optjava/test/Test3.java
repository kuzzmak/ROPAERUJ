package hr.fer.zemris.optjava.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import hr.fer.zemris.optjava.GA.Evaluator;
import hr.fer.zemris.optjava.GA.GASolution;
import hr.fer.zemris.optjava.GA.Util;
import hr.fer.zemris.optjava.GrayScaleImage.GrayScaleImage;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

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
		

		Runnable job = new Runnable() {

			@Override
			public void run() {

				IRNG rng = RNG.getRNG();

				Evaluator evaluator = new Evaluator(template);
				
				List<GASolution<int[]>> population = Util.makePopulation(populationSize, solutionSize, rng);
				
				Util.evaluatePopulation(population, evaluator);
				
				System.out.println("prije sorta");
				for(int i = 0; i < population.size(); i++) {
					System.out.println(population.get(i).fitness);
				}
			
				Util.sort(population);
				
				System.out.println("poslije sorta");
				for(int i = 0; i < population.size(); i++) {
					System.out.println(population.get(i).fitness);
				}
				
				
			}
		};

		EVOThread thread = new EVOThread(job);
		thread.start();

	}

}
