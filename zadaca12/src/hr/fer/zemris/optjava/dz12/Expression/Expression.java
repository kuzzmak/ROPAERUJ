package hr.fer.zemris.optjava.dz12.Expression;

public abstract class Expression implements IExpression{

	public String name;
	
	Status status;

	@Override
	public String toString() {
		return "Expression " + name + ", status=" + status;
	}
}
