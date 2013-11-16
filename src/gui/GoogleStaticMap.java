package gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class GoogleStaticMap {

	/**
	 * Prover aa lage et bilde utifra url linken gitt
	 * 
	 * @param url
	 *            String
	 * @return img BufferedImage
	 */

	private static BufferedImage readURL(String url) throws Exception {
		BufferedImage img = null;
		img = ImageIO.read(new URL(url));
		return img;
	}

	/**
	 * Setter sammen alle variablene til en url string klar til aa hente et
	 * statisk kart fra google.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param zoom
	 * @param width
	 * @param height
	 * @param scale
	 * @return url String
	 */
	private static String createURL(double latitude, double longitude, int zoom, int width, int height, int scale) {
		String url = "http://maps.googleapis.com/maps/api/staticmap?";
		url += "center=" + latitude + "," + longitude;
		url += "&zoom=" + zoom;
		url += "&size=" + width + "x" + height;
		url += "&scale=" + scale;
		url += "&sensor=false";
		url += "&style=feature:all|element:labels|visibility:off";
		return url;
	}

	/**
	 * 
	 * @param latitude
	 *            definerer hoydegraden for senter av kartet
	 * @param longitude
	 *            definerer breddegraden for senter av kartet
	 * @param zoom
	 *            definerer forstorrelse nivaa av kartet
	 * @param width
	 *            definerer bredden paa kartet, max 640
	 * @param height
	 *            defines hoyden paa kartet, max 640
	 * @param scale
	 *            definerer om kartet skal skaleres x1 eller x2
	 * @return bildet som hentes utifra variablene gitt
	 * @see readURL
	 * @see createUrl
	 */

	public static Image getImage(double latitude, double longitude, int zoom, int width, int height, int scale)
			throws Exception {
		Image img = readURL(createURL(latitude, longitude, zoom, width, height, scale));
		return img;
	}
}
