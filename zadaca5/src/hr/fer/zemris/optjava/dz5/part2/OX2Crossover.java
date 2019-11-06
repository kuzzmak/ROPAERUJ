package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class OX2Crossover implements ICrossover {

	private int n;

	public OX2Crossover(int n) {
		this.n = n;
	}

	@Override
	public List<Chromosome> cross(Chromosome c1, Chromosome c2, Random rand) {

		int[] permutation1 = c1.getPermutation();
		int[] permutation2 = c2.getPermutation();
		List<Integer> perm1List = new ArrayList<>();
		List<Integer> perm2List = new ArrayList<>();
		for (int i = 0; i < permutation1.length; i++) {
			perm1List.add(permutation1[i]);
		}
		for (int i = 0; i < permutation2.length; i++) {
			perm2List.add(permutation2[i]);
		}

		List<Integer> selectedIndexes = new ArrayList<>();
		while (selectedIndexes.size() < this.n) {
			int index = rand.nextInt(permutation1.length);
			if (!selectedIndexes.contains(index))
				selectedIndexes.add(index);
		}

		// izabrani geni prvog dijeteta
		List<Integer> genes = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			genes.add(permutation2[selectedIndexes.get(i)]);
		}
		// indeksi u prvom kromosomu koji se nalaze na mjestu izabranih gena
		List<Integer> indexesInFirst = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			indexesInFirst.add(perm1List.indexOf(genes.get(i)));

		}

		// inicijalizacija djeteta na polje -1 tako da se lakse
		// moze provjeravati nalazi li se neki gen u kromosomu
		int[] child1 = new int[permutation1.length];
		for (int i = 0; i < child1.length; i++) {
			child1[i] = -1;
		}

		// geni koji su prekopirani iz prvog roditelja
		List<Integer> leftInFirst = new ArrayList<>();
		for (int i = 0; i < permutation1.length; i++) {
			if (!indexesInFirst.contains(i)) {
				child1[i] = permutation1[i];
				leftInFirst.add(permutation1[i]);
			}
		}

		// nadodavanje ostatka kromosoma onim genima koji nedostaju
		for (int i = 0; i < permutation1.length; i++) {
			if (child1[i] != -1)
				continue;
			else {
				for (int j = 0; j < permutation1.length; j++) {
					if (leftInFirst.contains(permutation2[j]))
						continue;
					else {
						child1[i] = permutation2[j];
						leftInFirst.add(permutation2[j]);
						break;
					}
				}
			}
		}

		// izabrani geni drugog dijeteta
		genes = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			genes.add(permutation1[selectedIndexes.get(i)]);
		}
		// indeksi u drugom kromosomu koji se nalaze na mjestu izabranih gena
		List<Integer> indexesInSecond = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			indexesInSecond.add(perm2List.indexOf(genes.get(i)));

		}

		// inicijalizacija djeteta na polje -1 tako da se lakse
		// moze provjeravati nalazi li se neki gen u kromosomu
		int[] child2 = new int[permutation2.length];
		for (int i = 0; i < child2.length; i++) {
			child2[i] = -1;
		}

		// geni koji su prekopirani iz drugog roditelja
		List<Integer> leftInSecond = new ArrayList<>();
		for (int i = 0; i < permutation2.length; i++) {
			if (!indexesInSecond.contains(i)) {
				child2[i] = permutation2[i];
				leftInSecond.add(permutation2[i]);
			}
		}

		// nadodavanje ostatka kromosoma onim genima koji nedostaju
		for (int i = 0; i < permutation2.length; i++) {
			if (child2[i] != -1)
				continue;
			else {
				for (int j = 0; j < permutation2.length; j++) {
					if (leftInSecond.contains(permutation1[j]))
						continue;
					else {
						child2[i] = permutation1[j];
						leftInSecond.add(permutation1[j]);
						break;
					}
				}
			}
		}
		
		List<Chromosome> children = new ArrayList<>();
		children.add(new Chromosome(child1));
		children.add(new Chromosome(child2));
		return children;
	}

}
