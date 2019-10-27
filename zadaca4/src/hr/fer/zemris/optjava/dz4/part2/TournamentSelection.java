package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TournamentSelection implements ISelection{

	
	public TournamentSelection() {
		
	}

	@Override
	public Chromosome select(List<Chromosome> cList, Random rand, int numberOfSelections, boolean best) {
		
		IFunction function = new OptimizationFunction(BoxFilling.getK());
		
		// inicijalizacija najboljeg i najgoreg rjesenja
		Chromosome fittest = cList.get(0);
		Chromosome worst = cList.get(0);
		
		// set zabranih indexa da se ne ponavljaju novo izabrana rjesenja
		Set<Integer> pickedIndexes = new HashSet<>();
		
		for(int i = 0; i < numberOfSelections; i++) {
			int index = rand.nextInt(cList.size());
			
			// sve do kad nije izabran neki novi index
			while(pickedIndexes.contains(index)) {
				index = rand.nextInt(cList.size());
			}
			pickedIndexes.add(index);

			Chromosome temp = cList.get(index);
			if(function.valueAt(temp) > function.valueAt(fittest)) {
				fittest = temp;
			}else if(function.valueAt(temp) < function.valueAt(worst)) {
				worst = temp;
			}
		}
		// ovisno o tome trebamo li najbolje ili najgore rjesenje
		if(best) {
			return fittest;
		}else {
			return worst;
		}
	}
}
