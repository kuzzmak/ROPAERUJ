package hr.fer.zemris.optjava.dz7.CLONALG;

import java.util.Arrays;
import java.util.Random;

public class Antigene implements Comparable<Antigene>{

	private int sizeOfAntigene;
	private double functionValue;
	private double affinity;
	
	private double[] value;
	private Random rand;
	
	public Antigene(int sizeOfAntigene) {
		this.sizeOfAntigene = sizeOfAntigene;
		this.rand = new Random();
		value = new double[this.sizeOfAntigene];
		for(int i = 0; i < value.length; i++) {
			this.value[i] = rand.nextDouble();
		}
	}
	

	public Antigene(double[] value) {
		this.value = value;
		this.sizeOfAntigene = this.value.length;
	}
	
	
	public Antigene copy() {
		Antigene ag = new Antigene(this.value);
		ag.setAffinity(this.affinity);
		ag.setFunctionValue(this.functionValue);
		return ag;
	}
	
	public double getFunctionValue() {
		return functionValue;
	}


	public void setFunctionValue(double functionValue) {
		this.functionValue = functionValue;
	}


	public double getAffinity() {
		return affinity;
	}


	public void setAffinity(double affinity) {
		this.affinity = affinity;
	}


	public double[] getValue() {
		return value;
	}


	public void setValue(double[] value) {
		this.value = value;
	}


	public int getSizeOfAntigene() {
		return sizeOfAntigene;
	}


	@Override
	public String toString() {
		return "Antigene [value=" + Arrays.toString(value) + "]";
	}


	@Override
	public int compareTo(Antigene ag) {
		
		if(ag.getFunctionValue() < this.getFunctionValue()) return 1;
		if(ag.getFunctionValue() > this.getFunctionValue()) return -1;
		return 0;
		
	}
	
	
}
