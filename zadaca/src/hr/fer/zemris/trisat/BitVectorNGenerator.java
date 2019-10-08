package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BitVectorNGenerator implements Iterable<MutableBitVector>{

	private MutableBitVector assignment;
	int current = 0;
	
	public BitVectorNGenerator(MutableBitVector assignment) {
		this.assignment = assignment;
	}
	
	class MutableBitVectorIterator implements Iterator<MutableBitVector>{

		@Override
		public boolean hasNext() {
			return current < assignment.getSize();
		}

		@Override
		public MutableBitVector next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}else {
				MutableBitVector cur = assignment.copy();
				cur.bits.set(current, !assignment.bits.get(current));
				current++;
				return cur;
			}
		}
		
	}
	
	@Override
	public Iterator<MutableBitVector> iterator() {
	
		return new MutableBitVectorIterator();
	}
	
	public List<MutableBitVector> createNeighborhood() {
		
		List<MutableBitVector> neighbours = new ArrayList<>();
		
		Iterator<MutableBitVector> iter = new MutableBitVectorIterator();
		
		for(int i = 0; i < assignment.getSize(); i++) {
			neighbours.add(iter.next());
		}
		
		return neighbours;
	}
}
