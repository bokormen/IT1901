package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.border.AbstractBorder;

/**
 * Egen version av AbstractBorder som tegner kantene paa knapper paa en annen
 * maate enn standard.
 * 
 * @author andreas
 * 
 */
@SuppressWarnings("serial")
public class MyBorder extends AbstractBorder {
	private Color color = Color.WHITE;
	private boolean draw;
	private int arc;

	public MyBorder(int arc) {
		this.arc = arc;
		this.draw = true;
	}

	/**
	 * Skriver over en metode fra AbstractBorder som tegner kanten slik satt i
	 * metoden.
	 */

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		if (draw) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(color);
			g2.drawRoundRect(x, y, width - 1, height - 1, arc, arc);
		}
	}

	/**
	 * Setter variablen draw til true eller false som bestemmer om kantene skal
	 * tegnes eller ikke.
	 * 
	 * @param bool
	 *            Boolean
	 */
	public void setDraw(Boolean bool) {
		this.draw = bool;
	}

	/**
	 * Endrer farge fra hvit til blaa, og fra blaa til hvit
	 */
	public void changeTargetColor() {
		if (color.equals(Color.WHITE)) {
			color = Color.BLUE;
		} else if (color.equals(Color.BLUE)) {
			color = Color.WHITE;
		}
	}

	/**
	 * Endrer fargen til kantene til gitt farge hvis den ikke er gronn eller rod
	 * fra for.
	 * 
	 * @param color
	 */
	public void changeColor(Color color) {
		if (!this.color.equals(Color.GREEN) || !this.color.equals(Color.RED)) {
			this.color = color;
		}
	}

	/**
	 * Endrer fargen til kantene til gitt farge.
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Returnerer fargen kantene har.
	 * 
	 * @return color Color
	 */
	public Color getColor() {
		return this.color;
	}
}
