package zadaca1;

import java.util.ArrayList;
import java.util.List;

public class clause {
	private int clauseLength;
	private List<Integer> indexes = new ArrayList<>();
	private List<Integer> booleanValues = new ArrayList<>();
	
	public clause(int clauseLength, List<Integer> indexes) {
		this.clauseLength = clauseLength;
		this.indexes = indexes;
	}

	public List<Integer> getBooleanValues() {
		return booleanValues;
	}

	public void setBooleanValues(List<Integer> booleanValues) {
		this.booleanValues = booleanValues;
	}

	public List<Integer> getIndexes() {
		return indexes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < indexes.size(); i++) {
			sb.append(indexes.get(i)).append(" ");
		}
		return sb.toString().trim();
	}
	
	
	public static List<Integer> calculateBoolIndexes(String bigNum, clause cl) {
		
		List<Integer> boolVal = new ArrayList<>();
		
		for(int index: cl.getIndexes()) {
			if(index < 0) {
				char temp = bigNum.charAt(Math.abs(index) - 1);
				
				switch(temp) {
				case '0':
					boolVal.add(1);
					break;
				case '1':
					boolVal.add(0);
					break;
				default:
					break;
				}
				
			}else {
				char temp = bigNum.charAt(index - 1);
				switch(temp) {
				case '0':
					boolVal.add(0);
					break;
				case '1':
					boolVal.add(1);
					break;
				default:
					break;
				}
			}
		}
		
		return boolVal;
	}
}

