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

public class MyButton extends JButton implements MouseListener {
	private JButton jb;
	private String text;
	private MyBorder border;
	private Icon icon;
	private Insets dummyInsets;

	public MyButton(JButton jb, String text, String icon) {
		this.jb = jb;
		this.text = text;
		this.border = new MyBorder();

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
		// this.setOpaque(false);
		this.setForeground(Color.WHITE);
		this.setBorder(border);
		this.dummyInsets = border.getBorderInsets(jb);
		this.setVisible(false);

		this.addMouseListener(this);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (this.icon != null) {
			int iconWidth = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			int x = dummyInsets.left + 10;
			int y = (this.getHeight() - iconHeight) / 2;
			icon.paintIcon(this, g, x, y);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto- method stub
		border.changeColor(Color.BLUE);
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		border.changeColor(Color.WHITE);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
