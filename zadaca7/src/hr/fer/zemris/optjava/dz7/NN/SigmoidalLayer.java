package hr.fer.zemris.optjava.dz7.NN;


public class SigmoidalLayer implements NeuralNetLayer {
	
	Neuron[] neurons;
	
	public SigmoidalLayer(int size) {
		neurons = new Neuron[size];
		for(int i = 0; i < size; i++) {
			this.neurons[i] = new Neuron();
		}
	}

	@Override
	public int getNumOfNeurons() {
		return neurons.length;
	}

	@Override
	public Neuron[] getNeurons() {
		return neurons;
	}

}
