package components;

import gui.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.Timer;

import div.Sheep;

public class MySheepButton extends JButton implements MouseListener {
	private JButton jb;
	private String text;
	private MyBorder border;
	private int diameter;
	private Sheep sheep;
	private Color color = Color.red;
	private GUI gui;

	private int buttonCount = 0;

	private Icon icon;
	private Insets dummyInsets;

	private Timer timer = new Timer(1000, new TimerListener());

	public MySheepButton(JButton jb, Sheep sheep, int x, int y, int diameter, GUI gui) {
		this.jb = jb;
		this.setVisible(true);
		this.diameter = diameter;
		this.sheep = sheep;
		this.gui = gui;

		this.setBounds(x - diameter / 2, y - diameter / 2, diameter, diameter);
		this.setOpaque(false);
		this.setContentAreaFilled(false);

		this.border = new MyBorder(80);
		border.setColor(Color.RED);

		this.setBorder(border);
		this.dummyInsets = border.getBorderInsets(jb);
		this.addMouseListener(this);
		this.addActionListener(new TimerListener());
	}

	public Sheep getSheep() {
		return sheep;
	}

	public void changeSize(int dim) {
		this.diameter = dim;
		this.setSize(dim, dim);
		repaint();
	}

	public void setColor(Color color) {
		this.color = color;
		this.border.setColor(color);
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, diameter, diameter);
		g2d.setColor(color);
		// g2d.translate(-diameter / 2, -diameter / 2);
		g2d.fill(circle);
		// g2d.translate(diameter / 2, diameter / 2);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.color = Color.yellow;
		gui.setLwEditSheep(this.sheep);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if (!color.equals(Color.yellow)) {
			this.color = Color.BLACK;
			border.changeColor(Color.BLACK);
			repaint();
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if (!color.equals(Color.yellow)) {
			this.color = Color.RED;
			border.changeColor(Color.RED);
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		System.out.println(this.sheep.getId());
		// System.out.println(this.sheep.getLocation());
		timer.start();
		if (buttonCount == 1) {
			System.out.println("GO TO EDIT SHEEP");
			buttonCount = 0;
		}
		buttonCount++;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	private class TimerListener implements ActionListener {
		int i = 0;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (i > 1) {
				buttonCount = 0;
				i = 0;
				timer.stop();
			}
			i++;
		}
	}

	public String toString() {
		int id = sheep.getId();

		String lat = String.format("%.4g%n", Double.parseDouble(sheep.getLocation().getLatitude()));
		String lon = String.format("%.4g%n", Double.parseDouble(sheep.getLocation().getLongitude()));

		return id + " - " + lat + "," + lon;
	}
}
