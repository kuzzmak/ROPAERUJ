package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;

public class ThreadLocalRNGProvider implements IRNGProvider{

	private ThreadLocal<IRNG> threadLocal = new ThreadLocal<>();
	
	@Override
	public IRNG getRNG() {
		return 
	}

}
