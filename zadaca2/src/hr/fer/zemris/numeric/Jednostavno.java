package hr.fer.zemris.numeric;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jfree.ui.RefineryUtilities;

public class Jednostavno {

	static double low = -5;
	static double high = 5;

	// parser datoteke za skiciranje grafa trenutnih rješenja
	public static List<double[]> getGraphData(String fileName){
		List<double[]> graphData = new ArrayList<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(fileName));) {
		    String line = br.readLine();
		    
		    while (line != null) {
		    	String[] temp = line.split(",");
		    	graphData.add(new double[] {Double.parseDouble(temp[0]), Double.parseDouble(temp[1])});
		    	line = br.readLine();
		    }
		}catch(IOException e) {
			e.printStackTrace();
		}
		return graphData;
	}
	
	public static void main(String[] args) throws IOException {
		
//		String funName = args[0];
//		int maxIterations = Integer.parseInt(args[1]);
		double[] initialPoint = new double[2];
		
		Random rand = new Random();
//		if(args.length == 4) {
//			initialPoint[0] = Double.parseDouble(args[2]);
//			initialPoint[1] = Double.parseDouble(args[3]);
//		}else {
//			initialPoint[0] = rand.nextDouble();
//			initialPoint[1]	= rand.nextDouble();
//		}
		
		IFunction function = new f1a();
		
//		if(funName.toLowerCase().equals("f1a")) {
//			function = new f1a();
//		}else if(funName.toLowerCase().equals("f1b")) {
//			function = new f1b();
//		}else if(funName.toLowerCase().equals("f2a")) {
//			function = new f2a();
//		}else {
//			function = new f2b();
//		}
		initialPoint[0] = rand.nextDouble();
		initialPoint[1] = rand.nextDouble();
		int maxIterations = 1000;
		double[] solution = NumOptAlgorithms.gradientDescent(function, maxIterations, initialPoint);
		
		List<double[]> graphData = getGraphData("graphData");
//		for(int i = 0; i < graphData.size(); i++) {
//			System.out.println(graphData.get(i)[0] + " " + graphData.get(i)[1]);
//		}
		final Graph demo = new Graph(graphData, "Graph");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		System.out.print(solution[0]);
		System.out.print(" ");
		System.out.print(solution[1]);
		System.out.println();
		
	}
	
	
	
	
	
}
