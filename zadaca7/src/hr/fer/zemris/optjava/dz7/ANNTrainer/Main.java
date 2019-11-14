package hr.fer.zemris.optjava.dz7.ANNTrainer;

public class Main {

	public static void main(String[] args) {
		
		String path = "data\\07-iris-formatirano.data";
		Dataset data = new Dataset(path);
		
		System.out.println(data.size());
		
		
	}
}
