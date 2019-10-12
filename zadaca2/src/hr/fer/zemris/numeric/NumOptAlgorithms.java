package hr.fer.zemris.numeric;

public class NumOptAlgorithms {
	public static double step = 0.02d; 
	public static double precision = 0.00001d;
	
	public static double[] gradientDescent(IFunction function, int maxIterations, double[] initial) {
	
		int numberOfVariables = function.numOfVariables();
		double[] current = initial;
		
		for(int i = 0; i < maxIterations; i++) {
			System.out.println(i);
			System.out.println(current[0] + " " + current[1]);
			double[] gradient = function.gradient(current);
			double f_val = function.valueAt(current);
			double g_val = f_val - (gradient[0] * step + gradient[1] * step);
			for(int j = 0; j < numberOfVariables; j++) {
				current[j] -= gradient[j] * step;
			}
			if(Math.abs(g_val - f_val) <= precision) break;
		}
		return current;
	}
}
