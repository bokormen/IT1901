package components;

import gui.GUI;
import gui.GoogleStaticMap;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import div.MyPoint;
import div.User;

public class MyMap extends JPanel implements MouseListener,
		MouseMotionListener, KeyListener, MouseWheelListener {

	private int width;
	private int height;

	private int zw;
	private int zh;

	private User user;
	private Image image;
	private Image scaledImg;
	private Image savedImg;

	private Image image2;
	private Image scaledImg2;
	private Image savedImg2;
	private ArrayList<MyImage> images;

	private GUI gui;

	private double numw = 0.0172;
	private double numh = 0.00577;

	private int x;
	private int y;

	public MyMap(int width, int height, GUI gui) {
		System.out.println(width + " " + height);
		images = new ArrayList<MyImage>();
		this.width = width;
		this.height = height;
		this.gui = gui;
		this.setVisible(false);

		this.x = width / 2;
		this.y = height / 2;

		this.zw = 1280;
		this.zh = 1280;

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.addMouseWheelListener(this);
		// this.setOpaque(true);
	}

	public void setUser(User u) {
		this.user = u;
		if (user == null) {
			this.setVisible(false);
		} else {
			this.setVisible(true);
			this.image = getGoogleImage(u.getLatitudeDouble(),
					u.getLongitudeDouble(), 15);
			this.image2 = getGoogleImage(
					u.getLatitudeDouble() /* + 0.00577 */,
					u.getLongitudeDouble() + (0.0275), 15);
			this.savedImg = image;
			this.savedImg2 = image2;
		}
	}

	public void zoomInOnSheep(double latitude, double longitude) {
		this.image = getGoogleImage(latitude, longitude, 17);
		this.savedImg = image;
		repaint();
	}

	public void bigMap() {
		this.image = getGoogleImage(this.user.getLatitudeDouble(),
				this.user.getLongitudeDouble(), 15);
		this.savedImg = image;
		repaint();
	}

	private BufferedImage getGoogleImage(double latitude, double longitude,
			int zoom) {
		BufferedImage img = (BufferedImage) (GoogleStaticMap.getImage(latitude,
				longitude, zoom, 640, 640, 2));
		return img;
	}

	private MyPoint getLocationPoint(String arg) throws Exception {
		String[] list = arg.split(",");
		try {
			return new MyPoint(Double.parseDouble(list[0]),
					Double.parseDouble(list[1]));
		} catch (Exception e) {
			System.err.println("Fikk ikke gjort om string til Point location.");
		}
		return null;
	}

	private Image scaleImage(Image img, int width, int height) {
		return img.getScaledInstance(width, height, Image.SCALE_FAST);
	}

	private void zoom(int dnum) {
		this.zw += dnum;
		this.zh += dnum;
		this.x += 7 * dnum / 10;
		this.y += 6 * dnum / 10;
		this.image = scaleImage(savedImg, zw, zh);
		this.image2 = scaleImage(savedImg2, zw, zh);
		System.out.println(x + "  " + y);
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, -x, -y, this);
			g.drawImage(image2, -x + image.getWidth(this), -y, this);
		}
	}

	private void updateMap() {
		// width
		// height
		System.out.println(x + "  " + y);
		image.getWidth(this);
		image.getHeight(this);

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
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

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		skip = 0;

	}

	int skip = 0;
	Point old = null;

	@Override
	public void mouseDragged(MouseEvent e) {
		if (skip == 1) {
			this.x += (int) (old.getX() - e.getX());
			this.y += (int) (old.getY() - e.getY());
			repaint();
			updateMap();
		}
		gui.changeMySheepButtonBounds(user.getLatitudeDouble(),
				user.getLongitudeDouble(), 15, -x, y);
		old = e.getPoint();
		skip = 1;
	}

	@Override
	public void mouseMoved(MouseEvent arg) {
		// TODO Auto-generated method stub
		// System.out.println("asd: " + arg.getPoint().toString());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		zoom(e.getWheelRotation());
	}

	@Override
	public void keyPressed(KeyEvent arg) {
		System.out.println(arg.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		System.out.println("asd");

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		System.out.println("asd");

	}
}
