package components;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 
 * @author andreas
 * 
 */
@SuppressWarnings("serial")
public class MyLabel extends JLabel {
	private Icon icon;

	public MyLabel(String text, String icon) {
		try {
			if (icon != null) {
				this.icon = new ImageIcon(ImageIO.read(this.getClass().getClassLoader()
						.getResource("images/" + icon + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		setText(text);
		setForeground(Color.RED);
		setHorizontalAlignment(JLabel.CENTER);
		setVisible(false);
	}

	/**
	 * Tegner den vanlige JLabel pluss et bildet viss det er satt.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.icon != null) {
			int iconHeight = icon.getIconHeight();
			int x = 5;
			int y = (this.getHeight() - iconHeight) / 2;
			icon.paintIcon(this, g, x, y);
		}
	}
}
