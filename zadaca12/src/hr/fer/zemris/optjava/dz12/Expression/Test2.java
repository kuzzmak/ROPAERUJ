package hr.fer.zemris.optjava.dz12.Expression;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Util;
import hr.fer.zemris.optjava.dz12.Function.IF;
import hr.fer.zemris.optjava.dz12.Function.IFunction;

public class Test2 {

	static Random rand = new Random();

	// TODO napraviti da su prosljedjene enumeracije umjesto roditelja
	public static void cross(DefaultMutableTreeNode parent1, DefaultMutableTreeNode parent2, Random rand) {

		// stvaranje kopija roditelja nad kojima se rade izmjene

		// prvo dijete koje je kopija roditelja i na njemu se rade izmjene
		DefaultMutableTreeNode parent01 = parent1;
// 		
// 		
// 		DefaultMutableTreeNode parent2 = parent01;

		// maksimalne dubine obaju roditelja
		int parent1Depth = parent1.getDepth();
		int parent2Depth = parent2.getDepth();

		System.out.println("parent1");
		System.out.println(((Expression) parent1.getUserObject()).name);
		Enumeration<DefaultMutableTreeNode> en = parent1.children();
		while (en.hasMoreElements()) {
			System.out.println(en.nextElement());
		}
		System.out.println();
		System.out.println("parent2");
		System.out.println(((Expression) parent2.getUserObject()).name);
		en = parent2.children();
		while (en.hasMoreElements()) {
			System.out.println(en.nextElement());
		}

		System.out.println();
		System.out.println("parent1 depth: " + parent1.getDepth());
		System.out.println("parent2 depth: " + parent2.getDepth());

		// dubina zajednicka obama roditeljima
		int maxDepth = Math.min(parent1Depth, parent2Depth);

		System.out.println("maxDepth: " + maxDepth);

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

		functionParentOfChosen1.addOutput((Expression) chosen2.getUserObject());
		functionParentOfChosen2.addOutput((Expression) chosen1.getUserObject());

		System.out.println();

		System.out.println("izabrana1: " + chosen1 + " " + chosen1.getDepth());
		System.out.println("parent of chosen1\n\t" + chosen1.getParent());
		System.out.println("izabrana2: " + chosen2 + " " + chosen2.getDepth());
		System.out.println("parent of chosen2\n\t" + chosen2.getParent());

		System.out.println();
		System.out.println("parent1");
		System.out.println(((Expression) parent1.getUserObject()).name);
		en = parent1.children();
		while (en.hasMoreElements()) {
			System.out.println(en.nextElement());
		}
		System.out.println();
		System.out.println("parent2");
		System.out.println(((Expression) parent2.getUserObject()).name);
		en = parent2.children();
		while (en.hasMoreElements()) {
			System.out.println(en.nextElement());
		}

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

	
	private static DefaultMutableTreeNode deepCopy(DefaultMutableTreeNode node) {
		
		Expression e = (Expression) node.getUserObject();
		
		DefaultMutableTreeNode copy = new DefaultMutableTreeNode(e.duplicate());
		
//		if(e.status == Status.TERMINAL) {
//			return copy;
//		}
//		
//		IFunction function = ((IFunction) node.getUserObject()).copy();
		
		if(node.isLeaf()) {
			return copy;
		}else {
			
			int numOfChildren = node.getChildCount();
			
			for(int i = 0; i < numOfChildren; i++) {
				copy.add(deepCopy((DefaultMutableTreeNode)node.getChildAt(i)));
			}
			return copy;
		}
	}
	

	public static void main(String[] args) {

		int depth = 3;

		DefaultMutableTreeNode parent1 = Util.makeTree(depth, rand);

		DefaultMutableTreeNode parent2 = Util.makeTree(depth, rand);

//		Random rand = new Random();
//
//		cross(parent1, parent2, rand);
		
		DefaultMutableTreeNode newParent = deepCopy(parent1);
		
		IFunction fn = (IFunction) newParent.getUserObject();
		fn.addOutput(new IF());
		newParent.add(new DefaultMutableTreeNode(new IF()));
		
		
		System.out.println("parent1");
		Enumeration<DefaultMutableTreeNode> en = parent1.children();
		while (en.hasMoreElements()) {
			System.out.println(en.nextElement());
		}
		System.out.println();
		en = newParent.children();
		while (en.hasMoreElements()) {
			System.out.println(en.nextElement());
		}

	}
}
