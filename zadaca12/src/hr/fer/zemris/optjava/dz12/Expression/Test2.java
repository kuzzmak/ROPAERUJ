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
	public static void cross(DefaultMutableTreeNode parent01, DefaultMutableTreeNode parent02, Random rand) {

		// stvaranje kopija roditelja nad kojima se rade izmjene

//		Expression e1 = (Expression) parent01.getUserObject();
//		Expression e2 = (Expression) parent02.getUserObject();
//		
//		// prvo dijete koje je kopija roditelja i na njemu se rade izmjene
//		DefaultMutableTreeNode parent1 = new DefaultMutableTreeNode(e1.duplicate());
// 		DefaultMutableTreeNode parent2 = new DefaultMutableTreeNode(e2.duplicate());

		DefaultMutableTreeNode parent1 = Util.deepCopy(parent01, null);
		DefaultMutableTreeNode parent2 = Util.deepCopy(parent02, null);
		
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
		List<DefaultMutableTreeNode> nodes1 = Util.fromNodeToList(parent1);
		List<DefaultMutableTreeNode> nodes2 = Util.fromNodeToList(parent2);

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
	

	public static void main(String[] args) {

		int depth = 3;

		DefaultMutableTreeNode parent1 = Util.makeTree(depth, rand);

		DefaultMutableTreeNode parent2 = Util.makeTree(depth, rand);
		
//		Expression e = (Expression) parent1.getUserObject();
//		
//		DefaultMutableTreeNode par1 = new DefaultMutableTreeNode(e.duplicate());
//		
		Random rand = new Random();

		cross(parent1, parent2, rand);
//		
//		
//		
//		Expression e = new IF();
		
		
		
		
//		DefaultMutableTreeNode newParent = deepCopy(parent1);
//		
//		IFunction fn = (IFunction) newParent.getUserObject();
//		fn.addOutput(new IF());
//		newParent.add(new DefaultMutableTreeNode(new IF()));
//		
//		
//		System.out.println("parent1");
//		Enumeration<DefaultMutableTreeNode> en = parent1.children();
//		while (en.hasMoreElements()) {
//			System.out.println(en.nextElement());
//		}
//		System.out.println();
//		en = newParent.children();
//		while (en.hasMoreElements()) {
//			System.out.println(en.nextElement());
//		}

	}
}
