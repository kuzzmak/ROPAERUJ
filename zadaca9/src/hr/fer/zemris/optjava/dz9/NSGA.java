package hr.fer.zemris.optjava.dz9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NSGA {

	// optimizacijski problem koji se rjesava
	private MOOPProblem problem;
	// velicina populacije
	private int populationSize;
	// broj iteracija nakonkojih algoritam staje
	private int maxIterations;
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
	// vjerojatnost mutacije
	private double m = 0.05;
	// alpha kod BLXa krizanja
	private double alphaB = 0.1;
	// iznos dobrote pojedine jedinke za svaku od optimizacijskih funkcija
	private List<double[]> functionValues;
	// fitnes vrijednosti svake jednke po fronti
	private List<double[]> fitness;
	// sortiran fitnes populacije padajuce
	private List<Double> sortedFitness;
	// sortirana trenutna populacija po fitnesu
	private List<double[]> populationSorted;

	private List<double[]> constraints;

	private Random rand;

	public NSGA(MOOPProblem problem, int populationSize, int maxIterations, String distanceString, double sigmaShare,
			double alpha) {
		this.problem = problem;
		this.populationSize = populationSize;
		this.maxIterations = maxIterations;
		this.dimension = problem.getDimension();
		this.distanceString = distanceString;
		this.sigmaShare = sigmaShare;
		this.alpha = alpha;
		this.rand = new Random();

		this.constraints = new ArrayList<>();

		// dohvacanje ogranicenja funkcija
		for (IFunction f : problem.getObjectiveFunctions()) {
			this.constraints.add(f.getConstraints());
		}

		this.min = new double[this.dimension];
		this.max = new double[this.dimension];
	}

	
	public List<double[]> getFunctionValues() {
		return functionValues;
	}


	public List<Double> getSortedFitness() {
		return sortedFitness;
	}


	public List<double[]> getPopulationSorted() {
		return populationSorted;
	}


	public List<List<double[]>> run() {

		int currentIteration = 0;

		List<double[]> population = this.makePopulation();

		while (currentIteration < this.maxIterations) {

			this.evaluatePopulation(population);

			this.makeFronts(population);

			this.fitness = this.calcuateFitness();

			this.sortFitness();
			
			this.populationSorted = this.populationSort();
			
			population = new ArrayList<>(this.makeNewPopulation(population));
			
			System.out.println("current iteration: " + currentIteration);
			currentIteration++;
		}
		
		this.evaluatePopulation(population);

		this.makeFronts(population);
		
		this.fitness = this.calcuateFitness();

		this.sortFitness();
		
		this.populationSorted = this.populationSort();
		
		return fronts;
	}

	/**
	 * Funkcija za stvaranje inicijalne populacije jedinki
	 * 
	 * @return lista jedinki
	 */
	public List<double[]> makePopulation() {

		// lista stvorenih jedinki
		List<double[]> population = new ArrayList<>();

		for (int i = 0; i < this.populationSize; i++) {

			double[] point = new double[this.dimension];

			for (int j = 0; j < this.dimension; j++) {

				// ogranicenja za svaku komponentu rjesenja
				double min = this.constraints.get(j)[0];
				double max = this.constraints.get(j)[1];

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

		for (int i = 0; i < population.size(); i++) {

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
		for (int i = 0; i < population.size(); i++) {
			dominates.add(new ArrayList<>());
		}

		// broj jedinki koje dominiraju trenutnom jedinkom
		List<Integer> isDominated = new ArrayList<>();
		for (int i = 0; i < population.size(); i++) {
			isDominated.add(0);
		}

		// lista svih fronti
		fronts = new ArrayList<>();

		for (int i = 0; i < population.size(); i++) {

			// vrijednosti funkcija tenutno promatrane jedinke
			double[] funcVal_i = this.functionValues.get(i);

			for (int j = 0; j < population.size(); j++) {
				if (i == j)
					continue;

				// prmomijeni se u false ako rjesenje ne dominira nad nekim drugim
				boolean flag = true;

				double[] funcVal_j = this.functionValues.get(j);

				for (int k = 0; k < this.dimension; k++) {

					if (funcVal_i[k] >= funcVal_j[k]) {
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

		int addedPoints = 0;
		while (addedPoints < population.size()) {

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
			
			double distance = 0;
			
			double[] p1fv = problem.evaluate(p1);
			double[] p2fv = problem.evaluate(p2);
					
			for(int i = 0; i < this.dimension; i++) {
				
				distance += Math.pow(p1fv[i] - p2fv[i], 2);
			}
			
			return Math.sqrt(distance);
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
					niecheCount[i] += 1 - Math.pow(distances[i][j] / this.sigmaShare, this.alpha);
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
	 * @param front        fronta koja se sortira
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

	/**
	 * Funkcija koja svaku frontu sortira po fitnesu i dodaje u novu listu gdje su
	 * jedinke sortirane silazno po fitnesu
	 * 
	 * @return lista sortiranih jedinki
	 */
	public List<double[]> populationSort() {

		List<double[]> sortedPopulation = new ArrayList<>();

		for (int i = 0; i < this.fronts.size(); i++) {

			sortedPopulation.addAll(this.frontSort(this.fronts.get(i), this.fitness.get(i)));
		}

		return sortedPopulation;
	}

	/**
	 * Funkcija za odabir roditelja pomocu proporcionalne selekcije
	 * 
	 * @return roditelj
	 */
	public double[] proportionalSelection() {

		// vjerojatnost koju treba preci da se izabere jedinka
		double p_sel = rand.nextDouble();
		// suma kojom se dijeli svaki fitnes radi normalizacije
		double sum = 0;

		List<Double> tempFitness = new ArrayList<>();
		tempFitness.addAll(this.sortedFitness);

		for (int i = 0; i < tempFitness.size(); i++) {

			sum += tempFitness.get(i);
		}

		// zbroj svih fitnesa je 1
		for (int i = 0; i < tempFitness.size(); i++) {
			tempFitness.set(i, tempFitness.get(i) / sum);
		}

		double s = 0;
		for (int i = 0; i < tempFitness.size(); i++) {

			s += tempFitness.get(i);
			if (s >= p_sel)
				return this.populationSorted.get(i);
		}
		return this.populationSorted.get(populationSorted.size() - 1);
	}

	/**
	 * Funkcija za krizanje dva roditelja metodom BLXa
	 * 
	 * @param parent1 prvi roditelj
	 * @param parent2 drugi roditelj
	 * @return dijete
	 */
	public double[] BLXa(double[] parent1, double[] parent2) {

		double[] child = parent1.clone();

		for (int i = 0; i < child.length; i++) {

			double di = Math.abs(parent1[i] - parent2[i]);

			double lower = Math.min(parent1[i], parent2[i]) - alphaB * di;
			double upper = Math.max(parent1[i], parent2[i]) + alphaB * di;

			double u = rand.nextDouble() * (upper - lower) + lower;

			child[i] = u;

		}

		return child;
	}

	/**
	 * Funkcija za mutaciju jedinke
	 * 
	 * @param child jedinka koja se mutira
	 */
	public void mutate(double[] child) {

		for (int i = 0; i < child.length; i++) {

			if (this.m > rand.nextDouble()) {
				child[i] += rand.nextGaussian();
			}
			
			double min = this.constraints.get(i)[0];
			double max = this.constraints.get(i)[1];
			
			if(child[i] < min){
				child[i] = min;
			}
			if(child[i] > max){
				child[i] = max;
			}
		}
	}

	/**
	 * Funkcija za stvaranje nove populacije
	 * 
	 * @param population trenutna populacija
	 * @return nova populacija
	 */
	public List<double[]> makeNewPopulation(List<double[]> population){
		
		List<double[]> newPopulation = new ArrayList<>();

		while (newPopulation.size() < population.size()) {
			
			double[] parent1 = this.proportionalSelection();

			double[] parent2 = this.proportionalSelection();

			while (Arrays.equals(parent1, parent2)) {
				parent2 = this.proportionalSelection();
			}

			double[] child = this.BLXa(parent1, parent2);
			
			this.mutate(child);
			
			while(newPopulation.contains(child)) {
				this.mutate(child);
			}
			
			newPopulation.add(child);
		}
		
		return newPopulation;
	}
	
	/**
	 * Funkcija za provejru je li neko rjesenje zadovoljivo
	 * 
	 * @param solution rjesenje koje se provjerva
	 * @return true ili false
	 */
	public boolean isFeasible(double[] solution) {
		
		for(int i = 0; i < solution.length; i++) {
			
			double min = this.constraints.get(i)[0];
			double max = this.constraints.get(i)[1];
			
			if(solution[i] > max || solution[i] < min) return false;
		}
		
		return true;
	}
}
