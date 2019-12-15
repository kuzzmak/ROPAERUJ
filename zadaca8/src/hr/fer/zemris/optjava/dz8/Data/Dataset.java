package hr.fer.zemris.optjava.dz8.Data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dataset {

	private static String path;
	private static int window;
	private static int numOfSamples;
	private static List<Sample> data;
	private static double min = Double.MAX_VALUE;
	private static double max = Double.MIN_VALUE;
	
	
	/**
	 * Konstruktor razreda 
	 * 
	 * @param path staza do datoteke u kojoj su podatci
	 * @param window velicina vremenskog prozora koji se prosljedjuje na ulaz mreze 
	 * @param numOfSamples broj uzoraka koji se koriste za ucenje
	 */
	public Dataset(String path, int window, int numOfSamples) {
		Dataset.path = path;
		Dataset.window = window;
		Dataset.numOfSamples = numOfSamples;
	}
	
	
	public static void load() {
		
		List<Double> rawData = new ArrayList<>();
		
		// ucitavanje podataka u listu
		try(BufferedReader reader = new BufferedReader(new FileReader(path))){
					
					String line = reader.readLine();
					
					while(line != null) {
						
						line = line.trim();
						
						double value = Double.parseDouble(line);
						
						if(value < min) min = value;
						if(value > max) max = value;
								
						rawData.add(value);
						
						line = reader.readLine();
					}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// normalizacija podatka		
		List<Double> normalizedData = new ArrayList<>();
		
		for(int i = 0; i < rawData.size(); i++) {
			
			double normValue = ((rawData.get(i) - min) / (max - min)) * 2 - 1;
			normalizedData.add(normValue);
			
		}
		
		data = new ArrayList<>();
		
		for(int i = 0; i < numOfSamples; i++) {
			
			double[] x = new double[window];
			double[] y = new double[] {normalizedData.get(i + window)};

			for(int j = 0; j < window; j++) {
			
				x[j] = normalizedData.get(i + j);
				
			}
			
			data.add(new Sample(x, y));
		}
		
	}
	
	public static List<Sample> getData(){
		return data;
	}
	
	public static void print() {
		data.forEach(System.out::println);
	}
	
	public static int size() {
		return data.size();
	}


	public static double getMin() {
		return min;
	}


	public static double getMax() {
		return max;
	}
	
}
