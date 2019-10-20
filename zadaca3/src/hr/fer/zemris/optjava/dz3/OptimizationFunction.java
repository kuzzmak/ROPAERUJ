package hr.fer.zemris.optjava.dz3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class OptimizationFunction implements IFunction{
	
	private String path;
	private Matrix matrixData;
	
	public OptimizationFunction(String path) {
		this.path = path;
		this.matrixData = getCoefficients(this.path);
	}
	
	public Matrix getMatrixData() {
		return matrixData;
	}
	
	public static Matrix getCoefficients(String path) {

		int numOfVariables = 6;
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

		double[][] matrixData = new double[numOfRows][numOfVariables];
		// punjenje matrice matrixData iz liste u kojoj su koeficijenti
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfVariables; j++) {
				matrixData[i][j] = Double.parseDouble(coeffs.get(i)[j].trim());
			}
		}
		return new Matrix(matrixData);
	}
	
	
	
	
	@Override
	public double valueAt(double[] v) {
		
		double[] value = new double[matrixData.getRowDimension()];
		
		for(int i = 0; i < matrixData.getRowDimension(); i++) {
			value[i] += v[0] * matrixData.get(i, 0)
					+ v[1] * Math.pow(matrixData.get(i, 0), 3) * matrixData.get(i, 1)
					+ v[2] * Math.pow(Math.E, v[3] * matrixData.get(i, 2)) * (1 + Math.cos(v[4] * matrixData.get(i, 3)))
					+ v[5] * matrixData.get(i, 3) * Math.pow(matrixData.get(i, 4), 2);
		}
			
		double functionValue = 0;
		for(int i = 0; i < matrixData.getRowDimension(); i++) {
			functionValue += Math.pow(matrixData.get(i, matrixData.getColumnDimension() - 1) - value[i], 2);
		}
		return functionValue / matrixData.getRowDimension();
	}

}
