package hr.fer.zemris.optjava.rng.rngimpl;

import java.util.Random;

import hr.fer.zemris.optjava.rng.IRNG;

public class RNGRandomImpl implements IRNG{
	
	private Random rand;
	
	public RNGRandomImpl() {
		this.rand = new Random();
	}

	@Override
	public double nextDouble() {
		return this.rand.nextDouble();
	}

	@Override
	public double nextDouble(double min, double max) {
		
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		
		return this.rand.nextDouble() * (max - min) + min;
	}

	@Override
	public float nextFloat() {
		return this.rand.nextFloat();
	}

	@Override
	public float nextFloat(float min, float max) {
		
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		
		return this.rand.nextFloat() * (max - min) + min;
	}

	@Override
	public int nextInt() {
		return this.rand.nextInt();
	}

	@Override
	public int nextInt(int min, int max) {
		
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		
		return this.rand.nextInt((max - min) + 1) + min;
	}

	@Override
	public boolean nextBoolean() {
		return this.rand.nextBoolean();
	}

	@Override
	public double nextGaussian() {
		return this.rand.nextGaussian();
	}

}
