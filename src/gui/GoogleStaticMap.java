package gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GoogleStaticMap {

	/**
	 * Tries to create an image out of the given url link
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

	private static String createURL(double latitude, double longitude,
			int zoom, int width, int height, int scale) {
		String url = "http://maps.googleapis.com/maps/api/staticmap?";
		url += "center=" + latitude + "," + longitude;
		url += "&zoom=" + zoom;
		url += "&size=" + width + "x" + height;
		url += "&scale=" + scale;
		url += "&sensor=false";
		url += "&style=feature:all|element:labels|visibility:off";
		// System.out.println(url);
		return url;
	}

	/**
	 * 
	 * @param latitude
	 *            defines the latitude for the center of the map
	 * @param longitude
	 *            defines the longitude for the center of the map
	 * @param zoom
	 *            determines the magnification level of the map
	 * @param width
	 *            defines the width of the map Image
	 * @param height
	 *            defines the height of the map Image
	 * @param scale
	 *            defines the rectangular dimensions of the map image.
	 * @return the image at the specified URL
	 * @see readURL
	 * @see createUrl
	 */

	public static Image getImage(double latitude, double longitude, int zoom,
			int width, int height, int scale) throws Exception {
		Image img = readURL(createURL(latitude, longitude, zoom, width, height,
				scale));
		return img;
	}
}
