package hr.fer.zemris.optjava.dz5.part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class OptimizaitonFunction implements IFunction {

	private String problemName;
	private static int n;
	private static int[][] distances;
	private static int[][] flow;

	public OptimizaitonFunction(String problemName) {
		this.problemName = problemName;
		load(problemName);
	}

	public static int[][] getFlow() {
		return flow;
	}

	public static int[][] getDistances() {
		return distances;
	}

	@Override
	public int getN() {
		return n;
	}
	
	public void load(String problemName) {

		StringBuilder sb = new StringBuilder();
		sb.append("data\\").append(problemName).append(".dat");

		try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {
			String line = br.readLine().trim();
			this.n = Integer.parseInt(line);
			distances = new int[n][n];
			flow = new int[n][n];

			br.readLine();
			line = br.readLine().trim();
			int row = 0;
			while (line != null && !line.equals("")) {

				String[] temp = line.split("\\s+");
				for (int i = 0; i < n; i++) {
					distances[row][i] = Integer.parseInt(temp[i]);
				}
				row++;
				line = br.readLine().trim();

			}
			
			row = 0;
			line = br.readLine().trim();
			while (line != null) {

				String[] temp = line.split("\\s+");
				for (int i = 0; i < n; i++) {
					flow[row][i] = Integer.parseInt(temp[i]);
				}
				row++;
				if(row == n) break;
				line = br.readLine().trim();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public int valueAt(Chromosome c) {

		int value = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				value += flow[i][j] * distances[c.getPermutation()[i]][c.getPermutation()[j]];
			}
		}
		return value;
	}
}
