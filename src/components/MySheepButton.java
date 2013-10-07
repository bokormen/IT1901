package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.Icon;
import javax.swing.JButton;

import div.Sheep;

public class MySheepButton extends JButton implements MouseListener {
	private JButton jb;
	private String text;
	private MyBorder border;
	private int diameter;
	private Sheep sheep;
	private Color color = Color.red;

	private Icon icon;
	private Insets dummyInsets;

	public MySheepButton(JButton jb, Sheep sheep, int x, int y, int diameter) {
		this.jb = jb;
		this.setVisible(true);
		this.diameter = diameter;
		this.sheep = sheep;

		this.setBounds(x, y, diameter, diameter);
		this.setOpaque(false);
		this.setContentAreaFilled(false);

		this.border = new MyBorder(80);
		border.setColor(Color.RED);

		this.setBorder(border);
		this.dummyInsets = border.getBorderInsets(jb);
		this.addMouseListener(this);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		// Assume x, y, and diameter are instance variables.
		Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, diameter, diameter);
		g2d.setColor(color);
		g2d.fill(circle);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto- method stub
		this.color = Color.black;
		border.changeColor(Color.BLACK);
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		this.color = color.red;
		border.changeColor(Color.RED);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
