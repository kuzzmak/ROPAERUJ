package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;

public class Clause {
	
	private List<Integer> indexes;
	
	public Clause(List<Integer> indexes) {
		this.indexes = new ArrayList<>(indexes);
	}
	
	public int getSize() {
		return indexes.size();
	}
	
	public int getLiteral(int index) {
		return indexes.get(index);
	}
	
	public List<Integer> getIndexes() {
		return indexes;
	}

	public boolean isSatisfied(BitVector assignment) {
		List<Boolean> temp = new ArrayList<>();
		for(int index: this.indexes) {
			if(index < 0) {
				temp.add(!assignment.bits.get(Math.abs(index) - 1));
			}else {
				temp.add(assignment.bits.get(index - 1));
			}
		}
		if(temp.contains(true)) return true;
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		for(int index: getIndexes()) {
			sb.append(index).append(", ");
		}
		sb.replace(sb.length() - 2, sb.length() - 1, ")");
		return sb.toString();
	}
}
