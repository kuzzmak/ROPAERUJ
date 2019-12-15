package hr.fer.zemris.optjava.dz8.TDNN;

public interface NeuralNetLayer {

	public int getNumOfNeurons();
	public Neuron[] getNeurons();
	public IActivationFunction getActivationFunction();
	
}
