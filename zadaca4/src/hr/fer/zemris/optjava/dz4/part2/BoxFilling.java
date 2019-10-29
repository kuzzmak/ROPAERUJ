package hr.fer.zemris.optjava.dz4.part2;


public class BoxFilling {

	protected static int k = 2;
	
	public static void main(String[] args) {
		
		String path = "kutije\\problem-20-50-1.dat";
		int populationSize = 20;
		int n = 4;
		int m = 3;
		boolean p = true;
		int numOfIterations = 2000;
		int binSize = 20;
		float mutationRate = 0.1f;
		
		IOptAlgorithm alg = new BinPackingGeneticAlgorithm(path, populationSize, n, m, p, numOfIterations, binSize, mutationRate);
		Chromosome solution = alg.run();
		System.out.println(solution);
		
		
	}
	
	
}
