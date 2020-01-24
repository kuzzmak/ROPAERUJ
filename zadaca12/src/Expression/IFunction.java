package Expression;

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
	public void addOutput(IExpression expression);
	
}
