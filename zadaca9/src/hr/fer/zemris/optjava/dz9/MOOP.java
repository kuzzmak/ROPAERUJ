package hr.fer.zemris.optjava.dz9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.optjava.dz9.Functions.IFunction;
import hr.fer.zemris.optjava.dz9.NSGA.NSGA;

public class MOOP {
	
	private static List<double[]> functionValues;
	private static List<List<Integer>> fronts;

	public static void main(String[] args) {

//		IFunction f1 = new IFunction() {
//
//			@Override
//			public double valueAt(double[] point, boolean minimize) {
//				if(minimize) return point[0];
//				return -point[0];
//			}
//
//			@Override
//			public int getDimension() {
//				return 2;
//			}
//
//			@Override
//			public double[] getConstraints() {
//
//				return new double[] { 0.1, 1 };
//			}
//		};
//
//		IFunction f2 = new IFunction() {
//
//			@Override
//			public double valueAt(double[] point, boolean minimize) {
//				if(minimize) return (1 + point[1]) / point[0];
//				return -(1 + point[1]) / point[0];
//			}
//
//			@Override
//			public int getDimension() {
//				return 2;
//			}
//
//			@Override
//			public double[] getConstraints() {
//
//				return new double[] { 0, 5 };
//			}
//		};
//		
//		MOOPProblem problem1 = new Problem1();
//		problem1.add(f1);
//		problem1.add(f2);
//		
//		int populationSize = 10;
//		NSGA nsga = new NSGA(problem1, populationSize);
//		nsga.run();
		functionValues = new ArrayList<>();
		functionValues.add(new double[] {6,4});
		functionValues.add(new double[] {5,2});
		functionValues.add(new double[] {4,1});
		functionValues.add(new double[] {3,3});
		functionValues.add(new double[] {2,2});
		makeFronts();

	}

	public static void makeFronts() {

		// lista jedinki kojima dominira trenutna jedinka
		List<List<Integer>> dominates = new ArrayList<>();
		// stvaranje lista dominacije za svaku jedinku
		for (int i = 0; i < 5; i++) {
			dominates.add(new ArrayList<>());
		}
		// broj jedinki koje dominiraju trenutnom jedinkom
		List<Integer> isDominated = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			isDominated.add(0);
		}

		fronts = new ArrayList<>();
		
		for (int i = 0; i < 5; i++) {

			// vrijednosti funkcija tenutno promatrane jedinke
			double[] funcVal_i = functionValues.get(i);

			for (int j = 0; j < 5; j++) {
				if (i == j)
					continue;

				// prmomijeni se u false ako rjesenje ne dominira nad nekim drugim
				boolean flag = true;

				double[] funcVal_j = functionValues.get(j);

				for (int k = 0; k < 2; k++) {

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

		System.out.println("isDominated " + isDominated);
		System.out.println();
		for (int i = 0; i < dominates.size(); i++) {
			System.out.println(dominates.get(i));
		}

		List<Integer> population = new ArrayList<>();
		population.add(1);
		population.add(2);
		population.add(3);
		population.add(4);
		population.add(5);
		
		int addedPoints = 0;
		int populationSize = population.size();
		while(addedPoints < populationSize) {
			
			List<Integer> front = new ArrayList<>();

			for(int i = 0; i < population.size(); i++) {
				
				
				if(isDominated.get(i) == 0) {
					
					front.add(population.get(i));
					addedPoints ++;
					
					for(Integer in: dominates.get(i)) {
						
						isDominated.set(in, isDominated.get(in) - 1);
					}
				}
				
			}
			
			for(Integer in: front) {
				isDominated.remove(population.indexOf(in));
				dominates.remove(population.indexOf(in));
				population.remove(in);
			}
			
			
			
			fronts.add(front);
		}
		System.out.println();
		System.out.println("fronte");
		for(int i = 0; i < fronts.size(); i++) {
			System.out.println(fronts.get(i));
		}
	}
	
}
