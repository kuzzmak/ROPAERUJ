package hr.fer.zemris.optjava.dz8;

import java.util.List;

public class ANNTrainer {

	public static void main(String[] args) {
		
		String path = "data\\08-Laser-generated-data.txt";
					  
		int window = 4;
		int numOfSamples = 600;
		
		Dataset dataset = new Dataset(path, window, numOfSamples);
		Dataset.load();
		List<Sample> data = Dataset.getData();
		Dataset.print();
	}
}
