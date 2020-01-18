package hr.fer.zemris.optjava.test;

import hr.fer.zemris.optjava.GA.GASolution;
import hr.fer.zemris.optjava.GA.IntSolution;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;
import hr.fer.zemris.optjava.GA.Util;

public class Test2 {
	
	public static void main(String[] args) {
		
		Runnable job = new Runnable() {
			
			@Override
			public void run() {
				
				IRNG rng = RNG.getRNG();
				
//				IntSolution solution = new IntSolution(new int[] {1,2,120,-45,-70});
//				System.out.println(solution);
//				for(int i = 0; i < 20; i++) {
//					
//					Util.mutate(solution, rng);
//					System.out.println(solution);
//				}
				
				GASolution<int[]> parent1 = new IntSolution(new int[] {-125, 100, 0, 45, 10, 113});
				GASolution<int[]> parent2 = new IntSolution(new int[] {-128, 100, 23, -90, -10, 103});
				System.out.println(parent1);
				System.out.println(parent2);
				GASolution<int[]> child = Util.BLXa(parent1, parent2, rng);
				System.out.println(child);
			}
		};
		
		EVOThread thread = new EVOThread(job);
		thread.start();
	}
}
