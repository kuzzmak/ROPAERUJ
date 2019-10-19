package hr.fer.zemris.optjava.dz3;

public class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution>{
	
	private double fitness;
	private double value;
	
	public SingleObjectiveSolution() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(SingleObjectiveSolution obj){
		return Double.compare(fitness, obj.fitness);
	}
}
