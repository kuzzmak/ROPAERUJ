package hr.fer.zemris.optjava.dz3;

public class NaturalBinaryDecoder extends BitVectorDecoder{

	public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] nBits, int n) {
		super(mins, maxs, nBits, n);
	}
	
	public NaturalBinaryDecoder(double min, double max, int nBits, int n) {
		super(min, max, nBits, n);
	}
	
	
	@Override
	public double[] decode(SingleObjectiveSolution obj) {
		
		BitVectorSolution solution = (BitVectorSolution)obj;
		// broj varijabli
		int n = getN();
		// bitvektor cijelog rjesenja
		byte[] bits = solution.getBits();
		// dekodiran vektor
		double[] decoded = new double[n];
		for(int i = 0; i < n; i++) {
			
			// this.getnBits()[i] -- broje bitova za svaku varijablu
			double k = 0;
			int numOfBits = this.getnBits()[i];
			for(int j = 0; j < numOfBits; j++) {
				
				k += bits[i * numOfBits + j] * Math.pow(2, numOfBits - j - 1);
			}
			
			decoded[i] = getMins()[i] + k / (Math.pow(2, numOfBits) - 1) * (getMaxs()[i] - getMins()[i]);
		}
		return decoded;
	}

	@Override
	public void decode(double[] v, SingleObjectiveSolution obj) {
		v = decode(obj);
	}
}
