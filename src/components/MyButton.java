package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Egen version av JButton som implementerer MouseListener, for aa bestemme
 * handling av trykk og beroring.
 * 
 * @author andreas
 * 
 */
@SuppressWarnings("serial")
public class MyButton extends JButton implements MouseListener {
	private MyBorder border;
	private Icon icon;
	private Insets dummyInsets;

	public MyButton(String text, String icon) {
		setVisible(false);
		setForeground(Color.WHITE);
		setOpaque(false);
		setContentAreaFilled(false);
		this.border = new MyBorder(20, -1);

		try {
			if (icon != null) {
				this.icon = new ImageIcon(ImageIO.read(this.getClass().getClassLoader()
						.getResource("images/" + icon + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		setText(text);
		setBorder(border);
		this.dummyInsets = border.getBorderInsets(this);
		addMouseListener(this);
	}

	/**
	 * Skriver over en metode fra JButton som tegner et bilde paa knappen.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.icon != null) {
			int iconHeight = icon.getIconHeight();
			int x = dummyInsets.left + 10;
			int y = (this.getHeight() - iconHeight) / 2;
			icon.paintIcon(this, g, x, y);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		border.changeColor(Color.BLUE);
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		border.changeColor(Color.WHITE);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
