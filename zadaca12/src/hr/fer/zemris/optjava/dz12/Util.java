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
	 * Funkcija za generiranje stabla dubine <code>depth</code> gdje full i grow
	 * metoda imaju jednaku sansu za generiranje stabla
	 * 
	 * @param depth zeljena dubina stabla
	 * @param rand  generator slucajnih brojeva
	 * @return generirano stablo
	 */
	public static DefaultMutableTreeNode makeTree(int depth, Random rand) {

		Expression root = functions.get(rand.nextInt(functions.size())).duplicate();

		DefaultMutableTreeNode rootnode = new DefaultMutableTreeNode(root);

		if (rand.nextFloat() > 0.5) {
			grow(rootnode, depth - 1, rand);
		} else {
			full(rootnode, depth - 1, rand);
		}
		return rootnode;
	}

	/**
	 * Funkcija za mutaciju pojedinog stabla, proces krece od root cvora i rekurzivno kroz svu djecu
	 * gdje svaki funkcijski cvor ima vjerojatnost mutacije 2 * <code>p</code>, a terminalni <code>p</code>.
	 * Pod pojmom mutacije se misli sljedece. Neka je cvor c trenutni cvor i ako je odredjeno da se treba 
	 * vrsiti mutacija nad njim, dohvaca se njegov roditelj ako cvor c nije root cvor, brise se sadrzaj
	 * cvora c iz liste izraza roditelja, uklanja se cvor c iz djece roditelja i stvara se novo stablo
	 * s dubinom maksimalna dopustena dubina - dubina cvora c. Postupak se nastavlja kroz svu djecu.
	 * 
	 * @param node cvor ili stablo nad kojim se vrsi mutacija
	 * @param maxDepth maksimalna dopustena dubina stabla
	 * @param rand generator slucajnih brojeva
	 * @param p vjerojatnost mutacije
	 */
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

				DefaultMutableTreeNode newNode = Util.makeTree(maxDepth - currentDepth, rand);

				parent.add(newNode);

				functionParent = (IFunction) parent.getUserObject();
				functionParent.addOutput((Expression) newNode.getUserObject());
			}
		}
	}
}
