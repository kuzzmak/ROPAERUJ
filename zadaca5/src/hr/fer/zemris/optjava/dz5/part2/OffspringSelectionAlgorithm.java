package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class OffspringSelectionAlgorithm{

	private int chromosomeSize;
	private List<Chromosome> population;
	private int maxIterations;
	private double maxSelPress;
	private double actSelPress = 1;
	private double succRatio = 0.5;
	private double compFactor = 0;
	private ICrossover crossover;
	private IMutation mutation;
	
	// broj k u k-turnirsokj selekciji
	public static int numberOfParticipants = 4;
	private static Random rand = new Random();
	public static IFunction function;

	public OffspringSelectionAlgorithm(IFunction function,
			int chromosomeSize,
			List<Chromosome> population,
			int maxIterations,
			double maxSelPress,
			ICrossover crossover,
			IMutation mutation) {
		
		this.function = function;
		this.chromosomeSize = chromosomeSize;
		this.population = population;
		this.maxIterations = maxIterations;
		this.maxSelPress = maxSelPress;
		this.crossover = crossover;
		this.mutation = mutation;
	}
	

	public List<Chromosome> run() {

		ISelection tournament = new TournamentSelection(numberOfParticipants);

		List<Chromosome> thisPopulation = new ArrayList<>(population);
		
		double fact = 0.99d;

		int numOfIter = 0;

		while ((numOfIter < maxIterations) && (actSelPress < maxSelPress)) {
			List<Chromosome> nextPop = new ArrayList<>();
			List<Chromosome> badChildrenPool = new ArrayList<>();

			int poolChildrenCount = 0;
			int nextPopCount = 0;

			while ((nextPop.size() < thisPopulation.size() * succRatio)
					&& (nextPop.size() + badChildrenPool.size() < thisPopulation.size() * maxSelPress)) {

				// izbor roditelja
				Chromosome parent1 = tournament.select(thisPopulation, rand);
				Chromosome parent2 = tournament.select(thisPopulation, rand);
				// djeca dobivena krizanjem
				List<Chromosome> children = crossover.cross(parent1, parent2, rand);
				
				Chromosome child;
				if(function.valueAt(children.get(0)) > function.valueAt(children.get(1))) child = children.get(0);
				else child = children.get(1);
				
				// mutacija djeteta
				mutation.mutate(child, rand);
				child.setFitness(function.valueAt(child));
				
				double tFit = function.valueAt(parent2)
						+ Math.abs(function.valueAt(parent1) - function.valueAt(parent2)) * compFactor;

				// ako dijete ima vec vrijednost funkcije dodamo ga u pool s losijom djecom
				if (child.getFitness() > tFit) {
					badChildrenPool.add(child);
					poolChildrenCount++;
				} else {
					nextPop.add(child);
					nextPopCount++;
				}

			}

			actSelPress = (nextPopCount + poolChildrenCount) / thisPopulation.size();
			succRatio = (double) nextPopCount / (nextPopCount + poolChildrenCount);

			// ako nije niti jedno dijete lose dijete, da popunimo ostatak nove populacije
			// samo generiramo nove random vektore
			if (badChildrenPool.size() == 0) {
				while (nextPop.size() < thisPopulation.size()) {
					Chromosome c = new Chromosome(chromosomeSize);
					c.shuffle();
					c.setFitness(function.valueAt(c));
					nextPop.add(c);
				}
			} else {
				// ako je broj djece u badChildrenPoolu nedovoljan da se popuni
				// ostatak praznine u novoj populaciji
				if (badChildrenPool.size() <= thisPopulation.size() - nextPop.size()) {
					// prvo dodamo sve iz badChildrenPoola
					
					for(Chromosome c: badChildrenPool) {
						nextPop.add(c);
					}
					
					// zatim popunimo ostatak praznog mjesta do pune populacije random vektorima
					while (nextPop.size() < thisPopulation.size()) {
						Chromosome c = new Chromosome(chromosomeSize);
						c.shuffle();
						c.setFitness(function.valueAt(c));
						nextPop.add(c);
					}

				}

				// slucaj kada u badChildrenPoolu ima vise djece no sto je potrebno
				// da se popuni nova populacija do kraja
				Collections.sort(badChildrenPool);
				
				int i = 0;
				while (nextPop.size() < thisPopulation.size()) {
					nextPop.add(badChildrenPool.get(i));
					i++;
				}
			}

			compFactor = 1 - Math.pow(fact, numOfIter);
			thisPopulation = nextPop;
			numOfIter++;
		}

		return new ArrayList<>(thisPopulation);
	}
}
