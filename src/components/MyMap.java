package components;

import gui.GUI;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import div.User;

/**
 * Klasse som tegner kartet paa skjermen og holder styr paa hvor brukeren
 * navigerer seg. Den inneholder musdrag, mustrykk, knappe trykk til zoom.
 * Sentrering paa kartet av sau og dynamisk oppdatering av kartet. Klassen
 * bruker metoder i GUI til aa forandre paa posisjone til saueknappene.
 * 
 * @author andreas
 * @see GUI.changeMySheepButtonBounds()
 */
@SuppressWarnings("serial")
public class MyMap extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	private boolean changesMade = false;
	private int imageLength;
	private double latitude;
	private double longitude;
	private int dx;
	private int dy;
	private int z;

	private ArrayList<MyImage> images;
	private User user;
	private GUI gui;

	public MyMap(int width, int height, double latitude, double longitude, GUI gui) {
		this.images = new ArrayList<MyImage>();
		this.imageLength = 1280;
		this.latitude = latitude;
		this.longitude = longitude;
		this.gui = gui;
		this.setVisible(false);

		this.dx = 0;
		this.dy = 0;
		this.z = 1280;

		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setFocusable(true);

		getInputMap().put(KeyStroke.getKeyStroke('+'), "zoomin");
		getActionMap().put("zoomin", new ZoomInAction());

		getInputMap().put(KeyStroke.getKeyStroke('-'), "zoomout");
		getActionMap().put("zoomout", new ZoomOutAction());

	}

	private class ZoomInAction extends AbstractAction {
		public void actionPerformed(ActionEvent tf) {
			zoom(30);
		}
	}

	private class ZoomOutAction extends AbstractAction {
		public void actionPerformed(ActionEvent tf) {
			zoom(-30);
		}
	}

	/**
	 * Setter igang kartet med aa legge til de sentrale bildene rundt brukerens
	 * beliggenhet
	 * 
	 * @param u
	 *            User brukeren
	 */
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

	/**
	 * Metode som sentrerer kartet rundt en gitt sau med hoyde og breddegrad
	 * 
	 * @param latitude
	 *            hoydegrad
	 * @param longitude
	 *            breddegrad
	 */
	public void centerInOnSheep(double latitude, double longitude) {
		double numw = 0.0275;
		double numh = 0.0123;
		changesMade = true;
		zoom(1280 - z);
		int x = (int) (-(this.longitude - longitude) * imageLength / numw);
		int y = (int) ((this.latitude - latitude) * imageLength / numh);

		for (int i = 0; i < Math.sqrt(Math.pow(((dx - (x + 250)) % imageLength / 2), 2)); i += imageLength / 2) {
			dx += imageLength / 2;
			try {
				updateMap();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < Math.sqrt(Math.pow(((dy - (y + 390)) % imageLength / 2), 2)); i += imageLength / 2) {
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

	public void home() {
		this.dx = 1280 / 3;
		this.dy = 1280 / 3;
		zoom(1280 - z);
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

	/**
	 * Metode som sjekker om bildet finnes allerede i bilde-kordinatsystemet.
	 * 
	 * @param x
	 *            Integer x-kordinaten
	 * @param y
	 *            Integer y-kordinaten
	 * @return boolean sann hvis den ikke er der
	 */
	private boolean isImageInList(int x, int y) {
		for (MyImage img : images) {
			if (img.getX() == x && img.getY() == y) {
				return false;
			}
		}
		return true;
	}

	// Ulike variabler som trengs i updateMap(), som trengs for aa hente riktig
	// kart en gang.
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

	/**
	 * Metode som legger til flere kart-bilder til kartet hvis nodvendig.
	 * 
	 * @throws Exception
	 *             hvis ingen internett og ikke faar hentet bildet fra nett
	 */
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
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!images.isEmpty()) {
			for (MyImage i : images) {
				int width = i.getImage().getWidth(this);
				int height = i.getImage().getHeight(this);
				g.drawImage(i.getImage(), i.getX() * width - dx, i.getY() * height - dy, this);
				gui.repaintMySheepButtons();
			}
		}
		if (changesMade) {
			gui.changeMySheepButtonBounds(latitude, longitude, 15, dx, dy, imageLength);
			changesMade = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		this.requestFocus();
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
}
