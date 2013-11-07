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

	private int sideLength;

	private double latitude;
	private double longitude;

	private int zw;
	private int zh;

	private User user;
	private Image image;
	private Image scaledImg;
	private Image savedImg;

	private ArrayList<MyImage> images;

	private GUI gui;

	private double numw = 0.0123;
	private double numh = 0.0275;

	private int dx;
	private int dy;

	public MyMap(int width, int height, double latitude, double longitude,
			GUI gui) {
		System.out.println(width + " " + height);
		images = new ArrayList<MyImage>();
		this.width = width;
		this.height = height;
		this.sideLength = 1280;
		this.latitude = latitude;
		this.longitude = longitude;
		this.gui = gui;
		this.setVisible(false);

		this.dx = width / 2;
		this.dy = height / 2;

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
			images.add(new MyImage(0, 0, latitude, longitude));

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

	public void changeToSheepImages(double lat, double lon) {
		images.clear();
		images.add(new MyImage(0, 0, lat, lon));
		this.latitude = lat;
		this.longitude = lon;
		repaint();
	}

	private void updateImageSideLength(int length) {
		this.sideLength = length;
	}

	private void zoom(int dnum) {
		this.zw += dnum;
		this.zh += dnum;

		this.dx += 7 * dnum / 10;
		this.dy += 6 * dnum / 10;

		if (!images.isEmpty()) {
			for (MyImage i : images) {
				i.scaleImage(zw, zh);
			}
			updateImageSideLength(images.get(0).getImage().getHeight(this));
		}
		updateMap();
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!images.isEmpty()) {
			for (MyImage i : images) {
				int width = i.getImage().getWidth(this);
				int height = i.getImage().getHeight(this);
				g.drawImage(i.getImage(), i.getX() * width - dx, i.getY()
						* height - dy, this);
			}
		}
	}

	int wi = 0;
	int wj = 0;

	int si = 0;
	int sj = 0;

	int ei = 0;
	int ej = 0;

	int ni = 0;
	int nj = 0;

	int wc = 0;
	int sc = 0;
	int ec = 0;
	int nc = 0;

	private boolean isImageInList(int x, int y) {
		for (MyImage img : images) {
			if (img.getX() == x && img.getY() == y) {
				return false;
			}
		}
		return true;
	}

	boolean update = false;

	private void updateMap() {
		double percent = 1280 / -sideLength;
		if (dx < wi * -sideLength) {
			if (wi > wj) {
				wj++;
			}
			if (wi == wj) {
				wi++;
				images.add(new MyImage(-wi, 0, latitude, longitude));
				for (int i = -ni; i <= si; i++) {
					if (isImageInList(-wi, i)) {
						// System.out.println(i + " " + -wi);
						images.add(new MyImage(-wi, i, latitude, longitude));
					}
				}
			}
		}

		if (dy > (int) (750 * percent) + si * sideLength) {
			if (si > sj) {
				sj++;
			}
			if (si == sj) {
				si++;
				images.add(new MyImage(0, si, latitude, longitude));
				for (int i = -wi; i <= ei; i++) {
					if (isImageInList(i, si)) {
						// System.out.println(i + " " + si);
						images.add(new MyImage(i, si, latitude, longitude));
					}
				}
			}
		}

		if (dx > (int) (470 * percent) + ei * sideLength) {
			if (ei > ej) {
				ej++;
			}
			if (ei == ej) {
				ei++;
				images.add(new MyImage(ei, 0, latitude, longitude));
				for (int i = -ni; i <= si; i++) {
					if (isImageInList(ei, i)) {
						// System.out.println(i + " " + ei);
						images.add(new MyImage(ei, i, latitude, longitude));
					}
				}
			}
		}

		if (dy < ni * -sideLength) {
			if (ni > nj) {
				nj++;
			}
			if (ni == nj) {
				ni++;
				images.add(new MyImage(0, -ni, latitude, longitude));

				for (int i = -wi; i <= ei; i++) {
					System.out.println("asdasd" + i + "  " + ei);
					if (isImageInList(i, -ni)) {
						// System.out.println(i + " " + -ni);
						images.add(new MyImage(i, -ni, latitude, longitude));
					}
				}
			}
		}
		// System.out.println(ni + " " + si + " " + wi + " " + ei);
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
		// System.out.println(dx + "  " + dy);
		if (skip == 1) {
			this.dx += (int) (old.getX() - e.getX());
			this.dy += (int) (old.getY() - e.getY());
			repaint();
			updateMap();
		}
		gui.changeMySheepButtonBounds(user.getLatitudeDouble(),
				user.getLongitudeDouble(), 15, -dx, dy);
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
		System.out.println(arg.getKeyChar() * 4);
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
