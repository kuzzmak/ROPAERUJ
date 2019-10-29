package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crossover implements ICrossover {

	private Random rand;

	public Crossover(Random rand) {
		this.rand = rand;
	}

	@Override
	public Chromosome cross(Chromosome c1, Chromosome c2) {

		Chromosome child1 = c1.copy();
		Chromosome child2 = c2.copy();

		// u prvom dijetetu se uzimaju dvije tocke izmedju kojih se nalaze binovi
		// koji se ubacuju poslije tocke koja je izabrana u drugom dijetetu
		int index1 = rand.nextInt(child1.size());
		int index2 = rand.nextInt(child1.size());

		int from = Math.min(index1, index2);
		int to = Math.max(index1, index2);

		// indeks na koji se ubacuje u drugom djetetu
		int insertionPoint = rand.nextInt(child2.size());

		// binovi u prvom dijetetu koji se umecu u drugo dijete
		List<Bin> selectedBins = new ArrayList<>();
		for (int i = from; i <= to; i++) {
			selectedBins.add(child1.getBinList().get(i));
		}

		// umetanje izabranih binova u drugo dijete
		child2.insertBinsAt(insertionPoint, selectedBins);
	
		// binovi u novom kromosomu koji se sastoje od binova djeteta2, ali bez 
		// stapova koji su u odabranim binovima
		List<Bin> binsForNewChromosome = new ArrayList<>();
		
		// petlja koja ide od pocetka pa do mjesta za ubacaj u drugom djetetu
		for (int i = 0; i < insertionPoint; i++) {

			Bin child2Bin = child2.getBinList().get(i);
			// bin koji sadrzi stapove koji se ne pojavljuju u izabranim binovima
			Bin binForNewChromosome = new Bin(child2.getBinCapacity());
			
			for (Stick child2Stick : child2Bin.getSticksInBin()) {
				// ako izabrani bin sadrzi trenutno provjeravani stap
				boolean contains = false;
				for (Bin b : selectedBins) {
					if (b.getSticksInBin().contains(child2Stick)) {
						contains = true;
						break;
					}
				}
				// ako stap nije u niti jednom odabranom binu dodajemo ga u bin za novi kromosom
				if(!contains) binForNewChromosome.add(child2Stick);
			}
			// ako je bin za novi kromosom prazan, ne dodajemo ga
			if(binForNewChromosome.getSticksInBin().size() > 0) binsForNewChromosome.add(binForNewChromosome);
		}

		// petlja za dodavanje izabranih binova
		for(int i = 0; i < selectedBins.size(); i++) {
			binsForNewChromosome.add(selectedBins.get(i));
		}
		
		// petlja za dodavanja binova koji su poslije izabranog mjesta ubacaja u djetetu2
		for (int i = insertionPoint + selectedBins.size() - 1; i < child2.size(); i++) {

			Bin child2Bin = child2.getBinList().get(i);

			Bin binForNewChromosome = new Bin(child2.getBinCapacity());

			for (Stick child2Stick : child2Bin.getSticksInBin()) {

				boolean contains = false;
				for (Bin b : selectedBins) {
					if (b.getSticksInBin().contains(child2Stick)) {
						contains = true;
						break;
					}
				}
				if(!contains) binForNewChromosome.add(child2Stick);
			}
			if(binForNewChromosome.getSticksInBin().size() > 0) binsForNewChromosome.add(binForNewChromosome);
		}

		// vadjenje stapova iz binova u svrhu sortiranja po redu prilikom stvaranja novog kromosoma 
		List<Stick> newSticks = new ArrayList<>();
		for(int i = 0; i < binsForNewChromosome.size(); i++) {
			for(int j = 0; j < binsForNewChromosome.get(i).getSticksInBin().size(); j++) {
				newSticks.add(binsForNewChromosome.get(i).getSticksInBin().get(j));
			}
		}
		
		return new Chromosome(newSticks, child2.getBinCapacity());
	}

}
