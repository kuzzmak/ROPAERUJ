package hr.fer.zemris.optjava.dz12;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Expression.Expression;
import hr.fer.zemris.optjava.dz12.Expression.Status;
import hr.fer.zemris.optjava.dz12.Terminal.Action;
import hr.fer.zemris.optjava.dz12.Terminal.Terminal;

public class AntTrail {

	private static int maxIterations;
	private static int populationSize;
	private static int minFitness;
	private static int maxDepth;
	private static int maxSteps;
	private static float p;
	private static int k;
	private static int maxInitialDepth;
	private static int maxNodes;

	// matrica gumba koji predstavljaju pojedino polje
	static JButton[][] grid;
	// matrica s hranom
	static int[][] mapData;
	// pomocna matrica s hranom
	static int[][] tempMapData;
	// broj stupaca mape
	static int width;
	// broj redaka mape
	static int height;

	// slika mrava u svakoj mogucoj orijentaciji
	static ImageIcon ant0;
	static ImageIcon ant90;
	static ImageIcon ant180;
	static ImageIcon ant270;

	// slike strelice u svakoj poziciji
	static ImageIcon arrow0;
	static ImageIcon arrow90;
	static ImageIcon arrow180;
	static ImageIcon arrow270;

	// slika hrane
	static ImageIcon food;

	// brojac pojedene hrane, fitnes
	static int foodEaten = 0;
	// trenutni redak rava
	private static int row = 0;
	// tenutni stupac mrava
	private static int column = 0;
	// usmjerenje mrava, moze biti 0, 90, 180 i 270
	static int degrees = 0;
	// labela koja prikazuje broj pojedene hrane
	static JLabel score = new JLabel("Score: 0");
	// lista akcija koje su se dogodile do sada slijedno jedna iza druge
	static List<Action> actionsTaken = new ArrayList<>();
	// brojac za kretanje korak po korak
	static int currentStep = 0;
	// maksimalan broj koraka koje mrav moze napraviti
	static int maxStepsAllowed;
	// slika strelice usmjerenja mrava
	static JLabel arrowPicture;
	// za ispis koraka mrava
	static JTextArea textArea;

	static Random rand;
	// fitnes pojedinog stabla
	static List<Float> fitness;

	private static List<DefaultMutableTreeNode> population = new ArrayList<>();

	public AntTrail(String pathToMap, int maxIterations, int populationSize, int minFitness, int maxDepth,
			int maxSteps, float p, int k, int maxInitialDepth, int maxNodes) {

		AntTrail.maxIterations = maxIterations;
		AntTrail.populationSize = populationSize;
		AntTrail.minFitness = minFitness;
		AntTrail.maxDepth = maxDepth;
		AntTrail.maxSteps = maxSteps;
		AntTrail.p = p;
		AntTrail.k = k;
		AntTrail.maxInitialDepth = maxInitialDepth;
		AntTrail.maxNodes = maxNodes;

		rand = new Random();

		loadMap(pathToMap);
	}

	public static List<DefaultMutableTreeNode> sortList(List<DefaultMutableTreeNode> population, List<Float> fitness) {

		List<DefaultMutableTreeNode> sorted = new ArrayList<>();
		
		List<Float> tempFit = new ArrayList<>();
		tempFit.addAll(fitness);
		
		while(sorted.size() < population.size()) {
			
			int index = findBest(tempFit);
			
			sorted.add(population.get(index));
			
			tempFit.set(index, (float) -1);
		}
		
	    return sorted;
	}
	
