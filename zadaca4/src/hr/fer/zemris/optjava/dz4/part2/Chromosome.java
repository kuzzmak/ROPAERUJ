package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.List;

public class Chromosome {

	private List<Bin> binList;
	private int binCapacity;
	
	public Chromosome(List<Stick> sticks, int binCapacity) {
		
		this.binList = new ArrayList<>();
		this.binCapacity = binCapacity;
		
		addSticks(sticks);
		
	}
	
	/**
	 * Funkcija za inicijalno dodavanja stapova u kromosom
	 * 
	 * @param sticks stapovi koje je potrebno doadati
	 */
	public void addSticks(List<Stick> sticks) {
		
		for(Stick s: sticks) {
			
			boolean stickAdded = false;
			for(Bin b: binList) {
				// ako smo dodatli stap idemo na sljedeci
				if(stickAdded) break;
				if(b.add(s)) {
					stickAdded = true;
				}
			}
			// ako stap nije bilo moguce dodati u neku od ranijih binova, stvaramo novi bin
			if(!stickAdded) {
				Bin bin = new Bin(binCapacity);
				bin.add(s);
				binList.add(bin);
			}
		}
	}

	/**
	 * Funkcija za dohvat kolicine binova u kromosomu
	 * 
	 * @return broj binova
	 */
	public int size() {
		return binList.size();
	}
	
	public List<Bin> getBinList() {
		return binList;
	}

	public int getBinCapacity() {
		return binCapacity;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for(Bin b: binList) {
			sb.append("[");
			for(Stick s: b.getSticksInBin()) {
				sb.append(s);
				sb.append(", ");
			}
			sb.replace(sb.length() - 2, sb.length(), "");
			sb.append("]\n");
		}
		return sb.toString();
	}
	
	
	
}
