package hr.fer.zemris.optjava.dz12.test;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Test {

	JFrame frame = new JFrame();
	JButton[][] grid;
	int[][] mapData;
	Icon antIcon;
	private int row = 0;
	private int column = 0;
	int width;
	int height;

	int degrees = 0;

	// slika mrava u svakoj mogucoj orijentaciji
	ImageIcon ant0;
	ImageIcon ant90;
	ImageIcon ant180;
	ImageIcon ant270;

	ImageIcon food;

	Random rand;
	
	int foodEaten = 0;

	public Test(int width, int height) {

		this.rand = new Random();

		this.width = width;
		this.height = height;

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

				step();

			}
		});

		buttonPanel.add(left);
		buttonPanel.add(step);
		buttonPanel.add(right);

		frame.add(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Funkcija koja okrece mrava lijevo za 90 stupnjeva
	 * 
	 */
	public void leftTurn() {
		degrees += 270;
		degrees = degrees % 360;
		setAntIcon(degrees);
	}

	/**
	 * Funkcija koja okrece mrava desno za 90 stupnjeva
	 * 
	 */
	public void rightTurn() {
		degrees += 90;
		degrees = degrees % 360;
		setAntIcon(degrees);
	}

	/**
	 * Funkcija koja pomice mrava za jedan korak u smjeru odredjenom s
	 * <code>degrees</code>
	 * 
	 */
	public void step() {
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
		if(mapData[row][column] == 1) {
			foodEaten++;
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
	public void setAntIcon(int degrees) {

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

	public void walk(int numOfMoves) {

		int[] moves = generateMoves(numOfMoves);

		for (int i = 0; i < moves.length; i++) {

			switch (moves[i]) {

			case 0:
				leftTurn();
				break;
			case 1:
				rightTurn();
				break;
			case 2:
				step();
				break;
			default:
				break;
			}

			try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}

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

	public void loadMap(String pathToMap) {

		try (BufferedReader br = new BufferedReader(new FileReader(pathToMap))) {

			String line = br.readLine();

			String[] dimension = line.split("x");

			width = Integer.parseInt(dimension[0]);
			height = Integer.parseInt(dimension[1]);

			mapData = new int[height][width];

			line = br.readLine();

			int row = 0;

			while (line != null) {

				for (int column = 0; column < height; column++) {
					if (line.charAt(column) == '1') {
						mapData[row][column] = 1;
						grid[row][column].setIcon(food);
						
					}else {
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
