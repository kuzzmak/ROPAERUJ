package hr.fer.zemris.optjava.dz9.NSGA;

import java.util.ArrayList;
import java.util.Arrays;
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
	// paretove fronte
	private List<List<double[]>> fronts;

	// iznos dobrote pojedine jedinke za svaku od optimizacijskih funkcija
	private List<double[]> functionValues;

	private Random rand;

	public NSGA(MOOPProblem problem, int populationSize) {
		this.problem = problem;
		this.populationSize = populationSize;
		this.dimension = problem.getDimension();
		this.rand = new Random();
	}

	public void run() {

		List<double[]> population = this.makePopulation();
		this.evaluatePopulation(population);

		System.out.println("Function values");
		for (int i = 0; i < this.functionValues.size(); i++) {
			System.out.println(Arrays.toString(this.functionValues.get(i)));
		}
		System.out.println();

		System.out.println();
		this.makeFronts(population);

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

		this.functionValues = new ArrayList<>();

		for (int i = 0; i < this.populationSize; i++) {

			this.functionValues.add(this.problem.evaluate(population.get(i)));

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
			for (double[] d: front) {

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
	
	

}
