package hr.fer.zemris.optjava.dz9.NSGA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz9.MOOPProblem;
import hr.fer.zemris.optjava.dz9.Functions.IFunction;

public class NSGA {

	// optimizacijski problem koji se rjesava
	private MOOPProblem problem;
	// velicina populacije
	private int populationSize;
	// dimenzionalnost pojedine jedinke
	private int dimension;
	// vrsta udaljenosti izmedju jedinki
	private String distanceString;
	// prag dijeljenje fitnesa
	private double sigmaShare;
	// konstanta koja daje oblik funkcije dijeljenja fitnesa
	private double alpha;
	// paretove fronte
	private List<List<double[]>> fronts;

	// minimumi i maksimumi pojedinih komponenata trenutne populacije
	private double[] min;
	private double[] max;
	// konstanta kojojm se mnozi najmanje fitness trenutne fronte i prosljedjuje
	// sljedecoj
	private double p = 0.8;

	// iznos dobrote pojedine jedinke za svaku od optimizacijskih funkcija
	private List<double[]> functionValues;

	private List<double[]> fitness;

	private List<Double> sortedFitness;

	private Random rand;

	public NSGA(MOOPProblem problem, int populationSize, String distanceString, double sigmaShare, double alpha) {
		this.problem = problem;
		this.populationSize = populationSize;
		this.dimension = problem.getDimension();
		this.distanceString = distanceString;
		this.sigmaShare = sigmaShare;
		this.alpha = alpha;
		this.rand = new Random();

		this.min = new double[this.dimension];
		this.max = new double[this.dimension];
	}

	public void run() {

		List<double[]> population = this.makePopulation();
		this.evaluatePopulation(population);

//		for(int i = 0; i < population.size(); i++) {
//			System.out.println(Arrays.toString(population.get(i)));
//		}

//		System.out.println("Function values");
//		for (int i = 0; i < this.functionValues.size(); i++) {
//			System.out.println(Arrays.toString(this.functionValues.get(i)));
//		}
//		System.out.println();
//
//		System.out.println();
		this.makeFronts(population);

		this.fitness = this.calcuateFitness();
		
		System.out.println("populacija");
		for(int i = 0; i < population.size(); i++) {
			System.out.println(Arrays.toString(population.get(i)));
		}
		System.out.println();

		
		System.out.println("fronte");
		
		for(int i = 0; i < fronts.size(); i++) {
			System.out.println("fronta: " + i);
			for(int j = 0; j < fronts.get(i).size(); j++) {
				System.out.println("\t" + Arrays.toString(fronts.get(i).get(j)));
			}
			
		}
		
		
		
		
		System.out.println("fitness");
		for (int i = 0; i < fitness.size(); i++) {
			System.out.println(Arrays.toString(fitness.get(i)));
		}

		List<double[]> populationSorted = this.populationSort();
		System.out.println();
		System.out.println("population sorted");
		
		for(int i = 0; i < populationSorted.size(); i++) {
			
			System.out.println(Arrays.toString(populationSorted.get(i)));
		}

//		System.out.println("front0");
//		for(int i = 0; i < fronts.get(0).size(); i++) {
//			System.out.println(Arrays.toString(fronts.get(0).get(i)));
//		}
//		System.out.println();
//		
//		System.out.println("dorted front 0");
//		List<double[]> sortedFront = this.frontSort(fronts.get(0), fitness.get(0));
//		for(int i = 0; i < sortedFront.size(); i++) {
//			System.out.println(Arrays.toString(sortedFront.get(i)));
//		}

	}

	/**
	 * Funkcija za stvaranje inicijalne populacije jedinki
	 * 
	 * @return lista jedinki
	 */
	public List<double[]> makePopulation() {

		// lista stvorenih jedinki
		List<double[]> population = new ArrayList<>();

		List<double[]> constraints = new ArrayList<>();

		// dohvacanje ogranicenja funkcija
		for (IFunction f : problem.getObjectiveFunctions()) {
			constraints.add(f.getConstraints());
		}

		for (int i = 0; i < this.populationSize; i++) {

			double[] point = new double[this.dimension];

			for (int j = 0; j < this.dimension; j++) {

				// ogranicenja za svaku komponentu rjesenja
				double min = constraints.get(j)[0];
				double max = constraints.get(j)[1];

				point[j] = min + (max - min) * rand.nextDouble();
			}

			population.add(point);
		}

		return population;
	}

