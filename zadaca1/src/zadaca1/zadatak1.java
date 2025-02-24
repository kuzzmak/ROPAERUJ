package zadaca1;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class zadatak1 {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> stringClauses = new ArrayList<String>();
		List<clause> clauses = new ArrayList<clause>();
		
		try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\kuzmi\\OneDrive - fer.hr\\faks\\5sem\\ROPAERUJ\\1zad\\uf20-01000.cnf"));) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    
		    
		    while (line != null & !line.startsWith("%")) {
		    	if(line.startsWith("c")) {
		    		line = br.readLine();
		    		continue;
		    	}
		    	stringClauses.add(line.trim());
		        
		        line = br.readLine();
		    }
		}
		
		Integer numOfVariables = Integer.parseInt(stringClauses.get(0).split("\\s+")[2]);
//		System.out.printf("broj varijabli: %d\n", numOfVariables);
		
		for(int i = 1; i < stringClauses.size(); i++) {
			List<String> temp = Arrays.asList(stringClauses.get(i).split("\\s+"));
		
			List<Integer> tempInt = new ArrayList<>();
			for(int j = 0; j < temp.size() - 1; j++) {
				tempInt.add(Integer.parseInt(temp.get(j)));
			}
			clauses.add(new clause(temp.size() - 1, tempInt));
		}
		

//		List<Integer> booleanValues = clause.calculateBoolIndexes(bigNum, clauses.get(2));
//		System.out.println(booleanValues);
//		clauses.get(1).setBooleanValues(booleanValues);
//		System.out.println(clauses.get(0));
//		for(int i = 0; i < clauses.size(); i++) {
//			System.out.println(clauses.get(i));
//		}
		List<String> solutions = new ArrayList<>();
		int length = 20;
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < length; i++) {
			sb.append(0);
		}
//		String num = "00000000000000000000";
//		for(clause cl: clauses) {
//			List<Integer> booleanValues = clause.calculateBoolIndexes(num, cl);
//			System.out.println(booleanValues);
//		}
		
		for(int i = 0; i < Math.pow(2, length); i++) {
			StringBuilder sbin = new StringBuilder(Integer.toBinaryString(i));
			while(sbin.length() < length) {
				sbin.insert(0, 0);
			}
			int counter = 0;
			for(int j = 0; j < clauses.size(); j++) {
			
				List<Integer> booleanValues = clause.calculateBoolIndexes(sbin.toString(), clauses.get(j));
				
				if(booleanValues.contains(1)) {
//					System.out.println("da");
					counter++;
					continue;
				}else {
					break;
//					System.out.println(sbin.toString());
					
				}
				
			}
			if(counter == clauses.size()) {
				counter = 0;
				solutions.add(sbin.toString());
				System.out.println(sbin.toString());
			}
			
		}
		
		
		
	}
}