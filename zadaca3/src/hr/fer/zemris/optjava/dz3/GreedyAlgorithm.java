package hr.fer.zemris.optjava.dz3;

public class GreedyAlgorithm<T extends SingleObjectiveSolution> implements IOptAlgorithm<SingleObjectiveSolution>{

	private IDecoder<T> decoder;
	private INeighbourhood<T> neighbourhood;
	private T startWith;
	private IFunction function;
	private boolean minimize;
	
	private final int MAX_ITER = 50000;
	
	
	public GreedyAlgorithm(IDecoder<T> decoder, INeighbourhood<T> neighbourhood, T startWith, IFunction function,
			boolean minimize) {
		super();
		this.decoder = decoder;
		this.neighbourhood = neighbourhood;
		this.startWith = startWith;
		this.function = function;
		this.minimize = minimize;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T run() {

		T solution = (T)startWith.newLikeThis();
		solution.setValue(function.valueAt(decoder.decode(startWith)), minimize);
		
		for(int i = 0; i < MAX_ITER; i++) {
			 T neighbour = neighbourhood.randomNeighbour(solution);
			 neighbour.setValue(function.valueAt(decoder.decode(neighbour)), minimize);
			 System.out.println(solution.getFitness());
			 if(neighbour.getFitness() > solution.getFitness()) solution = neighbour;
		}
		return solution;
	}
}
