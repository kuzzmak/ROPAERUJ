package hr.fer.zemris.optjava.dz12;

import javax.swing.tree.DefaultMutableTreeNode;

public class AntTrailGA {

	public static void main(String[] args) {
		
		String pathToMap = args[0];
		int maxIterations = Integer.parseInt(args[1]);
		int populationSize = Integer.parseInt(args[2]);
		int minFitness = Integer.parseInt(args[3]);
		String pathToResult = args[4];
		
		int maxInitialDepth = 6;
		int maxDepth = 30;
		int maxNodes = 200;
		int maxSteps = 300;
		float p = 0.15f;
		int k = 7;
		
		AntTrail at = new AntTrail(pathToMap, 
				maxIterations, 
				populationSize, 
				minFitness, 
				maxDepth, 
				maxSteps, 
				p, 
				k,
				maxInitialDepth,
				maxNodes);
		
		DefaultMutableTreeNode best = AntTrail.run();
		
		Util.saveResult(pathToResult, best);
		
		AntTrail.gui();
	}
}
