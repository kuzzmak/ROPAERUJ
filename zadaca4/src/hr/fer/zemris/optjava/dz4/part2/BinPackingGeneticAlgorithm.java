package hr.fer.zemris.optjava.dz4.part2;

import java.util.List;
import java.util.Random;

public class BinPackingGeneticAlgorithm implements IOptAlgorithm {

	// staza do datoteke sa stapovima
	private String path;
	// velicina inicijalne populacije rjesenja
	private int populationSize;
	// broj jedinki koji se odabere kod n-turnirske selekcije
	private int n;
	// broj jedinki koji se odabere kod m-turnirske selekcije(najgore rjesenje)
	private int m;
	/**
	 * Ako je parametar p
	 * postavljen na false, dijete bezuvjetno zamjenjuje jedinku pobjednika m-turnira. Ako je parametar
     * p postavljen na true, dijete ce pobjednika m-turnira zamijeniti samo ako ima bolju dobrotu od
     * pobjednika; u suprotnom, pobjednik ostaje u populaciji a dijete se eliminira.
	 */
	private boolean p;
	// broj iteracija koje ima algoritam za naci rjesenje
	private int numOfIterations;
	// velicina pojedinog bina u kromosomu
	private int binSize;
	// postotak mutacije
	private float mutationRate;
	private Random rand;
	
	
	public BinPackingGeneticAlgorithm(String path, int populationSize, int n, int m, boolean p, int numOfIterations, int binSize, float mutationRate) {
		this.path = path;
		this.populationSize = populationSize;
		this.n = n;
		this.m = m;
		this.p = p;
		this.numOfIterations = numOfIterations;
		this.binSize = binSize;
		this.mutationRate = mutationRate;
		this.rand = new Random();
	}
	
	@Override
	public Chromosome run() {
		IFunction function = new OptimizationFunction(BoxFilling.k);
		// ucitavanje iz datoteke
		StickLoader loader = new StickLoader(path);
		// lista svih stapova iz datoteke
		List<Stick> sticks = loader.loadSticks();
		// inicijalna populacija kromosoma
		List<Chromosome> cList = Population.makePopulation(populationSize, sticks, this.binSize);
		
		IMutation mutation = new Mutation(mutationRate);
		
		String[] splitPath = this.path.split("-");
		int solLength = Integer.parseInt(splitPath[2]);

		// random inicijalizacija rjesenja
		Chromosome solution = new Chromosome(sticks, this.binSize);
		// ako je neki od random inicijaliziranih kromosoma rjesenje
		for(Chromosome c: cList) {
			if(c.size() == solLength) solution = c;
		}
		
		int counter = 0;
		while(counter <= this.numOfIterations && solution.size() != solLength) {
			System.out.println(counter);
			ISelection tournament = new TournamentSelection();
			
			Chromosome parent1 = tournament.select(cList, this.rand, this.n, true);
			Chromosome parent2 = tournament.select(cList, this.rand, this.n, true);	
			
			ICrossover crossover = new Crossover(this.rand);
			Chromosome child = crossover.cross(parent1, parent2);
			mutation.mutate(child);
			
			if(function.valueAt(child) > function.valueAt(solution)) solution = child;
			Chromosome worst = tournament.select(cList, rand, this.m, false);
			
			if(!p) {
				cList.remove(worst);
				cList.add(child);
			}else{
				// ako je dijete bolje od najgore jedinke u m-turnirskoj 
				// selekciji, doda se u populaciju
				if(function.valueAt(child) > function.valueAt(worst)) {
					cList.remove(worst);
					cList.add(child);
				}
			}
			counter++;
		}
		
		System.out.println("Broj potrebnih spremnika: " + solution.size());
		return solution;
	}

}
