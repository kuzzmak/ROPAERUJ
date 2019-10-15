package hr.fer.zemris.numeric;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
	
	
	public static double[] gradientDescent(IFunction function, int maxIterations, double[] initial) throws IOException {
		
		int numberOfVariables = function.numOfVariables();
		double[] current = initial;
		
//		BufferedWriter writer = new BufferedWriter(new FileWriter("graphData"));
//		writer.write(current[0] + "," + current[1] + "\n");
		
		for (int i = 0; i < maxIterations; i++) {
			//System.out.println(i);

			double[] gradient = function.gradient(current);
			double f_val = function.valueAt(current);
			double step = 0;
			for (int k = 0; k < numberOfVariables; k++) {
				step += gradient[k] * gamma;
			}
			double g_val = f_val - step;
//			double bi = bisection(current, function);
//			System.out.println("lambda " + bi);
//			System.out.println("iter: " + i);
			for (int j = 0; j < numberOfVariables; j++) {
				current[j] -= gradient[j] * gamma;
				
			}
			//writer.write(current[0] + "," + current[1] + "\n");
			if (Math.abs(g_val - f_val) <= precision) {
//				writer.close();
				break;
			}
			
		}
		
//		System.out.println("prvi " + graphData.get(0)[0] + " " + graphData.get(0)[1]);
//		System.out.println("pedeseti " + graphData.get(49)[0] + " " + graphData.get(49)[1]);
//		System.out.println(graphData.size());
		//writer.close();
		return current;
	}

	public static double[] newton(IHFunction function, int maxIterations, double[] initial) {
		
		double[] current = initial;
		int numberOfVariables = function.numOfVariables();
		
		for(int i = 0; i < maxIterations; i++) {
			
			double[][] hessianData = function.hessian(current);
			Matrix hessian = new Matrix(hessianData);
			Matrix inverseHessian = hessian.inverse();
			
			double[] grad = function.gradient(current);
			Matrix gradient = new Matrix(grad, grad.length);
			Matrix lambda = inverseHessian.times(gradient);
			
			for(int j = 0; j < numberOfVariables; j++) {
				current[j] -= lambda.get(j, 0);
			}
			
//			double f_val = function.valueAt(current);
//			hessianData = function.hessian(current);
//			inverseHessian = hessian.inverse();
//			
//			Matrix firstPart = gradient.transpose().times(lambda);
//			Matrix thirdPart = gradient.transpose().times(hessian).times(gradient);
//			thirdPart = thirdPart.times(0.5);
//			double g_val = f_val + firstPart.get(0, 0) + thirdPart.get(0, 0);
//			
//			if(Math.abs(g_val - f_val) <= precision) break;
		}

		return current;
	}

}
