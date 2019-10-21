package hr.fer.zemris.optjava.dz3;

public class GeometricTempSchedule implements ITempSchedule{

	private double alpha;
	private double tInitial;
	private double tCurrent;
	private int innerLimit;
	private int outerLimit;
	private int k = 0;
	
	public GeometricTempSchedule(double alpha, double tInitial, int innerLimit, int outerLimit) {
		super();
		this.alpha = alpha;
		this.tInitial = tInitial;
		this.tCurrent = Math.pow(this.alpha, this.k) * this.tInitial;
		this.innerLimit = innerLimit;
		this.outerLimit = outerLimit;
	}

	@Override
	public double getNextTemperature() {
		this.k++;
		double nextTemp = Math.pow(this.alpha, this.k) * this.tInitial;
		this.tCurrent = nextTemp; 
		return nextTemp;
	}

	@Override
	public int getInnerLoopCounter() {
		return innerLimit;
	}

	@Override
	public int getOuterLoopCounter() {
		return outerLimit;
	}
}
