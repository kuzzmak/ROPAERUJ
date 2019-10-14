package hr.fer.zemris.numeric;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

public class Jednostavno {

	// parser datoteke za skiciranje grafa trenutnih rješenja
	public static List<double[]> getGraphData(String fileName){
		List<double[]> graphData = new ArrayList<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(fileName));) {
		    String line = br.readLine();
		    
		    while (line != null) {
		    	String[] temp = line.split(",");
		    	graphData.add(new double[] {Double.parseDouble(temp[0]), Double.parseDouble(temp[1])});
		    	line = br.readLine();
		    }
		}catch(IOException e) {
			e.printStackTrace();
		}
		return graphData;
	}
	
	public static void main(String[] args) throws IOException {
		
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
		
		int maxIterations = 1000;
		
		
		double[] sol = NumOptAlgorithms.gradientDescent(f1a, maxIterations);
		
		List<double[]> graphData = getGraphData("graphData");
		for(int i = 0; i < graphData.size(); i++) {
			System.out.println(graphData.get(i)[0] + " " + graphData.get(i)[1]);
		}
		final Graph demo = new Graph(graphData, "XY Series Demo");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		System.out.print(df.format(sol[0]));
		System.out.print(" ");
		System.out.print(df.format(sol[1]));
		System.out.println();
		
	}
	
	
	
	
	
}
