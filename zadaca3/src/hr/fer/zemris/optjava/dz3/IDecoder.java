package hr.fer.zemris.optjava.dz3;

public interface IDecoder<T> {
	double[] decode(T obj);
	void decode(double[] v, T obj);
}
