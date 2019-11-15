package hr.fer.zemris.optjava.dz7.NN;

public interface NeuralNetLayer {

	public int getNumOfNeurons();
	public Neuron[] getNeurons();
	public IActivationFunction getActivationFunction();
	
}
