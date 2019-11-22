package hr.fer.zemris.optjava.dz7.ANNTrainer;

import hr.fer.zemris.optjava.dz7.CLONALG.CLONALG;
import hr.fer.zemris.optjava.dz7.DATA.Dataset;
import hr.fer.zemris.optjava.dz7.NN.NeuralNet;

public class ANNTrainer {

	public static void main(String[] args) {
		
		//String path = "data\\07-iris-formatirano.data";
		String path = args[0];
		String algName = args[1];
		int populationSize = Integer.parseInt(args[2]);
		double merr = Double.parseDouble(args[3]);
		int maxIter = Integer.parseInt(args[4]);
		
		Dataset data = new Dataset(path);
		int[] architecture = new int[] {4,5,3};
		NeuralNet nn = new NeuralNet(architecture, data);
		
		if(algName.startsWith("pso")) {
			
			char version = algName.charAt(4);
			
			if(version == 'a') {
				
			}else {
				
			}
			
		}else {
			int d = 5;
			double beta = 5;
			CLONALG alg = new CLONALG(nn, populationSize, d, beta);	
			nn.setWeights(alg.run());
			System.out.println("classification error: " + nn.classificationError());
		}
		
		//int populationSize = 40;
		//PSO alg = new PSO(populationSize, nn.getNumOfWeights(), nn);
		//nn.setWeights(alg.run());
		
//		System.out.println("classification error: " + nn.classificationError());
	
//		double[] prediction = nn.predict(new double[] {5.1,3.5,1.4,0.2});
//		System.out.println("prediction1: " + Arrays.toString(prediction));
//		prediction = nn.predict(new double[] {4.9,3.0,1.4,0.2});
//		System.out.println("prediction2: " + Arrays.toString(prediction));
//		prediction = nn.predict(new double[] {6.3,3.3,6.0,2.5});
//		System.out.println("prediction3: " + Arrays.toString(prediction));
//		prediction = nn.predict(new double[] {6.9,3.1,4.9,1.5}); //010
//		System.out.println("prediction3: " + Arrays.toString(prediction));
//		prediction = nn.predict(new double[] {6.1,2.8,4.0,1.3}); //010
//		System.out.println("prediction3: " + Arrays.toString(prediction));
//		prediction = nn.predict(new double[] {7.7,3,6.1,2.3}); //001
//		System.out.println("prediction3: " + Arrays.toString(prediction));
	
	}
	
}
