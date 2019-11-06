package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Chromosome implements Comparable<Chromosome>{

	private int[] permutation;
	private float fitness;
	private int n;
	
	public Chromosome(int[] permutation) {
		this.permutation = permutation;
	}
	
	public Chromosome(int n) {
		this.permutation = new int[n];
		this.n = n;
		List<Integer> intList = new ArrayList<>();
		for(int i = 0; i < n; i++) {
			intList.add(i);
		}
		Collections.shuffle(intList);
		for(int i = 0; i < n; i++) {
			this.permutation[i] = intList.get(i);
		}
	}
	
	public void shuffle() {
		
		List<Integer> intList = new ArrayList<>();
		for(int i = 0; i < n; i++) {
			intList.add(this.permutation[i]);
		}
		Collections.shuffle(intList);
		for(int i = 0; i < n; i++) {
			this.permutation[i] = intList.get(i);
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(fitness);
		result = prime * result + Arrays.hashCode(permutation);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chromosome other = (Chromosome) obj;
		if (Float.floatToIntBits(fitness) != Float.floatToIntBits(other.fitness))
			return false;
		if (!Arrays.equals(permutation, other.permutation))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Chromosome [permutation=" + Arrays.toString(permutation) + "]";
	}

	@Override
	public int compareTo(Chromosome arg0) {
		
		if (this.fitness < arg0.fitness) {
			return -1;
		} else if (this.fitness > arg0.fitness) {
			return 1;
		}
		return 0;
	}

	public int[] getPermutation() {
		return permutation;
	}

	public void setPermutation(int[] permutation) {
		this.permutation = permutation;
	}

	public float getFitness() {
		return fitness;
	}

	public void setFitness(float fitness) {
		this.fitness = fitness;
	}
}
