package hr.fer.zemris.optjava.dz9;

import java.util.ArrayList;
import java.util.List;

public class Problem implements MOOPProblem {
	
	private List<IFunction> objectiveFunctions;
	private int dimension;
	
	public Problem() {
		
		this.objectiveFunctions = new ArrayList<>();
	}
	
	public Problem(List<IFunction> objectiveFunctions) {
		
		this.objectiveFunctions = objectiveFunctions;
		this.dimension = this.objectiveFunctions.get(0).getDimension();
	}
	
	@Override
	public void add(IFunction f) {
		
		if(this.objectiveFunctions.size() == 0) {
			this.objectiveFunctions = new ArrayList<>();
		}
		this.objectiveFunctions.add(f);
		this.dimension = f.getDimension();
	}
	
	@Override
	public double[] evaluate(double[] point) {
		
		double[] value = new double[this.dimension];
		
		for(int i = 0; i < this.dimension; i++) {
			value[i] = this.objectiveFunctions.get(i).valueAt(point, true);
		}
		
		return value;
	}

	@Override
	public int getDimension() {
		
		return this.dimension;
	}

	public List<IFunction> getObjectiveFunctions() {
		return this.objectiveFunctions;
	}

}
