package hr.fer.zemris.optjava.dz6;

public class City {

	private double xCoordinate;
	private double yCoordinate;
	private int index;
	
	public City(double xCoordinate, double yCoordinate, int index) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.index = index;
	}
	
	/**
	 * Funkcija za racunanje euklidske udaljenosti izmedju dva grada.
	 * 
	 * @param c1 prvi grad
	 * @param c2 drugi grad
	 * @return udljanost izmedju dva grada
	 */
	public static double euclideanDistanceTo(City c1, City c2) {
		return Math.sqrt(Math.pow(c1.xCoordinate - c2.xCoordinate, 2) + 
				Math.pow(c1.yCoordinate - c2.yCoordinate, 2));
	}

	@Override
	public String toString() {
		return "City [xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + ", index=" + index + "]";
	}

	public int getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		long temp;
		temp = Double.doubleToLongBits(xCoordinate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yCoordinate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (index != other.index)
			return false;
		if (Double.doubleToLongBits(xCoordinate) != Double.doubleToLongBits(other.xCoordinate))
			return false;
		if (Double.doubleToLongBits(yCoordinate) != Double.doubleToLongBits(other.yCoordinate))
			return false;
		return true;
	}
}
