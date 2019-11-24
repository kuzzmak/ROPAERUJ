package hr.fer.zemris.optjava.dz7.ANNTrainer;

import hr.fer.zemris.optjava.dz7.CLONALG.CLONALG;
import hr.fer.zemris.optjava.dz7.DATA.Dataset;
import hr.fer.zemris.optjava.dz7.NN.NeuralNet;
import hr.fer.zemris.optjava.dz7.PSO.PSO;

public class ANNTrainer {

	public static void main(String[] args) {
		// put do datoteke
		String path = args[0];
		// algoritam optimizacije
		String algName = args[1];
		// velicina populacije
		int populationSize = Integer.parseInt(args[2]);
		// minimalna prihvatljiva greska
		double minError = Double.parseDouble(args[3]);
		// najveci dozvoljeni broj iteracija
		int maxIterations = Integer.parseInt(args[4]);
		
		Dataset data = new Dataset(path);
		// izgled slojeva mreze
		int[] architecture = new int[] {4,5,3};
		NeuralNet nn = new NeuralNet(architecture, data);
		
		if(algName.startsWith("pso")) {
			// a ili b inacica PSO algoritma
			// a -> globalno susjedstvo
			// b -> lokalno susjedstvo
			char version = algName.charAt(4);
			
			if(version == 'a') {
				PSO alg = new PSO(populationSize, nn, maxIterations, minError, version);
				nn.setWeights(alg.run());
				System.out.println("classification error: " + nn.classificationError());
			}else {
				int numOfNeighbours = Integer.parseInt(algName.split("-")[2]);
				PSO alg = new PSO(populationSize, nn, maxIterations, minError, version, numOfNeighbours);
				nn.setWeights(alg.run());
				System.out.println("classification error: " + nn.classificationError());
			}
			
		}else {
			// broj novo ubacenih antigena
			int d = (int) (populationSize * 0.05);
			// konstanta za ogranicavanje kolicine klonova
			double beta = 5;
			CLONALG alg = new CLONALG(nn, populationSize, d, beta, maxIterations, minError);	
			nn.setWeights(alg.run());
			System.out.println("classification error: " + nn.classificationError());
		}
	}
	
}
