package hr.fer.zemris.optjava.dz12.test;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import hr.fer.zemris.optjava.dz12.Util;
import hr.fer.zemris.optjava.dz12.Expression.Expression;
import hr.fer.zemris.optjava.dz12.Expression.Status;
import hr.fer.zemris.optjava.dz12.Function.IF;
import hr.fer.zemris.optjava.dz12.Function.PR2;
import hr.fer.zemris.optjava.dz12.Function.PR3;
import hr.fer.zemris.optjava.dz12.Terminal.Action;
import hr.fer.zemris.optjava.dz12.Terminal.Terminal;

public class Test {

	JFrame frame = new JFrame();
	static JButton[][] grid;
	static int[][] mapData;
	Icon antIcon;
	private static int row = 0;
	private static int column = 0;
	static int width;
	static int height;

	static int degrees = 0;

	// slika mrava u svakoj mogucoj orijentaciji
	static ImageIcon ant0;
	static ImageIcon ant90;
	static ImageIcon ant180;
	static ImageIcon ant270;

	static ImageIcon food;

	static Random rand;

	static int foodEaten = 0;

	static JLabel score = new JLabel("Score: 0");

	static List<Expression> functions = new ArrayList<>();
	static List<Expression> terminals = new ArrayList<>();

	public Test(int width, int height) {

		rand = new Random();

		Test.width = width;
		Test.height = height;

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		JPanel gridPanel = new JPanel(new GridLayout(height, width));
		container.add(gridPanel);

		grid = new JButton[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(20, 20));
				grid[i][j] = button;
				gridPanel.add(button);
			}
		}

		// postavljanje inicijalnog mrava
		ant0 = new ImageIcon("pictures/ant0.jpg");
		ant0 = resizeImageIcon(ant0);
		grid[0][0].setIcon(ant0);

		// ucitavanje ostalih slika mrava
		ant90 = new ImageIcon("pictures/ant90.jpg");
		ant90 = resizeImageIcon(ant90);

		ant180 = new ImageIcon("pictures/ant180.jpg");
		ant180 = resizeImageIcon(ant180);

		ant270 = new ImageIcon("pictures/ant270.jpg");
		ant270 = resizeImageIcon(ant270);

		// ucitavanje slike hrane
		food = new ImageIcon("pictures/food.png");
		food = resizeImageIcon(food);

		loadMap("C:\\Users\\kuzmi\\OneDrive - fer.hr\\faks\\5sem\\ROPAERUJ\\12zad\\13-SantaFeAntTrail.txt");

		// panel s gumbima
		JPanel buttonPanel = new JPanel();
		container.add(buttonPanel);

		// gumb za okret mrava udesno
		JButton right = new JButton("right");
		right.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				rightTurn();

			}
		});

		JButton left = new JButton("left");
		left.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				leftTurn();

			}
		});

		JButton step = new JButton("step");
		step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				move();

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

		buttonPanel.add(score);
		buttonPanel.add(left);
		buttonPanel.add(step);
		buttonPanel.add(right);
		buttonPanel.add(isFood);

		frame.add(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Funkcija koja okrece mrava lijevo za 90 stupnjeva
	 * 
	 */
	public static void leftTurn() {
		degrees += 270;
		degrees = degrees % 360;
		setAntIcon(degrees);
	}

	/**
	 * Funkcija koja okrece mrava desno za 90 stupnjeva
	 * 
	 */
	public static void rightTurn() {
		degrees += 90;
		degrees = degrees % 360;
		setAntIcon(degrees);
	}

	/**
	 * Funkcija koja pomice mrava za jedan korak u smjeru odredjenom s
	 * <code>degrees</code>
	 * 
	 */
	public static void move() {
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
		if (mapData[row][column] == 1) {
			// hrana se uklanja
			mapData[row][column] = 0;
			foodEaten++;
			score.setText("Score: " + String.valueOf(foodEaten));
		}

		setAntIcon(degrees);
	}

	/**
	 * Funkcija za reskaliranje slika mrava i hrane
	 * 
	 * @param imageIcon slika koja se reskalira
	 * @return reskalirana slika
	 */
	public ImageIcon resizeImageIcon(ImageIcon imageIcon) {
		Image image = imageIcon.getImage();
		Image newimg = image.getScaledInstance(10, 10, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
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
			break;
		case 90:
			grid[row][column].setIcon(ant90);
			break;
		case 180:
			grid[row][column].setIcon(ant180);
			break;
		case 270:
			grid[row][column].setIcon(ant270);
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

		// pomocne varijeble kako se ne bi globalne column i row prepisale
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

		if (mapData[row][tempColumn] == 1)
			return true;

		return false;
	}

//	public void walk(int numOfMoves) {
//
//		int[] moves = generateMoves(numOfMoves);
//
//		for (int i = 0; i < moves.length; i++) {
//
//			switch (moves[i]) {
//
//			case 0:
//				leftTurn();
//				break;
//			case 1:
//				rightTurn();
//				break;
//			case 2:
//				move();
//				break;
//			default:
//				break;
//			}
//
//			try {
//				Thread.currentThread();
//				Thread.sleep(1000);
//			} catch (InterruptedException ie) {
//				ie.printStackTrace();
//			}
//
//		}
//	}

	public static void executeNode(DefaultMutableTreeNode node) {

//		try {
//			Thread.currentThread();
//			Thread.sleep(500);
//		} catch (InterruptedException ie) {
//			ie.printStackTrace();
//		}

		Expression e = (Expression) node.getUserObject();

		// ako je trnutni cvor funkcija
		if (e.status == Status.FUNCTION) {

			Enumeration<DefaultMutableTreeNode> en = node.children();

			if (e.name == "IF") {

				// ako je hrana ispred, izvodi se prvi izraz
				if (isFoodInFront()) {
					executeNode(en.nextElement());
				} else {
					// ako hrana nije ispred izvodi se drugi izraz, zato se
					// poziva en.nextElement() radi micanja prvog clana
					en.nextElement();
					executeNode(en.nextElement());
				}
			} else if (e.name == "PR2") { // ako je PR2 funkcija
				executeNode(en.nextElement());
				executeNode(en.nextElement());
			} else { // ako je PR3 funkcija
				executeNode(en.nextElement());
				executeNode(en.nextElement());
				executeNode(en.nextElement());
			}
		} else {
			Terminal t = (Terminal) node.getUserObject();
			executeTerminal(t.name);
		}
//			else { // ako cvor sadrzi terminale
//
//			// dohvat podatka
//			IFunction f = (IFunction) node.getUserObject();
//			// lista terminala pojedine funkcije
//			List<Expression> exp = f.getOutputs();
//			
//			if (e.name == "IF") {
//
//				// ako je hrana ispred, izvodi se prvi izraz
//				if (isFoodInFront()) {
//					executeTerminal(exp.get(0).name);
//				} else {
//					executeTerminal(exp.get(1).name);
//				}
//			} else if (e.name == "PR2") { // ako je PR2 funkcija
//				executeTerminal(exp.get(0).name);
//				executeTerminal(exp.get(1).name);
//			} else { // ako je PR3 funkcija
//				executeTerminal(exp.get(0).name);
//				executeTerminal(exp.get(1).name);
//				executeTerminal(exp.get(2).name);
//			}
//			
////			executeTerminal(e.name);
//		}

	}

	public static void executeTerminal(String name) {

		if (name == "RIGHT") {
			System.out.println("RIGHT");
			rightTurn();
		} else if (name == "LEFT") {
			System.out.println("LEFT");
			leftTurn();
		} else {
			System.out.println("MOVE");
			move();
		}

	}

	public void walkTree(DefaultMutableTreeNode tree, int numOfMoves) {

		int i = 0;

		while (i < numOfMoves) {

			i++;
		}
	}

	public static int[] generateMoves(int n) {

		Random rand = new Random();

		int[] moves = new int[n];

		for (int i = 0; i < n; i++) {
			moves[i] = rand.nextInt(3);
			System.out.println();
		}

		return moves;
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

			width = Integer.parseInt(dimension[0]);
			height = Integer.parseInt(dimension[1]);

			// matrica koja sadrzi lokacije hrane
			mapData = new int[height][width];

			line = br.readLine();

			int row = 0;

			while (line != null) {

				for (int column = 0; column < height; column++) {
					if (line.charAt(column) == '1') {
						mapData[row][column] = 1;
						grid[row][column].setIcon(food);

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
	}

	public static void main(String[] args) {

		Test test = new Test(32, 32);

		int depth = 10;

		DefaultMutableTreeNode rootnode = Util.makeTree(depth, rand);
		System.out.println(rootnode.getUserObject());

		executeNode(rootnode);
		System.out.println("gotovo");
//		test.walk(20);

//		DefaultMutableTreeNode node = new DefaultMutableTreeNode(new car("bugatti"));
//		
//		node.add(new DefaultMutableTreeNode(new car("nissan")));
//		node.add(new DefaultMutableTreeNode(new car("ferrari")));
//		 
//        Enumeration<DefaultMutableTreeNode> en = node.breadthFirstEnumeration();
//        while (en.hasMoreElements()) {
//            System.out.println(en.nextElement());
//        }

//		loadMap("C:\\Users\\kuzmi\\OneDrive - fer.hr\\faks\\5sem\\ROPAERUJ\\12zad\\13-SantaFeAntTrail.txt");

//		for (int i = 0; i < test.mapData.length; i++) {
//			for(int j = 0; j < test.mapData[i].length; j++) {
//				System.out.printf("%d", test.mapData[i][j]);
//			}
//			System.out.println("\n");
//		}

	}

}
