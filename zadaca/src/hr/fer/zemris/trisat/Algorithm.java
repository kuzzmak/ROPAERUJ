package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Algorithm {
	private SATFormula formula;
	private int numberOfTheAlgorithm;
	
	public Algorithm(SATFormula formula, int numberOfTheAlgorithm) {
		this.formula = formula;
		this.numberOfTheAlgorithm = numberOfTheAlgorithm;
	}
	
	// metoda za odabir zeljenog algoritma za rjesavanje problema
	public String solve() {
		String solution = null;
		switch(numberOfTheAlgorithm) {
			case 1:
				solution = bruteforce();
				break;
			case 2:
				solution = IteratedLocalSearchV1();
				break;
			case 3:
				solution = alg3();
				break;
			case 4:
				solution = GSAT();
				break;
			case 5:
				solution = RandomWalkSAT();
				break;
			case 6:
				solution = IteratedLocalSearchV2();
				break;
			default:
				break;
		}
		if(solution != null) return solution;
		return null;
	}
	/**
	 * Funkcija za pretvorbu string reprezentacije rjesenja u BitVector
	 * 
	 * @param vec string koji je potrebno pretvoriti
	 * @return BitVector reprezentacija predanog vektora
	 */
	public BitVector transformStringToBoolean(String vec) {
		
		List<Boolean> bitList= new ArrayList<>();
		int length = vec.length();
		
		for(int index = 0; index < length; index++) {
			char temp = vec.charAt(index);
			switch(temp) {
			case '1':
				bitList.add(true);
				break;
			case '0':
				bitList.add(false);
				break;
			default:
				break;
			}
		}
		return new BitVector(bitList);
	}
	/**
	 * Algoritam koji prodje i ispita sva moguca rjesenja
	 * @return String rjesenja
	 */
	public String bruteforce(){
		
		List<String> solutions = new ArrayList<>();
		StringBuilder sb = new StringBuilder("");
		
		int numOfVar = formula.getNumberOfVariables();
		
		for(int i = 0; i < Math.pow(2, numOfVar); i++) {
			// pretvorba int-a u binarni string
			sb.append(Integer.toBinaryString(i));
			// ako binarni string nije dovoljno dugacak, doda mu se odredjen broj nula
			while(sb.length() < numOfVar) {
				sb.insert(0, '0');
			}
			
			BitVector assignment = transformStringToBoolean(sb.toString());
			
			if(formula.isSatisfied(assignment)) solutions.add(sb.toString());
			
			sb.delete(0, sb.length());
		}
		// odabir recimo prvog od rjesenja
		if(solutions.size() != 0) return solutions.get(0);
		return null;
	}
	
	public String IteratedLocalSearchV1(){
		
		Random rand = new Random();
		BitVector currentSolution = new BitVector(rand, formula.getNumberOfVariables());
		int currentFitness = fitness(currentSolution, formula);
		
		if(currentFitness == formula.getNumberOfClauses()) return currentSolution.toString(); 
		
		for(int i = 0; i < 100000; i++) {
			
			BitVectorNGenerator ng = new BitVectorNGenerator(new MutableBitVector(currentSolution.bits));
			List<MutableBitVector> neighbours = ng.createNeighborhood();
			
			Map<MutableBitVector, Integer> fit = new HashMap<>();	
			for(MutableBitVector mbv: neighbours) {
				fit.put(mbv, fitness(mbv, formula));
			}
			
			int maxFit = 0;
			for(Map.Entry<MutableBitVector, Integer> m: fit.entrySet()){  			
				if(m.getValue() >= maxFit) {
					maxFit = m.getValue();
				}
			}
		
			List<MutableBitVector> bestNeighbours = new ArrayList<>();
			for(Map.Entry<MutableBitVector, Integer> m: fit.entrySet()){
				if(m.getValue() == maxFit) bestNeighbours.add(m.getKey());
			}
			
//			for(MutableBitVector n: neighbours) {
//				System.out.println(n + " " + fitness(n, formula));
//			}
//			for(MutableBitVector n: bestNeighbours) {
//				System.out.println("\t" + n + " " + fitness(n, formula));
//			}
			
			BitVector bestNeighbour = bestNeighbours.get(rand.nextInt(bestNeighbours.size()));
//			System.out.println("\t\t" + bestNeighbour + " " + fitness(bestNeighbour, formula));
			
			if(maxFit < currentFitness) {
				break;
			}else if(maxFit == formula.getNumberOfClauses()){
				currentSolution = bestNeighbour;
				currentFitness = maxFit;
				break;
			}
			else {
				currentSolution = bestNeighbour;
				currentFitness = maxFit;
			}
		}
		return currentSolution.toString();
	}
	
	/**
	 * Algoritam koji
	 * 
	 * @return
	 */
	public String alg3() {
		
		int percentageUnitAmount = 50;
		int numberOfBest = 2;
		Random rand = new Random();
		// trenutno najbolje rjesenje
		BitVector currentSolution = new BitVector(rand, formula.getNumberOfVariables());
		
		SATFormulaStats formulaStats = new SATFormulaStats(formula);
		
		// azuriranje statistike o klauzulama
		Float[] post = formulaStats.getPost();
		
		while(true) {
			
			BitVectorNGenerator ng = new BitVectorNGenerator(new MutableBitVector(currentSolution.bits));
			List<MutableBitVector> neighbours = ng.createNeighborhood();
			
			formulaStats.setAssignment(currentSolution, true);
			// mapa kojoj je kljuc susjed, a vrijednost broj zadovoljenih klauzula
			Map<MutableBitVector, Float> fit = new LinkedHashMap<>();	
			for(MutableBitVector mbv: neighbours) {
				fit.put(mbv, (float)fitness(mbv, formula));
				
				for(int index = 0; index < formula.getNumberOfClauses(); index++) {
					if(formula.getClause(index).isSatisfied(mbv)) {
						fit.put(mbv, fit.get(mbv) + percentageUnitAmount * (1 - post[index]));
					}else {
						fit.put(mbv, fit.get(mbv) - percentageUnitAmount * (1 - post[index]));
					}
				}
			}
			
			fit = sortByValue(fit);
			
			// najbolja rjesenja iz mape fit
			List<MutableBitVector> bestNeighbours = new ArrayList<>();
			
			int count = 0;
			for(Map.Entry<MutableBitVector, Float> entry: fit.entrySet()) {
				bestNeighbours.add(entry.getKey());
				count++;
				if(count == numberOfBest) break;
			}
			
			// kao rjesenje se uzima nasumicno jedno iz liste bestNeighbours
			// ako ih ima vise
			currentSolution = bestNeighbours.get(rand.nextInt(bestNeighbours.size()));
			//System.out.println(currentSolution);
			//System.out.println(fitness(currentSolution, formula));
			if(fitness(currentSolution, formula) == formula.getNumberOfClauses()) return currentSolution.toString();
		}
	}
	
	/**
	 *GSAT(klauzule,varijable,MAX-FLIPS,MAX-TRIES)
	 *for restart = 1 do MAX-TRIES
  	 *	T = random dodjela varijabli
     *  for promjena = 1 do MAX-FLIPS
     *  	ako T zadovoljava klauzule, vrati T
     *		V je varijabla cijom promjenom dobivam 
	 *			minimalni broj nezadovoljenih klauzula
     *		ako V nije jedinstven, random odaberi jedan
     * 		promijeni u T vrijednost varijable V
  	 * 	kraj
	 *kraj
	 *vrati "dodjela nije pronadjena"
	 * 
	 * @return String vrijednost rjesenja
	 */
	public String GSAT() {
		
		int maxFlips = 5;
		int maxTries = 1000;
		
		Random rand = new Random();
		
		BitVector solution = null;
		
		// ukupan broj iteracija
		for(int i = 0; i < maxTries; i++) {
			//System.out.println(i);
			BitVector currentSolution = new BitVector(rand, formula.getNumberOfVariables());
			
			// petlja za broj bitova koji se okrenu na rjesenju
			for(int j = 0; j < maxFlips; j++) {
				
				// ako je trnutno rjesenje zadovoljivo, algoritam zavrsava
				if(formula.isSatisfied(currentSolution)) return currentSolution.toString();
				
				// lista rjesenja
				List<MutableBitVector> sol = new ArrayList<>();
				
				// mapa gdje je kljuc neko od rjesenja, a vrijednost njegova dobrota
				Map<MutableBitVector, Float> fit = new LinkedHashMap<>();
				
				// petlja koja modificira trenutno rjesenje na drugoj poziciji u
				// svakoj iteraciji i izracunava dobrotu toga rjesenja
				for(int k = 0; k < formula.getNumberOfVariables(); k++) {
					MutableBitVector temp = new MutableBitVector(currentSolution.bits);
					temp.set(k, !temp.get(k));
					fit.put(temp, (float)fitness(temp, formula));
				}
				
				fit = sortByValue(fit);
				
				// nakon izracuna i sortiranja dobrote, nađe se najbolje rjesenje
				// koje zatim postaje kao pocetno rjesenje u sljedecoj iteraciji
				float best = 0.0f;
				for(Map.Entry<MutableBitVector, Float> entry: fit.entrySet()) {
					if(entry.getValue() < best) break;
					sol.add(entry.getKey());
					best = entry.getValue();
				}
				
				currentSolution = sol.get(rand.nextInt(sol.size()));
				solution = currentSolution;
			}
		}
		return solution.toString();
	}
	
	
	/**
	 *RandomWalkSAT(klauzule,varijable,MAX-FLIPS,MAX-TRIES)
	 *--------------------------------------------------------------------
	 *Ideja: u svakom koraku:
  	 *	random odaberi jednu nezadovoljenu klauzulu
  	 *	+- s vjerojatnosću p random okreni jednu varijablu u toj klauzuli
  	 *	+- s vjerojatnosću (1-p) okreni onu varijablu koja rezultira 
     * 		novom dodjelom s minimalnim brojem nezadovoljenih klauzula
	 * --------------------------------------------------------------------
	 *for restart = 1 do MAX-TRIES
  	 * 	T = random dodjela varijabli
  	 * 	for promjena = 1 do MAX-FLIPS
     *		ako T zadovoljava klauzule, vrati T
     *		random odaberi jednu nezadovoljenu klauzulu
     *		+- s vjerojatnosću p random okreni jednu varijablu u toj klauzuli
     *		+- s vjerojatnosću (1-p) okreni onu varijablu koja rezultira 
     * 	  	novom dodjelom s minimalnim brojem nezadovoljenih klauzula
  	 * 	kraj
	 *kraj
	 *vrati "dodjela nije pronađena"
	 *
	 * @return vraća se jedno od rjesenja 
	 */
	public String RandomWalkSAT() {
		// broj okreta varijabli u nekom rjesenju
		int maxFlips = 5;
		// broj iteracija
		int maxTries = 1000;
		// vjerojatnost da se okrene neka varijabla u klauzuli
		float p = 0.3f;
		
		Random rand = new Random();
		
		BitVector solution = null;
		
		for(int i = 0; i < maxTries; i++) {
			//System.out.println(i);
			BitVector bv = new BitVector(rand, formula.getNumberOfVariables());
			// pretvorba u MultiBitVector radi mogućnosti koristenja metode set()
			MutableBitVector currentSolution = new MutableBitVector(bv.bits);
			
			for(int j = 0; j < maxFlips; j++) {
				
				// ako je generirano rjesenje, algoritam zavrsava
				if(formula.isSatisfied(currentSolution)) return currentSolution.toString();
	
				// odabir neke klauzule koja nije zadovoljena trenutnim rjesenjem
				Clause unsatisfied = pickUnsatisfiedClause(formula, currentSolution);
				int varNum = 0;
				
				// vjerojatnoscu p okrenemo jednu varijablu u klauzuli
				if(rand.nextFloat() <= p) {
					// random izaberemo index neke varijable obzirom na velicinu klauzule
					varNum = unsatisfied.getLiteral(rand.nextInt(unsatisfied.getSize()));
					// mijenja se bit u trenutnom rjesenju na indeksu izabrane varijable
					currentSolution.set(Math.abs(varNum) - 1, !currentSolution.get(Math.abs(varNum) - 1));
				}
				
				// vjerojatnosću 1 - p okrenemo onu varijablu koja rezultira
				// najvecim brojem zadovoljenih klauzula
				if(rand.nextFloat() <= (1 - p)) {
					
					// mapa za pamcenje dobrote rjesenja
					Map<MutableBitVector, Float> fit = new LinkedHashMap<>();
					// lista indexa u nezadovoljenoj klauzuli
					List<Integer> indexes = unsatisfied.getIndexes();
					// za svaki index se racuna dobrota rjesenja
					for(Integer in: indexes) {
						int tempFit = 0;
						MutableBitVector tempSol= currentSolution.copy();
						tempSol.set(Math.abs(in) - 1, !currentSolution.get(Math.abs(in) - 1));
						// dobrota trnutnog rjesenja kojem je modificiran bit na 
						// indexima koji se nalaze u listi "indexes"
						tempFit = fitness(tempSol, formula);
						fit.put(tempSol, (float)tempFit);
					}
					
					fit = sortByValue(fit);
					
					// lista s najboljim rjesenjima
					List<MutableBitVector> sol = new ArrayList<>();
					float best = 0.0f;
					// petlja za pronalazak najboljeg/ih u mapi fit
					for(Map.Entry<MutableBitVector, Float> entry: fit.entrySet()) {
						if(entry.getValue() < best) break;
						sol.add(entry.getKey());
						best = entry.getValue();
					}
					
					// odabir jednog od najboljih rjesenja u slucaju da ih ima vise
					currentSolution = sol.get(rand.nextInt(sol.size()));
					solution = currentSolution;
				}
			}
		}
		return solution.toString();
	}
	
	/**
	 * Algoritam iterativne lokalne pretrage s modifikacijom u slucaju kada
	 * dodje do zastoja da se svaka varijabla s odredjenim postotkom okrene.
	 * 
	 * @return String rjesenje
	 */
	public String IteratedLocalSearchV2() {
		// postotak varijabli koje se mijanjaju ako algoritam zapne
		float p = 0.3f;
		Random rand = new Random();
		BitVector currentSolution = new BitVector(rand, formula.getNumberOfVariables());
		int currentFitness = fitness(currentSolution, formula);
		
		if(currentFitness == formula.getNumberOfClauses()) return currentSolution.toString(); 
		
		while(true) {
			BitVectorNGenerator ng = new BitVectorNGenerator(new MutableBitVector(currentSolution.bits));
			List<MutableBitVector> neighbours = ng.createNeighborhood();
			
			Map<MutableBitVector, Integer> fit = new HashMap<>();	
			for(MutableBitVector mbv: neighbours) {
				fit.put(mbv, fitness(mbv, formula));
			}
			
			int maxFit = 0;
			for(Map.Entry<MutableBitVector, Integer> m: fit.entrySet()){  			
				if(m.getValue() >= maxFit) {
					maxFit = m.getValue();
				}
			}
			//System.out.println(maxFit);
			List<MutableBitVector> bestNeighbours = new ArrayList<>();
			for(Map.Entry<MutableBitVector, Integer> m: fit.entrySet()){
				if(m.getValue() == maxFit) bestNeighbours.add(m.getKey());
			}
			
			BitVector bestNeighbour = bestNeighbours.get(rand.nextInt(bestNeighbours.size()));
			
			if(maxFit <= currentFitness) {
				MutableBitVector temp = new MutableBitVector(currentSolution.bits);
				for(int index = 0; index < temp.getSize(); index++) {
					if(rand.nextFloat() < p) temp.set(index, !temp.get(index));
				}
				currentSolution = temp;
			}else if(maxFit == formula.getNumberOfClauses()){
				currentSolution = bestNeighbour;
				currentFitness = maxFit;
				break;
			}
			else {
				currentSolution = bestNeighbour;
				currentFitness = maxFit;
			}
		}
		return currentSolution.toString();
	}
	
	
	/**
	 * Funkcija služi za izracun dobrote određenog rjesenja tipa BitVector.
	 * Dobrota je izracunata kao broj zadovoljenih klauzula neke formule.
	 * 
	 * @param assignment BitVector kojeg se izracunava fitness
	 * @param formula SATFormula koju treba zadovoljiti
	 * @return int vrijednost broja zadovoljenih klauzula
	 */
	public int fitness(BitVector assignment, SATFormula formula) {
		int satisfiedClauses = 0;
		
		for(int i = 0; i < formula.getNumberOfClauses(); i++) {
			if(formula.getClause(i).isSatisfied(assignment)) satisfiedClauses++;
		}
		return satisfiedClauses;
	}
	
	/**
	 * Funkcija sluzi za sortiranje mape silaznim redoslijedom, a moguce
	 * je da postoji vise istih vrijednosti
	 * 
	 * @param wordCounts mapa koju je potrebno sortirati
	 * @return sortirana mapa tipa LinkedHashMap
	 */
	public static Map<MutableBitVector, Float> sortByValue(final Map<MutableBitVector, Float> map) {
        return map.entrySet()
                .stream()
                .sorted((Map.Entry.<MutableBitVector, Float>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
	
	/**
	 * Funkcija sluzi za dohvat jedne random odabrane nezadovoljene klauzule
	 * tipa Clause, ako ne postoji vraća se null
	 * 
	 * @param formula zadana formula 
	 * @param assignment BitVector potencijalnog rjesenja
	 * @return nezadovoljena Clause ako postoji
	 */
	public Clause pickUnsatisfiedClause(SATFormula formula, BitVector assignment) {
		
		List<Clause> unsatisfiedClauses = new ArrayList<>();
		Random rand = new Random();
		
		for(int index = 0; index < formula.getNumberOfClauses(); index++) {
			// ako klauzula ne zadovoljava, stavimo ju u listu 
			if(!formula.getClause(index).isSatisfied(assignment)) { 
				unsatisfiedClauses.add(formula.getClause(index));
			}
		}
		
		// ako postoji neka klauzula u listi
		if(unsatisfiedClauses.size() != 0) {
			return unsatisfiedClauses.get(rand.nextInt(unsatisfiedClauses.size()));
		}else {
			return null;
		}
	}

	
}
