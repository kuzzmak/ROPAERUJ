package hr.fer.zemris.optjava.dz8;

import java.util.Arrays;
import java.util.List;

public class ANNTrainer {

	public static void main(String[] args) {
		
		String path = "data\\08-Laser-generated-data.txt";
					  
		int window = 4;
		int numOfSamples = 600;
		
//		Dataset dataset = new Dataset(path, window, numOfSamples);
//		Dataset.load();
//		List<Sample> data = Dataset.getData();
//		Dataset.print();
		
		int populationSize = 10;
		int dimension = 5;
		double minVal = 0;
		double maxVal = 1;
		
		
		List<double[]> weights = Util.makeInitialPopulation(populationSize, dimension, minVal, maxVal);
		
		for(int i = 0; i < populationSize; i++) {
			System.out.println(Arrays.toString(weights.get(i)));
		}
	}
}
