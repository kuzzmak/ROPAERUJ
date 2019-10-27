package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BoxFilling {
	
	// staza do datoteke stapova
	private static String path;
	// varijabla za turnirskiu selekciju
	private int n;
	// varijabla za turnirsku selekciju mjesta na koje se ubacuje novonastalo dijete
	private int m;
	// varijabla za odnos omjera ispunjenosti pojedinih binova
	private static int k;
	// velicina populacije
	private static int populationSize;
	
	private static int numOfIterations;
	
	private static int binCapacity = 20;
	
	public static Random rand; 
	/**
	 * Ako je parametar p
	 * postavljen na false, dijete bezuvjetno zamjenjuje jedinku pobjednika m-turnira. Ako je parametar
     * p postavljen na true, dijete ce pobjednika m-turnira zamijeniti samo ako ima bolju dobrotu od
     * pobjednika; u suprotnom, pobjednik ostaje u populaciji a dijete se eliminira.
	 */
	private boolean p;

	
	public BoxFilling(String path, int n, int m, int populationSize, int numOfIterations, boolean p) {
		super();
		BoxFilling.path = path;
		this.n = n;
		this.m = m;
		BoxFilling.populationSize = populationSize;
		BoxFilling.numOfIterations = numOfIterations;
		this.p = p;
		BoxFilling.rand = new Random();
	}


	public static void setK(int k) {
		BoxFilling.k = k;
	}


	public static int getK() {
		return k;
	}


	public Chromosome run() {
		
		
		IFunction function = new OptimizationFunction(getK());
		// ucitavanje iz datoteke
		StickLoader loader = new StickLoader(path);
		// lista svih stapova iz datoteke
		List<Stick> sticks = loader.loadSticks();
		// inicijalna populacija kromosoma
		List<Chromosome> cList = Population.makePopulation(populationSize, sticks, binCapacity);
		
		Chromosome solution = new Chromosome(sticks, binCapacity);
		for(Chromosome c: cList) {
			if(c.size() == 10) solution = c;
		}
		
		int counter = 0;
		boolean best = true;
		while(counter <= numOfIterations) {
			
			ISelection tournament = new TournamentSelection();
			
			Chromosome child1 = tournament.select(cList, rand, n, best);
			Chromosome child2 = tournament.select(cList, rand, n, best);			
			
			
			counter++;
		}
		
		System.out.println("Value at: " + function.valueAt(solution));
		return solution;
	}
	
	
	public static void main(String[] args) {
		
		int n = 4;
		int m = 3;
		int populationSize = 10;
		int numOfIterations = 1000;
		float mutationRate = 0.2f;
		boolean p = true;
		setK(2);
		
		IMutation mutation = new Mutation(mutationRate);
		String path = "kutije\\problem-20-10-1.dat";
		BoxFilling bf = new BoxFilling(path, n, m, populationSize, numOfIterations, p);
		Chromosome sol = bf.run();
		System.out.println(sol);
		Chromosome mutated = mutation.mutate(sol);
		System.out.println();
		System.out.println(mutated);
		
//		Random rand = new Random();
//		StickLoader loader = new StickLoader(path);
//		
//		function = new OptimizationFunction(K);
//		
//		List<Stick> stickList = new ArrayList<>(); 
//		stickList = loader.loadSticks();
//		
//		Chromosome c = new Chromosome(stickList, 20);
//		int k = 2;
//		IFunction function = new OptimizationFunction(k);
//
//		int binSize = 20;
//		List<Chromosome> clist = Population.makePopulation(populationSize, stickList, binSize);
//		
//		System.out.println("Population");
//		for(int i = 0; i < clist.size(); i++) {
//			System.out.println(clist.get(i));
//			System.out.println("\t\t\t" + function.valueAt(clist.get(i)));
//		}
//		System.out.println();
//		ISelection tournamentSelection = new TournamentSelection();
//		Chromosome fittest = tournamentSelection.select(clist, rand, N, true);
//		System.out.println("fittest:");
//		System.out.println(fittest);
//		System.out.println(function.valueAt(fittest));
//		Chromosome worst = tournamentSelection.select(clist, rand, N, false);
//		System.out.println("worst:");
//		System.out.println(worst);
//		System.out.println(function.valueAt(worst));

		
	}
	
	
}
