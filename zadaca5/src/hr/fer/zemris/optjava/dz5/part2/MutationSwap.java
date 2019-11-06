package hr.fer.zemris.optjava.dz5.part2;

import java.util.Random;

public class MutationSwap implements IMutation {

	private float mutationRate;

	public MutationSwap(float mutationRate) {
		this.mutationRate = mutationRate;
	}

	// mutacija gdje se odaberu dva broja u kromosomu i zamijene se
	@Override
	public Chromosome mutate(Chromosome c, Random rand) {

		int[] permutation = c.getPermutation();
		if (rand.nextFloat() < mutationRate) {

			int index1 = rand.nextInt(permutation.length);
			int index2 = rand.nextInt(permutation.length);
			
			int temp = permutation[index1];
			permutation[index1] = permutation[index2];
			permutation[index2] = temp;
		}
		return new Chromosome(permutation);
	}
}
