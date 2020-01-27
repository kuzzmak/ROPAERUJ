package hr.fer.zemris.optjava.dz12;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Expression.Expression;
import hr.fer.zemris.optjava.dz12.Expression.Status;
import hr.fer.zemris.optjava.dz12.Function.IF;
import hr.fer.zemris.optjava.dz12.Function.IFunction;
import hr.fer.zemris.optjava.dz12.Function.PR2;
import hr.fer.zemris.optjava.dz12.Function.PR3;
import hr.fer.zemris.optjava.dz12.Terminal.Action;
import hr.fer.zemris.optjava.dz12.Terminal.Terminal;

public class Util {

	static List<Expression> functions = Arrays.asList(new IF(), new PR2(), new PR3());
	static List<Expression> terminals = Arrays.asList(new Terminal("RIGHT", Action.RIGHT),
			new Terminal("LEFT", Action.LEFT), new Terminal("MOVE", Action.MOVE));

	/**
	 * Funkcija za generiranje potpunog stabla dubine <code>depth</code>
	 * 
	 * @param node  cvor ispod kojeg se generira stablo
	 * @param depth maksimalna dubina stabla
	 * @param rand  generator slucajnih brojeva
	 */
	public static void full(DefaultMutableTreeNode node, int depth, Random rand) {

		IFunction fun = (IFunction) node.getUserObject();

		// ako je dubina 0 moraju se generirati terminali
		if (depth == 0) {

			for (int i = 0; i < fun.getNumberOfOutputs(); i++) {

				Expression e = terminals.get(rand.nextInt(terminals.size())).duplicate();
				fun.addOutput(e);
				node.add(new DefaultMutableTreeNode(e));
			}

			return;

		} else {
			// dubina nije 0, generiraju se funkcije
			for (int i = 0; i < fun.getNumberOfOutputs(); i++) {

				Expression e = functions.get(rand.nextInt(functions.size())).duplicate();
				fun.addOutput(e);
				node.add(new DefaultMutableTreeNode(e));
			}
		}

		// dohvat novo dodanih cvorova
		Enumeration<DefaultMutableTreeNode> en = node.children();

		// za svaki dohvaceni cvor se rekurzivno generiraju novi cvorovi ovisno
		// o broju funkcija koje neki cvor moze imati i dubini na kojoj se cvor nalazi
		while (en.hasMoreElements()) {

			DefaultMutableTreeNode child = en.nextElement();

			full(child, depth - 1, rand);
		}
	}

	/**
	 * Funkcija za stvaranje stabla dubine <code>depth</code> gdje terminal i
	 * funkcija imaju jednaku sansu prilikom stvaranja novog cvora
	 * 
	 * @param node  cvor na kojem se stvara novo stablo
	 * @param depth dubina novo stvorenog stabla
	 */
	public static void grow(DefaultMutableTreeNode node, int depth, Random rand) {

		Expression exp = (Expression) node.getUserObject();

		// ako je node terminal odmah se izlazi iz funkcije
		if (exp.status == Status.TERMINAL) {
			return;
		}

		IFunction fun = (IFunction) node.getUserObject();

		// stvaranje terminala ako je maksimalna dubina
		if (depth == 0) {

			for (int i = 0; i < fun.getNumberOfOutputs(); i++) {

				Expression e = terminals.get(rand.nextInt(terminals.size())).duplicate();
				fun.addOutput(e);
				node.add(new DefaultMutableTreeNode(e));
			}

			return;

		} else {

			// ako je razina != 0 onda se stvara ili terminal ili funkciju
			for (int i = 0; i < fun.getNumberOfOutputs(); i++) {

				if (rand.nextFloat() > 0.5) {
					Expression e = terminals.get(rand.nextInt(terminals.size())).duplicate();
					fun.addOutput(e);
					node.add(new DefaultMutableTreeNode(e));

				} else {
					Expression e = functions.get(rand.nextInt(functions.size())).duplicate();
					fun.addOutput(e);
					node.add(new DefaultMutableTreeNode(e));
				}
			}
		}

		// dohvat novo dodanih cvorova
		Enumeration<DefaultMutableTreeNode> en = node.children();

		// za svaki dohvaceni cvor se rekurzivno generiraju novi cvorovi ovisno
		// o broju funkcija koje neki cvor moze imati i dubini na kojoj se cvor nalazi
		while (en.hasMoreElements()) {

			DefaultMutableTreeNode child = en.nextElement();

			grow(child, depth - 1, rand);
		}
	}
	
	/**
	 * Funkcija za generiranje stabla dubine <code>depth</code> gdje full
	 * i grow metoda imaju jednaku sansu za generiranje stabla
	 * 
	 * @param depth zeljena dubina stabla
	 * @param rand generator slucajnih brojeva
	 * @return generirano stablo
	 */
	public static DefaultMutableTreeNode makeTree(int depth, Random rand) {
		
		Expression root = functions.get(rand.nextInt(functions.size())).duplicate();

		DefaultMutableTreeNode rootnode = new DefaultMutableTreeNode(root);
		
		if(rand.nextFloat() > 0.5) {
			System.out.println("grow");
			grow(rootnode, depth - 1, rand);
		}else {
			System.out.println("full");
			full(rootnode, depth - 1, rand);
		}
		return rootnode;
	}
	
	public static void mutate(DefaultMutableTreeNode node, Random rand) {
		
		
//		node.get
		
		
		
	}
	
	
	
}
