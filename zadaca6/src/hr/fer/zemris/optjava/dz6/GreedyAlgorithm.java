package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GreedyAlgorithm {
	
	private static List<City> cities;

	public GreedyAlgorithm(List<City> cities) {
		this.cities = cities;
	}
	
	public static double run() {

		List<City> cityList = new ArrayList<>(cities);
		List<City> route = new ArrayList<>();
		
		for(int i = 0; i < cityList.size(); i++) {
			double[] distancesFromCity = MMAS.distances[cityList.get(i).getIndex()].clone();
			
			Arrays.sort(distancesFromCity);

			for(int j = 0; j < distancesFromCity.length; j++) {
				
				if(i == j) continue;
				
				double value = distancesFromCity[j];
				
				for(int k = 0; k < distancesFromCity.length; k++) {
					if(value == MMAS.distances[i][k]) {
						if(!route.contains(cities.get(k))) {
							route.add(cities.get(k));
						}
					}
				}
			}
		}
		
		Ant a = new Ant(route);
		return a.pathDistence;
	}
}
