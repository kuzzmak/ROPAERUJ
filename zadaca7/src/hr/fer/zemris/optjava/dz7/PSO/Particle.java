package hr.fer.zemris.optjava.dz7.PSO;

import java.util.Arrays;

public class Particle {

	private double[] personalBest;
	private double[] currentLocation;
	private double[] currentSpeed;
	private double bestValue;
	
	public Particle(int dimension) {
		this.personalBest = new double[dimension];
		this.currentLocation = new double[dimension];
		this.currentSpeed = new double[dimension];
		this.bestValue = Double.MAX_VALUE;
	}

	public double[] getPersonalBest() {
		return personalBest;
	}

	public void setPersonalBest(double[] personalBest) {
		this.personalBest = personalBest;
	}

	public double[] getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(double[] currentLocation) {
		this.currentLocation = currentLocation;
	}

	public double[] getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(double[] currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public double getBestValue() {
		return bestValue;
	}

	public void setBestValue(double bestValue) {
		this.bestValue = bestValue;
	}

	@Override
	public String toString() {
		return "Particle [personalBest=" + Arrays.toString(personalBest) + ", currentLocation="
				+ Arrays.toString(currentLocation) + ", currentSpeed=" + Arrays.toString(currentSpeed) + "]";
	}
	
	
}
