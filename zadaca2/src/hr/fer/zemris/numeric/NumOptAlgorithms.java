package hr.fer.zemris.numeric;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import Jama.Matrix;

public class NumOptAlgorithms {
	public static double gamma = 0.02d;
	public static double precision = 1E-5;

	public static double bisection(double[] current, IFunction function) {
		
		double lambda = 0;
		double lambdaL = 0;
		double lambdaH = 0.001;
		double[] d = function.gradient(current);
		
		
		while(true) {
			
			double[] tempX = new double[current.length];
			
			for(int i = 0; i < current.length; i++) {
				tempX[i] = current[i] + lambdaH * d[i];
			}
			
			double dl = 0;
			double[] gradient = function.gradient(tempX);
			for(int i = 0; i < current.length; i++) {
				dl += gradient[i] * d[i];
			}
			if(dl > 0) {
				break;
			}
			lambdaH *= 2;
		}

		while(true) {
			double dl = 0;
			lambda = (lambdaL + lambdaH) / 2;
			double[] gradient = function.gradient(current);
			for(int i = 0; i < current.length; i++) {
				dl += gradient[i] * d[i];
			}
			if(dl > 0 && dl <= precision) {
				break;
			}else if(dl < 0) {
				lambdaL = lambda;
			}else {
				
				lambdaH = lambda;
			}
		}
		return lambda;
	}
	
	
	public static double[] gradientDescent(IFunction function, int maxIterations, double[] initial){
		
		int numberOfVariables = function.numOfVariables();
		double[] current = initial;
		
//		BufferedWriter writer = new BufferedWriter(new FileWriter("graphData"));
//		writer.write(current[0] + "," + current[1] + "\n");
		
		for (int i = 0; i < maxIterations; i++) {
			// ispis trenutne iteracije i rjesenja
			System.out.printf("iteration: %d current solution: ", i);
			for(int k = 0; k < current.length; k++) {
				System.out.printf("%f ", current[k]);
			}
			System.out.println();
			
			double[] gradient = function.gradient(current);
			double f_val = function.valueAt(current);
			double step = 0;
			for (int k = 0; k < numberOfVariables; k++) {
				step += gradient[k] * gamma;
			}
			for (int j = 0; j < numberOfVariables; j++) {
				current[j] -= gradient[j] * gamma;
				
			}
			//writer.write(current[0] + "," + current[1] + "\n");
			
			// za provjeru je li potrebno prekinuti algoritam
			double g_val = f_val - step;
			if (Math.abs(g_val - f_val) <= precision) {
//				writer.close();
				break;
			}
			
		}
		return current;
	}

	public static double[] newton(IHFunction function, int maxIterations, double[] initial){
		
		double[] current = initial;
		int numberOfVariables = function.numOfVariables();
//		BufferedWriter writer = new BufferedWriter(new FileWriter("graphData"));
//		writer.write(current[0] + "," + current[1] + "\n");
		
		for(int i = 0; i < maxIterations; i++) {
			// ispis trenutne iteracije i rjesenja
			System.out.printf("iteration: %d current solution: ", i);
			for(int k = 0; k < current.length; k++) {
				System.out.printf("%f ", current[k]);
			}
			System.out.println();
			
			double[][] hessianData = function.hessian(current);
			Matrix hessian = new Matrix(hessianData);
			Matrix inverseHessian = hessian.inverse();
			
			double[] grad = function.gradient(current);
			Matrix gradient = new Matrix(grad, grad.length);
			Matrix lambda = inverseHessian.times(gradient);
			
			for(int j = 0; j < numberOfVariables; j++) {
				current[j] -= lambda.get(j, 0);
			}
//			writer.write(current[0] + "," + current[1] + "\n");
			
			// za provjeru je li potrebno prekinuti algoritam
			double f_val = function.valueAt(current);
			hessianData = function.hessian(current);
			inverseHessian = hessian.inverse();
			
			Matrix firstPart = gradient.transpose().times(lambda);
			Matrix thirdPart = gradient.transpose().times(hessian).times(gradient);
			thirdPart = thirdPart.times(0.5);
			double g_val = f_val + firstPart.get(0, 0) + thirdPart.get(0, 0);
//			System.out.println(i);
//			System.out.println(Math.abs(g_val - f_val));
			if(Math.abs(g_val - f_val) <= precision) {
//				writer.close();
				break;
			}
		}
		return current;
	}
}
