package hr.fer.zemris.optjava.dz7.DATA;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dataset {
	
	private List<Sample> data = new ArrayList<>();
	
	public Dataset(String path) {
		this.data = load(path);
	}
	
	public List<Sample> getData() {
		return data;
	}

	public int size() {
		return data.size();
	}
	
	private List<Sample> load(String path) {
		
		List<Sample> samples = new ArrayList<>();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(path))){
			
			String line = reader.readLine();
			
			while(line != null) {
				
				line = line.replace("(", "");
				line = line.replace(")", "");
				
				String[] xANDy = line.split(":");
				String[] xOnly = xANDy[0].split(",");
				String[] yOnly = xANDy[1].split(",");
				
				// ulaz
				double[] x = new double[xOnly.length];
				// izlaz
				double[] y = new double[yOnly.length];
				
				for(int i = 0; i < xOnly.length; i++) {
					x[i] = Double.parseDouble(xOnly[i]);
				}
				
				for(int i = 0; i < yOnly.length; i++) {
					y[i] = Double.parseDouble(yOnly[i]);
				}
				
				Sample s = new Sample(x, y);
				
				samples.add(s);
				
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return samples;
	}
}
