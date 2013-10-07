package components;

import gui.GoogleStaticMap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import div.MyPoint;
import div.Sheep;
import div.User;

public class MyMap extends JPanel implements MouseListener, MouseMotionListener {

	private int width;
	private int height;
	private int zoom;
	private BufferedImage image;
	private JLayeredPane lp;
	private Color color;

	private double numh;
	private double numw;

	// private double num = 0.0171;
	private double num = 0.02745;

	private User user;
	private ArrayList<Sheep> sheeps;

	private ArrayList<MySheepButton> sheepButton;

	public MyMap(int width, int height, JLayeredPane lp) {
		System.out.println(width + " " + height);
		this.lp = lp;
		this.width = width;
		this.height = height;
		this.lp = lp;
		this.color = Color.BLACK;
		this.sheeps = new ArrayList<Sheep>();
		this.sheepButton = new ArrayList<MySheepButton>();
		this.zoom = 15;
		this.numw = width / 2 / num;
		this.numh = height / 2 / num;
		this.setVisible(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		// this.setOpaque(true);
	}

	public ArrayList<MySheepButton> getSheepButtons() {
		return sheepButton;
	}

	public void setUser(User u) {
		this.user = u;
		if (user == null) {
			this.setVisible(false);
		} else {
			this.setVisible(true);
			// System.out.println("coords: " + u.getLatitudeDouble() + " " +
			// u.getLongitudeDouble());
			this.image = getGoogleImage(u.getLatitudeDouble(), u.getLongitudeDouble());
		}

	}

	private BufferedImage getGoogleImage(double latitude, double longitude) {
		BufferedImage img = (BufferedImage) (GoogleStaticMap.getImage(latitude, longitude, this.zoom, width / 2,
				height / 2, 2));
		return img;
	}

	private MyPoint getLocationPoint(String arg) throws Exception {
		String[] list = arg.split(",");
		try {
			return new MyPoint(Double.parseDouble(list[0]), Double.parseDouble(list[1]));
		} catch (Exception e) {
			System.err.println("Fikk ikke gjort om string til Point location.");
		}
		return null;
	}

	protected void drawBackground(Graphics g) {
		if (image != null) {
			g.drawImage(image, 0, 0, this);
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBackground(g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO

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
	public void mousePressed(MouseEvent e) {
		// TODO
		Point l = e.getPoint();
		// System.out.println(l.y + "  " + l.x);
		for (Sheep s : sheeps) {
			MyPoint p = null;
			l = e.getPoint();
			try {
				p = getLocationPoint(s.getLocation().getPosition());
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			double mylon = (user.getLongitudeDouble() - p.getLongitude()) * numw;
			double mylat = (user.getLatitudeDouble() - p.getLatitude()) * numh;

			int lon = width / 2 - (int) mylon;
			int lat = height / 2 - (int) mylat;

			// System.out.println(lat + "  " + lon);
			// System.out.println(l.x + "  " + l.y);
			// System.out.println(lon + "<" + (l.y + 5) + " , " + lon + ">" +
			// (l.y - 5));
			if (lon < l.x + 10 && lon > l.x - 10) {
				if (lat < l.y + 10 && lat > l.y - 10) {
					System.out.println(s.getLocation());
				}
			}
		}

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
	public void mouseMoved(MouseEvent arg) {
		// TODO Auto-generated method stub
		// System.out.println("asd: " + arg.getPoint().toString());

	}
}
