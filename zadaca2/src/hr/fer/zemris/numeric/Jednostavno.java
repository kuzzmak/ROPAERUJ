package hr.fer.zemris.numeric;

import java.util.Random;

public class Jednostavno {

	public static void main(String[] args) {
		
		IFunction f1a = new IFunction() {

			@Override
			public int numOfVariables() {
				return 2;
			}

			@Override
			public double valueAt(double[] v) {
				return Math.pow(v[0], 2) + Math.pow(v[1] - 1, 2);
			}

			@Override
			public double[] gradient(double[] v) {
				double[] gradient = new double[numOfVariables()];
				gradient[0] = 2 * v[0];
				gradient[1] = 2 * (v[1] - 1);
				return gradient;
			}
			
		};
		
		int maxIterations = 10000;
		Random rand = new Random();
		double low = -5;
		double high = 5;
		double[] initial = new double[] {low + (high - low) * rand.nextDouble(), low + (high - low) * rand.nextDouble()};
		double[] sol = NumOptAlgorithms.gradientDescent(f1a, maxIterations, initial);
		//System.out.println(initial[0] + " " + initial[1]);
		System.out.println(sol[0] + " " + sol[1]);
		
	}
	
	
	
	
}
