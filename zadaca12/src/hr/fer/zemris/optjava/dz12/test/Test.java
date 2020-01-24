package hr.fer.zemris.optjava.dz12.test;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test {

	JFrame frame = new JFrame();
	JButton[][] grid;
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

	public Test(int width, int height) {

		this.width = width;
		this.height = height;

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		JPanel gridPanel = new JPanel(new GridLayout(width, height));
		container.add(gridPanel);

		grid = new JButton[width][height];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				JButton button = new JButton(i + " " + j);
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

		// panel s gumbima
		JPanel buttonPanel = new JPanel();
		container.add(buttonPanel);

		// gumb za okret mrava udesno
		JButton right = new JButton("right");
		right.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				rightTurn();

				setAntIcon(degrees % 360);
			}
		});

		JButton left = new JButton("left");
		left.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				leftTurn();
				
				setAntIcon(degrees);
			}
		});

		JButton step = new JButton("step");
		step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				grid[row][column].setIcon(null);
				
				step();
				
				setAntIcon(degrees);
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
	}
	
	/**
	 * Funkcija koja okrece mrava desno za 90 stupnjeva
	 * 
	 */
	public void rightTurn() {
		degrees += 90;
		degrees = degrees % 360;
	}
	
	/**
	 * Funkcija koja pomice mrava za jedan korak u smjeru odredjenom s <code>degrees</code>
	 * 
	 */
	public void step() {
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
	}

	/**
	 * Funkcija za reskaliranje slika mrava i hrane
	 * 
	 * @param imageIcon slika koja se reskalira
	 * @return reskalirana slika
	 */
	public ImageIcon resizeImageIcon(ImageIcon imageIcon) {
		Image image = imageIcon.getImage();
		Image newimg = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
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

	public static void main(String[] args) {
		Test test = new Test(10, 10);
	}

}
