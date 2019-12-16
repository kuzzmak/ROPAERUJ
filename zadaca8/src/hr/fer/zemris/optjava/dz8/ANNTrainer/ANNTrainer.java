package hr.fer.zemris.optjava.dz8.ANNTrainer;

import java.util.Arrays;
import java.util.Random;

import javax.swing.text.ElementIterator;

import hr.fer.zemris.optjava.dz8.Data.Dataset;
import hr.fer.zemris.optjava.dz8.Evaluator.ElmanEvaluator;
import hr.fer.zemris.optjava.dz8.Evaluator.IEvaluator;
import hr.fer.zemris.optjava.dz8.Evaluator.TDNNEvaluator;

public class ANNTrainer {

	static Random rand = new Random();

	public static void main(String[] args) {

		// staza do datoteke s podatcima
		String path = args[0];
		// ime mreze i arhitektura
		String net = args[1];
		// velicina populacije
		int populationSize = Integer.parseInt(args[2]);
		// minimalna greska nakon koje algoritam staje
		double minError = Double.parseDouble(args[3]);
		// maksimalan broj iteracija nakon cega algoritam staje
		int maxIterations = Integer.parseInt(args[4]);
		// strategija kod diferencijske evolucije
		String strategy = "DE/best/1/either-or";
		// vjerojatnost mutacije 
		double Cr = 0.98;
		// interval u kojem se stvaraju inicijalna rjesenja
		double minVal = -1;
		double maxVal = 1;
		// broj uzoraka za ucenje
		int numOfSamples = 600;

		String[] netString = net.split("-");
		// ime neuronske mreze
		String netName = netString[0];

		String[] stringArch = netString[1].split("-");
		// arhitektura mreze
		int[] architecture = Arrays.asList(stringArch).stream().mapToInt(Integer::parseInt).toArray();

		Dataset data = new Dataset(path, architecture[0], numOfSamples);

		if (netName == "tdnn") {

			IEvaluator evaluator = new TDNNEvaluator(architecture, data);
			DiffEvol alg = new DiffEvol(evaluator, strategy, populationSize, maxIterations, Cr, minError, minVal, maxVal);
			double[] solution = alg.run();
			
		} else {
			
			IEvaluator evaluator = new ElmanEvaluator(architecture, data);
			DiffEvol alg = new DiffEvol(evaluator, strategy, populationSize, maxIterations, Cr, minError, minVal, maxVal);
			double[] solution = alg.run();
		}

	}
}
