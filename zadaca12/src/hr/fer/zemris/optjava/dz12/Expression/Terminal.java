package hr.fer.zemris.optjava.dz12.Expression;

/**
 * Razred koji predstavlja terminale, odnosno listove stabla
 * 
 * @author kuzmi
 *
 */
public class Terminal extends Expression{

	Action action;
	
	public Terminal(String name, Action action) {
		this.name = name;
		this.status = Status.TERMINAL;
		this.action = action;
	}

	@Override
	public String toString() {
		return "Terminal [" + action + "]";
	}

	@Override
	public Expression duplicate() {
		return new Terminal(this.name, this.action);
	}
}
