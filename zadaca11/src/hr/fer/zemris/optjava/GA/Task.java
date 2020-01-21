package hr.fer.zemris.optjava.GA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import hr.fer.zemris.optjava.GrayScaleImage.GrayScaleImage;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;

public class Task implements Callable<List<GASolution<int[]>>>{

	private List<GASolution<int[]>> population;
	private double p;
	private Evaluator evaluator;
	private int numOfChildren;
	// svaki zadatak ima svoju sliku za evaluaciju
	private GrayScaleImage im;

	
	/**
	 * @param population trenutna populacija
	 * @param evaluator za evaluaciju pojedine jedinke
	 * @param p vjerojatnost mutacije
	 * @param numOfChildren broj djece koji treba stvoriti
	 * @param tlgsi za dohvat GrayScaleImage, koji sluzi za evaluaciju, svake pojedine dretve
	 * @param rng generator slucajnih brojeva
	 */
	public Task(List<GASolution<int[]>> population,
							  Evaluator evaluator,
							  	 double p,
							  	 	int numOfChildren,
			ThreadLocal<GrayScaleImage> tlgsi) {
		
		this.population = population;
		this.evaluator = evaluator;
		this.p = p;
		this.numOfChildren = numOfChildren;
		this.im = tlgsi.get();
	}
	
	@Override
	public List<GASolution<int[]>> call() throws Exception {
		
		IRNG rng = ((EVOThread)Thread.currentThread()).getRNG();
		
		// lista djece koje svaki zadatak mora stvoriti
		List<GASolution<int[]>> children = new ArrayList<>();
		
		// evaluacije populacije
		for(GASolution<int[]> solution: this.population) {
			this.evaluator.evaluate(solution, this.im);
		}
		
//		// sortiranje populacije
//		Util.sort(this.population);
		
		while(children.size() < this.numOfChildren) {
			
			GASolution<int[]> parent1 = Util.select(this.population, rng);
			GASolution<int[]> parent2 = Util.select(this.population, rng);
			
			while(parent1 == parent2) {
				parent2 = Util.select(this.population, rng);
			}
			
			// krizanje roditelja da bi se dobilo dijete
			GASolution<int[]> child = Util.cross(parent1, parent2, rng);
			// mutacija djetea
			Util.mutate(child, rng, p);
			// evaluacija djeteta
			evaluator.evaluate(child, im);
			
			children.add(child);
		}
		return children;
	}
}
