package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Ant implements Comparable<Ant>{

	private int[] indexes;
	public double pathDistence = 0;
	
	public Ant(int numOfCities) {
		
		this.indexes = new int[numOfCities];
		List<Integer> list = new ArrayList<>();
		
		for(int i = 0; i < numOfCities; i++) {
			list.add(i);
		}
		Collections.shuffle(list);

		this.indexes = list.stream().mapToInt(x -> x).toArray();
		evaluate();
	}
	
	public Ant(int[] indexes) {
		this.indexes = indexes;
	}

	public Ant(List<City> cities) {
		this.indexes = new int[cities.size()]; 
		for(int i = 0; i < cities.size(); i++) {
			this.indexes[i] = cities.get(i).getIndex();
		}
		evaluate();
	}

	public int[] getIndexes() {
		return indexes;
	}
	
	/**
	 * Funkcija koje vrednuje pojedino rjesenje
	 */
	public void evaluate() {
		
		for(int i = 0; i < indexes.length - 1; i++) {
			this.pathDistence += MMAS.distances[indexes[i]][indexes[i + 1]];
		}
		this.pathDistence += MMAS.distances[indexes.length - 1][0];
	}
	
	@Override
	public String toString() {
		return "Ant "+ Arrays.toString(indexes) ;
	}

	@Override
	public int compareTo(Ant a) {
		if(this.pathDistence > a.pathDistence) return 1;
		if(this.pathDistence < a.pathDistence) return -1;
		else return 0;
	}
	
	public int size() {
		return indexes.length;
	}
}
