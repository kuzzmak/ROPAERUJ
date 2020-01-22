package hr.fer.zemris.optjava.rng;

import hr.fer.zemris.optjava.GA.Evaluator;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

public class EVOThread extends Thread implements IRNGProvider {

	private IRNG rng = new RNGRandomImpl();
	private Evaluator evaluator;

	public EVOThread() {
	}
	
	public EVOThread(Runnable target, Evaluator evaluator) {
		super(target);
		this.evaluator = evaluator;
	}

	public EVOThread(Runnable target) {
		super(target);
	}

	public EVOThread(String name) {
		super(name);
	}

	public EVOThread(ThreadGroup group, Runnable target) {
		super(group, target);
	}

	public EVOThread(ThreadGroup group, String name) {
		super(group, name);
	}

	public EVOThread(Runnable target, String name) {
		super(target, name);
	}

	public EVOThread(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
	}

	public EVOThread(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
	}

	@Override
	public IRNG getRNG() {
		return rng;
	}
	
	public Evaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(Evaluator evaluator) {
		this.evaluator = evaluator;
	}
}