	public static void run() {

		population = Util.makePopulation(populationSize, maxInitialDepth, rand, maxNodes);
		
		evaluate(population);
		
//		population = sortList(population, fitness);

		int currentIteration = 0;
		
		float currentBestFit = Float.MIN_VALUE;
		
		
		while(currentIteration < maxIterations && currentBestFit < minFitness) {
			
			List<DefaultMutableTreeNode> offSpring = new ArrayList<>();
			
			// dodavanje prvih firstN najboljih jedinki u novu populaciju
			offSpring.add(population.get(findBest(fitness)));
			
			while(offSpring.size() < population.size()) {
				
				DefaultMutableTreeNode parent01 = tournamentSelect(k, population, rand);
				DefaultMutableTreeNode parent02 = tournamentSelect(k, population, rand);
				
				while(parent01 == parent02) parent02 = selectParent(population);
				
				List<DefaultMutableTreeNode> children = Util.cross(parent01, parent02, rand);
				
				DefaultMutableTreeNode child1 = children.get(0);
				
				DefaultMutableTreeNode child2 = children.get(1);
				
//				Util.mutate(child1, maxDepth, rand, p);
//				Util.mutate(child2, maxDepth, rand, p);
				
				offSpring.add(child1);
				offSpring.add(child2);
			}

			evaluate(offSpring);
//			offSpring = sortList(offSpring, fitness);
			
			population = new ArrayList<>();
			// uzimanje prvih populationSize
			population.addAll(offSpring);
			
//			int max = 0;
//			
//			for(int i = 0; i < populationSize; i++) {
//				if(population.get(i).getDepth() > max) max = population.get(i).getDepth(); 
//			}
			
			currentBestFit = fitness.get(findBest(fitness));
			
			System.out.println("iter: " + currentIteration + ", bestfit: " + currentBestFit);
			
			currentIteration++;
		}
	}
	
	/**
	 * Funkcija za odabir roditelja proporcionalnom selekcijom iz populacije.
	 * 
	 * @param population populacija iz koje se bira roditelj
	 * @return odabran roditelj iz populacije 
	 */
	public static DefaultMutableTreeNode selectParent(List<DefaultMutableTreeNode> population) {
		
		float sum = 0;
		
		List<Float> tempFit = new ArrayList<>();
		tempFit.addAll(fitness);
		
		for(int i = 0; i < tempFit.size(); i++) {
			sum += tempFit.get(i);
		}
		
		for(int i = 0; i < tempFit.size(); i++) {
			tempFit.set(i, tempFit.get(i) / sum);
		}
		
		float p = rand.nextFloat();
		
		float cumSum = 0;
		
		for(int i = 0; i < tempFit.size(); i++) {
			cumSum += tempFit.get(i);
			if(cumSum > p) return population.get(i);
		}
		
		return population.get(population.size() - 1);
	}
	
	/**
	 * Funkcija za odabir roditelja k turnirskom selekcijom
	 * 
	 * @param k broj turnira
	 * @param population populacija iz koje se bira roditelj
	 * @param rand generator slucajnih brojeva
	 * @return izabran roditelj
	 */
	public static DefaultMutableTreeNode tournamentSelect(int k, List<DefaultMutableTreeNode> population, Random rand) {
		
		List<Integer> indexes = new ArrayList<>();
		
		int index = rand.nextInt(population.size());
		
		while(indexes.size() < k) {
			
			while(indexes.contains(index)) index = rand.nextInt(population.size());
			
			indexes.add(index);
		}
		
		int best = indexes.get(0);
		
		for(int i = 0; i < k; i++) {
			
			if(fitness.get(i) > fitness.get(best)) best = i;
		}
		
		return population.get(best);
	}
	
	

