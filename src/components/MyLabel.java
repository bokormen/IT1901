package components;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MyLabel extends JLabel {
	private JLabel label;
	private String text;
	private Icon icon;

	public MyLabel(JLabel label, String text, String icon) {
		this.label = label;
		this.text = text;

		try {
			if (icon != null) {
				this.icon = new ImageIcon(ImageIO.read(this.getClass().getClassLoader()
						.getResource("images/" + icon + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setText(text);
		this.setForeground(Color.RED);
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVisible(false);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.icon != null) {
			int iconWidth = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			int x = 5;
			int y = (this.getHeight() - iconHeight) / 2;
			icon.paintIcon(this, g, x, y);
		}
	}
}
