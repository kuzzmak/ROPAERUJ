package hr.fer.zemris.numeric;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class Prijenosna {
	
	public static Matrix getCoefficients(String path, IHFunction function) {

		int numOfVariables = function.numOfVariables();
		// lista retcanih koeficijenata
		List<String[]> coeffs = new ArrayList<>();
		
		int numOfRows = 0;
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();

			while (line != null) {
				if (!line.startsWith("[")) {
					line = br.readLine();
					continue;
				}
				numOfRows++;
				// micu se zagrade radi jednostavnosti odvajanja medjusobnih koeficijenata
				line = line.replace("[", "");
				line = line.replace("]", "");

				coeffs.add(line.split(","));
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// matrica 10 * 11 gdje je zadnji stupac rjesenje svake jednadzbe
		double[][] matrixData = new double[numOfRows][numOfVariables + 1];
		// punjenje matrice matrixData iz liste u kojoj su koeficijenti
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfVariables + 1; j++) {
				matrixData[i][j] = Double.parseDouble(coeffs.get(i)[j].trim());
			}
		}
		return new Matrix(matrixData);
	}
	
	
	public static void main(String[] args) {
		
		IHFunction prijenosna = new IHFunction() {

			@Override
			public int numOfVariables() {
				return 5;
			}

			@Override
			public double valueAt(double[] v) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public double[] gradient(double[] v) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public double[][] hessian(double[] v) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		// ucitavanje vektora i vrijednosti koje ta funkcija poprima za odredjeni vektor
		String path = "02-zad-prijenosna.txt";
		Matrix values = getCoefficients(path, prijenosna);
		for(int i = 0; i < values.getRowDimension(); i++) {
			for(int j = 0; j < values.getColumnDimension(); j++) {
				System.out.printf("%f ", values.get(i, j));
			}
			System.out.println();
		}
		
	}
}
