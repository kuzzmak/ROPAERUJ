package hr.fer.zemris.optjava.dz9;

import java.util.Arrays;

import hr.fer.zemris.optjava.dz9.Functions.IFunction;

public class MOOP {

	public static void main(String[] args) {

		IFunction f1 = new IFunction() {

			@Override
			public double valueAt(double[] point) {
				return point[0];
			}

			@Override
			public int getDimension() {
				return 2;
			}

			@Override
			public double[] getConstraints() {

				return new double[] { 0.1, 1 };
			}
		};

		IFunction f2 = new IFunction() {

			@Override
			public double valueAt(double[] point) {
				return (1 + point[1]) / point[0];
			}

			@Override
			public int getDimension() {
				return 2;
			}

			@Override
			public double[] getConstraints() {

				return new double[] { 0, 5 };
			}
		};
		
		MOOPProblem problem1 = new Problem1();
		problem1.add(f1);
		problem1.add(f2);
		
		double[] value = problem1.evaluate(new double[] {1,1});
		System.out.println(Arrays.toString(value));

	}

}
