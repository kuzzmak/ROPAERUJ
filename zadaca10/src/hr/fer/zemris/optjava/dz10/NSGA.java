package hr.fer.zemris.optjava.dz10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
	private List<List<Double[]>> fronts;
	// minimumi i maksimumi pojedinih komponenata trenutne populacije
	private Double[] min;
	private Double[] max;
	// konstanta kojojm se mnozi najmanje fitness trenutne fronte i prosljedjuje
	// sljedecoj
	private double p = 0.8;
	// vjerojatnost mutacije
	private double m = 0.05;
	// alpha kod BLXa krizanja
	private double alphaB = 0.1;
	// iznos dobrote pojedine jedinke za svaku od optimizacijskih funkcija
	private List<Double[]> functionValues;
	// fitnes vrijednosti svake jednke po fronti
	private List<Double[]> fitness;
	// sortiran fitnes populacije padajuce
	private List<Double> sortedFitness;
	// sortirana trenutna populacija po fitnesu
	private List<Double[]> populationSorted;

	private List<Double[]> constraints;

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

		this.min = new Double[this.dimension];
		this.max = new Double[this.dimension];
	}

	public List<Double[]> getFunctionValues() {
		return functionValues;
	}

	public List<Double> getSortedFitness() {
		return sortedFitness;
	}

	public List<Double[]> getPopulationSorted() {
		return populationSorted;
	}

	public List<List<Double[]>> run() {

		int currentIteration = 0;

		// inicijalna populacija
		List<Double[]> population = this.makePopulation();

		// evaluacija inicijalne populacije
		this.evaluatePopulation(population);
		// stvaranje fronta inicijalne populacije
		this.makeFronts(population);
		// odredjivanje fitnesa inicjalne populacije
		this.fitness = this.calcuateFitness();
		// sortiranje fitnesa padajuce
		this.sortFitness();
		// sortiranje populacije prema fitnesu
		this.populationSorted = this.populationSort();

		while (currentIteration < this.maxIterations) {

			// novo stvorena djeca
			List<Double[]> children = this.makeNewPopulation(population);
			// unija djece i inicijalne populacije
			population.addAll(children);
			// evaluacija cijele populacije
			this.evaluatePopulation(population);
			// stvaranje fronta cijele populacije
			this.makeFronts(population);
			// nova populacija za sljedecu iteraciju algoritma
			List<Double[]> newPopulation = new ArrayList<>();

			// brojac za trenutnu frontu
			int i = 0;

			List<Double[]> cda = this.crowdingDistanceAssignment();

			// dodavanje jedinki iz fronta sve do kada ima mjesta smjestiti cijelu frontu u
			// novu populaciju
			while (newPopulation.size() < this.populationSize) {
				// ako ima mjesta dodaju se jedinke iz sljedece fronte
				if (newPopulation.size() + fronts.get(i).size() < this.populationSize) {
					newPopulation.addAll(fronts.get(i));
					i++;
				} else { // nema mjesta za sve jedinke

					// broj jedinki koji nedostaje do potpune populacije
					int populationNeeded = this.populationSize - newPopulation.size();

					// sortirana fronta na kojoj je zapelo prema crowding udaljenosti
					List<Double[]> sortedFrontByCD = this.frontSort(fronts.get(i), cda.get(i));

					for (int j = 0; j < populationNeeded; j++) {
						newPopulation.add(sortedFrontByCD.get(j));
					}
				}
			}
			
			children = this.makeChildren(newPopulation, population, cda);
			population = new ArrayList<>(newPopulation);
			currentIteration++;
			
			System.out.println("current iteration: " + currentIteration);
		}
		return fronts;
	}
	
	/**
	 * Funkcija za turnirsku selekciju roditelja iz populacije u koju su vec dodane 
	 * najbolje jedinke. Odaberu se dvije jedinke iz populacije, odredi se njihov rang(fronta)
	 * pomocu liste fronti i usporedi. Ako je jednak rang obje jedinke, dohvaca se njihova crowding
	 * udaljenost koja je prije toga izracunata za svaku frontu pa se onda prema tome usporede.
	 * 
	 * @param newPopulation populacije iz koje se biraju jedinke
	 * @param cda lista crowding udaljenosti za svaku frontu
	 * @return odabrana jedinka
	 */
	public Double[] binaryTournament(List<Double[]> newPopulation, List<Double[]> cda) {
		
		Set<Integer> selectedIndexes = new HashSet<>();
		
		int selectedIndex = this.rand.nextInt(newPopulation.size());
		selectedIndexes.add(selectedIndex);
		
		Double[] child1 = newPopulation.get(selectedIndex);
		
		selectedIndex = this.rand.nextInt(newPopulation.size());
		
		while(selectedIndexes.contains(selectedIndex)) {
			selectedIndex = this.rand.nextInt(newPopulation.size());
		}
		
		Double[] child2 = newPopulation.get(selectedIndex);
		
		int rank1 = this.findFrontIndex(child1);
		int rank2 = this.findFrontIndex(child2);
		
		if(rank1 < rank2) return child1;
		if(rank2 < rank1) return child2;
		
		
		int child1IndexInFront = fronts.get(rank1).indexOf(child1);
		int child2IndexInFront = fronts.get(rank2).indexOf(child2);
		
		double crowdingDistance1 = cda.get(rank1)[child1IndexInFront];
		double crowdingDistance2 = cda.get(rank2)[child2IndexInFront];
		
		if(crowdingDistance1 > crowdingDistance2) return child1;
		return child2;
		
	}
	
	
	
	
	public List<Double[]> makeChildren(List<Double[]> newPopulation, List<Double[]> population, List<Double[]> cda){
		
		List<Double[]> children = new ArrayList<>();
		
		while(children.size() < this.populationSize) {
			
			Double[] parent1 = this.binaryTournament(newPopulation, cda);

			Double[] parent2 = this.binaryTournament(newPopulation, cda);

			while (Arrays.equals(parent1, parent2)) {
				parent2 = this.binaryTournament(newPopulation, cda);
			}

			Double[] child = this.BLXa(parent1, parent2);

			this.mutate(child);

			while (children.contains(child) || population.contains(child)) {
				this.mutate(child);
			}

			children.add(child);
		}
		return children;
	}
	
	/**
	 * Funkcija za pronalazak fronte kojoj neko rjesenje pripada
	 * 
	 * @param chosen rjesenje za koje se pokusava naci fronta
	 * @return indeks fronte u kojoj se chosen nalazi
	 */
	public int findFrontIndex(Double[] chosen) {
		
		for(int i = 0; i < fronts.size(); i++) {
			
			if(fronts.get(i).contains(chosen)) return i;
		}
		return -1;
	}

	/**
	 * Funkcija za izracun crowding udaljenosti svih fronti
	 * 
	 * @return lista crowding udaljenosti pojedine fronte
	 */
	public List<Double[]> crowdingDistanceAssignment() {

		List<Double[]> crowdingDistances = new ArrayList<>();
		List<IFunction> functions = problem.getObjectiveFunctions();

		for (int i = 0; i < fronts.size(); i++) {

			// crowding udaljenosti pojedine fronte
			Double[] tempCrowding = new Double[fronts.get(i).size()];
			Arrays.fill(tempCrowding, 0.);

			for (int j = 0; j < functions.size(); j++) {

				// sortirana fronta prema kriterijskoj funkciji
				List<Double[]> sortedFront = this.sortByFunction(fronts.get(i), functions.get(j));

				// prvi i zadnji clan postavljaju se na beskonacno
				tempCrowding[0] = Double.POSITIVE_INFINITY;
				tempCrowding[tempCrowding.length - 1] = Double.POSITIVE_INFINITY;

				for (int k = 1; k < sortedFront.size() - 1; k++) {

					tempCrowding[k] = tempCrowding[k]
							+ (sortedFront.get(k + 1)[j] - sortedFront.get(k - 1)[j]) / (this.max[j] - this.min[j]);

				}
			}
			crowdingDistances.add(tempCrowding);
		}
		return crowdingDistances;
	}

	/**
	 * Funkcija za sortiranje pojedine fronte prema predanoj funkciji
	 * 
	 * @param front    fronta koja se sortira
	 * @param function funkcija prema kojoj se sortira fronta
	 */
	public List<Double[]> sortByFunction(List<Double[]> front, IFunction function) {

		List<Double[]> sortedFront = new ArrayList<>(front);

		Collections.sort(sortedFront, new Comparator<Double[]>() {

			@Override
			public int compare(Double[] arg0, Double[] arg1) {

				double v0 = function.valueAt(arg0, true);
				double v1 = function.valueAt(arg1, true);

				if (v0 > v1)
					return 1;
				if (v1 > v0)
					return -1;
				return 0;
			}
		});

		return sortedFront;
	}

	/**
	 * Funkcija za stvaranje inicijalne populacije jedinki
	 * 
	 * @return lista jedinki
	 */
	public List<Double[]> makePopulation() {

		// lista stvorenih jedinki
		List<Double[]> population = new ArrayList<>();

		for (int i = 0; i < this.populationSize; i++) {

			Double[] point = new Double[this.dimension];

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
	public void evaluatePopulation(List<Double[]> population) {

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
	public void makeFronts(List<Double[]> population) {

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
			Double[] funcVal_i = this.functionValues.get(i);

			for (int j = 0; j < population.size(); j++) {
				if (i == j)
					continue;

				// prmomijeni se u false ako rjesenje ne dominira nad nekim drugim
				boolean flag = true;

				Double[] funcVal_j = this.functionValues.get(j);

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
			List<Double[]> front = new ArrayList<>();

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
			for (Double[] d : front) {

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
	public double distance(Double[] p1, Double[] p2) {

		if (this.distanceString == "decision-space") {

			double distance = 0;

			for (int i = 0; i < this.dimension; i++) {

				distance += Math.pow((p1[i] - p2[i]) / (this.max[i] - this.min[i]), 2);
			}

			return Math.sqrt(distance);

		} else {

			double distance = 0;

			Double[] p1fv = problem.evaluate(p1);
			Double[] p2fv = problem.evaluate(p2);

			for (int i = 0; i < this.dimension; i++) {

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
	public Double[][] calculateDistances(List<Double[]> front) {

		Double[][] distances = new Double[front.size()][front.size()];

		for (int i = 0; i < front.size(); i++) {
			for (int j = 0; j < front.size(); j++) {
				// tocka je od sebe udaljena 0
				if (i == j) {
					distances[i][j] = (double) 0;
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
	public Double[] calculateNiecheCount(List<Double[]> front) {

		Double[][] distances = this.calculateDistances(front);

		Double[] niecheCount = new Double[front.size()];
		Arrays.fill(niecheCount, 0.);

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
	public Double[] calculateFrontFitness(List<Double[]> front, double fitness) {

		Double[] frontFitness = new Double[front.size()];
		Arrays.fill(frontFitness, fitness);

		Double[] niecheCount = this.calculateNiecheCount(front);

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
	public double findMinFitness(Double[] frontFitness) {

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
	public List<Double[]> calcuateFitness() {

		List<Double[]> fitness = new ArrayList<>();

		// fitnes koji se prosljedjuje sljedecoj fronti kao mmaksimalni fitness koji se
		// dodjeljuje svakoj jedinci fronte
		double nextFitness = this.fronts.get(0).size();

		for (int i = 0; i < this.fronts.size(); i++) {

			Double[] frontFitness = this.calculateFrontFitness(this.fronts.get(i), nextFitness * this.p);

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
	public int maxIndex(Double[] frontFitness) {
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
	public List<Double[]> frontSort(List<Double[]> front, Double[] frontFitness) {

		Double[] fitnessCopy = frontFitness.clone();
		List<Double[]> sortedFront = new ArrayList<>();

		for (int i = 0; i < front.size(); i++) {

			int maxIndex = this.maxIndex(fitnessCopy);
			sortedFront.add(front.get(maxIndex));
			fitnessCopy[maxIndex] = -1.;
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
	public List<Double[]> populationSort() {

		List<Double[]> sortedPopulation = new ArrayList<>();

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
	public Double[] proportionalSelection() {

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
	public Double[] BLXa(Double[] parent1, Double[] parent2) {

		Double[] child = parent1.clone();

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
	public void mutate(Double[] child) {

		for (int i = 0; i < child.length; i++) {

			if (this.m > rand.nextDouble()) {
				child[i] += rand.nextGaussian();
			}

			double min = this.constraints.get(i)[0];
			double max = this.constraints.get(i)[1];

			if (child[i] < min) {
				child[i] = min;
			}
			if (child[i] > max) {
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
	public List<Double[]> makeNewPopulation(List<Double[]> population) {

		List<Double[]> newPopulation = new ArrayList<>();

		while (newPopulation.size() < population.size()) {

			Double[] parent1 = this.proportionalSelection();

			Double[] parent2 = this.proportionalSelection();

			while (Arrays.equals(parent1, parent2)) {
				parent2 = this.proportionalSelection();
			}

			Double[] child = this.BLXa(parent1, parent2);

			this.mutate(child);

			while (newPopulation.contains(child) || population.contains(child)) {
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

		for (int i = 0; i < solution.length; i++) {

			double min = this.constraints.get(i)[0];
			double max = this.constraints.get(i)[1];

			if (solution[i] > max || solution[i] < min)
				return false;
		}

		return true;
	}
}
