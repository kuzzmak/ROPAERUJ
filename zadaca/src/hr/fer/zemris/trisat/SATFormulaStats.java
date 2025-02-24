package hr.fer.zemris.trisat;

public class SATFormulaStats {
	
	private SATFormula formula;
	private int numberOfSatisfied = 0;
	private Float[] post;
	private static float percentageConstantUp = 0.01f;
	private static float percentageConstantDown = 0.1f;
	private boolean isSatisfied;
	
	
	public SATFormulaStats(SATFormula formula) {
		this.formula = formula;
		post = new Float[formula.getNumberOfClauses()];
		for(int i = 0; i < formula.getNumberOfClauses(); i++) {
			post[i] = 0.0f;
		}
	}
	
	public void setAssignment(BitVector assignment, boolean updatePercentages) {
		for(int index = 0; index < formula.getNumberOfClauses(); index++) {
			if(formula.getClause(index).isSatisfied(assignment)) {
				post[index] += (1 - post[index]) * percentageConstantUp;
				numberOfSatisfied++;
			}else {
				post[index] += (0 - post[index]) * percentageConstantDown;
			}
		}
		isSatisfied = formula.isSatisfied(assignment);
	}
	
	public Float[] getPost() {
		return post;
	}
	
	public int getNumberOfSatisfied() {
		return numberOfSatisfied;
	}
	
	public boolean isSatisfied() {
		return isSatisfied;
	}
	
//	public double getPercentageBonus() {
//		
//	}
//	
	public double getPercentage(int index) {
		return post[index];
	}
}
