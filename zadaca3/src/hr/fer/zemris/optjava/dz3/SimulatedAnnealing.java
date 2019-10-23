package hr.fer.zemris.optjava.dz3;

import java.util.Random;

public class SimulatedAnnealing<T extends SingleObjectiveSolution> implements IOptAlgorithm<SingleObjectiveSolution>{

	
	private       IDecoder<T> decoder; 
	private INeighbourhood<T> neighbourhood;
	private     ITempSchedule schedule;
	private                 T startWith;
	private         IFunction function;
	private           boolean minimize;
	private            Random rand;
	
	
	public SimulatedAnnealing(IDecoder<T> decoder, INeighbourhood<T> neighbourhood, ITempSchedule schedule, T startWith, IFunction function,
			boolean minimize, Random rand) {
		super();
		this.decoder = decoder;
		this.neighbourhood = neighbourhood;
		this.schedule = schedule;
		this.startWith = startWith;
		this.function = function;
		this.minimize = minimize;
		this.rand = new Random();
	}


	@SuppressWarnings("unchecked")
	@Override
	public SingleObjectiveSolution run() {
		
		T solution = (T)startWith.duplicate();
		
		solution.setValue(function.valueAt(decoder.decode(startWith)), minimize);
		
		for(int i = 0; i < schedule.getOuterLoopCounter(); i++) {
			double currentTemp = schedule.getNextTemperature();
			for(int j = 0; j < schedule.getInnerLoopCounter(); j++) {

				T neighbour = neighbourhood.randomNeighbour(solution);
				neighbour.setValue(function.valueAt(decoder.decode(neighbour)), minimize);
				
				double diff = neighbour.getFitness() - solution.getFitness();
				if(diff >= 0) {
					solution = neighbour;
				}else {
					double p = Math.pow(Math.E, diff / currentTemp);
//					double p = 0.4 * Math.pow(0.9, i);
					if(p > rand.nextFloat()) {
						solution = neighbour;
					}
					
				}
			}
//			if(i % 100 == 0) {
//				System.out.printf("out: %d, value: %f, temp: %f\n", i, function.valueAt(decoder.decode(solution)), currentTemp);
//			}
		}
		return solution;
	}

}
