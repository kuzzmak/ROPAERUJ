package hr.fer.zemris.optjava.dz12;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

public class AntTrailGA {

	
	public static void main(String[] args) {
		
		
		String pathToMap = "C:\\Users\\kuzmi\\OneDrive - fer.hr\\faks\\5sem\\ROPAERUJ\\12zad\\13-SantaFeAntTrail.txt";
		int maxIterations = 100;
		int populationSize = 10;
		int minFitness = 90;
		String pathToResult = "result.txt";
		
		AntTrail at = new AntTrail(pathToMap, maxIterations, populationSize, minFitness);
		
		AntTrail.gui();
		
		int maxDepth = 10;
		Random rand = new Random();
		
		List<DefaultMutableTreeNode> population = Util.makePopulation(populationSize, maxDepth, rand);
		
		AntTrail.evaluate(population);
		System.out.println(Arrays.toString(AntTrail.fitness));
		AntTrail.walkTree(population.get(0));
		
	}
	
	
	
	
	
	
}
