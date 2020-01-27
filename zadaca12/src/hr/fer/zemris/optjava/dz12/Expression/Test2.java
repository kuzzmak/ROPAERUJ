package hr.fer.zemris.optjava.dz12.Expression;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Util;
import hr.fer.zemris.optjava.dz12.Function.IFunction;
import hr.fer.zemris.optjava.dz12.test.Test;

public class Test2 {

	static List<Expression> functions = new ArrayList<>();
	static List<Expression> terminals = new ArrayList<>();

	static Random rand = new Random();

	public static void main(String[] args) {

		Test test = new Test(32, 32, true);

		int depth = 10;

		DefaultMutableTreeNode rootnode = Util.makeTree(depth, rand);
		
		Util.mutate(rootnode, rootnode.getDepth(), rand, 0.3f);
		
		Test.executeNode(rootnode, false);
		
	}
}
