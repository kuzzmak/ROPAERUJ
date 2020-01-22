package hr.fer.zemris.optjava.dz11;

import java.io.IOException;

import hr.fer.zemris.optjava.GA.GA1;
import hr.fer.zemris.optjava.GA.GASolution;
import hr.fer.zemris.optjava.GA.Util;

public class Pokretac1 {

	public static void main(String[] args) {
		
		String pathToTemplate = args[0];
		int np = Integer.parseInt(args[1]);
		int populationSize = Integer.parseInt(args[2]);
		int maxIterations = Integer.parseInt(args[3]);
		double minError = Double.parseDouble(args[4]);
		String pathToParameterFile = args[5];
		String pathToGeneratedPicture = args[6];
		
		int firstN = 5;
		double p = 0.05;

		GA1 ga = new GA1(pathToTemplate, np, populationSize, maxIterations, minError, 
				pathToGeneratedPicture, firstN, p);
		
		GASolution<int[]> solution = ga.run();
		
		// zapis parametara u datoteku
		try {
			Util.write(pathToParameterFile, solution);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
