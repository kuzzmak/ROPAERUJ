package hr.fer.zemris.optjava.dz12.Function;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.optjava.dz12.Expression.Expression;
import hr.fer.zemris.optjava.dz12.Expression.Status;

public class PR2 extends Expression implements IFunction {

	private int numberOfOutputs;
	private List<Expression> outputs;
	
	public PR2() {
		this.name = "PR2";
		this.status = Status.FUNCTION;
		this.numberOfOutputs = 2;
		this.outputs = new ArrayList<>();
	}
	
	public PR2(String name, Status status, int numberOfOutputs, List<Expression> outputs) {
		this.name = name;
		this.status = status;
		this.numberOfOutputs = numberOfOutputs;
		this.outputs = outputs;
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
		return "PR2 " + outputs;
	}
	
	@Override
	public boolean canAdd() {
		return outputs.size() + 1 <= getNumberOfOutputs();
	}
	
	@Override
	public List<Expression> getOutputs() {
		return outputs;
	}
	
	@Override
	public Expression duplicate() {
		List<Expression> copy = new ArrayList<>();
		copy.addAll(this.outputs);
		return new PR2(this.name, this.status, this.numberOfOutputs, copy);
	}
	
	@Override
	public void removeOutput(Expression epxression) {
		outputs.remove(epxression);
	}
}
