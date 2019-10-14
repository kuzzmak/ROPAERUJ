package hr.fer.zemris.numeric;
import java.text.DecimalFormat;

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
		
		IHFunction f1b = new IHFunction() {

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

			@Override
			public double[][] hessian(double[] vector) {
				return new double[][] {{2,0}, {0,2}};
			}
		};
		
		IFunction f2a = new IFunction() {

			@Override
			public int numOfVariables() {
				return 2;
			}

			@Override
			public double valueAt(double[] v) {
				return Math.pow(v[0] - 1, 2) + 10 * Math.pow(v[1] - 2, 2);
			}

			@Override
			public double[] gradient(double[] v) {
				double[] gradient = new double[numOfVariables()];
				gradient[0] = 2 * (v[0] - 1);
				gradient[1] = 20 * (v[1] - 2);
				return gradient; 
			}
			
		};
		
		IHFunction f2b = new IHFunction() {

			@Override
			public int numOfVariables() {
				return 2;
			}

			@Override
			public double valueAt(double[] v) {
				return Math.pow(v[0] - 1, 2) + 10 * Math.pow(v[1] - 2, 2);
			}

			@Override
			public double[] gradient(double[] v) {
				double[] gradient = new double[numOfVariables()];
				gradient[0] = 2 * (v[0] - 1);
				gradient[1] = 20 * (v[1] - 2);
				return gradient; 
			}

			@Override
			public double[][] hessian(double[] vector) {
				return new double[][] {{2,0}, {0,20}};
			}
		};
		
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(10);
		
		int maxIterations = 50;
		
		
		double[] sol = NumOptAlgorithms.newton(f2b, maxIterations);
		//System.out.println(initial[0] + " " + initial[1]);
		
		
		System.out.print(df.format(sol[0]));
		System.out.print(" ");
		System.out.print(df.format(sol[1]));
		System.out.println();
		
	}
	
	
	
	
	
}
