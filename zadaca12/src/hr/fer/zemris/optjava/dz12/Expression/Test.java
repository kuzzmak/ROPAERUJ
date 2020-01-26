package hr.fer.zemris.optjava.dz12.Expression;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Util;

public class Test {

	static List<Expression> functions = new ArrayList<>();
	static List<Expression> terminals = new ArrayList<>();

	static Random rand = new Random();

	public static void main(String[] args) {

		int depth = 3;
		
		DefaultMutableTreeNode rootnode = Util.makeTree(depth, rand);

		System.out.println("root\n" + rootnode.getUserObject());
		
		Enumeration<DefaultMutableTreeNode> en = rootnode.children();
		
		while (en.hasMoreElements()) {
			DefaultMutableTreeNode e = en.nextElement();
			System.out.println(e);
		}
		
	}

	public static void makeTree(int depth) {

		Expression root = functions.get(rand.nextInt(functions.size()));

		DefaultMutableTreeNode rootnode = new DefaultMutableTreeNode(root);

		System.out.println("root\n" + rootnode.getUserObject());
		Util.grow(rootnode, depth, rand);

		System.out.println("root children: " + rootnode.getChildCount());
		System.out.println("children");
		Enumeration<DefaultMutableTreeNode> en = rootnode.children();

		System.out.println();
		while (en.hasMoreElements()) {
			DefaultMutableTreeNode e = en.nextElement();
			System.out.println(e);
		}


	}

}
