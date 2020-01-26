package hr.fer.zemris.optjava.dz12.Expression;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

public class Test {

	static List<Expression> functions = new ArrayList<>();
	static List<Expression> terminals = new ArrayList<>();

	static Random rand = new Random();

	public static void main(String[] args) {

		functions.add(new IF());
		functions.add(new PR2());
		functions.add(new PR3());

		terminals.add(new Terminal("Right", Action.RIGHT));
		terminals.add(new Terminal("Left", Action.LEFT));
		terminals.add(new Terminal("Move", Action.MOVE));

		makeTree(2);

	}

	public static void generateExpressions(DefaultMutableTreeNode node, int depth) {

		Expression exp = (Expression) node.getUserObject();

		// ako je node terminal odmah se izlazi iz funkcije
		if (exp.status == Status.TERMINAL) {
			return;
		}

		IFunction fun = (IFunction) node.getUserObject();
		// za svako dijete se stvara neki izraz
		for (int i = 0; i < fun.getNumberOfOutputs(); i++) {
			
			// zadnja dozvoljena razina
			if(depth == 0) {
				
				Expression e = terminals.get(rand.nextInt(terminals.size())).duplicate();
				fun.addOutput(e);
				
			}else {
				// ako je moguce dodati jos izraza
				if (fun.canAdd()) {

					// nasumican izbor izraza
					if (rand.nextFloat() > 0.5) {
						Expression e = terminals.get(rand.nextInt(terminals.size())).duplicate();
						fun.addOutput(e);
					} else {
						Expression e = functions.get(rand.nextInt(functions.size())).duplicate();
						fun.addOutput(e);
					}
				}
			}
		}

		DefaultMutableTreeNode result = new DefaultMutableTreeNode(fun);
		for (Expression e : fun.getOutputs()) {
			node.add(new DefaultMutableTreeNode(e));
		}

		Enumeration<DefaultMutableTreeNode> en = node.children();
		
		while (en.hasMoreElements()) {
			
			DefaultMutableTreeNode child = en.nextElement();
			
			exp = (Expression) child.getUserObject();
			
			if(exp.status == Status.FUNCTION) {
				
				fun = (IFunction) child.getUserObject();
				
				if(fun.canAdd()) {
					generateExpressions(child, depth--);
				}
			}
			
		}
	}

	/**
	 * Metoda za generiranje stabala s maksimalnom dubinom
	 * 
	 * @param node cvor za koji se generira stablo dubine <code>depth</code>
	 * @param depth dubina generiranog stabla
	 */
	public static void full(DefaultMutableTreeNode node, int depth) {
		
		IFunction fun = (IFunction) node.getUserObject();
		
		// ako je dubina 0 moraju se generirati terminali
		if(depth == 0) {
			
			for (int i = 0; i < fun.getNumberOfOutputs(); i++) {
				
				Expression e = terminals.get(rand.nextInt(terminals.size())).duplicate();
				fun.addOutput(e);
				
			}
			
			return;
			
		}else {
			// dubina nije 0, generiraju se funkcije
			for (int i = 0; i < fun.getNumberOfOutputs(); i++) {

				Expression e = functions.get(rand.nextInt(functions.size())).duplicate();
				fun.addOutput(e);
			}
		}

		// dodavanje novih cvorova
		for (Expression e : fun.getOutputs()) {
			node.add(new DefaultMutableTreeNode(e));
		}

		// dohvat novo dodanih cvorova
		Enumeration<DefaultMutableTreeNode> en = node.children();
		
		// za svaki dohvaceni cvor se rekurzivno generiraju novi cvorovi ovisno 
		// o broju funkcija koje neki cvor moze imati i dubini na kojoj se cvor nalazi
		while (en.hasMoreElements()) {
			
			DefaultMutableTreeNode child = en.nextElement();
			
			full(child, depth - 1);
		}
	}
	
	
	public static void makeTree(int depth) {

		Expression root = functions.get(rand.nextInt(functions.size()));

		DefaultMutableTreeNode rootnode = new DefaultMutableTreeNode(root);

		System.out.println("root\n" + rootnode.getUserObject());
		full(rootnode, depth);

		System.out.println("children");
		Enumeration<DefaultMutableTreeNode> en = rootnode.children();
//		while (en.hasMoreElements()) {
//			generateExpressions(en.nextElement(), 10);
//		}

		while (en.hasMoreElements()) {
			System.out.println(en.nextElement());
		}
		
		System.out.println();
		
		en = rootnode.breadthFirstEnumeration();

		while (en.hasMoreElements()) {
			
			DefaultMutableTreeNode n = en.nextElement();
			
			Expression e = (Expression) n.getUserObject();
//			if(e.status == Status.TERMINAL) {
//				Terminal t = (Terminal) n.getUserObject();
//				System.out.println(t.action);
//			}else {
//				
//				if(e.name == "IF") {
//					IFunction f = (IFunction) n.getUserObject();
//					
//					System.out.println("prva akcija: ");
//					System.out.println(f.getOutputs().get(0));
//					System.out.println("druga akcija: ");
//					System.out.println(f.getOutputs().get(1));
//					
//				}
//				
//			}
//			System.out.println(e.name);
			System.out.println(e.name);
		}


	}

}
