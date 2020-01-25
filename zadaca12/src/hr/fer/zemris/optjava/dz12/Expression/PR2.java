package hr.fer.zemris.optjava.dz12.Expression;

import java.util.ArrayList;
import java.util.List;

public class PR2 extends Expression implements IFunction {

	private int numberOfOutputs;
	private List<Expression> outputs;
	
	public PR2() {
		this.name = "PR2";
		this.status = Status.FUNCTION;
		this.numberOfOutputs = 2;
		this.outputs = new ArrayList<>();
	}
	@Override
	public int getNumberOfOutputs() {
		return this.numberOfOutputs;
	}

	@Override
	public void addOutput(Expression expression) {
		
		if(outputs.size() + 1> this.getNumberOfOutputs()) {
			throw new AssertionError("Nemoguce dodati vise od dva izlaza u PR2");
		}
		this.outputs.add(expression);
	}

	@Override
	public String toString() {
		return "PR2 [" + outputs + "]";
	}
	
	@Override
	public boolean canAdd() {
		return outputs.size() + 1 <= getNumberOfOutputs();
	}
	
	@Override
	public List<Expression> getOutputs() {
		return outputs;
	}

}
