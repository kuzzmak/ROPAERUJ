package hr.fer.zemris.trisat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
	
	private String path;
	
	public Parser(String path) {
		this.path = path;
	}
	
	public SATFormula parse(){
		
		List<Clause> clauses = new ArrayList<>();
		List<String> stringClauses = new ArrayList<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(path));) {
		    String line = br.readLine();
		    
		    while (line != null & !line.startsWith("%")) {
		    	if(line.startsWith("c")) {
		    		line = br.readLine();
		    		continue;
		    	}
		    	stringClauses.add(line.trim());
		        
		        line = br.readLine();
		    }
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		Integer numberOfVariables = Integer.parseInt(stringClauses.get(0).split("\\s+")[2]);

		for(int i = 1; i < stringClauses.size(); i++) {
			List<String> temp = Arrays.asList(stringClauses.get(i).split("\\s+"));
		
			List<Integer> tempInt = new ArrayList<>();
			for(int j = 0; j < temp.size() - 1; j++) {
				tempInt.add(Integer.parseInt(temp.get(j)));
			}
			clauses.add(new Clause(tempInt));
		}
		
		return new SATFormula(numberOfVariables, clauses);
	}
}
