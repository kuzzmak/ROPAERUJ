package hr.fer.zemris.optjava.dz5.part1;

public class GeneticAlgorithm {

	public static void main(String[] args) {
		
		int n = Integer.parseInt(args[0]);
		IOptAlgorithm alg = new RAPGA(n);
		System.out.println(alg.run());
	}
}
