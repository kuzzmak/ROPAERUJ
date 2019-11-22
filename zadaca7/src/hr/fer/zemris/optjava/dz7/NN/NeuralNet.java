package hr.fer.zemris.optjava.dz7.NN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.DATA.Dataset;
import hr.fer.zemris.optjava.dz7.DATA.Sample;

public class NeuralNet {

	private static double[] weights;
	private double[] nets;
	// indeks pojedinog kromosoma
	private int neuronId = 0;
	private Dataset data;

	private List<NeuralNetLayer> layers = new ArrayList<>();

	private Random rand;

	public NeuralNet(int[] architecture, Dataset data) {

		this.data = data;
		this.rand = new Random();

		int numOfWeights = 0;

		for (int i = 0; i < architecture.length; i++) {

			if (i == 0) {
				// dodavanje ulaznog sloja
				Neuron[] neurons = new Neuron[architecture[0]];
				for (int j = 0; j < architecture[0]; j++) {
					neurons[j] = new Neuron(this.neuronId);
					this.neuronId++;
				}

				this.layers.add(new InputLayer(neurons));

			} else if (i == architecture.length - 1) {
				// dodavanje izlaznog sloja
				Neuron[] neurons = new Neuron[architecture[architecture.length - 1]];
				for (int j = 0; j < architecture[architecture.length - 1]; j++) {
					neurons[j] = new Neuron(this.neuronId);
					this.neuronId++;
				}

				this.layers.add(new OutputLayer(neurons));

			} else {
				// dodavanje skrivenih slojeva
				Neuron[] neurons = new Neuron[architecture[i]];
				for (int k = 0; k < architecture[i]; k++) {
					neurons[k] = new Neuron(this.neuronId);
					this.neuronId++;
				}

				this.layers.add(new SigmoidalLayer(neurons));
			}
		}

		// izracun ukupnog broja tezina
		for (int i = 0; i < this.layers.size() - 1; i++) {
			numOfWeights += (this.layers.get(i).getNumOfNeurons() + 1) * this.layers.get(i + 1).getNumOfNeurons();
		}

		NeuralNet.weights = new double[numOfWeights];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = this.rand.nextDouble();
		}

	}

	public double[] calculateNet(double[] x) {
		
		// izlazi iz pojedinih neurona
		this.nets = new double[this.neuronId];

		Neuron[] inputNeurons = this.layers.get(0).getNeurons();
		IActivationFunction inputFunction = this.layers.get(0).getActivationFunction();

		for (int i = 0; i < inputNeurons.length; i++) {
			this.nets[inputNeurons[i].getId()] = inputFunction.valueAt(x[i]);
		}
		
		// pomocna varijabla za dohvacanje pojedinih tezina
		int numOfWeight = 0;
		
		// za svaki sloj izmedju ulaznog i izlaznog
		for (int i = 1; i < this.layers.size(); i++) {
			
			// neuroni speceificni za svaki sloj mreze, od prvog pa nadalje
			Neuron[] layerNeurons = this.layers.get(i).getNeurons();
			// aktivacijska funkcija pojedinog sloja
			IActivationFunction layerFunction = this.layers.get(i).getActivationFunction();
			
			// za svaki neuron u sloju
			for(int k = 0; k < layerNeurons.length; k++) {
				
				Neuron n = layerNeurons[k];
				// neuroni iz prijasnjeg sloja mreze
				Neuron[] neuronsFromLayerBefore = this.layers.get(i - 1).getNeurons();
				// vrijednost kmulativne sume prije propustanja kroz aktivacijsku funkciju
				double value = 0;
				// mnozenje tezina s neuronima iz prethodnog sloja
				for(int j = 0; j < neuronsFromLayerBefore.length; j++) {
				
					value += this.nets[neuronsFromLayerBefore[j].getId()] * weights[numOfWeight];
					numOfWeight++;
				}
				// dodavanje biasa
				value += weights[numOfWeight];
				// propustanje kroz aktivacijsku funkciju
				this.nets[n.getId()] = layerFunction.valueAt(value);
				numOfWeight++;
			}

		}
		
		// broj neurona u zadnjem sloju
		int numOfOutputs = layers.get(layers.size() - 1).getNumOfNeurons();
		// vracanje zadnjih numOfOuptpus net vrijednosti
		return Arrays.copyOfRange(this.nets, this.nets.length - numOfOutputs, this.nets.length);
	}

	public double calculateError() {
		
		double error = 0;
		
		for(Sample s: this.data.getData()) {
			
			double[] predicted = calculateNet(s.getX());
			double[] output = s.getY();
			error += Math.pow(predicted[0] - output[0], 2) +
					Math.pow(predicted[1] - output[1], 2) + 
					Math.pow(predicted[2] - output[2], 2);
			
			
		}
		
		return 1. / this.data.size() * error;
	}
	
	
	public double[] predict(double[] x) {
		
		double[] net = this.calculateNet(x);
		double[] prediction = new double[net.length];
		for(int i = 0; i < net.length; i++) {
			prediction[i] = net[i] >= 0.5 ? 1 : 0;
		}
		return prediction;
	}
	

	public double[] getWeights() {
		return weights;
	}
	

	public void setWeights(double[] weights) {
		NeuralNet.weights = weights;
	}
	

	public int getNumOfWeights() {
		return weights.length;
	}
	
	
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder("");
		sb.append("NeuralNet\n");
		for (int i = 0; i < layers.size(); i++) {
			sb.append(this.layers.get(i).toString()).append("\n");
		}
		return sb.toString();

	}

}
