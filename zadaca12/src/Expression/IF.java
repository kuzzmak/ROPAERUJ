package Expression;

import java.util.ArrayList;
import java.util.List;

public class IF extends Expression implements IFunction{

	private int numberOfOutputs;
	private List<IExpression> outputs;
	
	public IF() {
		this.name = "IF";
		this.status = Status.FUNCTION;
		this.numberOfOutputs = 2;
		this.outputs = new ArrayList<>();
	}
	
	@Override
	public int getNumberOfOutputs() {
		return this.numberOfOutputs;
	}

	@Override
	public void addOutput(IExpression expression) {
		
		if(outputs.size() + 1> this.getNumberOfOutputs()) {
			throw new AssertionError("Nemoguce dodati vise od dva izlaza u IF");
		}
		this.outputs.add(expression);
	}

	@Override
	public String toString() {
		return "IF [" + outputs + "]";
	}
}
