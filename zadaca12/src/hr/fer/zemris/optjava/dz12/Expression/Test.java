package hr.fer.zemris.optjava.dz12.Expression;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Util;
import hr.fer.zemris.optjava.dz12.Function.IFunction;

public class Test {

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
					IFunction functionChild = (IFunction) node.getUserObject();

					// uklanjanje elementa liste djeteta iz liste roditelja
					functionParent.removeOutput((Expression) node.getUserObject());

					// uklanja se cvor koji se mutira iz roditelja
					parent.remove(node);

					// stvaranje novog stabla umjesto uklonjenog cvora
					DefaultMutableTreeNode newNode = Util.makeTree(maxDepth - currentDepth, rand);
					
					System.out.println();
					System.out.println("novo: " + newNode.getUserObject());
					System.out.println();
					
					parent.add(newNode);

					functionParent = (IFunction) parent.getUserObject();
					functionParent.addOutput((Expression) newNode.getUserObject());

					return;

				} else {

					Enumeration<DefaultMutableTreeNode> en = node.children();

					while (en.hasMoreElements()) {
						mutate(en.nextElement(), maxDepth, rand, p);
					}
				}
			}
		} else {
			// terminalni cvor
//			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
//			
//			parent.remove(node);
		}

	}

	public static void main(String[] args) {

		int depth = 3;

		DefaultMutableTreeNode rootnode = Util.makeTree(depth, rand);

		System.out.println("root\n" + rootnode.getUserObject());

		Enumeration<DefaultMutableTreeNode> en = rootnode.children();

		while (en.hasMoreElements()) {
			DefaultMutableTreeNode e = en.nextElement();
			System.out.println(e);
		}

		System.out.println("dubina: " + rootnode.getDepth());

		mutate(rootnode, rootnode.getDepth(), rand, 0.3f);

		en = rootnode.children();

		System.out.println();
		System.out.println("root\n" + rootnode.getUserObject());
		while (en.hasMoreElements()) {
			DefaultMutableTreeNode e = en.nextElement();
			System.out.println(e);
		}

	}
}
