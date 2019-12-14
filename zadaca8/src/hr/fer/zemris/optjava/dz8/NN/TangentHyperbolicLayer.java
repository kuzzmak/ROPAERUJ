package hr.fer.zemris.optjava.dz8.NN;

import java.util.Arrays;

public class TangentHyperbolicLayer implements NeuralNetLayer {

	private Neuron[] neurons;
	
	public TangentHyperbolicLayer(Neuron[] neurons) {
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
		return "TangentHyperbolicLayer [neurons=" + Arrays.toString(neurons) + "]";
	}
	
}
