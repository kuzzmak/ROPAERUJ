package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;

public class SATFormula {
	private int numberOfVariables;
	private List<Clause> clauses;
	
	public SATFormula(int numberOfVaribales, List<Clause> clauses) {
		this.numberOfVariables = numberOfVaribales;
		this.clauses = new ArrayList<>(clauses);
	}
	
	public int getNumberOfVariables() {
		return numberOfVariables;
	}
	
	public int getNumberOfClauses() {
		return clauses.size();
	}
	
	public Clause getClause(int index) {
		return clauses.get(index);
	}
	
	public boolean isSatisfied(BitVector assignment) {
		for(Clause cl: clauses) {
			if(!cl.isSatisfied(assignment)) return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Clause cl: this.clauses) {
			sb.append(cl.toString()).append(", ");
		}
		sb.replace(sb.length() - 3, sb.length(), "");
		sb.append("]");
		return sb.toString();
	}
}
