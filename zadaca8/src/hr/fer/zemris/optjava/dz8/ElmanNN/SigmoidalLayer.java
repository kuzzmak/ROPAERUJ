package hr.fer.zemris.optjava.dz8.ElmanNN;

import java.util.Arrays;

public class SigmoidalLayer implements NeuralNetLayer {
	
	Neuron[] neurons;
	
	public SigmoidalLayer(Neuron[] neurons) {
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
		return new Sigmoid();
	}

	@Override
	public String toString() {
		return "SigmoidalLayer [neurons=" + Arrays.toString(neurons) + "]";
	}

}
