package components;

import gui.GoogleStaticMap;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		double numh = 0.0123;
		double numw = 0.0275;
		this.image = getStartImage(lat - (numh) * y, lon + (numw) * x);
		this.savedImage = image;
		this.x = x;
		this.y = y;
	}

	/**
	 * Metode for aa hente ut bildet til rett sted. Prover forst aa finne riktig
	 * bildet hvis det er lagret i src/resources. Hvis ikke saa laster den ned
	 * et statisk png bilde fra maps.google. Hvis en ikke faar bildet fra google
	 * grunnet for mange foresporsler, eller paa grunn av ingen internett
	 * forbindelse.
	 * 
	 * @param latitude
	 *            hoydegraden til senterpunktet
	 * @param longitude
	 *            lengdegraden til senterpunktet
	 * @return img BufferedImage
	 * @throws Exception
	 */

	private BufferedImage getStartImage(double latitude, double longitude)
			throws Exception {
		String file = "googlestaticmap_" + latitude + "_" + longitude + "_"
				+ 15 + ".png";

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("src/resources/" + file));
		} catch (IOException e) {
			// System.out.println("Kunne ikke finne lagret bildet");
		}

		if (img == null) {
			img = (BufferedImage) (GoogleStaticMap.getImage(latitude,
					longitude, 15, 640, 640, 2));
			try {
				File outputfile = new File("src/resources/" + file);
				ImageIO.write(img, "png", outputfile);
			} catch (IOException e) {
				// System.out.println("kunne ikke lagre bildet");
			}
		}
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
