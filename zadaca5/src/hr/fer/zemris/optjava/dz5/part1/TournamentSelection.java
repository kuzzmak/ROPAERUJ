package hr.fer.zemris.optjava.dz5.part1;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

public class TournamentSelection implements ISelection {

	@Override
	public BitVector select(LinkedHashMap<BitVector, Double> population, Random rand) {
		
		LinkedHashMap<BitVector, Double> selected = new LinkedHashMap<>();
		
		
		while(selected.size() < OffspringSelectionAlgorithm.numberOfParticipants) {
			BitVector b = population.keySet().toArray(new BitVector[0])[rand.nextInt(population.size())];
			selected.put(b, OffspringSelectionAlgorithm.function.valueAt(b));
		}
		
		BitVector best = population.keySet().toArray(new BitVector[0])[0];
		for(Map.Entry<BitVector, Double> entry: selected.entrySet()) {
			if(entry.getValue() > OffspringSelectionAlgorithm.function.valueAt(best)) best = entry.getKey();
		}
		
		return best;
	}

}
