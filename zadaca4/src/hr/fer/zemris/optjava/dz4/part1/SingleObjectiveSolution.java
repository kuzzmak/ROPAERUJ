package hr.fer.zemris.optjava.dz4.part1;

public abstract class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution>{
	
	private double fitness;
	private double value;
	
	public SingleObjectiveSolution() {
		// TODO Auto-generated constructor stub
	}

	public abstract SingleObjectiveSolution newLikeThis();
	
	public abstract SingleObjectiveSolution duplicate();
	
	public void setValue(double value, boolean minimize) {
		this.value = value;
		this.fitness = minimize ? -value : value;
	}
	
	
	
	@Override
	public int compareTo(SingleObjectiveSolution obj){
		return Double.compare(fitness, obj.fitness);
	}
	
	public double getFitness() {
		return fitness;
	}
}
