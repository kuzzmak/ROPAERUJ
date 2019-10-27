package hr.fer.zemris.optjava.dz4.part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StickLoader {

	private static String path;
	
	public StickLoader(String path) {
		this.path = path;
	}
	
	public List<Stick> loadSticks(){
		
		List<Stick> tempList = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();

			line = line.replace("[", "");
			line = line.replace("]", "");
				
			String[] temp = line.split(", ");
			
			for(String str: temp) {
				Stick s = new Stick(Integer.parseInt(str));
				tempList.add(s);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempList;
	}
}
