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

		makeTree(3);

//		IExpression iff = new IF();
//		((IF) iff).addOutput(new IF());
//		
//		IExpression right = new Terminal("Right", Action.RIGHT);
//		
//		((IF) iff).addOutput(right);
//		System.out.println(iff);

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
					generateExpressions(child, depth - 1);
				}
			}
			
		}
	}

	public static void makeTree(int depth) {

		Expression root = functions.get(rand.nextInt(functions.size()));

		DefaultMutableTreeNode rootnode = new DefaultMutableTreeNode(root);

		System.out.println("root\n" + rootnode.getUserObject());
		generateExpressions(rootnode, depth);

		System.out.println("children");
		Enumeration<DefaultMutableTreeNode> en = rootnode.children();
//		while (en.hasMoreElements()) {
//			generateExpressions(en.nextElement(), 10);
//		}

		en = rootnode.children();
		while (en.hasMoreElements()) {
			System.out.println(en.nextElement());
		}

//		PR3 pr31 = new PR3();
//		pr31.addOutput(new IF());
//		PR3 pr32 = (PR3)pr31.duplicate();
//		PR3 pr33 = (PR3)pr31.duplicate();

//		while (rootnode.getDepth() < 5) {
//
//			Enumeration<DefaultMutableTreeNode> en = rootnode.children();
//			while (en.hasMoreElements()) {
//
//				DefaultMutableTreeNode curr = en.nextElement();
//
//				generateExpressions(curr);
//			}
//			System.out.println(rootnode.getDepth());
//
//		}
//		while(true) {
//			
//			Enumeration<DefaultMutableTreeNode> en = rootnode.children();
//			 
//			break;
//			
//			
//		}
//		for(int i = 0; i < ((IFunction) root).getNumberOfOutputs(); i++) {
//			
//			if(rand.nextFloat() > 0.5) {
//				rootnode.add(new DefaultMutableTreeNode(terminals.get(rand.nextInt(terminals.size()))));
//			}else {
//				rootnode.add(new DefaultMutableTreeNode(functions.get(rand.nextInt(functions.size()))));
//			}
//		}

//		generateExpressions(rootnode);
//		
//		while()
//		System.out.println("root\n" + rootnode.getUserObject());
//		System.out.println("children");
//		Enumeration<DefaultMutableTreeNode> en = rootnode.children();
//		while (en.hasMoreElements()) {
//			System.out.println(en.nextElement());
//		}

	}

}
