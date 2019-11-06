package hr.fer.zemris.optjava.dz5.part2;

public class GeneticAlgorithm {

	public static void main(String[] args) {

		String path = args[0];
		int populationSize = Integer.parseInt(args[1]);
		int numOfSubpopulations = Integer.parseInt(args[2]);
		IOptAlgorithm alg = new SASEGASA(path, populationSize, numOfSubpopulations);

		Chromosome solution = alg.run();
		System.out.println(solution);
		System.out.println(SASEGASA.function.valueAt(solution));
	}
}
