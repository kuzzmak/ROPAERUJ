package hr.fer.zemris.optjava.dz4.part2;


public class BoxFilling {

	protected static int k = 2;
	
	public static void main(String[] args) {
		
		String         path = args[0];
		int  populationSize = Integer.parseInt(args[1]);
		int               n = Integer.parseInt(args[2]);
		int               m = Integer.parseInt(args[3]);
		boolean           p = Boolean.parseBoolean(args[4]);
		int numOfIterations = Integer.parseInt(args[5]);
		int         binSize = Integer.parseInt(args[6]);
		float  mutationRate = Float.parseFloat(args[7]);
		
		IOptAlgorithm alg = new BinPackingGeneticAlgorithm(path, populationSize, n, m, p, numOfIterations, binSize, mutationRate);
		Chromosome solution = alg.run();
		System.out.println(solution);
	}
}
