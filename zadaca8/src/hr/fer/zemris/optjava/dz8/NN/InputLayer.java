package hr.fer.zemris.optjava.dz8.NN;

import java.util.Arrays;

public class InputLayer implements NeuralNetLayer {
	
	Neuron[] neurons;
	
	public InputLayer(Neuron[] neurons) {
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
		return new Linear();
	}

	@Override
	public String toString() {
		return "InputLayer [neurons=" + Arrays.toString(neurons) + "]";
	}
	
	

}
