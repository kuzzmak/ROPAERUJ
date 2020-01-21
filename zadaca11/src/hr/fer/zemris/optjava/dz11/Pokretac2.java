package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.optjava.GA.GA2;
import hr.fer.zemris.optjava.GA.GASolution;

public class Pokretac2 {

	public static void main(String[] args) {

		String pathToTemplate = "picture/11-kuca-200-133.png";
		int np = 100;
		int populationSize = 200;
		int maxIterations = 5000;
		double minError = 1000;
		String pathToParameterFile = "parameters.txt";
		String pathToGeneratedPicture = "generated.png";

		GA2 ga = new GA2(pathToTemplate, np, populationSize, maxIterations, minError, pathToParameterFile,
				pathToGeneratedPicture);
		
		GASolution<int[]> solution = ga.run();

	}
}
