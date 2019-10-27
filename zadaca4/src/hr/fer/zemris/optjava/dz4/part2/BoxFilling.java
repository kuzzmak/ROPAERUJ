package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoxFilling {

	
	public static void main(String[] args) {
		
		String path = "kutije\\problem-20-10-1.dat";
		StickLoader loader = new StickLoader(path);
		
		List<Stick> stickList = new ArrayList<>(); 
		stickList = loader.loadSticks();
		
		Chromosome c = new Chromosome(stickList, 20);
		int k = 2;
		IFunction function = new OptimizationFunction(k);

		int binSize = 20;
		List<Chromosome> clist = Population.makePopulation(5, stickList, binSize);
		
		for(int i = 0; i < clist.size(); i++) {
			System.out.println(clist.get(i));
		}
		
	}
	
	
}
