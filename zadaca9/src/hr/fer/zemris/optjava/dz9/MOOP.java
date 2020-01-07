package hr.fer.zemris.optjava.dz9;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class MOOP {

	public static void main(String[] args) {

		IFunction f11 = new IFunction() {

			@Override
			public double valueAt(double[] point, boolean minimize) {
				if (minimize)
					return point[0] * point[0];
				return -(point[0] * point[0]);
			}

			@Override
			public int getDimension() {
				return 4;
			}

			@Override
			public double[] getConstraints() {
				return new double[] { -5, 5 };
			}
		};

		IFunction f12 = new IFunction() {

			@Override
			public double valueAt(double[] point, boolean minimize) {
				if (minimize)
					return point[1] * point[1];
				return -(point[1] * point[1]);
			}

			@Override
			public int getDimension() {
				return 4;
			}

			@Override
			public double[] getConstraints() {
				return new double[] { -5, 5 };
			}
		};

		IFunction f13 = new IFunction() {

			@Override
			public double valueAt(double[] point, boolean minimize) {
				if (minimize)
					return point[2] * point[2];
				return -(point[2] * point[2]);
			}

			@Override
			public int getDimension() {
				return 4;
			}

			@Override
			public double[] getConstraints() {
				return new double[] { -5, 5 };
			}
		};

		IFunction f14 = new IFunction() {

			@Override
			public double valueAt(double[] point, boolean minimize) {
				if (minimize)
					return point[3] * point[3];
				return -(point[3] * point[3]);
			}

			@Override
			public int getDimension() {
				return 4;
			}

			@Override
			public double[] getConstraints() {
				return new double[] { -5, 5 };
			}
		};

		MOOPProblem problem1 = new Problem();
		problem1.add(f11);
		problem1.add(f12);
		problem1.add(f13);
		problem1.add(f14);

		IFunction f21 = new IFunction() {

			@Override
			public double valueAt(double[] point, boolean minimize) {
				if (minimize)
					return point[0];
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

		IFunction f22 = new IFunction() {

			@Override
			public double valueAt(double[] point, boolean minimize) {
				if (minimize)
					return (1 + point[1]) / point[0];
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

		MOOPProblem problem2 = new Problem();
		problem2.add(f21);
		problem2.add(f22);

		int problemNum = Integer.parseInt(args[0]);
		int populationSize = Integer.parseInt(args[1]);
		String distanceString = args[2];
		int maxIterations = Integer.parseInt(args[3]);
		double sigmaShare = 0.1;
		double alpha = 2;

		String solutions = "izlaz-dec.txt";
		String solutionsFitness = "izlaz-obj.txt";

		if (problemNum == 1) {

			NSGA nsga = new NSGA(problem1, populationSize, maxIterations, distanceString, sigmaShare, alpha);
			List<List<double[]>> fronts = nsga.run();

			System.out.println("Broj fronte: broj jedinki u fronti");
			for (int i = 0; i < fronts.size(); i++) {
				System.out.println(i + ": " + fronts.get(i).size());
			}

			List<Double> fitness = nsga.getSortedFitness();
			List<double[]> population = nsga.getPopulationSorted();

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
			List<List<double[]>> fronts = nsga.run();

			System.out.println("Broj fronte: broj jedinki u fronti");
			for (int i = 0; i < fronts.size(); i++) {
				System.out.println(i + ": " + fronts.get(i).size());
			}

			List<Double> fitness = nsga.getSortedFitness();
			List<double[]> population = nsga.getPopulationSorted();
			

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

			final XYSeries data = new XYSeries(chartName);
			
			List<double[]> fv = nsga.getFunctionValues();
			
			for(int i = 0; i < fv.size(); i++) {
				data.add(fv.get(i)[0], fv.get(i)[1]);
			}

			
			final XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(data);
			

			JFreeChart xylineChart = ChartFactory.createXYLineChart(chartName, "f1", "f2",
					dataset, PlotOrientation.VERTICAL, true, true, false);
			
			XYPlot plot = xylineChart.getXYPlot();
			XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
			plot.setRenderer(renderer);

			
			int width = 640; 
			int height = 480; 
			File XYChart = new File(chartName + ".jpeg");
			try {
				ChartUtils.saveChartAsJPEG(XYChart, xylineChart, width, height);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
