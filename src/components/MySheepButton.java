package components;

import gui.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;
import javax.swing.Timer;

import div.MyPoint;
import div.Sheep;

/**
 * Klassse som tegner sauene grafisk riktig paa kartet. Den inneholder musedrag
 * og musetrykk for aa haandtere brukerens onske om aa trykke paa knappen.
 * 
 * @author andreas
 */
@SuppressWarnings("serial")
public class MySheepButton extends JButton implements MouseListener {
	private MyBorder border;
	private int diameter;
	private Sheep sheep;
	private Color color = Color.WHITE;
	private GUI gui;
	private MyPoint mp;
	private boolean attackSheep;

	private int buttonCount = 0;

	private Timer timer = new Timer(1000, new TimerListener());
	private Timer dTimer = new Timer(100, new DrawingTimer());

	public MySheepButton(JButton jb, Sheep sheep, int x, int y, int diameter, GUI gui) {
		this.diameter = diameter;
		this.sheep = sheep;
		this.gui = gui;

		setBounds(0, 0, diameter, diameter);
		setOpaque(false);
		setContentAreaFilled(false);
		setVisible(true);
		this.border = new MyBorder(80);
		this.border.setColor(Color.BLACK);
		setBorder(border);
		addMouseListener(this);
		addActionListener(new TimerListener());
	}

	/**
	 * Metode for aa returnere sauen knappen tilhorer
	 * 
	 * @return sheep Sheep
	 */
	public Sheep getSheep() {
		return sheep;
	}

	/**
	 * Metode som returnerer true om sauen er angrepet
	 * 
	 * @return attackSheep boolean
	 */
	public boolean isAttacked() {
		return attackSheep;
	}

	/**
	 * Setter saueknappens tilhorende sau lik gitt sau
	 * 
	 * @param sheep
	 *            Sheep
	 */
	public void setSheep(Sheep sheep) {
		this.sheep = sheep;
	}

	/**
	 * Setter objektes MyPoint lik input
	 * 
	 * @param mp
	 *            MyPoint
	 */
	public void setMyPoint(MyPoint mp) {
		this.mp = mp;
	}

	/**
	 * Returnerer saueknappens MyPoint objekt
	 * 
	 * @return mp MyPoint
	 */
	public MyPoint getMyPoint() {
		return mp;
	}

	/**
	 * Setter om knappen skal tegnes eller ikke
	 * 
	 * @param bool
	 *            boolean
	 */
	public void setDraw(boolean bool) {
		this.setEnabled(bool);
		this.setVisible(bool);
	}

	/**
	 * Metode for aa endre storrelse paa knappen
	 * 
	 * @param dim
	 *            pixels
	 */

	public void changeSize(int dim) {
		this.diameter = dim;
		this.setSize(dim, dim);
		repaint();
	}

	/**
	 * Metode for aa endre fargen til knappen
	 * 
	 * @param color
	 *            fargen til knappen
	 */

	public void setColor(Color color) {
		if (this.color.equals(Color.RED) && !color.equals(Color.WHITE)) {
			dTimer.stop();
			border.setColor(Color.BLACK);
		}
		this.color = color;
		repaint();
	}

	/**
	 * Metode for aa sette knappen til rod og starte angreps animasjon
	 * 
	 * @param bool
	 *            true for aa sette knapp til angrep
	 */

	public void attackSheep(boolean bool) {
		attackSheep = bool;
		if (bool) {
			dTimer.start();
			this.color = Color.RED;
		} else {
			this.color = Color.WHITE;
			dTimer.stop();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		// super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, diameter, diameter);
		if (attackSheep) {
			g2d.setColor(Color.RED);
		} else {
			g2d.setColor(color);
		}
		g2d.fill(circle);

		if (attackSheep && color.equals(Color.RED)) {
			if (border.getColor().equals(Color.BLACK)) {
				border.setColor(Color.YELLOW);
			} else {
				border.setColor(Color.BLACK);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.gui.setLwEditSheep(this);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if (!color.equals(Color.RED)) {
			this.color = Color.BLACK;
			repaint();
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if (!color.equals(Color.RED) && !color.equals(Color.BLUE)) {
			this.color = Color.WHITE;
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		timer.start();
		if (buttonCount == 1) {
			buttonCount = 0;
		}
		buttonCount++;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	/**
	 * Klasse som haandterer dobbel tastetrykk pŒ saueknappene
	 * 
	 * @author andreas
	 * 
	 */

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

	/**
	 * Klasse som tegner en paa nytt knapp hver gang den blir tilkalt.
	 * 
	 * @author andreas
	 * 
	 */

	private class DrawingTimer implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			repaint();
		}
	}

	@Override
	public String toString() {
		if (sheep == null) {
			return "";
		}
		int id = sheep.getId();
		String lat = String.format("%.6g%n", Double.parseDouble(sheep.getLocation().getLatitude()));
		String lon = String.format("%.6g%n", Double.parseDouble(sheep.getLocation().getLongitude()));

		return id + " - " + lat + "," + lon;
	}
}
