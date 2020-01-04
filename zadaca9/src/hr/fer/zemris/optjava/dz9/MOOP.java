package hr.fer.zemris.optjava.dz9;

import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

public class MOOP {
	
	private static List<double[]> functionValues;
	private static List<List<Integer>> fronts;

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
		
		int populationSize = 50;
		int maxIterations = 500;
		String distanceString = "decision-space";
		double sigmaShare = 0.1;
		double alpha = 2;
		
		NSGA nsga = new NSGA(problem1, populationSize, maxIterations, distanceString, sigmaShare, alpha);
		List<List<double[]>> fronts = nsga.run();
		
		for(int i = 0; i < fronts.size(); i++) {
			System.out.println("fronta: " + i);
			for(int j = 0; j < fronts.get(i).size(); j++) {
				System.out.println("\t" + Arrays.toString(fronts.get(i).get(j)));
			}
		}
		
		Graph graph = new Graph("Graph", fronts);
		graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graph.pack();
		graph.setLocationRelativeTo(null);
		graph.setVisible(true);

	}
}
