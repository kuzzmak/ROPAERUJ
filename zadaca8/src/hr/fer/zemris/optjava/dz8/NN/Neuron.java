package hr.fer.zemris.optjava.dz8.NN;

public class Neuron {
	
	private int id;
	private double output = 0;
	
	public Neuron(int id) {
		this.id = id;
	}
	
	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Neuron [id=" + id + "]";
	}
	
}
