package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Chromosome {

	private List<Bin> binList;
	private int binCapacity;
	
	
	public Chromosome(List<Stick> sticks, int binCapacity) {
		
		this.binList = new ArrayList<>();
		this.binCapacity = binCapacity;
		
		addSticks(sticks);
		
	}
	
	
	public void setBinList(List<Bin> binList) {
		this.binList = binList;
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
	 * Funkcija za dodavanje binova u kromosom
	 * 
	 * @param binList list binova za dodati
	 */
	public void addBins(List<Bin> binList) {
		for(Bin b: binList) {
			if(b.getSticksInBin().size() == 0) {
				continue;
			}
			this.binList.add(b);
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
	
	/**
	 * Funkcija za dohvat stapova iz kromosoma
	 * 
	 * @return stapovi kromosoma
	 */
	public List<Stick> getSticks(){
		List<Stick> stickList = new ArrayList<>();
		for(Bin b: binList) {
			for(Stick s: b.getSticksInBin()) {
				stickList.add(s);
			}
		}
		return stickList;
	}
	
	/**
	 * Funkcija za ubacivanje binova u kromosom na odredjeni index
	 * Ne provjerava postoje li vec ti binovi u kromosomu
	 * 
	 * @param index mjesto na koje se ubacju binovi
	 * @param binList lista binova koju treba ubaciti
	 */
	public void insertBinsAt(int index, List<Bin> binList) {
		
		List<Bin> tempList = new ArrayList<>();
		
		// kopiranje prvod dijela kromosoma od 0 do indexa
		for(int i = 0; i < index; i++) {
			tempList.add(this.binList.get(i));
		}
		
		// ubacivanje novih binova
		for(int i = 0; i < binList.size(); i++) {
			tempList.add(binList.get(i));
		}
		
		// kopiranje ostatke kromosoma
		for(int i = index; i < this.size(); i++) {
			tempList.add(this.binList.get(i));
		}
		
		this.setBinList(tempList);
	}
	
	
	/**
	 * Funkcija za stvaranje kopije kromosoma
	 *  
	 * @return kopija kromosoma
	 */
	public Chromosome copy() {
		return new Chromosome(this.getSticks(), this.binCapacity);
	}
	
	public void removeSticks(HashSet<Stick> sticks) {
		
		// kopija binova kromosoma
		List<Bin> binList = new ArrayList<>(this.getBinList());
		// lista za iteraciju
		List<Bin> binListIter = new ArrayList<>(this.getBinList());
		
		for(int i = 0; i < binListIter.size(); i++) {
			for(Stick s: sticks) {
				if(binListIter.get(i).getSticksInBin().contains(s)) {
					binList.get(i).removeStick(s);
				}
			}
		}
		this.setBinList(binList);
	}
	
	
}
