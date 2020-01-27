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

	public static void mutate(DefaultMutableTreeNode node, int maxDepth, Random rand, float p) {

		Expression exp = (Expression) node.getUserObject();

		// dubina na kojoj se nalazi cvor
		int currentDepth = node.getDepth();

		// ako je cvor funkcija
		if (exp.status == Status.FUNCTION) {

			// vjerojatnost mutacije funkcijskih cvorova je dvostruko veca od terminalnih
			// cvorova
			if (rand.nextFloat() < 2 * p) {

				// roditelj trenutnog cvora ako nije root
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

				if (parent != null) {

					IFunction functionParent = (IFunction) parent.getUserObject();

					// uklanjanje elementa liste djeteta iz liste roditelja
					functionParent.removeOutput((Expression) node.getUserObject());

					// uklanja se cvor koji se mutira iz roditelja
					parent.remove(node);

					// stvaranje novog stabla umjesto uklonjenog cvora
					DefaultMutableTreeNode newNode = Util.makeTree(maxDepth - currentDepth, rand);

					parent.add(newNode);

					functionParent = (IFunction) parent.getUserObject();
					functionParent.addOutput((Expression) newNode.getUserObject());

					return;

				} else {
					
					// rekurzivno pozivanje mutacije nad djecom 
					Enumeration<DefaultMutableTreeNode> en = node.children();

					while (en.hasMoreElements()) {
						mutate(en.nextElement(), maxDepth, rand, p);
					}
				}
			}
		} else {
			// terminalni cvor
			if (rand.nextFloat() < p) {
				
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
				
				IFunction functionParent = (IFunction) parent.getUserObject();
				
				functionParent.removeOutput((Expression) node.getUserObject());
				
				parent.remove(node);
				
				// stvaranje novog stabla umjesto uklonjenog cvora
				DefaultMutableTreeNode newNode = Util.makeTree(maxDepth - currentDepth, rand);

				parent.add(newNode);

				functionParent = (IFunction) parent.getUserObject();
				functionParent.addOutput((Expression) newNode.getUserObject());
			}
		}
	}

	public static void main(String[] args) {

//		int depth = 3;
//
//		DefaultMutableTreeNode rootnode = Util.makeTree(depth, rand);
//
//		System.out.println(rootnode.getUserObject());
//
//		Enumeration<DefaultMutableTreeNode> en = rootnode.children();
//
//		while (en.hasMoreElements()) {
//			DefaultMutableTreeNode e = en.nextElement();
//			System.out.println(e);
//		}
//
//		mutate(rootnode, rootnode.getDepth(), rand, 0.3f);
//
//		en = rootnode.children();
//
//		System.out.println();
//		System.out.println(rootnode.getUserObject());
//		while (en.hasMoreElements()) {
//			DefaultMutableTreeNode e = en.nextElement();
//			System.out.println(e);
//		}

		Test test = new Test(32, 32, true);

		int depth = 10;

		DefaultMutableTreeNode rootnode = Util.makeTree(depth, rand);
		
		mutate(rootnode, rootnode.getDepth(), rand, 0.3f);
		
		Test.executeNode(rootnode, true);
		
	}
}