	/**
	 * Funkcija za evaluaciju populacije
	 * 
	 * @param population populacija koja se evaluira
	 */
	public void evaluatePopulation(List<double[]> population) {

		Arrays.fill(this.min, Double.MAX_VALUE);
		Arrays.fill(this.max, Double.MIN_VALUE);

		this.functionValues = new ArrayList<>();

		for (int i = 0; i < this.populationSize; i++) {

			this.functionValues.add(this.problem.evaluate(population.get(i)));

			// odredjivanje minimuma i maksimuma pojedine komponente trenutne populacije
			for (int j = 0; j < this.dimension; j++) {
				if (population.get(i)[j] > this.max[j]) {
					this.max[j] = population.get(i)[j];
				}

				if (population.get(i)[j] < this.min[j]) {
					this.min[j] = population.get(i)[j];
				}
			}

		}
	}

	/**
	 * Funkcija za stvaranje paretovih fronta
	 * 
	 * @param population populacija kojoj se fronte stvaraju
	 */
	public void makeFronts(List<double[]> population) {

		// lista jedinki kojima dominira trenutna jedinka
		List<List<Integer>> dominates = new ArrayList<>();

		// stvaranje lista dominacije za svaku jedinku
		for (int i = 0; i < this.populationSize; i++) {
			dominates.add(new ArrayList<>());
		}

		// broj jedinki koje dominiraju trenutnom jedinkom
		List<Integer> isDominated = new ArrayList<>();
		for (int i = 0; i < this.populationSize; i++) {
			isDominated.add(0);
		}

		// lista svih fronti
		fronts = new ArrayList<>();

		for (int i = 0; i < this.populationSize; i++) {

			// vrijednosti funkcija tenutno promatrane jedinke
			double[] funcVal_i = this.functionValues.get(i);

			for (int j = 0; j < this.populationSize; j++) {
				if (i == j)
					continue;

				// prmomijeni se u false ako rjesenje ne dominira nad nekim drugim
				boolean flag = true;

				double[] funcVal_j = this.functionValues.get(j);

				for (int k = 0; k < this.dimension; k++) {

					if (funcVal_i[k] > funcVal_j[k]) {
						flag = false;
					}
				}

				// ako jedinka dominira nad nekom jedinom poveca se brojac
				if (flag) {
					isDominated.set(j, isDominated.get(j) + 1);
					dominates.get(i).add(j);
				}

			}
		}

		// broj jedinki koje su rasporedjene u fronte
		int addedPoints = 0;
		while (addedPoints < this.populationSize) {

			// trenutna fronta
			List<double[]> front = new ArrayList<>();

			for (int i = 0; i < population.size(); i++) {

				// ako neka jedinka nije dominirana nekom drugom, doda se u frontu
				if (isDominated.get(i) == 0) {

					// -1 su vec dodane jedinke, odnosno one se preskac u sljedecim iteracijama
					isDominated.set(i, -1);
					front.add(population.get(i));
					addedPoints++;
				}
			}

			// za svaku jedinku u fronti
			for (double[] d : front) {

				int index = population.indexOf(d);

				for (int j : dominates.get(index)) {

					// za svaku jedinku u fronti se pogleda skup jedinki kojima ona dominira,
					// a zatim se tim jedinkama umanji brojac jedinki koje njima dominiraju
					isDominated.set(j, isDominated.get(j) - 1);
				}
			}

			fronts.add(front);
		}
//		System.out.println("fronte");
//		for (int i = 0; i < fronts.size(); i++) {
//			System.out.println("fronta: " + i);
//			for (int j = 0; j < fronts.get(i).size(); j++) {
//				System.out.println("\t" + Arrays.toString(fronts.get(i).get(j)) + " " + Arrays.toString(problem.evaluate(fronts.get(i).get(j))));
//
//			}
//
//		}
	}

	/**
	 * Funkcija za izracun udaljenosti dvije tocke Ta udaljenost moze biti u
	 * prostoru rjesenja ili u prostoru kriterijskih funkcija
	 * 
	 * @param p1 prva tocka
	 * @param p2 druga tocka
	 * @return udaljenost tih tocaka
	 */
	public double distance(double[] p1, double[] p2) {

		if (this.distanceString == "decision-space") {

			double distance = 0;

			for (int i = 0; i < this.dimension; i++) {

				distance += Math.pow((p1[i] - p2[i]) / (this.max[i] - this.min[i]), 2);
			}

			return Math.sqrt(distance);

		} else {
			return 0;
		}

	}

	/**
	 * Funkcija za izracun udaljenosti izedju tocaka neke fronte
	 * 
	 * @param front fronta za koju se racunaju udaljenosti
	 * @return matrica udaljenosti gdje svaki i-ti redak predstavlja udaljenosti do
	 *         ostalih tocaka
	 */
	public double[][] calculateDistances(List<double[]> front) {

		double[][] distances = new double[front.size()][front.size()];

		for (int i = 0; i < front.size(); i++) {
			for (int j = 0; j < front.size(); j++) {
				// tocka je od sebe udaljena 0
				if (i == j) {
					distances[i][j] = 0;
				} else {
					distances[i][j] = this.distance(front.get(i), front.get(j));
				}
			}
		}
		return distances;
	}

