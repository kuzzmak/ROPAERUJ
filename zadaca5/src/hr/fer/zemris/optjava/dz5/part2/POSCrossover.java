package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class POSCrossover implements ICrossover {

	private int n;
	
	public POSCrossover(int n) {
		this.n = n;
	}
	
	@Override
	public List<Chromosome> cross(Chromosome c1, Chromosome c2, Random rand) {
		
		int[] permutation1 = c1.getPermutation();
		int[] permutation2 = c2.getPermutation();
		
		List<Integer> selectedIndexes = new ArrayList<>();
		while (selectedIndexes.size() < this.n) {
			int index = rand.nextInt(permutation1.length);
			if (!selectedIndexes.contains(index))
				selectedIndexes.add(index);
		}
		
		int[] child1 = new int[permutation1.length];
		int[] child2 = new int[permutation2.length];
		for(int i = 0; i < permutation1.length; i++) {
			child1[i] = -1;
			child2[i] = -1;
		}

		// geni koji se kopiraju u suprotno dijete
		List<Integer> genesInFirst = new ArrayList<>();
		List<Integer> genesInSecond = new ArrayList<>();
		
		// kopiranje gena u djecu i dodavanje gena u listu koja prati koje gene ima koje dijete
		for(int i = 0; i < this.n; i++) {
			child1[selectedIndexes.get(i)] = permutation2[selectedIndexes.get(i)];
			genesInFirst.add(permutation2[selectedIndexes.get(i)]);
			child2[selectedIndexes.get(i)] = permutation1[selectedIndexes.get(i)];
			genesInSecond.add(permutation1[selectedIndexes.get(i)]);
		}
		
		// dodavanje ostatka gena iz suprotnog djeteta
		for(int i = 0; i < permutation1.length; i++) {
			if(child1[i] != -1) continue;
			for(int j = 0; j < permutation1.length; j++) {
				if(genesInFirst.contains(permutation1[j])) continue;
				else {
					child1[i] = permutation1[j];
					genesInFirst.add(permutation1[j]);
					break;
				}
			}
		}
		
		for(int i = 0; i < permutation2.length; i++) {
			if(child2[i] != -1) continue;
			for(int j = 0; j < permutation2.length; j++) {
				if(genesInSecond.contains(permutation2[j])) continue;
				else {
					child2[i] = permutation2[j];
					genesInSecond.add(permutation2[j]);
					break;
				}
			}
		}
		
		List<Chromosome> children = new ArrayList<>();
		children.add(new Chromosome(child1));
		children.add(new Chromosome(child2));
		return children;
	}

}
