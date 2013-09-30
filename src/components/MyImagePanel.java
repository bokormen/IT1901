package components;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * En egen klasse for JPanel som har en egen verson av painComponent metoden.
 * 
 * @author andreas
 * 
 */
public class MyImagePanel extends JPanel {
	private BufferedImage[] img;
	private int[] x;
	private int[] y;

	public MyImagePanel(BufferedImage[] img, int[] x, int[] y) {
		this.img = img;
		this.x = x;
		this.y = y;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img != null) {
			for (int i = 0; i < img.length; i++) {
				g.drawImage(img[i], x[i], y[i], this);
			}
		}
	}
}