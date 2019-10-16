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
	public static Matrix getCoefficients(String path, IHFunction function) {

		int numOfVariables = function.numOfVariables();
		// matrica 10 * 11 gdje je zadnji stupac rjesenje svake jednadzbe
		double[][] matrixData = new double[numOfVariables][numOfVariables + 1];
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
		for (int i = 0; i < numOfVariables; i++) {
			for (int j = 0; j < numOfVariables + 1; j++) {
				matrixData[i][j] = Double.parseDouble(coeffs.get(i)[j].trim());
			}
		}
		return new Matrix(matrixData);
	}

	/**
	 * Funkcija za pomoc pri racunanju gradijenta
	 * 
	 * @param coeffs Matric koeficijenata sustava
	 * @param v trenutni vektor
	 * @param row red matrice koeficijenata za koji racunamo gradijent
	 * @return pomocna vrijednost
	 */
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

	/**
	 * Funkcija za izracun pojedine komponente Hesseove matrice na poziciji (i, j)
	 * 
	 * @param var1 redak Hesseove matrice
	 * @param var2 stupac Hesseove matrice
	 * @return suma derivacija po odredjenim komponentama
	 */
	public static double sum(int var1, int var2) {
		double total = 0;
		for (int i = 0; i < 10; i++) {
			total += coeffs.get(i, var1) * coeffs.get(i, var2);
		}		
		return total;
	}

	public static double error(IHFunction function, double[] solution) {
		return 1. / function.numOfVariables() * Math.sqrt(function.valueAt(solution));
	}

	public static void main(String[] args) throws IOException {

		IHFunction sustav = new IHFunction() {

			@Override
			public int numOfVariables() {
				return 10;
			}

			@Override
			public double valueAt(double[] v) {

				double value = 0;
				for (int i = 0; i < numOfVariables(); i++) {
					for (int j = 0; j < numOfVariables(); j++) {
						// kao vrijednost funkcije uzima se greska izmedju prave vrijednost i vrijednosti
						// funkcije s trenutnim najboljim rjesenjem pa na kvadrat i suma svega toga
						value += Math
								.pow((coeffs.get(i, j) * v[j]) - coeffs.get(i, coeffs.getColumnDimension() - 1), 2);
					}
				}
				return value;
			}

			@Override
			public double[] gradient(double[] v) {
				
				// lista gradijenata pojedinog retka sustava
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
						hessian[i][j] = 2 * sum(i, j);
					}
				}
				return hessian;
			}
		};

		// random inicijalizacija pocetnog rjesenja
		Random rand = new Random();
		double[] initial = new double[sustav.numOfVariables()];
		for (int i = 0; i < initial.length; i++) {
			initial[i] = rand.nextDouble();
		}

		int maxIterations = Integer.parseInt(args[1]);
		// putanja do datoteke sa zadatkom
		String path = args[2];
		// matrica koeficijenata
		coeffs = getCoefficients(path, sustav);

		double[] solution;
		String algName = args[0];
		if(algName.toLowerCase().equals("newton")) {
			solution = NumOptAlgorithms.newton(sustav, maxIterations, initial); 
		}else {
			solution = NumOptAlgorithms.gradientDescent(sustav, maxIterations, initial);
		}
		
		System.out.println("Rjesenja u obliku x1, x2,...");
		for (int i = 0; i < solution.length; i++) {
			System.out.printf("%f ", solution[i]);
		}
		System.out.printf("\nGreška u trenutku prekida algoritma: %f ", error(sustav, solution));
	}
}
