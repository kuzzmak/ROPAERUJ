package hr.fer.zemris.numeric;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Jama.Matrix;

public class Sustav {

	// matrica koeficijnata sustava
	public static Matrix coeffs;

	public static Matrix getCoefficients(String path) {

		double[][] matrixData = new double[10][11];
		List<String[]> coeffs = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();

			while (line != null) {
				if (!line.startsWith("[")) {
					line = br.readLine();
					continue;
				}

				line = line.replace("[", "");
				line = line.replace("]", "");

				coeffs.add(line.split(","));
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 11; j++) {
				matrixData[i][j] = Double.parseDouble(coeffs.get(i)[j].trim());
			}
		}
		return new Matrix(matrixData);
	}

	public static double ex(Matrix coeffs, double[] v, int row) {
		double result = 0;
		for (int i = 0; i < v.length; i++) {
			result += coeffs.get(row, i) * v[i];
		}
		result = result - coeffs.get(row, coeffs.getColumnDimension() - 1);
		return result;
	}

	public static double[] sumRowGradients(List<double[]> rowGradients) {
		double[] gradient = new double[rowGradients.size()];

		for (int k = 0; k < rowGradients.size(); k++) {
			gradient[k] = 0;
		}
		for (int i = 0; i < rowGradients.size(); i++) {
			for (int j = 0; j < rowGradients.size(); j++) {
				gradient[i] += rowGradients.get(j)[i];
			}
		}
		double sum = 0;
		for (int i = 0; i < gradient.length; i++) {
			sum += Math.pow(gradient[i], 2);
		}
		for (int i = 0; i < gradient.length; i++) {
			gradient[i] /= Math.sqrt(sum);
		}
		return gradient;
	}

	public static double derivativeByVariable(Matrix coeffs, int row, int column) {
//		double result = 0;
//		
//		for(int i = 0; i < coeffs.getColumnDimension() - 1; i++) {
//			result += 2 * coeffs.get(row, column);
//		}
		System.out.println(coeffs.get(row, column));
		return 2 * coeffs.get(row, column) * coeffs.get(row, column);
	}

	public static void main(String[] args) throws IOException {

		IHFunction sustav = new IHFunction() {

			@Override
			public int numOfVariables() {
				return 10;
			}

			@Override
			public double valueAt(double[] v) {

				double[] functionValue = new double[numOfVariables()];

				for (int i = 0; i < numOfVariables(); i++) {
					functionValue[i] = 0;
				}

				for (int i = 0; i < numOfVariables(); i++) {
					for (int j = 0; j < numOfVariables(); j++) {
						functionValue[i] += Math
								.pow((coeffs.get(i, j) * v[j]) - coeffs.get(i, coeffs.getColumnDimension() - 1), 2);
					}
				}

				double error = 0;

				for (int i = 0; i < numOfVariables(); i++) {
					error += functionValue[i];
				}

				return error;
			}

			@Override
			public double[] gradient(double[] v) {

				List<double[]> rowGradients = new ArrayList<>();

				for (int i = 0; i < numOfVariables(); i++) {

					int counter = 0;
					double[] temp = new double[numOfVariables()];

					for (int j = 0; j < numOfVariables(); j++) {
						temp[j] += 2 * coeffs.get(i, counter) * ex(coeffs, v, i);
						counter++;
					}
					rowGradients.add(temp);
				}
				return sumRowGradients(rowGradients);
			}

			@Override
			public double[][] hessian(double[] v) {

				double[][] hessian = new double[numOfVariables()][numOfVariables()];

				for (int i = 0; i < numOfVariables(); i++) {
					for (int j = 0; j < numOfVariables(); j++) {
						hessian[i][j] = 0;
					}
				}

				for (int i = 0; i < numOfVariables(); i++) {
					for (int j = 0; j < numOfVariables(); j++) {
						// System.out.printf("coef:2 * %f * %f\n", coeffs.get(i, i), coeffs.get(i, j));
						hessian[i][j] = 2 * coeffs.get(i, i) * coeffs.get(i, j);
					}
					// System.out.println();
				}
				return hessian;
			}

		};

		coeffs = getCoefficients("02-zad-sustav.txt");

		Random rand = new Random();
		double[] initial = sustav.gradient(new double[] { sustav.numOfVariables() });
		for (int i = 0; i < initial.length; i++) {
			initial[i] = rand.nextDouble();
		}

		int maxIterations = 100000;

		double[] sol = NumOptAlgorithms.newton(sustav, maxIterations, initial);
		for (int i = 0; i < sol.length; i++) {
			System.out.println(sol[i]);
		}
//		 double[][] hessian = sustav.hessian(new double[] {1,1,1,1,1,1,1,1,1,1});
//
//		for(int i = 0; i < 10; i++) {
//			for(int j = 0; j < 10; j++) {
//				System.out.printf("%f ", hessian[i][j]);
//			}
//			System.out.println();
//		}
	}
}
