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
	private boolean update;
	private int speed;
	private int arc;

	public MyBorder(int arc, int speed) {
		this.arc = arc;
		this.speed = speed;
		this.draw = true;
		setPaintAnimationVar();
	}

	/**
	 * Setter update sann som faar animationen til aa oppdatere seg
	 */
	public void setUpdateTrue() {
		this.update = true;
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
			if (c instanceof MyTextField || c instanceof MyPasswordField && speed != -1) {
				if (color.equals(Color.BLUE)) {
					int[] ret;
					int w = 2;
					g2.setColor(Color.WHITE);
					g2.drawOval(dx1, dy1, w, w);
					g2.drawOval(dx2, dy2, w, w);
					g2.drawOval(dx3, dy3, w, w);
					g2.drawOval(dx4, dy4, w, w);
					g2.drawOval(dx5, dy5, w, w);
					g2.drawOval(dx6, dy6, w, w);

					if (update) { // oppdaterer posisjonen til punktene
						ret = updateAnimation(dx1, dy1, width, height);
						dx1 = ret[0];
						dy1 = ret[1];
						ret = updateAnimation(dx2, dy2, width, height);
						dx2 = ret[0];
						dy2 = ret[1];
						ret = updateAnimation(dx3, dy3, width, height);
						dx3 = ret[0];
						dy3 = ret[1];
						ret = updateAnimation(dx4, dy4, width, height);
						dx4 = ret[0];
						dy4 = ret[1];
						ret = updateAnimation(dx5, dy5, width, height);
						dx5 = ret[0];
						dy5 = ret[1];
						ret = updateAnimation(dx6, dy6, width, height);
						dx6 = ret[0];
						dy6 = ret[1];
						update = false;
					}
				}
			}
		}
	}

	int m = 1;
	int n = 1;

	/**
	 * Oppdaterer animasjone sine posisjoner riktig i forhold til komponentens
	 * kanter
	 * 
	 * @param dx
	 *            tidligere x punkt
	 * @param dy
	 *            tidligere y punkt
	 * @param width
	 *            bredden paa komponenten
	 * @param height
	 *            hoyden paa komonenten
	 * @return en liste med ny x og y posisjon
	 */
	public int[] updateAnimation(int dx, int dy, int width, int height) {
		int min = 3;
		if (dx >= 0 && dx < width - min && dy == 0) {
			m = 1;
			n = 0;
		} else if (dx == width - min && dy >= 0 && dy < height - min) {
			m = 0;
			n = 1;
		} else if (dx <= width - min && dx > 0 && dy == height - min) {
			m = -1;
			n = 0;
		} else if (dx == 0 && dy <= height - min && dy > 0) {
			m = 0;
			n = -1;
		}
		dx += 1 * m;
		dy += 1 * n;
		int[] ret = { dx, dy };
		return ret;
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
	 * Setter dx og dy lik start posisjonene.
	 */
	public void setPaintAnimationVar() {

		this.dx1 = 0;
		this.dy1 = 0;
		this.dx2 = 5;
		this.dy2 = 0;
		this.dx3 = 10;
		this.dy3 = 0;

		this.dx4 = 200 - 3;
		this.dy4 = 40 - 3;
		this.dx5 = 200 - 3 - 5;
		this.dy5 = 40 - 3;
		this.dx6 = 200 - 3 - 10;
		this.dy6 = 40 - 3;
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

	private int dx1;
	private int dy1;
	private int dx2;
	private int dy2;
	private int dx3;
	private int dy3;
	private int dx4;
	private int dy4;
	private int dx5;
	private int dy5;
	private int dx6;
	private int dy6;
}
