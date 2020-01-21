package hr.fer.zemris.optjava.GA;

import hr.fer.zemris.optjava.GrayScaleImage.GrayScaleImage;

public interface IGAEvaluator<T> {
	public void evaluate(GASolution<T> p);
}
