package hr.fer.zemris.numeric;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class Sustav {
	
	public static Matrix getCoefficients(String path) {
		
		double[][] matrixData = new double[10][11];
		List<String[]> coeffs = new ArrayList<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line = br.readLine();
		    
		    while (line != null) {
		    	if(!line.startsWith("[")) {
		    		line = br.readLine();
		    		continue;
		    	}
		    	
		    	line = line.replace("[", "");
		    	line = line.replace("]", "");
		    	
		    	coeffs.add(line.split(","));
		    	line = br.readLine();
		    }
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 11; j++) {
				matrixData[i][j] = Double.parseDouble(coeffs.get(i)[j].trim());
			}
		}
		return new Matrix(matrixData);
	}
	
	
	
	public static void main(String[] args) {
		Matrix m = getCoefficients("02-zad-sustav.txt");
		System.out.println(m.getColumnDimension());
	}
}
