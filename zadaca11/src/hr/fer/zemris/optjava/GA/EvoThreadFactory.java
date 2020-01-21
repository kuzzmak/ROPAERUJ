package hr.fer.zemris.optjava.GA;

import java.util.concurrent.ThreadFactory;

import hr.fer.zemris.optjava.rng.EVOThread;

public class EvoThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable arg0) {
		return new EVOThread(arg0);
	}

}
