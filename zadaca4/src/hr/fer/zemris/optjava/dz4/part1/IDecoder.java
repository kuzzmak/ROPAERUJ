package hr.fer.zemris.optjava.dz4.part1;

public interface IDecoder<T> {
	double[] decode(T obj);
	void decode(double[] v, T obj);
}
