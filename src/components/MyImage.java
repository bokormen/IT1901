package components;

import gui.GoogleStaticMap;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class MyImage {
	protected Image image;
	protected Image savedImage;
	protected int x;
	protected int y;

	public MyImage(int x, int y, double lat, double lon) {
		// 63.43,10.39 0.0123, 0.0275
		// 58.43,15.39 0.0143, 0.0275
		// 53.43,15.39 0.0163
		System.out.println(x + "  " + y);
		this.image = getGoogleImage(lat - (0.0123) * y, lon + (0.0275) * x);
		this.savedImage = image;
		this.x = x;
		this.y = y;
	}

	private BufferedImage getGoogleImage(double latitude, double longitude) {
		// System.out.println(latitude+" " + longitude);
		BufferedImage img = (BufferedImage) (GoogleStaticMap.getImage(latitude,
				longitude, 15, 640, 640, 2));
		return img;
	}

	public Image getImage() {
		return this.image;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void scaleImage(int width, int height) {
		this.image = savedImage.getScaledInstance(width, height,
				Image.SCALE_FAST);
	}

}
