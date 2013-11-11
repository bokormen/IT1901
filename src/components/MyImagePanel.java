package components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

/**
 * En egen klasse for JPanel som har en egen verson av painComponent metoden.
 * 
 * @author andreas
 * 
 */
public class MyImagePanel extends JPanel {
	private Image[] img;
	private int[] x;
	private int[] y;

	public MyImagePanel(Image[] img, int[] x, int[] y) {
		this.img = img;
		this.x = x;
		this.y = y;
	}

	public void changeImage(Image img, int num) {
		this.img[num] = img;
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