	public static void gui() {

		JFrame frame = new JFrame();

		// glavni container
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		// lijevi container s
		JPanel containerLeft = new JPanel();
		containerLeft.setLayout(new BoxLayout(containerLeft, BoxLayout.Y_AXIS));

		JPanel gridPanel = new JPanel(new GridLayout(height, width));
		containerLeft.add(gridPanel);

		// ucitavanje slike hrane
		food = new ImageIcon("pictures/food.png");
		food = Util.resizeImageIcon(food);

		grid = new JButton[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {

				JButton button = new JButton();
				button.setPreferredSize(new Dimension(20, 20));
				grid[i][j] = button;

				if (mapData[i][j] == 1) {
					grid[i][j].setIcon(food);
				}

				gridPanel.add(button);
			}
		}

		// postavljanje inicijalnog mrava
		ant0 = new ImageIcon("pictures/ant0.png");
		ant0 = Util.resizeImageIcon(ant0);
		grid[0][0].setIcon(ant0);

		// ucitavanje ostalih slika mrava
		ant90 = new ImageIcon("pictures/ant90.png");
		ant90 = Util.resizeImageIcon(ant90);

		ant180 = new ImageIcon("pictures/ant180.png");
		ant180 = Util.resizeImageIcon(ant180);

		ant270 = new ImageIcon("pictures/ant270.png");
		ant270 = Util.resizeImageIcon(ant270);

		// ucitavanje slika strelice koja pokazuje usmjerenje mrava posto je ikona mrava
		// premala
		arrow0 = new ImageIcon("pictures/arrow0.png");

		arrow90 = new ImageIcon("pictures/arrow90.png");

		arrow180 = new ImageIcon("pictures/arrow180.png");

		arrow270 = new ImageIcon("pictures/arrow270.png");

		boolean animate = true;

		// panel s gumbima
		JPanel buttonPanel = new JPanel();
		containerLeft.add(buttonPanel);

		// gumb za okret mrava udesno
		JButton right = new JButton("right");
		right.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				rightTurn(animate);

			}
		});

		JButton left = new JButton("left");
		left.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				leftTurn(animate);

			}
		});

		JButton move = new JButton("step");
		move.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				move(animate);

			}
		});

		JButton step = new JButton("step");
		step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				step(true);

			}
		});

		JButton isFood = new JButton("isFood");
		isFood.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (isFoodInFront()) {
					System.out.println("hrana ispred");
				} else {
					System.out.println("hrana nije ispred");
				}
			}
		});

		JButton automatic = new JButton("Automatic");
		automatic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				automatic(true, 500);
			}
		});

		// container sa slikom strelice
		JPanel containerRight = new JPanel();
		containerRight.setLayout(new BoxLayout(containerRight, BoxLayout.Y_AXIS));

		containerRight.add(Box.createHorizontalGlue());
		JLabel direction = new JLabel("Usmjerenje mrava");
		containerRight.add(direction);

		arrowPicture = new JLabel(arrow0);
		containerRight.add(arrowPicture);

		// TODO napraviti da text area funkcionira
