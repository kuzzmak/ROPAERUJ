package hr.fer.zemris.optjava.dz7.NN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.ANNTrainer.Dataset;

public class NeuralNet {

	private double[] weights;
	private double[] nets;
	private int[] architecture;
	private int neuronId = 0;

//	private NeuralNetLayer inputLayer;
//	private List<NeuralNetLayer> hiddenLayers;
//	private NeuralNetLayer outputLayer;

	private List<NeuralNetLayer> layers = new ArrayList<>();

	private Random rand;

	public NeuralNet(int[] architecture, Dataset data) {

		this.architecture = architecture;
//		this.hiddenLayers = new ArrayList<>();
		this.rand = new Random();

		int numOfWeights = 0;

		for (int i = 0; i < architecture.length; i++) {

			if (i == 0) {
				// dodavanje ulaznog sloja
				Neuron[] neurons = new Neuron[architecture[0]];
				for (int j = 0; j < architecture[0]; j++) {
					neurons[j] = new Neuron(neuronId);
					neuronId++;
				}

//				this.inputLayer = new InputLayer(neurons);
				this.layers.add(new InputLayer(neurons));

			} else if (i == architecture.length - 1) {
				// dodavanje izlaznog sloja
				Neuron[] neurons = new Neuron[architecture[architecture.length - 1]];
				for (int j = 0; j < architecture[architecture.length - 1]; j++) {
					neurons[j] = new Neuron(neuronId);
					neuronId++;
				}

//				this.outputLayer = new OutputLayer(neurons);
				this.layers.add(new OutputLayer(neurons));

			} else {
				// dodavanje skrivenih slojeva
				Neuron[] neurons = new Neuron[architecture[i]];
				for (int k = 0; k < architecture[i]; k++) {
					neurons[k] = new Neuron(neuronId);
					neuronId++;
				}

//				hiddenLayers.add(new SigmoidalLayer(neurons));
				this.layers.add(new SigmoidalLayer(neurons));
			}
		}

		// izracun ukupnog broja tezina
		for (int i = 0; i < layers.size() - 1; i++) {
			numOfWeights += (layers.get(i).getNumOfNeurons() + 1) * layers.get(i + 1).getNumOfNeurons();
		}

		this.weights = new double[numOfWeights];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = rand.nextDouble();
		}

	}

	public double[] calculateNet(double[] x) {
		this.nets = new double[neuronId + 1];

		// polje na izlazu mreze
		double[] net = new double[layers.get(layers.size() - 1).getNumOfNeurons()];

		Neuron[] inputNeurons = layers.get(0).getNeurons();
		IActivationFunction inputFunction = layers.get(0).getActivationFunction();

		for (int i = 0; i < inputNeurons.length; i++) {
			net[inputNeurons[i].getId()] = inputFunction.valueAt(x[i]);
		}

		for (int i = 1; i < layers.size() - 1; i++) {
			
			Neuron[] hiddenNeurons = layers.get(i).getNeurons();
			IActivationFunction hiddenFunction = layers.get(i).getActivationFunction();
			
			for(int j = 0; j < layers.get(i - 1).getNumOfNeurons(); j++) {
				
				net[]
				
			}

		}

		return net;
	}

//	public NeuralNet(NeuralNetLayer inputLayer, List<NeuralNetLayer> hiddenLayers, NeuralNetLayer outputLayer) {
//		this.inputLayer = inputLayer;
//		this.hiddenLayers = hiddenLayers;
//		this.outputLayer = outputLayer;
//	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder("");
		sb.append("NeuralNet\n");
//		sb.append(inputLayer).append("\n");
		for (int i = 0; i < layers.size(); i++) {
			sb.append(layers.get(i).toString()).append("\n");
		}
//		sb.append(outputLayer).append("\n");
		return sb.toString();

	}

//	public void addLayer(NeuralNetLayer layer) {
//		this.layers.add(layer);
//	}

}
