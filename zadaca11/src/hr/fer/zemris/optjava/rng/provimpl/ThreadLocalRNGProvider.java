package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.RNG;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

public class ThreadLocalRNGProvider implements IRNGProvider{

	private ThreadLocal<IRNG> threadLocal = ThreadLocal.withInitial(() -> {return new RNGRandomImpl();}); 
	
	@Override
	public IRNG getRNG() {
		return threadLocal.get();
	}

}
