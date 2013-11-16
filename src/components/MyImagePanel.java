package components;

import gui.GoogleStaticMap;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * En egen klasse for JPanel som har en egen verson av painComponent metoden.
 * 
 * @author andreas
 * 
 */
@SuppressWarnings("serial")
public class MyImagePanel extends JPanel {
	private Image drawImage;
	private Image backgroundImage;
	private Image googleImage;
	private int width;
	private int height;

	public MyImagePanel(int width, int height, String file) {
		this.width = width;
		this.height = height;
		try {
			this.backgroundImage = ImageIO.read(this.getClass().getClassLoader().getResource(file));
			this.backgroundImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.drawImage = backgroundImage;

	}

	/**
	 * Henter et bildet fra maps.google gitt hoyde- og breddegrad og setter
	 * bildet som skal bli tegnet lik dette.
	 * 
	 * @param latitude
	 *            double hoydegrad
	 * @param longitude
	 *            double breddegrad
	 */
	public void changeToGoogleImage(double latitude, double longitude) {
		this.googleImage = getGoogleImage(latitude, longitude);
		this.googleImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		this.drawImage = googleImage;
		repaint();
	}

	/**
	 * Endrer tilbake til start bakgrunnsbildet.
	 */
	public void changeToBackgroundImage() {
		this.drawImage = backgroundImage;
		repaint();
	}

	/**
	 * Henter bilde fra maps.google
	 * 
	 * @param latitude
	 * @param longitude
	 * @return image BufferedImage
	 */
	private BufferedImage getGoogleImage(double latitude, double longitude) {
		try {
			return (BufferedImage) (GoogleStaticMap.getImage(latitude, longitude, 14, 640, 640, 2));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Tegner enten google bilde eller bakgrunnsbildet paa panelet
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (drawImage != null) {
			g.drawImage(drawImage, 0, 0, this);
		}
	}
}