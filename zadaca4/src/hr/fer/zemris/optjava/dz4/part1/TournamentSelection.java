package hr.fer.zemris.optjava.dz4.part1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class TournamentSelection implements ISelection<SingleObjectiveSolution> {

	public TournamentSelection() {
		
	}
	
	@Override
	public SingleObjectiveSolution select(TreeMap<SingleObjectiveSolution, Double> offspring, Random rand) {
		
		
		List<SingleObjectiveSolution> list = new ArrayList<>(offspring.keySet());
		SingleObjectiveSolution best = list.get(rand.nextInt(offspring.size()));
		
		List<SingleObjectiveSolution> tempPopulation = new ArrayList<>();
		
		for(int i = 0; i < GeneticAlgorithm.numberOfTournaments; i++) {
			
			SingleObjectiveSolution temp = list.get(rand.nextInt(list.size()));
			
			if(temp.getFitness() > best.getFitness()) best = temp;
			
			tempPopulation.add(temp);
			
		}
		
		return best;
	}
}
