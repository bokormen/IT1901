package gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class KartverketStaticMap {

	private BufferedImage readURL(String url) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	private String createURL(String layer, int x, int y, int zoom) {
		String url = "http://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?";
		url += "layers=" + layer;
		url += "&zoom=" + zoom;
		url += "&x=" + x;
		url += "&y=" + y;
		return url;
	}

	public Image getImage(String layer, int x, int y, int zoom) {
		Image img = readURL(createURL(layer, x, y, zoom));
		return img;
	}

}
