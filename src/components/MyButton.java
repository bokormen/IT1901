package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Timer;

public class MyButton extends JButton implements MouseListener {
	private JButton jb;
	private String text;
	private MyBorder border;
	private Icon icon;
	private Insets dummyInsets;

	private TimerListener tl;
	private Timer timer;

	public MyButton(JButton jb, String text, String icon) {
		this.jb = jb;
		this.text = text;
		this.setVisible(false);
		this.setForeground(Color.WHITE);
		this.setOpaque(false);
		this.setContentAreaFilled(false);

		this.tl = new TimerListener();
		this.timer = new Timer(10, tl);

		this.border = new MyBorder(20);

		try {
			if (icon != null) {
				this.icon = new ImageIcon(ImageIO.read(this.getClass()
						.getClassLoader()
						.getResource("images/" + icon + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setText(text);
		this.setBorder(border);
		this.dummyInsets = border.getBorderInsets(jb);

		this.addMouseListener(this);
	}

	int rotate = 0;

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (icon.equals("redgearicon")) {
			Graphics2D g2d = (Graphics2D) g;
			int iconHeight = icon.getIconHeight();
			int x = dummyInsets.left + 10;
			int y = (this.getHeight() - iconHeight) / 2;
			g2d.rotate(Math.toRadians(45));
			icon.paintIcon(this, g, x, y);
			rotate += 3;
		} else {
			if (this.icon != null) {
				int iconWidth = icon.getIconWidth();
				int iconHeight = icon.getIconHeight();
				int x = dummyInsets.left + 10;
				int y = (this.getHeight() - iconHeight) / 2;
				icon.paintIcon(this, g, x, y);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		border.changeColor(Color.BLUE);
		if (icon.equals("redgearicon")) {
			System.out.println("asd");
			timer.start();
		}
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		border.changeColor(Color.WHITE);
		if (icon.equals("redgearicon")) {
			timer.stop();
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * A class that handles time. Can be used for animations.
	 * 
	 * @author andreas
	 * 
	 */

	private class TimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			repaint();
		}
	}

}
