package hr.fer.zemris.optjava.dz7.NN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.ANNTrainer.Dataset;
import hr.fer.zemris.optjava.dz7.ANNTrainer.Sample;

public class NeuralNet {

	private static double[] weights;
	private static double[] nets;
	// indeks pojedinog kromosoma
	private static int neuronId = 0;
	private static Dataset data;

	private static List<NeuralNetLayer> layers = new ArrayList<>();

	private Random rand;

	public NeuralNet(int[] architecture, Dataset data) {

		NeuralNet.data = data;
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

				NeuralNet.layers.add(new InputLayer(neurons));

			} else if (i == architecture.length - 1) {
				// dodavanje izlaznog sloja
				Neuron[] neurons = new Neuron[architecture[architecture.length - 1]];
				for (int j = 0; j < architecture[architecture.length - 1]; j++) {
					neurons[j] = new Neuron(neuronId);
					neuronId++;
				}

				NeuralNet.layers.add(new OutputLayer(neurons));

			} else {
				// dodavanje skrivenih slojeva
				Neuron[] neurons = new Neuron[architecture[i]];
				for (int k = 0; k < architecture[i]; k++) {
					neurons[k] = new Neuron(neuronId);
					neuronId++;
				}

				NeuralNet.layers.add(new SigmoidalLayer(neurons));
			}
		}

		// izracun ukupnog broja tezina
		for (int i = 0; i < layers.size() - 1; i++) {
			numOfWeights += (layers.get(i).getNumOfNeurons() + 1) * layers.get(i + 1).getNumOfNeurons();
		}

		NeuralNet.weights = new double[numOfWeights];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = rand.nextDouble();
		}

	}

	public static double[] calculateNet(double[] x) {
		
		// izlazi iz pojedinih neurona
		nets = new double[neuronId];

		Neuron[] inputNeurons = layers.get(0).getNeurons();
		IActivationFunction inputFunction = layers.get(0).getActivationFunction();

		for (int i = 0; i < inputNeurons.length; i++) {
			nets[inputNeurons[i].getId()] = inputFunction.valueAt(x[i]);
		}
		
		// pomocna varijabla za dohvacanje pojedinih tezina
		int numOfWeight = 0;
		
		// za svaki sloj izmedju ulaznog i izlaznog
		for (int i = 1; i < layers.size(); i++) {
			
			// neuroni speceificni za svaki sloj mreze, od prvog pa nadalje
			Neuron[] layerNeurons = layers.get(i).getNeurons();
			// aktivacijska funkcija pojedinog sloja
			IActivationFunction layerFunction = layers.get(i).getActivationFunction();
			
			// za svaki neuron u sloju
			for(int k = 0; k < layerNeurons.length; k++) {
				
				Neuron n = layerNeurons[k];
				// neuroni iz prijasnjeg sloja mreze
				Neuron[] neuronsFromLayerBefore = layers.get(i - 1).getNeurons();
				// vrijednost kmulativne sume prije propustanja kroz aktivacijsku funkciju
				double value = 0;
				// mnozenje tezina s neuronima iz prethodnog sloja
				for(int j = 0; j < neuronsFromLayerBefore.length; j++) {
				
					value += nets[neuronsFromLayerBefore[j].getId()] * weights[numOfWeight];
					numOfWeight++;
				}
				// dodavanje biasa
				value += weights[numOfWeight];
				// propustanje kroz aktivacijsku funkciju
				nets[n.getId()] = layerFunction.valueAt(value);
				numOfWeight++;
			}

		}
		
		// broj neurona u zadnjem sloju
		int numOfOutputs = layers.get(layers.size() - 1).getNumOfNeurons();
		// vracanje zadnjih numOfOuptpus net vrijednosti
		return Arrays.copyOfRange(nets, nets.length - numOfOutputs, nets.length);
	}

	public static double calculateError() {
		
		double error = 0;
		
		for(Sample s: data.getData()) {
			
			double[] predicted = calculateNet(s.getX());
			double[] output = s.getY();
			error += Math.pow(predicted[0] - output[0], 2) +
					Math.pow(predicted[1] - output[1], 2) + 
					Math.pow(predicted[2] - output[2], 2);
			
			
		}
		
		return 1. / data.size() * error;
	}
	

	public double[] getWeights() {
		return weights;
	}
	

	public void setWeights(double[] weights) {
		NeuralNet.weights = weights;
	}
	

	public static int getNumOfWeights() {
		return weights.length;
	}
	
	
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder("");
		sb.append("NeuralNet\n");
		for (int i = 0; i < layers.size(); i++) {
			sb.append(layers.get(i).toString()).append("\n");
		}
		return sb.toString();

	}

}
