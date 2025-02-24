package hr.fer.zemris.optjava.dz8.ElmanNN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz8.Data.Dataset;
import hr.fer.zemris.optjava.dz8.Data.Sample;

public class ElmanNeuralNet {

	// tezine neurona
	private static double[] weights;
	// izlazi iz pojedinih neurona mreze
	private double[] nets;
	// indeks pojedinog kromosoma
	private int neuronId = 0;
	// podatci za ucenje mreze
	private Dataset data;
	// lista svih slojeva mreze
	private List<NeuralNetLayer> layers = new ArrayList<>();
	
	private Random rand;
	
	private double[] context;
	private int numOfContextWeights;

	public ElmanNeuralNet(int[] architecture, Dataset data) {

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

				this.layers.add(new TangentHyperbolicLayer(neurons));
			}
		}

		// broj kontekstnih neurona
		int numOfContextNeurons = layers.get(1).getNumOfNeurons();
		
		// izracun ukupnog broja tezina
		for (int i = 0; i < this.layers.size() - 1; i++) {
			numOfWeights += (this.layers.get(i).getNumOfNeurons() + 1) * this.layers.get(i + 1).getNumOfNeurons();
		}

		// broj kontekstnih tezina
		numOfContextWeights = this.layers.get(1).getNumOfNeurons() * this.layers.get(1).getNumOfNeurons();
		this.context = new double[numOfContextNeurons];
		Arrays.fill(this.context, 0);
		numOfWeights += numOfContextWeights;
		
		ElmanNeuralNet.weights = new double[numOfWeights];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = this.rand.nextDouble();
		}
	}

	
	/**
	 * Funkcija za izracun pravog izlaza iz mreze
	 * 
	 * @param x	vektor za koji se racuna izlaz
	 * @return izlaz mreze
	 */
	public double[] calculateNet(double[] x) {
		
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
			
			// neuroni specificni za svaki sloj mreze, od prvog pa nadalje
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
				// za svaki sloj koji nije prvo
				if(i != 1) {
					this.nets[n.getId()] = layerFunction.valueAt(value);
				}else {
					// za prvi sloj se pribroje vrijednosti iz prethodnog konteksta 
					// pomnozene sa svojim tezinama te se propuste kroz aktivacijsku funkciju
					for(int j = 0; j < layers.get(1).getNumOfNeurons(); j++) {
						// tezine kontekstnih neurona su na kraju polja tezina
						value += this.context[n.getId() - inputNeurons.length] * this.weights[this.weights.length - numOfContextWeights + j];
					}
					// zapis izlaza odredjenog neurona
					this.nets[n.getId()] = layerFunction.valueAt(value);
					// azuriranje konteksta odredjenog neurona za sljedecu iteraciju
					this.context[n.getId() - inputNeurons.length] = this.nets[n.getId()];
				}
				numOfWeight++;
			}
		}
		
		// broj neurona u zadnjem sloju
		int numOfOutputs = layers.get(layers.size() - 1).getNumOfNeurons();
		// vracanje zadnjih numOfOuptpus net vrijednosti
		return Arrays.copyOfRange(this.nets, this.nets.length - numOfOutputs, this.nets.length);
	}
	

	/**
	 * Funkcija za izracun srednje kvadratne greske
	 * 
	 * @return iznos greske
	 */
	public double calculateError() {
		
		double error = 0;
		
		for(Sample s: this.data.getData()) {
			
			// izlaz mreze za pojedini uzorak
			double[] predicted = calculateNet(s.getX());
			// ispravan izlaz
			double[] output = s.getY();
			for(int i = 0; i < output.length; i++) {
				error += Math.pow(predicted[i] - output[i], 2);
			}
		}
		
		return 1. / this.data.size() * error;
	}
	
	
	/**
	 * Funkcija za izracun izlaza iz mreze
	 * Primjer: izlaz mreze je (0.2,0.03,0.78), a ova funkcija 
	 * to prebacuje u pogodniji zapis koji karakterizira pojedini razred -> (0,0,1)
	 * 
	 * @param x ulazni vektor
	 * @return binarni uzorak
	 */
	public double[] predict(double[] x) {
		
		double[] net = this.calculateNet(x);
		double[] prediction = new double[net.length];
		for(int i = 0; i < net.length; i++) {
			prediction[i] = net[i] >= 0.5 ? 1 : 0;
		}
		return prediction;
	}
	
	/**
	 * Funkcija za izracun postotka pogresno klasificiranih uzoraka
	 * 
	 * @return postotak pogreske
	 */
	public double classificationError() {
		double wronglyClassified = 0;
		
		for(int i = 0; i < data.size(); i++) {
			double[] prediction = this.predict(data.getData().get(i).getX());
			if(!Arrays.equals(prediction, data.getData().get(i).getY())) wronglyClassified++;
		}
		
		return wronglyClassified / data.size();
	}

	public double[] getWeights() {
		return weights;
	}
	

	public void setWeights(double[] weights) {
		ElmanNeuralNet.weights = weights;
	}
	

	public int getNumOfWeights() {
		return weights.length;
	}
	
	
	public double[] getContext() {
		return context;
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
