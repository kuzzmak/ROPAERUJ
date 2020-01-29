package hr.fer.zemris.optjava.dz12.test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Util;
import hr.fer.zemris.optjava.dz12.Expression.Expression;
import hr.fer.zemris.optjava.dz12.Function.IF;
import hr.fer.zemris.optjava.dz12.Function.PR2;
import hr.fer.zemris.optjava.dz12.Function.PR3;

public class Test2 {

	static List<Expression> functions = Arrays.asList(new IF(), new PR2(), new PR3());
	
	
	public static void main(String[] args) {

		int populationSize = 10;
		int maxDepth = 10;
		Random rand = new Random();
		int maxNodes = 10;
		
		
		List<DefaultMutableTreeNode> population = Util.makePopulation(populationSize, 6, rand, maxNodes);
		
		population.forEach(System.out::println);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(functions.get(0).duplicate());
		
		
		
		
		Util.full(root, maxDepth, rand, maxNodes);
		
		System.out.println(root);
		
		System.out.println(Util.numberOfNodes(root));
		
		Util.mutate(root, maxDepth, rand, 0.3f, maxNodes);
		
		System.out.println(root);
		System.out.println(Util.numberOfNodes(root));
	}
}
