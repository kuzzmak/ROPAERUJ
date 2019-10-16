package hr.fer.zemris.numeric;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Jama.Matrix;

public class Prijenosna {
	
	public static Matrix values;
	
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
		double[][] matrixData = new double[numOfRows][numOfVariables];
		// punjenje matrice matrixData iz liste u kojoj su koeficijenti
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfVariables; j++) {
				matrixData[i][j] = Double.parseDouble(coeffs.get(i)[j].trim());
			}
		}
		return new Matrix(matrixData);
	}
	
	
	public static double ex(double[] v, int i) {
		double result = 0;
		
		result += v[0] * values.get(i, 0)
				+ v[1] * Math.pow(values.get(i, 0), 3) * values.get(i, 1)
				+ v[2] * Math.pow(Math.E, v[3] * values.get(i, 2)) * (1 + Math.cos(v[4] * values.get(i, 3)))
				+ v[5] * values.get(i, 3) * Math.pow(values.get(i, 4), 2) - values.get(i, values.getColumnDimension() - 1);
		
		return result;
	}
	
	public static void main(String[] args) {
		
		IHFunction prijenosna = new IHFunction() {

			@Override
			public int numOfVariables() {
				return 6;
			}

			@Override
			public double valueAt(double[] v) {
				
				double[] value = new double[values.getRowDimension()];
				
				for(int i = 0; i < values.getRowDimension(); i++) {
					value[i] += v[0] * values.get(i, 0)
							+ v[1] * Math.pow(values.get(i, 0), 3) * values.get(i, 1)
							+ v[2] * Math.pow(Math.E, v[3] * values.get(i, 2)) * (1 + Math.cos(v[4] * values.get(i, 3)))
							+ v[5] * values.get(i, 3) * Math.pow(values.get(i, 4), 2);
				}
 				
				double functionValue = 0;
				for(int i = 0; i < values.getRowDimension(); i++) {
					functionValue += Math.pow(values.get(i, values.getColumnDimension() - 1) - value[i], 2);
				}
				return functionValue;
			}

			@Override
			public double[] gradient(double[] v) {
				double[] grad = new double[numOfVariables()];
				for(int i = 0; i < values.getRowDimension(); i++) {
					
					grad[0] += 2 * values.get(i, 0) * ex(v, i);
					grad[1] += 2 * Math.pow(values.get(i, 0), 3) * values.get(i, 1) * ex(v, i);
					grad[2] += 2 * Math.pow(Math.E, v[3] *values.get(i, 2)) * (1 + Math.cos(v[4] * values.get(i, 3))) * ex(v, i);
					grad[3] += 2 * v[2] * values.get(i, 2) * Math.pow(Math.E, v[3] * values.get(i, 2)) * (1 + Math.cos(v[4] * values.get(i, 3))) * ex(v, i);
					grad[4] += -2 *v[2] * values.get(i, 3) * Math.pow(Math.E, v[3] * values.get(i, 2)) * Math.sin(v[4] * values.get(i, 3)) * ex(v, i);
					grad[5] += 2 * values.get(i, 3) * Math.pow(values.get(i, 4), 2) * ex(v, i);
				}
				
				double sum = 0;
				for (int i = 0; i < grad.length; i++) {
					sum += Math.pow(grad[i], 2);
				}
				for (int i = 0; i < grad.length; i++) {
					grad[i] /= Math.sqrt(sum);
				}
				
				return grad;
			}

			@Override
			public double[][] hessian(double[] v) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		
//		for(int i = 0; i < values.getRowDimension(); i++) {
//			for(int j = 0; j < values.getColumnDimension(); j++) {
//				System.out.printf("%f ", values.get(i, j));
//			}
//			System.out.println();
//		}
//		System.out.println(prijenosna.valueAt(new double[] {1,1,1,1,1,1}));
		
		Random rand = new Random();
		double[] initial = new double[prijenosna.numOfVariables()];
		for(int i = 0; i < prijenosna.numOfVariables(); i++) {
			initial[i] = rand.nextDouble();
		}
		int maxIterations = Integer.parseInt(args[1]);
		// putanja do datoteke sa zadatkom
		String path = args[2];
		
		// ucitavanje vektora i vrijednosti koje ta funkcija poprima za odredjeni vektor
		//String path = "02-zad-prijenosna.txt";
		values = getCoefficients(path, prijenosna);
				
		double[] solution = NumOptAlgorithms.gradientDescent(prijenosna, maxIterations, initial);
		String algName = args[0];
		if(algName.toLowerCase().equals("newton")) {
			solution = NumOptAlgorithms.newton(prijenosna, maxIterations, initial); 
		}else {
			solution = NumOptAlgorithms.gradientDescent(prijenosna, maxIterations, initial);
		}
		
		System.out.println("Rjesenja u obliku a, b,...");
		for (int i = 0; i < solution.length; i++) {
			System.out.printf("%f ", solution[i]);
		}
		System.out.printf("\nGreška u trenutku prekida algoritma: %f ", Sustav.error(prijenosna, solution));
	}
}
