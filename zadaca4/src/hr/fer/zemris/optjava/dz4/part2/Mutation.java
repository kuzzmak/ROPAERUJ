package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mutation implements IMutation {

	private float mutationRate;
	private static Random rand;

	public Mutation(float mutationRate) {
		this.mutationRate = mutationRate;
		this.rand = new Random();
	}

	@Override
	public Chromosome mutate(Chromosome c) {
		
		List<Bin> binList = c.getBinList();
		// lista binova koji su izabrani za mutaciju
		List<Bin> toRemove = new ArrayList<>();
		// stapovi koji su u izabranim binovima
		List<Stick> removedSticks = new ArrayList<>();
		

		for (int i = 0; i < binList.size(); i++) {
			if (mutationRate > rand.nextFloat()) {
				for(Stick s: binList.get(i).getSticksInBin()) {
					removedSticks.add(s);
				}
				toRemove.add(binList.get(i));
			}
		}
		
		binList.removeAll(toRemove);
		
		Chromosome mutated = c.copy();
		mutated.setBinList(binList);
		mutated.addSticks(removedSticks);
		
		return mutated;
	}

}
