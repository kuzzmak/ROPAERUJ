package hr.fer.zemris.optjava.dz4.part2;

import java.util.List;

public class OptimizationFunction implements IFunction {

	private int k;
	
	public OptimizationFunction(int k) {
		this.k = k;
	}
	
	@Override
	public double valueAt(Chromosome c) {
		
		List<Bin> listOfBins = c.getBinList();
		int binSize = c.getBinCapacity();
		double value = 0.d;
		for(Bin b: listOfBins) {
			value += Math.pow(b.getOccupiedSpace() / binSize, k) / listOfBins.size();
		}
		return value;
	}

}
