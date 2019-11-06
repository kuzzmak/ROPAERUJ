package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TournamentSelection implements ISelection {
	
	public TournamentSelection() {
	}

	@Override
	public Chromosome select(List<Chromosome> population, Random rand) {
		
		Chromosome best = population.get(0);
		List<Chromosome> selected = new ArrayList<>();
		
		for(int i = 0; i < CONSTANTS.numberOfParticipants; i++) {
			
			Chromosome c = population.get(rand.nextInt(population.size()));
			if(!selected.contains(c)) selected.add(c);
			
		}
		
		for(int i = 0; i < selected.size(); i++) {
			if(selected.get(i).getFitness() < best.getFitness()) best = selected.get(i);
		}
		return best;
	}
}
