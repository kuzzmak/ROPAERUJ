package hr.fer.zemris.trisat;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TriSatSolver {
	/**
	 * @param args
	 */
	
	public static Map<String, Integer> sortByValue(final Map<String, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
	
	public static void main(String[] args){
	
		//String path = "C:\\Users\\kuzmi\\OneDrive - fer.hr\\faks\\5sem\\ROPAERUJ\\1zad\\uf20-01000.cnf";
		String path = args[1];
		int algNum = Integer.parseInt(args[0]);
		Parser parser = new Parser(path);
		SATFormula formula = parser.parse();
		
		Algorithm alg = new Algorithm(formula, algNum);
		
		String solution = alg.solve();
		
		if(solution != null) System.out.println(solution);
		
	}
}
