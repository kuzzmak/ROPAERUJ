package hr.fer.zemris.optjava.dz12.Terminal;

import hr.fer.zemris.optjava.dz12.Expression.Expression;
import hr.fer.zemris.optjava.dz12.Expression.Status;

/**
 * Razred koji predstavlja terminale, odnosno listove stabla
 * 
 * @author kuzmi
 *
 */
public class Terminal extends Expression{

	public Action action;
	
	public Terminal(String name, Action action) {
		this.name = name;
		this.status = Status.TERMINAL;
		this.action = action;
	}

	@Override
	public String toString() {
		return "" + action;
	}

	@Override
	public Expression duplicate() {
		return new Terminal(this.name, this.action);
	}
}
