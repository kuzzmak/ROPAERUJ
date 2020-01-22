package hr.fer.zemris.optjava.dz11;

import java.io.IOException;

import hr.fer.zemris.optjava.GA.GA2;
import hr.fer.zemris.optjava.GA.GASolution;
import hr.fer.zemris.optjava.GA.Util;

public class Pokretac2 {

	public static void main(String[] args) {

		String pathToTemplate = args[0];
		int np = Integer.parseInt(args[1]);
		int populationSize = Integer.parseInt(args[2]);
		int maxIterations = Integer.parseInt(args[3]);
		double minFitness = Double.parseDouble(args[4]);
		String pathToParameterFile = args[5];
		String pathToGeneratedPicture = args[6];
		
		int firstN = 2;
		double p = 0.05;
		int numOfChildren = 5;

		GA2 ga = new GA2(pathToTemplate, np, populationSize, maxIterations, minFitness, 
				pathToGeneratedPicture, firstN, p, numOfChildren);
		
		GASolution<int[]> solution = ga.run();
		
		// zapis parametara u datoteku
		try {
			Util.write(pathToParameterFile, solution);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
