package hr.fer.zemris.optjava.dz8.Crossover;

import java.util.Random;

public class UniformCrossover implements ICrossover {
	
	private static Random rand;
	private static double Cr;
	
	public UniformCrossover(double Cr) {
		UniformCrossover.rand = new Random();
		UniformCrossover.Cr = Cr;
	}

	@Override
	public double[] cross(double[] mutantVector, double[] targetVector) {
		double[] crossed = new double[targetVector.length];
		
		// indeks koji se sigurno kopira iz ciljnog vektor u krizani
		int jRand = rand.nextInt(targetVector.length);
		
		for(int i = 0; i < targetVector.length; i++) {
			
			if(rand.nextDouble() < Cr || i == jRand) {
				crossed[i] = mutantVector[i];
			}else {
				crossed[i] = targetVector[i];
			}
			
		}
		return crossed;
	}

}
