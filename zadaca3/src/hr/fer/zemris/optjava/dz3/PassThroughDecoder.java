package hr.fer.zemris.optjava.dz3;

public class PassThroughDecoder implements IDecoder<SingleObjectiveSolution>{

	@Override
	public double[] decode(SingleObjectiveSolution obj) {
		
		DoubleArraySolution sol = (DoubleArraySolution)obj;
		return sol.values;
	}

	@Override
	public void decode(double[] v, SingleObjectiveSolution obj) {
		DoubleArraySolution sol = (DoubleArraySolution)obj;
		v = sol.values;
	}

}
