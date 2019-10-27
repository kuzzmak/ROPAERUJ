package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.List;

public class Bin {

	private int binSize;
	// zauzeto mjesto
	private int occupiedSpace = 0;
	
	// list stapova po jednom binu
	private List<Stick> sticksInBin;
	
	public Bin(int binSize) {
		this.binSize = binSize;
		this.sticksInBin = new ArrayList<>();
	}
	
	/**
	 * Metoda za dodavanje stapa u bin ako je moguce
	 * 
	 * @param s stap koji se proba dodati
	 * @return boolean je li dodan ili nije
	 */
	public boolean add(Stick s) {
		
		if(binSize - occupiedSpace >= s.getLength()) {
			sticksInBin.add(s);
			occupiedSpace += s.getLength();
			return true;
		}
		return false;
	}

	public int getOccupiedSpace() {
		return occupiedSpace;
	}

	@Override
	public String toString() {
		return "Bin [sticksInBin=" + sticksInBin + "]";
	}

	public List<Stick> getSticksInBin() {
		return sticksInBin;
	}
	
}
