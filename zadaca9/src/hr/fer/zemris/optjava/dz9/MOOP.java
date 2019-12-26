package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.Functions.IFunction;
import hr.fer.zemris.optjava.dz9.NSGA.NSGA;

public class MOOP {

	public static void main(String[] args) {

		IFunction f1 = new IFunction() {

			@Override
			public double valueAt(double[] point, boolean minimize) {
				if(minimize) return point[0];
				return -point[0];
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
			public double valueAt(double[] point, boolean minimize) {
				if(minimize) return (1 + point[1]) / point[0];
				return -(1 + point[1]) / point[0];
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
		
		int populationSize = 10;
		NSGA nsga = new NSGA(problem1, populationSize);
		nsga.run();

	}

}
