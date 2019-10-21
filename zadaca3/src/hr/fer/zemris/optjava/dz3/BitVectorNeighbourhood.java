package hr.fer.zemris.optjava.dz3;

import java.util.Random;

public class BitVectorNeighbourhood implements INeighbourhood<SingleObjectiveSolution>{

	@Override
	public BitVectorSolution randomNeighbour(SingleObjectiveSolution obj) {
		BitVectorSolution solution = (BitVectorSolution)obj.newLikeThis();
		Random rand = new Random();
		
		double p = 0.3d;
		byte[] bits = solution.getBits();
		for(int i = 0; i < solution.getBits().length; i++) {
			if(p > rand.nextDouble()) {
				bits[i] = (byte)(bits[i] == 1 ? 0 : 1);
			}
		}
		
		solution.setBits(bits);
		return solution;
	}

}
