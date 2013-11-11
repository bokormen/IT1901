package components;

import gui.GoogleStaticMap;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class MyImage {
	protected Image image;
	protected Image savedImage;
	protected int x;
	protected int y;

	public MyImage(int x, int y, double lat, double lon) throws Exception {
		// 63.43,10.39 0.0123, 0.0275
		// 58.43,15.39 0.0143, 0.0275
		// 53.43,15.39 0.0163
		// System.out.println(x + "  " + y);
		this.image = getGoogleImage(lat - (0.0123) * y, lon + (0.0275) * x);
		this.savedImage = image;
		this.x = x;
		this.y = y;
	}

	/**
	 * Tar inn bredde og hoydegrad for aa hente ut og lagre et kart fra
	 * maps.google
	 * 
	 * @param latitude
	 *            hoydegraden til senterpunktet
	 * @param longitude
	 *            lengdegraden til senterpunktet
	 * @return img BufferedImage
	 * @throws Exception
	 */

	private BufferedImage getGoogleImage(double latitude, double longitude)
			throws Exception {
		// System.out.println(latitude+" " + longitude);
		BufferedImage img = (BufferedImage) (GoogleStaticMap.getImage(latitude,
				longitude, 15, 640, 640, 2));
		return img;
	}

	/**
	 * Returnerer bildet som skal tegnes
	 * 
	 * @return image Image
	 */

	public Image getImage() {
		return this.image;
	}

	/**
	 * Returnerer x verdien av hvor bildet skal plasseres i koordinatsystemet
	 * 
	 * @return x Integer
	 */

	public int getX() {
		return this.x;
	}

	/**
	 * Returnerer y verdien av hvor bildet skal plasseres i koordinatsystemet
	 * 
	 * @return y Integer
	 */

	public int getY() {
		return this.y;
	}

	/**
	 * Skalerer et lagret bilde til et nytt bilde og setter bildet som skal
	 * tegnes til dette
	 * 
	 * @param width
	 *            ny bredde
	 * @param height
	 *            ny hoyde
	 */

	public void scaleImage(int width, int height) {
		this.image = savedImage.getScaledInstance(width, height,
				Image.SCALE_FAST);
	}

	/**
	 * Setter bildet som skal tegnes tilbake til orginalen
	 */

	public void resetImage() {
		this.image = savedImage;
	}

}
