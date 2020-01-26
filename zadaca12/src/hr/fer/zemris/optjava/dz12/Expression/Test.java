package hr.fer.zemris.optjava.dz12.Expression;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Util;
import hr.fer.zemris.optjava.dz12.Function.IF;
import hr.fer.zemris.optjava.dz12.Function.IFunction;
import hr.fer.zemris.optjava.dz12.Function.PR2;
import hr.fer.zemris.optjava.dz12.Function.PR3;
import hr.fer.zemris.optjava.dz12.Terminal.Action;
import hr.fer.zemris.optjava.dz12.Terminal.Terminal;

public class Test {

	static List<Expression> functions = new ArrayList<>();
	static List<Expression> terminals = new ArrayList<>();

	static Random rand = new Random();

	public static void main(String[] args) {

//		functions.add(new IF());
//		functions.add(new PR2());
//		functions.add(new PR3());
//
//		terminals.add(new Terminal("Right", Action.RIGHT));
//		terminals.add(new Terminal("Left", Action.LEFT));
//		terminals.add(new Terminal("Move", Action.MOVE));
//
//		makeTree(1);
		
		int depth = 2;
		
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

//		System.out.println();
//		
//		en = rootnode.breadthFirstEnumeration();
//
//		while (en.hasMoreElements()) {
//			
//			DefaultMutableTreeNode n = en.nextElement();
//			
//			Expression e = (Expression) n.getUserObject();
////			if(e.status == Status.TERMINAL) {
////				Terminal t = (Terminal) n.getUserObject();
////				System.out.println(t.action);
////			}else {
////				
////				if(e.name == "IF") {
////					IFunction f = (IFunction) n.getUserObject();
////					
////					System.out.println("prva akcija: ");
////					System.out.println(f.getOutputs().get(0));
////					System.out.println("druga akcija: ");
////					System.out.println(f.getOutputs().get(1));
////					
////				}
////				
////			}
////			System.out.println(e.name);
//			System.out.println(e.name);
//		}

	}

}
