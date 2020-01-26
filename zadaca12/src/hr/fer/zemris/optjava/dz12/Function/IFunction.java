package hr.fer.zemris.optjava.dz12.Function;

import java.util.List;

import hr.fer.zemris.optjava.dz12.Expression.Expression;

/**
 * Sucelje koje implementira pojedina funkcija
 * 
 * @author kuzmi
 *
 */
public interface IFunction {

	/**
	 * Funkcija koja vraca broj izlaza koje ima pojedina funkcija
	 * 
	 * @return
	 */
	public int getNumberOfOutputs();

	/**
	 * Funkcija za dodavanje izlaza u listu izlaza
	 * 
	 * @param expression
	 */
	
	public void addOutput(Expression expression);
	
	/**
	 * Funkcija koja vraca je li moguce dodati expression funkciji ili nije
	 * 
	 * @return true ili false
	 */
	public boolean canAdd();
	
	/**
	 * Dohvat izlaznih izraza
	 * 
	 * @return lista izlaznih izraza
	 */
	public List<Expression> getOutputs();
}
