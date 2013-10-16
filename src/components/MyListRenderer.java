package components;

import java.awt.Component;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

public class MyListRenderer extends DefaultListCellRenderer {

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean hasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
		JTextField field = new JTextField("asd");
		label.add(field);
		Icon icon = null;
		
		try {
			icon = new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("images/bluesheepicon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		label.setIcon(icon);

		return label;

	}
}
