package hr.fer.zemris.optjava.dz7.NN;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.optjava.dz7.ANNTrainer.Dataset;

public class NeuralNet {

	private double[] weights;
	private int[] architecture;
	
	private NeuralNetLayer inputLayer;
	private List<NeuralNetLayer> hiddenLayers;
	private NeuralNetLayer outputLayer;
	
	public NeuralNet(int[] architecture, Dataset data) {
		this.architecture = architecture; 	
	}
	
	public NeuralNet(NeuralNetLayer inputLayer, List<NeuralNetLayer> hiddenLayers, NeuralNetLayer outputLayer) {
		this.inputLayer = inputLayer;
		this.hiddenLayers = hiddenLayers;
		this.outputLayer = outputLayer;
	}
	
	public void addLayer(NeuralNetLayer layer) {
		this.layers.add(layer);
	}
	
	
}
