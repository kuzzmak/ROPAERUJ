package hr.fer.zemris.optjava.dz12.Expression;

import java.util.ArrayList;
import java.util.List;

public class PR3 extends Expression implements IFunction {

	private int numberOfOutputs;
	private List<Expression> outputs;
	
	public PR3() {
		this.name = "PR3";
		this.status = Status.FUNCTION;
		this.numberOfOutputs = 3;
		this.outputs = new ArrayList<>();
	}
	
	public PR3(String name, Status status, int numberOfOutputs, List<Expression> outputs) {
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
			throw new AssertionError("Nemoguce dodati vise od tri izlaza u IF");
		}
		this.outputs.add(expression);
	}
	
	@Override
	public String toString() {
		return "PR3 [" + outputs + "]";
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
		return new PR3(this.name, this.status, this.numberOfOutputs, new ArrayList<>());
	}
}
