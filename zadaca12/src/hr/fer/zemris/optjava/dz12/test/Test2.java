package hr.fer.zemris.optjava.dz12.test;

import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Util;
import hr.fer.zemris.optjava.dz12.Function.PR3;

public class Test2 {

	public static int numberOfNodes(DefaultMutableTreeNode node) {
		
		int result = 1;
		
		Enumeration<DefaultMutableTreeNode> children = node.children();
		
		result += node.getChildCount();
		
		while(children.hasMoreElements()) {
			
			result += numberOfNodes(children.nextElement()) - 1;
			
		}
		return result;
	}
	
	public static void main(String[] args) {

		int populationSize = 10;
		int maxDepth = 10;
		Random rand = new Random();
		int maxNodes = 10;
		
		
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new PR3());
		Util.grow(root, maxDepth, rand, maxNodes);
		
		System.out.println(root);
		
		System.out.println(numberOfNodes(root));
		
	}
}
