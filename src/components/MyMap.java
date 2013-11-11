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

	private int z;

	private User user;

	private ArrayList<MyImage> images;

	private GUI gui;

	private int dx;
	private int dy;

	private boolean changesMade = false;

	public MyMap(int width, int height, double latitude, double longitude,
			GUI gui) {
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

		this.z = 1280;

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
			try {
				images.add(new MyImage(0, 0, latitude, longitude));
				images.add(new MyImage(-1, 0, latitude, longitude));
				images.add(new MyImage(0, 1, latitude, longitude));
				images.add(new MyImage(1, 0, latitude, longitude));
				images.add(new MyImage(0, -1, latitude, longitude));
			} catch (Exception e) {
				System.err.println("Kunne ikke laste inn google bilder");
				this.setVisible(false);
			}
			changesMade = true;
			repaint();
		}
	}

	public void centerInOnSheep(double latitude, double longitude) {
		double numw = 0.0275;
		double numh = 0.0123;
		changesMade = true;
		int x = (int) (-(this.longitude - longitude) * imageLength / numw);
		int y = (int) ((this.latitude - latitude) * imageLength / numh);

		for (int i = 0; i < Math.sqrt(Math.pow(
				((dx - (x + 250)) % imageLength / 2), 2)); i += imageLength / 2) {
			dx += imageLength / 2;
			try {
				updateMap();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < Math.sqrt(Math.pow(
				((dy - (y + 390)) % imageLength / 2), 2)); i += imageLength / 2) {
			dy += imageLength / 2;
			try {
				updateMap();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.dx = x + 250 * imageLength / 1280;
		this.dy = y + 390 * imageLength / 1280;
		repaint();
	}

	public void bigMap() {
		this.dx = 0;
		this.dy = 0;
		try {
			updateMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
	}

	private void updateImageLength(int length) {
		this.imageLength = length;
	}

	private void zoom(int dnum) {
		int max = 1280 * 2;
		int min = 1280 / 2;
		if (dnum > 0 && z < 1280 * 2 || dnum < 0 && z > 1280 / 2) {
			this.z += dnum;
			if (z > max) {
				this.z = 1280 * 2;
			} else if (z < min) {
				this.z = 1280 / 2;
			}

			changesMade = true;
			if (!images.isEmpty()) {
				for (MyImage i : images) {
					i.scaleImage(z, z);
				}
				updateImageLength(images.get(0).getImage().getHeight(this));
			}
			try {
				updateMap();
			} catch (Exception e) {
				e.printStackTrace();
			}
			repaint();
		}
	}

	private void resetZoom() {
		this.dx = 0;
		this.dy = 0;
		if (!images.isEmpty()) {
			for (MyImage i : images) {

			}
		}
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
		if (changesMade) {
			gui.changeMySheepButtonBounds(latitude, longitude, 15, dx, dy,
					imageLength);
			changesMade = false;
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

	private void updateMap() throws Exception {
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
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
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
			changesMade = true;
			this.dx += (int) (old.getX() - e.getX());
			this.dy += (int) (old.getY() - e.getY());
			try {
				updateMap();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			repaint();
		}
		old = e.getPoint();
		skip = 1;
	}

	@Override
	public void mouseMoved(MouseEvent arg) {
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
