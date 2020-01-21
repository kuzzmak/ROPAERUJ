package hr.fer.zemris.optjava.GA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;

public class Task implements Callable<List<GASolution<int[]>>>{

	private List<GASolution<int[]>> population;
	private double p;
	private int numOfChildren;

	/**
	 * @param population trenutna populacija
	 * @param evaluator za evaluaciju pojedine jedinke
	 * @param p vjerojatnost mutacije
	 * @param numOfChildren broj djece koji treba stvoriti
	 * @param tlgsi za dohvat GrayScaleImage, koji sluzi za evaluaciju, svake pojedine dretve
	 * @param rng generator slucajnih brojeva
	 */
	public Task(List<GASolution<int[]>> population,
							  	 double p,
							  	 	int numOfChildren) {
		
		this.population = population;
		this.p = p;
		this.numOfChildren = numOfChildren;
	}
	
	@Override
	public List<GASolution<int[]>> call() throws Exception {
		
		IRNG rng = ((EVOThread)Thread.currentThread()).getRNG();
		
		Evaluator evaluator = ((EVOThread)Thread.currentThread()).getEvaluator();		
		// lista djece koje svaki zadatak mora stvoriti
		List<GASolution<int[]>> children = new ArrayList<>();
		
		// evaluacije populacije
		for(GASolution<int[]> solution: this.population) {
			evaluator.evaluate(solution);
		}
		
		
		while(children.size() < this.numOfChildren) {
			
			GASolution<int[]> parent1 = Util.select(this.population, rng);
			GASolution<int[]> parent2 = Util.select(this.population, rng);
			
			while(parent1 == parent2) {
				parent2 = Util.select(this.population, rng);
			}
			
			// krizanje roditelja da bi se dobilo dijete
			GASolution<int[]> child = Util.cross(parent1, parent2, rng);
			// mutacija djetea
			Util.mutate(child, rng, this.p);
			// evaluacija djeteta
			evaluator.evaluate(child);
			
			children.add(child);
		}
		return children;
	}
}
