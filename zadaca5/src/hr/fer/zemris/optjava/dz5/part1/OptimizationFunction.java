package hr.fer.zemris.optjava.dz5.part1;

public class OptimizationFunction implements IFunction {

	@Override
	public int numberOfOnes(BitVector b) {
		int numOfOnes = 0;
		for(int i = 0; i < b.size(); i++) {
			if(b.getBits()[i] == 1) numOfOnes++;
		}
		return numOfOnes;
	}
	
	@Override
	public double valueAt(BitVector b) {
		
		double value = 0;
		int k = numberOfOnes(b);
		int n = b.size();
		
		if(k <= 0.8 * n) {
			value = (double)k / n;
		}else if(k >= 0.8 * n && k <= 0.9 * n) {
			value = 0.8;
		}else {
			value = (double)(2 * k / n) - 1;
		}
		
		return value;
	}


	

}
