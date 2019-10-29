package hr.fer.zemris.optjava.dz4.part1;

public class GeneticAlgorithm {

	public static int numberOfTournaments;

	public static void main(String[] args) {
		

		int populationSize = Integer.parseInt(args[0]);
		double minError = Double.parseDouble(args[1]);
		int maxIterations = Integer.parseInt(args[2]);
		String sel = args[3];
		ISelection<SingleObjectiveSolution> selection;
		// broj jedinki izmedju kojih se bira najbolja
		if(sel.equals("rouletteWheel")) {
			selection = new RouletteWheelSelection();
		}else {
			selection = new TournamentSelection();
			String[] temp = args[3].split(":");
			numberOfTournaments = Integer.parseInt(temp[1]);
		}
		double sigma = Double.parseDouble(args[4]);
		
		IOptAlgorithm alg = new GenAlg(populationSize, minError, maxIterations, selection, sigma);
		DoubleArraySolution solution = (DoubleArraySolution)alg.run();
		System.out.println(solution);
	}
}
