package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.border.AbstractBorder;

public class MyBorder extends AbstractBorder {
	private Color color = Color.WHITE;
	private int arc;

	public MyBorder(int arc) {
		this.arc = arc;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.drawRoundRect(x, y, width - 1, height - 1, arc, arc);
	}

	public void changeTargetColor() {
		if (color.equals(Color.WHITE)) {
			color = Color.BLUE;
		} else if (color.equals(Color.BLUE)) {
			color = Color.WHITE;
		}
	}

	public void changeColor(Color color) {
		if (!this.color.equals(Color.GREEN) || !this.color.equals(Color.RED)) {
			this.color = color;
		}
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}
}
