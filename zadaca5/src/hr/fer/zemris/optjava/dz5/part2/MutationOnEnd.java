package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MutationOnEnd implements IMutation {
	
	private float mutationRate;
	
	public MutationOnEnd(float mutationRate) {
		this.mutationRate = mutationRate;
	}

	// mutacija se obavlja tako da se izabrani brojevi unutar kromosoma stavljaju na kraj
	@Override
	public Chromosome mutate(Chromosome c, Random rand) {
		
		int[] permutation = c.getPermutation();
		List<Integer> chosen = new ArrayList<>();
		for(int i = 0; i < permutation.length; i++) {
			if(rand.nextFloat() < this.mutationRate) {
				chosen.add(c.getPermutation()[i]);
			}
		}
		List<Integer> newPermutaion = new ArrayList<>();
		for(int i = 0; i < permutation.length; i++) {
			if(chosen.contains(permutation[i])) continue;
			else newPermutaion.add(permutation[i]);
		}
		for(int i = 0; i < chosen.size(); i++) {
			newPermutaion.add(chosen.get(i));
		}
		
		for(int i = 0; i < permutation.length; i++) {
			permutation[i] = newPermutaion.get(i);
		}
		return new Chromosome(permutation);
	}

}
