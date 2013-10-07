package components;

import gui.GoogleStaticMap;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import div.Sheep;
import div.User;

public class MyMap extends JPanel implements MouseListener, MouseMotionListener {

	private int width;
	private int height;
	private int zoom;

	private double num = 0.02745;

	private User user;
	private ArrayList<Sheep> sheeps;

	public MyMap(int width, int height) {
		this.width = width;
		this.height = height;
		this.sheeps = new ArrayList<Sheep>();
		this.setBounds(width / 3, 0, 2 * width / 3, height);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	private void testing(double x, double y) {
		String[] loc = { "63.420,10.38", "63.422,10.382", "63.424,10.384",
				"63.426,10.386", "63.428,10.388", "63.430,10.390",
				"63.432,10.392", "63.434,10.394", "63.436,10.396",
				"63.438,10.398" };
		ArrayList<String> locs = new ArrayList<String>();
		double xr = x - num;
		double yr = y - num;
		int quantity = 10;
		for (int i = 0; i < quantity; i++) {
			locs.add("" + xr + (i * 2 * num / quantity) + "," + yr
					+ (i * num / quantity));
		}
		try {
			for (int i = 0; i < 10; i++) {
				sheeps.add(new Sheep("bob", 0, 10, 'm', "Andy"));
				sheeps.get(sheeps.size() - 1).newLocation(loc[i], "01/01/2000");
			}
		} catch (Exception e) {
			System.out.println("Fikk ikke lasted inn sauene");
		}
	}

	public void setUser(User u) {
		this.user = u;
		testing(u.getLatitudeDouble(), u.getLongitudeDouble());
		drawSheeps();

	}

	private BufferedImage getGoogleImage(double latitude, double longitude) {
		BufferedImage img = (BufferedImage) (GoogleStaticMap.getImage(latitude,
				longitude, this.zoom, 2 * width / 3, height, 2));
		return img;
	}

	@SuppressWarnings("null")
	private Point getLocationPoint(String arg) throws Exception {
		Point p = null;
		String[] list = arg.split(",");
		try {
			p.setLocation(Double.parseDouble(list[0]),
					Double.parseDouble(list[1]));
		} catch (Exception e) {
			System.err.println("Fikk ikke gjort om string til Point location.");
		}
		return p;
	}

	public void drawSheeps() {
		for (Sheep s : sheeps) {

		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
