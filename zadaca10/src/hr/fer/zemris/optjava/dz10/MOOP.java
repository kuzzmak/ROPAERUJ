package hr.fer.zemris.optjava.dz10;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartUtils;

public class MOOP {

	public static void main(String[] args) {

		IFunction f11 = new IFunction() {

			@Override
			public double valueAt(Double[] point, boolean minimize) {
				if (minimize)
					return point[0] * point[0];
				return -(point[0] * point[0]);
			}

			@Override
			public int getDimension() {
				return 4;
			}

			@Override
			public Double[] getConstraints() {
				return new Double[] { -5., 5. };
			}
		};

		IFunction f12 = new IFunction() {

			@Override
			public double valueAt(Double[] point, boolean minimize) {
				if (minimize)
					return point[1] * point[1];
				return -(point[1] * point[1]);
			}

			@Override
			public int getDimension() {
				return 4;
			}

			@Override
			public Double[] getConstraints() {
				return new Double[] { -5., 5. };
			}
		};

		IFunction f13 = new IFunction() {

			@Override
			public double valueAt(Double[] point, boolean minimize) {
				if (minimize)
					return point[2] * point[2];
				return -(point[2] * point[2]);
			}

			@Override
			public int getDimension() {
				return 4;
			}

			@Override
			public Double[] getConstraints() {
				return new Double[] { -5., 5. };
			}
		};

		IFunction f14 = new IFunction() {

			@Override
			public double valueAt(Double[] point, boolean minimize) {
				if (minimize)
					return point[3] * point[3];
				return -(point[3] * point[3]);
			}

			@Override
			public int getDimension() {
				return 4;
			}

			@Override
			public Double[] getConstraints() {
				return new Double[] { -5., 5. };
			}
		};

		MOOPProblem problem1 = new Problem();
		problem1.add(f11);
		problem1.add(f12);
		problem1.add(f13);
		problem1.add(f14);

		IFunction f21 = new IFunction() {

			@Override
			public double valueAt(Double[] point, boolean minimize) {
				if (minimize)
					return point[0];
				return -point[0];
			}

			@Override
			public int getDimension() {
				return 2;
			}

			@Override
			public Double[] getConstraints() {

				return new Double[] { 0.1, 1. };
			}
		};

		IFunction f22 = new IFunction() {

			@Override
			public double valueAt(Double[] point, boolean minimize) {
				if (minimize)
					return (1 + point[1]) / point[0];
				return -(1 + point[1]) / point[0];
			}

			@Override
			public int getDimension() {
				return 2;
			}

			@Override
			public Double[] getConstraints() {

				return new Double[] { 0., 5. };
			}
		};

		MOOPProblem problem2 = new Problem();
		problem2.add(f21);
		problem2.add(f22);

		int problemNum = Integer.parseInt(args[0]);
		int populationSize = Integer.parseInt(args[1]);
		int maxIterations = Integer.parseInt(args[2]);
		String distanceString = "objective-space";
		double sigmaShare = 0.1;
		double alpha = 2;

		String solutions = "izlaz-dec.txt";
		String solutionsFitness = "izlaz-obj.txt";

		if (problemNum == 1) {

			NSGA nsga = new NSGA(problem1, populationSize, maxIterations, distanceString, sigmaShare, alpha);
			List<List<Double[]>> fronts = nsga.run();

			System.out.println("Broj fronte: broj jedinki u fronti");
			for (int i = 0; i < fronts.size(); i++) {
				System.out.println(i + ": " + fronts.get(i).size());
			}

			List<Double> fitness = nsga.getSortedFitness();
			List<Double[]> population = nsga.getPopulationSorted();

			// zapis populacije u datoteku
			try (FileWriter fw = new FileWriter(solutions)) {

				PrintWriter pw = new PrintWriter(fw);

				for (int i = 0; i < population.size(); i++) {
					pw.write(Arrays.toString(population.get(i)) + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// zapis fitnesa populacije u datoteku
			try (FileWriter fw = new FileWriter(solutionsFitness)) {

				PrintWriter pw = new PrintWriter(fw);

				for (int i = 0; i < fitness.size(); i++) {
					pw.write(fitness.get(i) + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			NSGA nsga = new NSGA(problem2, populationSize, maxIterations, distanceString, sigmaShare, alpha);
			List<List<Double[]>> fronts = nsga.run();

			System.out.println("Broj fronte: broj jedinki u fronti");
			for (int i = 0; i < fronts.size(); i++) {
				System.out.println(i + ": " + fronts.get(i).size());
			}

			List<Double> fitness = nsga.getSortedFitness();
			List<Double[]> population = nsga.getPopulationSorted();
			

			// zapis populacije u datoteku
			try (FileWriter fw = new FileWriter(solutions)) {

				PrintWriter pw = new PrintWriter(fw);

				for (int i = 0; i < population.size(); i++) {
					pw.write(Arrays.toString(population.get(i)) + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// zapis fitnesa populacije u datoteku
			try (FileWriter fw = new FileWriter(solutionsFitness)) {

				PrintWriter pw = new PrintWriter(fw);

				for (int i = 0; i < fitness.size(); i++) {
					pw.write(fitness.get(i) + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			String chartName = distanceString;
			
			// prikaz grafa
			SwingUtilities.invokeLater(() -> {
				Graph example = new Graph(chartName, nsga.getFunctionValues());
				example.setSize(800, 400);
				example.setLocationRelativeTo(null);
				example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				example.setVisible(true);
			});
		}
	}
}
