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

	/**
	 * Funkcija za čitanje koeficijenata matrice iz datoteke
	 * 
	 * @param path lokacija do datoteke
	 * @return Matrix s koeficijentima
	 */
	public static Matrix getCoefficients(String path) {

		// matrica 10 * 11 gdje je zadnji stupac rjesenje svake jednadzbe
		double[][] matrixData = new double[10][11];
		// lista retcanih koeficijenata
		List<String[]> coeffs = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();

			while (line != null) {
				if (!line.startsWith("[")) {
					line = br.readLine();
					continue;
				}

				// micu se zagrade radi jednostavnosti odvajanja medjusobnih koeficijenata
				line = line.replace("[", "");
				line = line.replace("]", "");

				coeffs.add(line.split(","));
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// punjenje matrice matrixData iz liste u kojoj su koeficijenti
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

	/**
	 * Funkcija koja sumira retcane gradijente u jedan vektor koji predstavlja
	 * gradijent cijele funkcije
	 * 
	 * @param rowGradients lista retcanih gradijenata
	 * @return gradijent koji je suma retcanih gradijenata
	 */
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
		// normalizacija gradijenta
		double sum = 0;
		for (int i = 0; i < gradient.length; i++) {
			sum += Math.pow(gradient[i], 2);
		}
		for (int i = 0; i < gradient.length; i++) {
			gradient[i] /= Math.sqrt(sum);
		}
		return gradient;
	}

	public static double sum(int var1, int var2) {
		double total = 0;
		for (int i = 0; i < 10; i++) {
			total += coeffs.get(i, var1) * coeffs.get(i, var2);
		}		
		return total;
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
						hessian[i][j] = 2 * sum(i, j);
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

		int maxIterations = 10000;

		double[] sol = NumOptAlgorithms.newton(sustav, maxIterations, initial);
		for (int i = 0; i < sol.length; i++) {
			System.out.println(sol[i]);
		}
//		double[][] hessian = sustav.hessian(sol);
//
//		for(int i = 0; i < 10; i++) {
//			for(int j = 0; j < 10; j++) {
//				System.out.printf("%-4.2f ", hessian[i][j]);
//			}
//			System.out.println();
//		}
	}
}