//				textArea = new JTextArea();
//				JScrollPane scrollPane = new JScrollPane(textArea);
//				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//				
//				textArea.setColumns(5);
//		        textArea.setLineWrap(true);
//		        textArea.setRows(5);
//		        textArea.setWrapStyleWord(true);
//				
//				containerRight.add(textArea);

		container.add(containerLeft);
		container.add(containerRight);

		buttonPanel.add(score);
		buttonPanel.add(left);
		buttonPanel.add(step);
		buttonPanel.add(right);
		buttonPanel.add(isFood);
		buttonPanel.add(step);
		buttonPanel.add(automatic);

		frame.add(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Funkcija za evaluaciju pojedine populacije stabala
	 * 
	 * @param population populacija koja se evaluira
	 */
	public static void evaluate(List<DefaultMutableTreeNode> population) {

		fitness = new ArrayList<>();
		reset();
		

		for (int i = 0; i < population.size(); i++) {

			executeNode(population.get(i), false);
			
			fitness.add((float) foodEaten);
			reset();
		}
	}

	public static int findBest(List<Float> fitness) {

		Float best = (float) -1;
		int index = 0;

		for (int i = 0; i < fitness.size(); i++) {
			if (fitness.get(i) > best) {
				index = i;
				best = fitness.get(index);
			}
		}
		return index;
	}
	
	

	/**
	 * Funkcija za automatsko izvodjenje generiranog stabla
	 * 
	 * @param animate prikazuju li se pokreti mrava ili ne
	 * @param sleep   vrijeme izmedju dva koraka u ms
	 */
	public static void automatic(boolean animate, long sleep) {

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {

				for (int i = 0; i < actionsTaken.size(); i++) {

//					step(animate);
//				
//					if (sleep != 0) {
//						try {
//							Thread.sleep(sleep);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
					reset();
					executeNode(population.get(findBest(fitness)), animate);
				}
				return (null);
			}
		};

		worker.execute();
	}

	/**
	 * Funkcija koja sluzi za kretanje korak po korak mrava po mapi
	 * 
	 * @param animate prikazuju li se pokreti mrava ili ne
	 */
	public static void step(boolean animate) {

		if (currentStep < actionsTaken.size() && currentStep < maxSteps) {
			
			Action a = actionsTaken.get(currentStep);

			if (a == Action.LEFT) {
				leftTurn(animate);
			} else if (a == Action.RIGHT) {
				rightTurn(animate);
			} else {
				move(animate);
			}
		} else {
			return;
		}
		currentStep++;
	}

	/**
	 * Funkcija za vracanje parametara igre na pocetne vrijednosti
	 * 
	 */
	public static void reset() {

		row = 0;
		column = 0;
		degrees = 0;
		foodEaten = 0;
		currentStep = 0;
		score.setText("Score: 0");
		actionsTaken = new ArrayList<>();
		tempMapData = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				tempMapData[i][j] = mapData[i][j] == 1 ? 1 : 0;
			}
		}
	}

	/**
	 * Funkcija koja okrece mrava lijevo za 90 stupnjeva
	 * 
	 * @param animate prikazuje li se ili ne pomak mrava na polju
	 * 
	 */
	public static void leftTurn(boolean animate) {
		degrees += 270;
		degrees = degrees % 360;
		if (animate)
			setAntIcon(degrees);

	}

	/**
	 * Funkcija koja okrece mrava desno za 90 stupnjeva
	 * 
	 * @param animate prikazuje li se ili ne pomak mrava na polju
	 * 
	 */
	public static void rightTurn(boolean animate) {
		degrees += 90;
		degrees = degrees % 360;
		if (animate)
			setAntIcon(degrees);
	}

	/**
	 * Funkcija koja pomice mrava za jedan korak u smjeru odredjenom s
	 * <code>degrees</code>
	 * 
	 * @param animate prikazuje li se ili ne pomak mrava na polju
	 * 
	 */
	public static void move(boolean animate) {

		if (animate)
			grid[row][column].setIcon(null);

		switch (degrees) {

		case 0:

			column++;
			column += width;
			column %= width;

			break;

		case 90:

			row++;
			row += height;
			row %= height;

			break;

		case 180:

			column--;
			column += width;
			column %= width;

			break;

		case 270:

			row--;
			row += height;
			row %= height;

			break;

		default:
			break;
		}

		// ako se nalazi hrana na sljedecoj poziciji povecamo brojac
		if (tempMapData[row][column] == 1) {
			// hrana se uklanja
			tempMapData[row][column] = 0;
			foodEaten++;
			score.setText("Score: " + String.valueOf(foodEaten));
		}

		if (animate)
			setAntIcon(degrees);
	}

	/**
	 * Funkcija za postavljanje ikone mrava ovisno o orijentaciji i lokaciji
	 * 
	 * @param degrees kut pod kojim je mrav usmjeren
	 */
	public static void setAntIcon(int degrees) {

		switch (degrees) {
		case 0:
			grid[row][column].setIcon(ant0);
			arrowPicture.setIcon(arrow0);
			break;
		case 90:
			grid[row][column].setIcon(ant90);
			arrowPicture.setIcon(arrow90);
			break;
		case 180:
			grid[row][column].setIcon(ant180);
			arrowPicture.setIcon(arrow180);
			break;
		case 270:
			grid[row][column].setIcon(ant270);
			arrowPicture.setIcon(arrow270);
			break;
		default:
			break;
		}
	}

	/**
	 * Funkcija za odredjivanje nalazi li se hrana ispred mrava ili ne
	 * 
	 * @return true ako se hrana nalazi ispred mrava, false inace
	 */
	public static boolean isFoodInFront() {

		// pomocne varijable kako se ne bi globalne column i row prepisale
		int tempColumn = column;
		int tempRow = row;

		// simulacija kretanja kao kod move()
		if (degrees == 0) {

			tempColumn++;
			tempColumn += width;
			tempColumn %= width;

		} else if (degrees == 90) {

			tempRow++;
			tempRow += height;
			tempRow %= height;

		} else if (degrees == 180) {

			tempColumn--;
			tempColumn += width;
			tempColumn %= width;

		} else {

			tempRow--;
			tempRow += height;
			tempRow %= height;
		}

		if (mapData[tempRow][tempColumn] == 1)
			return true;

		return false;
	}

	/**
	 * Funkcija koja sluzi za izvodjenje pojedinog cvora stabla.
	 * 
	 * Ako je funkcija {@link hr.fer.zemris.optjava.dz12.Function.IF IF()} tenutni
	 * cvor, prvo se pogleda pomocu funkcije {@link #isFoodInFront()} ako je ispred
	 * mrava hrana i ako je izvodi se lijevo dijete funkcije IF(), a ako nema hrane,
	 * izvodi se desno dijete.
	 * 
	 * Ako je funkcija {@link hr.fer.zemris.optjava.dz12.Function.PR2 PR2()}
	 * trenutni cvor, izvodi se prvo lijevo dijete cvora, a zatim desno.
	 * 
	 * Ako je funkcija {@link hr.fer.zemris.optjava.dz12.Function.PR3 PR3()}
	 * trenutni cvor, izvodi se prvo lijevo dijete, zatim srednje i potom desno
	 * dijete.
	 * 
	 * @param node    cvor koji se trenutno izvodi, obicno root cvor
	 * @param animate osvjezava li se pozicija mrava u gui-ju
	 */
	public static void executeNode(DefaultMutableTreeNode node, boolean animate) {

		Expression e = (Expression) node.getUserObject();

		// ako je trnutni cvor funkcija
		if (e.status == Status.FUNCTION) {

			Enumeration<DefaultMutableTreeNode> en = node.children();

			if (e.name == "IF") {

				// ako je hrana ispred, izvodi se prvi izraz
				if (isFoodInFront()) {
					executeNode(en.nextElement(), animate);
				} else {
					// ako hrana nije ispred izvodi se drugi izraz, zato se
					// poziva en.nextElement() radi micanja prvog clana
					en.nextElement();
					executeNode(en.nextElement(), animate);
				}
			} else if (e.name == "PR2") { // ako je PR2 funkcija
				executeNode(en.nextElement(), animate);
				executeNode(en.nextElement(), animate);
			} else { // ako je PR3 funkcija
				executeNode(en.nextElement(), animate);
				executeNode(en.nextElement(), animate);
				executeNode(en.nextElement(), animate);
			}
		} else {
			// trenutni cvor je terminal
			Terminal t = (Terminal) node.getUserObject();
			executeTerminal(t, animate);
		}
	}

	/**
	 * Funkcija koja sluzi za izvodjenje pojedinog terminala
	 * 
	 * @param t       terminal koji se treba izvesti
	 * @param animate azurira li se kretanje mrava u gui-ju
	 */
	public static void executeTerminal(Terminal t, boolean animate) {

		if (t.action == Action.RIGHT) {

			rightTurn(animate);
			// dodavanje akcije kako bi se moglo klikom na gumb ici akciju po akciju
			actionsTaken.add(Action.RIGHT);

		} else if (t.action == Action.LEFT) {

			leftTurn(animate);
			actionsTaken.add(Action.LEFT);

		} else {

			move(animate);
			actionsTaken.add(Action.MOVE);

		}
	}

	/**
	 * Funkcija za ucitavanje Santa Fe mape iz datoteke
	 * 
	 * @param pathToMap staza do mape
	 */
	public void loadMap(String pathToMap) {

		try (BufferedReader br = new BufferedReader(new FileReader(pathToMap))) {

			String line = br.readLine();

			String[] dimension = line.split("x");

			AntTrail.width = Integer.parseInt(dimension[0]);
			AntTrail.height = Integer.parseInt(dimension[1]);

			// matrica koja sadrzi lokacije hrane
			mapData = new int[height][width];

			line = br.readLine();

			int row = 0;

			while (line != null) {

				for (int column = 0; column < height; column++) {
					if (line.charAt(column) == '1') {
						mapData[row][column] = 1;
					} else {
						mapData[row][column] = 0;
					}
				}
				row++;
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		tempMapData = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				tempMapData[i][j] = mapData[i][j] == 1 ? 1 : 0;
			}
		}
	}

//	public static void main(String[] args) {

//		AntTrail test = new AntTrail(32, 32, true);

//		int populationSize = 20;
//		int maxDepth = 10;
//		Random rand = new Random();
//
//		List<DefaultMutableTreeNode> population = Util.makePopulation(populationSize, maxDepth, rand);
//
//		AntTrail.evaluate(population);
//
//		System.out.println(Arrays.toString(AntTrail.fitness));
//	}

}
