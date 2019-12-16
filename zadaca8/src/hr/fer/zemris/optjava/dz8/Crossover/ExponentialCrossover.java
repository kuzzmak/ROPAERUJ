package hr.fer.zemris.optjava.dz8.Crossover;

import java.util.Random;

public class ExponentialCrossover implements ICrossover {
	
	private static double Cr;
	private static Random rand;
	
	
	public ExponentialCrossover(double Cr) {
		ExponentialCrossover.Cr = Cr;
		ExponentialCrossover.rand = new Random();
	}

	@Override
	public double[] cross(double[] mutantVector, double[] targetVector) {
		
		// vektor dobiven krizanjem vektora mutanta i ciljnog vektora
		double[] crossed = new double[targetVector.length];
		// indeks na kojem se kopira vrijednost ciljnog vektora u krizani i nakon kojeg
		// se s vjerojatnoscu Cr kopira doticna vrijednost
		int index = rand.nextInt(targetVector.length);
		// kopiranje prvih index vrijednosti ciljnog vektora u krizani
		for(int i = 0; i < index; i++) {
			crossed[i] = targetVector[i];
		}
	
		crossed[index] = mutantVector[index];
		// indeks nakon kojeg se prvi puta pojavila vjerojatnost veca od Cr, nakon
		// ovog indeksa se kopiraju vrijednosti opet iz ciljnog vektora u krizani vektor
		int index2 = index;
		// s vjerojatnoscu Cr se kopiraju vrijednosti iz mutiranog vektora u krizani vektor
		for(int i = index + 1; i < targetVector.length; i++) {
			if(rand.nextDouble() < Cr) {
				crossed[i] = mutantVector[i];
				index2 = i;
			}else {
				break;
			}
		}
		
		for(int i = index2; i < targetVector.length; i++) {
			crossed[i] = targetVector[i];
		}
		return crossed;
	}

}
