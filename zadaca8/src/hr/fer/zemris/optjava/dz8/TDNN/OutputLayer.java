package hr.fer.zemris.optjava.dz8.TDNN;

import java.util.Arrays;

public class OutputLayer implements NeuralNetLayer {

	private Neuron[] neurons;
	
	public OutputLayer(Neuron[] neurons) {
		this.neurons = neurons;
	}
	
	@Override
	public int getNumOfNeurons() {
		return neurons.length;
	}

	@Override
	public Neuron[] getNeurons() {
		return neurons;
	}

	@Override
	public IActivationFunction getActivationFunction() {
		return new TangentHyperbolic();
	}

	@Override
	public String toString() {
		return "OutputLayer [neurons=" + Arrays.toString(neurons) + "]";
	}

}
