package hr.fer.zemris.optjava.dz12.test;

import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Util;

public class Test2 {

	public static void main(String[] args) {

		int populationSize = 10;
		int maxDepth = 5;
		Random rand = new Random();
		
		List<DefaultMutableTreeNode> population = Util.makePopulation(populationSize, maxDepth, rand);
		
		population.forEach(System.out::println);
	}
}