	/**
	 * Funkcija za izracun gudtoce nise za pojedinu jedinku fronte
	 * 
	 * @param front fronta za koju se izracunava
	 * @return polje gustoca nisa
	 */
	public double[] calculateNiecheCount(List<double[]> front) {

		double[][] distances = this.calculateDistances(front);

		double[] niecheCount = new double[front.size()];
		Arrays.fill(niecheCount, 0);

		for (int i = 0; i < front.size(); i++) {

			for (int j = 0; j < front.size(); j++) {

				if (distances[i][j] < this.sigmaShare) {

					// funkcija dijeljenja
					niecheCount[i] += 1 - Math.pow(distances[i][j] / this.sigmaShare, 2);
				}
			}
		}

		return niecheCount;
	}

	/**
	 * Funkcija za izracun fitnesa pojedine fronte
	 * 
	 * @param front fronta ciji se fitnes racuna
	 * @return polje fitnesa pojedine jedinke u fronti
	 */
	public double[] calculateFrontFitness(List<double[]> front, double fitness) {

		double[] frontFitness = new double[front.size()];
		Arrays.fill(frontFitness, fitness);

		double[] niecheCount = this.calculateNiecheCount(front);

		for (int i = 0; i < front.size(); i++) {
			frontFitness[i] /= niecheCount[i];
		}

		return frontFitness;
	}

	/**
	 * Funkcija za pronalazak minimalnog fitnesa neke fronte
	 * 
	 * @param frontFitness polje fitnesa neke fronte
	 * @return najmanji fitnes
	 */
	public double findMinFitness(double[] frontFitness) {

		double minFitness = frontFitness[0];

		for (int i = 0; i < frontFitness.length; i++) {
			if (frontFitness[i] < minFitness) {
				minFitness = frontFitness[i];
			}
		}
		return minFitness;
	}

	/**
	 * Funkcija za izracun fitnesa svih fronti
	 * 
	 * @return lista fitnesa pojedine fronte
	 */
	public List<double[]> calcuateFitness() {

		List<double[]> fitness = new ArrayList<>();

		// fitnes koji se prosljedjuje sljedecoj fronti kao mmaksimalni fitness koji se
		// dodjeljuje svakoj jedinci fronte
		double nextFitness = this.fronts.get(0).size();

		for (int i = 0; i < this.fronts.size(); i++) {

			double[] frontFitness = this.calculateFrontFitness(this.fronts.get(i), nextFitness * this.p);

			nextFitness = this.findMinFitness(frontFitness);

			fitness.add(frontFitness);

		}

		return fitness;
	}

	/**
	 * Funkcija za odabir indeksa s maksimalnim fitnesom
	 * 
	 * @param frontFitness trenutno promatrana fronta
	 * @return indeks s maksimalnim fitnesom
	 */
	public int maxIndex(double[] frontFitness) {
		int maxAt = 0;

		for (int i = 0; i < frontFitness.length; i++) {
			maxAt = frontFitness[i] > frontFitness[maxAt] ? i : maxAt;
		}
		return maxAt;
	}

	/**
	 * Funkcija za sortiranje jedinki u fronti prema njihovom fitnesu
	 * 
	 * @param front fronta koja se sortira
	 * @param frontFitness fitness konkretne fronte
	 */
	public List<double[]> frontSort(List<double[]> front, double[] frontFitness) {

		double[] fitnessCopy = frontFitness.clone();
		List<double[]> sortedFront = new ArrayList<>();

		for (int i = 0; i < front.size(); i++) {

			int maxIndex = this.maxIndex(fitnessCopy);
			sortedFront.add(front.get(maxIndex));
			fitnessCopy[maxIndex] = -1;
		}

		return sortedFront;
	}

	/**
	 * Funkcija koja fitness svake fronte stavi u listu i sortira padajuce
	 * 
	 */
	public void sortFitness() {
		
		this.sortedFitness = new ArrayList<>();

		for (int i = 0; i < this.fitness.size(); i++) {

			for (int j = 0; j < this.fitness.get(i).length; j++) {

				sortedFitness.add(this.fitness.get(i)[j]);
			}
		}

		Collections.sort(sortedFitness, Collections.reverseOrder());
	}

	public List<double[]> populationSort() {
		
		List<double[]> sortedPopulation = new ArrayList<>();
		
		for(int i = 0; i < this.fronts.size(); i++) {
			
			sortedPopulation.addAll(this.frontSort(this.fronts.get(i), this.fitness.get(i)));
		}
		
		return sortedPopulation;
	}
	
//	public double[] proportionateSelection() {
//		
//		
//		
//		return 0;
//	}

}
