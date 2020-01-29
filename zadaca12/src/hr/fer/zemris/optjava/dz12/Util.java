package hr.fer.zemris.optjava.dz12;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
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

	// lista mogucih funkcija u nekom cvoru
	static List<Expression> functions = Arrays.asList(new IF(), new PR2(), new PR3());
	// lista mogucih terminala u nekom cvoru
	static List<Expression> terminals = Arrays.asList(new Terminal("RIGHT", Action.RIGHT),
			new Terminal("LEFT", Action.LEFT), new Terminal("MOVE", Action.MOVE));

	// varijable za pratiti trenutnu velicinu stabla
	public static int emptySpaces = 1;
	public static int currentNodes = 1;
	public static boolean mutated = false;

	/**
	 * Funkcija za generiranje potpunog stabla dubine <code>depth</code>
	 * 
	 * @param node  cvor ispod kojeg se generira stablo
	 * @param depth maksimalna dubina stabla
	 * @param rand  generator slucajnih brojeva
	 */
	public static void full(DefaultMutableTreeNode node, int depth, Random rand, int maxNodes) {

		if (((Expression) node.getUserObject()).status == Status.TERMINAL)
			return;

		IFunction fun = (IFunction) node.getUserObject();

		if (node.isRoot()) {
			emptySpaces = fun.getNumberOfOutputs();
			currentNodes = 1;
		}

		// ako je dubina 0 moraju se generirati terminali
		if (depth == 0 || maxNodes <= currentNodes + emptySpaces) {

			for (int i = 0; i < fun.getNumberOfOutputs(); i++) {

				Expression e = terminals.get(rand.nextInt(terminals.size())).duplicate();
				fun.addOutput(e);
				node.add(new DefaultMutableTreeNode(e));
				currentNodes += 1;
				emptySpaces -= 1;
			}

			return;

		} else {
			// dubina nije 0, generiraju se funkcije
			for (int i = 0; i < fun.getNumberOfOutputs(); i++) {

				Expression e = functions.get(rand.nextInt(functions.size())).duplicate();

				// potrebno staviti terminale
				if (maxNodes <= currentNodes + emptySpaces) {

					e = terminals.get(rand.nextInt(terminals.size())).duplicate();
					fun.addOutput(e);
					node.add(new DefaultMutableTreeNode(e));
					emptySpaces -= 1;
					currentNodes += 1;

					break;
				} else {
					fun.addOutput(e);
					node.add(new DefaultMutableTreeNode(e));
					emptySpaces += ((IFunction) e).getNumberOfOutputs();
					emptySpaces -= 1;
					currentNodes += 1;
				}
			}
		}

		// dohvat novo dodanih cvorova
		Enumeration<DefaultMutableTreeNode> en = node.children();

		// za svaki dohvaceni cvor se rekurzivno generiraju novi cvorovi ovisno
		// o broju funkcija koje neki cvor moze imati i dubini na kojoj se cvor nalazi
		while (en.hasMoreElements()) {

			DefaultMutableTreeNode child = en.nextElement();

			full(child, depth - 1, rand, maxNodes);
		}
	}

	/**
	 * Funkcija za stvaranje stabla dubine <code>depth</code> gdje terminal i
	 * funkcija imaju jednaku sansu prilikom stvaranja novog cvora
	 * 
	 * @param node  cvor na kojem se stvara novo stablo
	 * @param depth dubina novo stvorenog stabla
	 */
	public static void grow(DefaultMutableTreeNode node, int depth, Random rand, int maxNodes) {

		Expression exp = (Expression) node.getUserObject();

		// ako je node terminal odmah se izlazi iz funkcije
		if (exp.status == Status.TERMINAL) {
			return;
		}

		IFunction fun = (IFunction) node.getUserObject();

		if (node.isRoot()) {
			emptySpaces = fun.getNumberOfOutputs();
			currentNodes = 1;
		}

		// stvaranje terminala ako je maksimalna dubina
		if (depth == 0 || maxNodes <= currentNodes + emptySpaces) {

			for (int i = 0; i < fun.getNumberOfOutputs(); i++) {

				Expression e = terminals.get(rand.nextInt(terminals.size())).duplicate();
				fun.addOutput(e);
				node.add(new DefaultMutableTreeNode(e));
				currentNodes += 1;
				emptySpaces -= 1;
			}

			return;

		} else {

			// ako je razina != 0 onda se stvara ili terminal ili funkciju
			for (int i = 0; i < fun.getNumberOfOutputs(); i++) {

				Expression e = rand.nextFloat() > 0.5 ? terminals.get(rand.nextInt(terminals.size())).duplicate()
						: functions.get(rand.nextInt(functions.size())).duplicate();

				if (maxNodes <= currentNodes + emptySpaces) {

					e = terminals.get(rand.nextInt(terminals.size())).duplicate();
					fun.addOutput(e);
					node.add(new DefaultMutableTreeNode(e));
					emptySpaces -= 1;
					currentNodes += 1;

					break;

				} else {

					fun.addOutput(e);
					node.add(new DefaultMutableTreeNode(e));
					if (e.status == Status.FUNCTION) {
						emptySpaces += ((IFunction) e).getNumberOfOutputs();
						emptySpaces -= 1;
						currentNodes += 1;
					} else {
						emptySpaces -= 1;
						currentNodes += 1;
					}
				}
			}
		}

		// dohvat novo dodanih cvorova
		Enumeration<DefaultMutableTreeNode> en = node.children();

		// za svaki dohvaceni cvor se rekurzivno generiraju novi cvorovi ovisno
		// o broju funkcija koje neki cvor moze imati i dubini na kojoj se cvor nalazi
		while (en.hasMoreElements()) {

			DefaultMutableTreeNode child = en.nextElement();

			grow(child, depth - 1, rand, maxNodes);
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
	public static DefaultMutableTreeNode makeTree(int depth, Random rand, int maxNodes) {

		Expression root = functions.get(rand.nextInt(functions.size())).duplicate();

		DefaultMutableTreeNode rootnode = new DefaultMutableTreeNode(root);

		if (rand.nextFloat() > 0.5) {
			grow(rootnode, depth - 1, rand, maxNodes);
		} else {
			full(rootnode, depth - 1, rand, maxNodes);
		}
		return rootnode;
	}

	/**
	 * Funkcija za mutaciju pojedinog stabla, proces krece od root cvora i
	 * rekurzivno kroz svu djecu gdje svaki funkcijski cvor ima vjerojatnost
	 * mutacije 2 * <code>p</code>, a terminalni <code>p</code>. Pod pojmom mutacije
	 * se misli sljedece. Neka je cvor c trenutni cvor i ako je odredjeno da se
	 * treba vrsiti mutacija nad njim, dohvaca se njegov roditelj ako cvor c nije
	 * root cvor, brise se sadrzaj cvora c iz liste izraza roditelja, uklanja se
	 * cvor c iz djece roditelja i stvara se novo stablo s dubinom maksimalna
	 * dopustena dubina - dubina cvora c. Postupak se nastavlja kroz svu djecu.
	 * 
	 * @param node     cvor ili stablo nad kojim se vrsi mutacija
	 * @param maxDepth maksimalna dopustena dubina stabla
	 * @param rand     generator slucajnih brojeva
	 * @param p        vjerojatnost mutacije
	 */
	public static void mutate(DefaultMutableTreeNode node, int maxDepth, Random rand, float p, int maxNodes) {

		if(mutated) return;
		
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
					
					int currentNodeCount = numberOfNodes(parent);

					// stvaranje novog stabla umjesto uklonjenog cvora
					DefaultMutableTreeNode newNode = Util.makeTree(maxDepth - currentDepth, rand, maxNodes - currentNodeCount);

					parent.add(newNode);

					functionParent = (IFunction) parent.getUserObject();
					functionParent.addOutput((Expression) newNode.getUserObject());

					mutated = true;

				} else {

					// rekurzivno pozivanje mutacije nad djecom
					Enumeration<DefaultMutableTreeNode> en = node.children();

					while (en.hasMoreElements()) {
						mutate(en.nextElement(), maxDepth, rand, p, maxNodes);
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
				
				int currentNodeCount = numberOfNodes(parent);

				DefaultMutableTreeNode newNode = Util.makeTree(maxDepth - currentDepth, rand, maxNodes - currentNodeCount);

				parent.add(newNode);

				functionParent = (IFunction) parent.getUserObject();
				functionParent.addOutput((Expression) newNode.getUserObject());
				
				mutated = true;;
			}
		}
		
		return;
	}

	/**
	 * Funkcija za dobivanje liste svih cvorova iz stabla
	 * 
	 * @param node stablo iz kojeg se dobiva lista
	 * @return lista cvorova
	 */
	public static List<DefaultMutableTreeNode> fromNodeToList(DefaultMutableTreeNode node) {

		List<DefaultMutableTreeNode> nodes = new ArrayList<>();

		Enumeration<DefaultMutableTreeNode> bfs = node.breadthFirstEnumeration();

		while (bfs.hasMoreElements()) {
			nodes.add(bfs.nextElement());
		}

		return nodes;
	}

	/**
	 * Funkcija za stvaranje kopije stabla koja radi na principu rekurzivne
	 * rekonstrukcije izvornog stabla. Za svaki cvor stvori se isti takav u novom
	 * stablu.
	 * 
	 * @param node stablo kojem treba stvoriti kopiju
	 * @param copy novo stablo koje se konstruira rekurzivnim prolaskom kroz
	 *             <code>node</code>
	 * @return kopija stabla <code>node</code>
	 */
	public static DefaultMutableTreeNode deepCopy(DefaultMutableTreeNode node, DefaultMutableTreeNode copy) {

		Expression e = (Expression) node.getUserObject();

		// izvodi se prilikom prvog ulaska u funkciju
		if (copy == null)
			copy = new DefaultMutableTreeNode(newExpresion(e));

		IFunction f = (IFunction) copy.getUserObject();

		Enumeration<DefaultMutableTreeNode> children = node.children();

		for (int i = 0; i < f.getNumberOfOutputs(); i++) {

			Expression eChild = (Expression) children.nextElement().getUserObject();

			Expression newExp = newExpresion(eChild);

			copy.add(new DefaultMutableTreeNode(newExp));
			f.addOutput(newExp);
		}

		int numOfChildren = node.getChildCount();

		for (int i = 0; i < numOfChildren; i++) {

			Expression ex = (Expression) ((DefaultMutableTreeNode) node.getChildAt(i)).getUserObject();

			if (ex.status == Status.FUNCTION) {

				deepCopy((DefaultMutableTreeNode) node.getChildAt(i), (DefaultMutableTreeNode) copy.getChildAt(i));
			}
		}
		return copy;
	}

	/**
	 * Funkcija za krizanje dva stabla. Krizanje radi tako da se prvo naprave kopije
	 * roditelja nad kojima ce se kasnije raditi izmjene. Nakon toga se svi cvorovi,
	 * svakog roditelja posebno dodaju u listu iz koje se nasumicno bira neki cvor
	 * iz prvog i drugog stabla. Moguce je izabrati cvorove kojima je dubina manja
	 * od najvece dubine prvog roditelja i najvece dubine drugog roditelja cime se
	 * nikada ne moze izabrati root cvor. Kada su odabrani cvorovi, oni se brisu iz
	 * roditelja i kopiraju u suprotnog roditelja cime postupak zavrsava.
	 * 
	 * @param parent01 prvo stablo za krizanje
	 * @param parent02 drugo stablo za krizanje
	 * @param rand     generator nasumicnih brojeva
	 * @return lista djece
	 */
	public static List<DefaultMutableTreeNode> cross(DefaultMutableTreeNode parent01, DefaultMutableTreeNode parent02,
			Random rand) {

		DefaultMutableTreeNode parent1 = deepCopy(parent01, null);
		DefaultMutableTreeNode parent2 = deepCopy(parent02, null);

		// maksimalne dubine obaju roditelja
		int parent1Depth = parent1.getDepth();
		int parent2Depth = parent2.getDepth();

		// dubina zajednicka obama roditeljima
		int maxDepth = Math.min(parent1Depth, parent2Depth);

		// lista cvorova dobivena obilaskom prvog i drugog roditelja u sirinu
		List<DefaultMutableTreeNode> nodes1 = fromNodeToList(parent1);
		List<DefaultMutableTreeNode> nodes2 = fromNodeToList(parent2);

		int index = rand.nextInt(nodes1.size());
		// dohvat odabranog cvora
		DefaultMutableTreeNode chosen1 = nodes1.get(index);

		// potrebno izabrati cvor koji ima barem za jedan manje svoju dubinu
		// kako se ne bi cijelo stablo prenijelo u novo
		while (chosen1.getDepth() >= maxDepth) {
			index = rand.nextInt(nodes1.size());
			chosen1 = nodes1.get(index);
		}

		index = rand.nextInt(nodes2.size());

		DefaultMutableTreeNode chosen2 = nodes2.get(index);

		while (chosen2.getDepth() >= maxDepth) {
			index = rand.nextInt(nodes2.size());
			chosen2 = nodes2.get(index);
		}

		// uklanjanje izabranih cvorova iz roditelja
		DefaultMutableTreeNode parentOfChosen1 = (DefaultMutableTreeNode) chosen1.getParent();
		// dohvat funkcije u kojoj su spremljeni izrazi
		IFunction functionParentOfChosen1 = (IFunction) parentOfChosen1.getUserObject();
		// uklanjanje elementa liste djeteta iz liste roditelja
		functionParentOfChosen1.removeOutput((Expression) chosen1.getUserObject());
		// uklanja se cvor iz prvog roditelja
		parentOfChosen1.remove(chosen1);

		// identicno za drugog roditelja
		DefaultMutableTreeNode parentOfChosen2 = (DefaultMutableTreeNode) chosen2.getParent();

		IFunction functionParentOfChosen2 = (IFunction) parentOfChosen2.getUserObject();

		functionParentOfChosen2.removeOutput((Expression) chosen2.getUserObject());

		// dodavanje odabranih cvorova iz suprotnih roditelja
		parentOfChosen1.add(chosen2);
		parentOfChosen2.add(chosen1);

		// dodavanje izrava u outpute pojedine funkcije
		functionParentOfChosen1.addOutput((Expression) chosen2.getUserObject());
		functionParentOfChosen2.addOutput((Expression) chosen1.getUserObject());

		return new ArrayList<>(Arrays.asList(new DefaultMutableTreeNode[] { parent1, parent2 }));
	}

	/**
	 * Funkcija za vracanje nove instance nekog izraza
	 * 
	 * @param e izraz cija se nova instanca treba
	 * @return nova instanca izraza
	 */
	public static Expression newExpresion(Expression e) {

		if (e.status == Status.FUNCTION) {

			if (e.name == "IF")
				return new IF();
			else if (e.name == "PR2")
				return new PR2();
			else
				return new PR3();

		} else {
			if (e.name == "RIGHT")
				return new Terminal("RIGHT", Action.RIGHT);
			else if (e.name == "LEFT")
				return new Terminal("LEFT", Action.LEFT);
			else
				return new Terminal("MOVE", Action.MOVE);
		}
	}

	/**
	 * Funkcija za stvaranje djece stabala
	 * 
	 * @param populationSize velicina populacije
	 * @param maxDepth       maksimalna dubina pojedinog stabla
	 * @param rand           generator nasumicnih brojeva
	 * @return lista stabala
	 */
	public static List<DefaultMutableTreeNode> makePopulation(int populationSize, int maxDepth, Random rand,
			int maxNodes) {

		List<DefaultMutableTreeNode> population = new ArrayList<>();

		for (int i = 0; i < populationSize; i++) {

			population.add(makeTree(maxDepth, rand, maxNodes));
		}

		return population;
	}

	/**
	 * Funkcija za reskaliranje slika mrava i hrane
	 * 
	 * @param imageIcon slika koja se reskalira
	 * @return reskalirana slika
	 */
	public static ImageIcon resizeImageIcon(ImageIcon imageIcon) {
		Image image = imageIcon.getImage();
		Image newimg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}

	/**
	 * Funkcija za izracunavanje broja cvorova stabla
	 * 
	 * @param node stablo ciji se cvorovi izracunavaju
	 * @return broj cvorova stabla
	 */
	public static int numberOfNodes(DefaultMutableTreeNode node) {

		int result = 1;

		Enumeration<DefaultMutableTreeNode> children = node.children();

		result += node.getChildCount();

		while (children.hasMoreElements()) {

			result += numberOfNodes(children.nextElement()) - 1;

		}
		return result;
	}
}
