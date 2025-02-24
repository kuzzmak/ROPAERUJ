package hr.fer.zemris.optjava.dz4.part1;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class RouletteWheelSelection implements ISelection<SingleObjectiveSolution>{
	
	public RouletteWheelSelection() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public SingleObjectiveSolution select(TreeMap<SingleObjectiveSolution, Double> offspring, Random rand) {
		
		double minFitness = Double.MAX_VALUE;

		Iterator<Map.Entry<SingleObjectiveSolution, Double>> iter = offspring.entrySet().iterator();
		
		while(iter.hasNext()) {
			Map.Entry<SingleObjectiveSolution, Double> entry = iter.next();
			if(entry.getValue() < minFitness) minFitness = entry.getValue();
		}
		
		iter = offspring.entrySet().iterator();
		TreeMap<SingleObjectiveSolution, Double> tempOffspring = new TreeMap<>(Util.comp.reversed());
		
		for(Map.Entry<SingleObjectiveSolution, Double> entry: offspring.entrySet()) {
			tempOffspring.put(entry.getKey(), (entry.getValue() - minFitness) / Math.abs(entry.getValue()));
		}
		// normalizacija vjerojatnosti na interval 0-1
		Util.normalize(tempOffspring);
		
		SingleObjectiveSolution selected = null;
		
		double randFit = rand.nextDouble();
		
		double total = 0;
		for (Map.Entry<SingleObjectiveSolution, Double> entry : tempOffspring.entrySet()) {
			total += entry.getValue();
			if(total > randFit) {
				selected = entry.getKey();
				selected.setValue(GenAlg.function.valueAt(GenAlg.decoder.decode(entry.getKey())), Util.minimize);
				break;
			}
		}
		return selected;
	}

	

}
