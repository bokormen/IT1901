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

	private int imageLength;

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
		// System.out.println(width + " " + height);
		images = new ArrayList<MyImage>();
		this.width = width;
		this.height = height;
		this.imageLength = 1280;
		this.latitude = latitude;
		this.longitude = longitude;
		this.gui = gui;
		this.setVisible(false);

		this.dx = 0;
		this.dy = 0;

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
			images.add(new MyImage(-1, 0, latitude, longitude));
			images.add(new MyImage(0, 1, latitude, longitude));
			images.add(new MyImage(1, 0, latitude, longitude));
			images.add(new MyImage(0, -1, latitude, longitude));
			gui.changeMySheepButtonBounds(latitude, longitude, 15, 0, 0,
					imageLength);

		}
	}

	public void zoomInOnSheep(double latitude, double longitude) {
		// -209 -> 40 (-249), -169->220 (-389)
		// 68 -> 319 (-251), -78->313 (-391)
		double numw = 0.0275;
		double numh = 0.0123;

		int x = (int) (-(this.longitude - longitude) * imageLength / numw);
		int y = (int) ((this.latitude - latitude) * imageLength / numh);
		this.dx = x + 250;
		this.dy = y + 390;
		System.out.println(dx + "  " + dy);
		updateMap();
		repaint();
		gui.changeMySheepButtonBounds(latitude, longitude, 15, x, y,
				imageLength);
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

	public void changeToSheepImages(double lat, double lon) {
		this.dx = 0;
		this.dy = 0;
		gui.changeMySheepButtonBounds(lat, lon, 15, dx, dy, imageLength);
		repaint();
	}

	private void updateImageLength(int length) {
		this.imageLength = length;
	}

	private void zoom(int dnum) {
		this.zw += dnum;
		this.zh += dnum;

		// this.dx += 7 * dnum / 10;
		// this.dy += 6 * dnum / 10;

		if (!images.isEmpty()) {
			for (MyImage i : images) {
				i.scaleImage(zw, zh);
			}
			updateImageLength(images.get(0).getImage().getHeight(this));
		}
		updateMap();
		repaint();
		gui.changeMySheepButtonBounds(latitude, longitude, 15, dx, dy,
				imageLength);
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

	private void updateMap() {
		double percent = 1280 / -imageLength;
		if (dx < wi * -imageLength) {
			if (wi > wj) {
				wj++;
			}
			if (wi == wj) {
				wi++;
				for (int i = -ni; i <= si; i++) {
					if (isImageInList(-wi, i)) {
						images.add(new MyImage(-wi, i, latitude, longitude));
					}
				}
			}
		}

		if (dy > (int) (750 * percent) + si * imageLength) {
			if (si > sj) {
				sj++;
			}
			if (si == sj) {
				si++;
				for (int i = -wi; i <= ei; i++) {
					if (isImageInList(i, si)) {
						images.add(new MyImage(i, si, latitude, longitude));
					}
				}
			}
		}

		if (dx > (int) (470 * percent) + ei * imageLength) {
			if (ei > ej) {
				ej++;
			}
			if (ei == ej) {
				ei++;
				for (int i = -ni; i <= si; i++) {
					if (isImageInList(ei, i)) {
						images.add(new MyImage(ei, i, latitude, longitude));
					}
				}
			}
		}

		if (dy < ni * -imageLength) {
			if (ni > nj) {
				nj++;
			}
			if (ni == nj) {
				ni++;
				for (int i = -wi; i <= ei; i++) {
					if (isImageInList(i, -ni)) {
						images.add(new MyImage(i, -ni, latitude, longitude));
					}
				}
			}
		}
		repaint();
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
			updateMap();
		}
		gui.changeMySheepButtonBounds(latitude, longitude, 15, dx, dy,
				imageLength);
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
