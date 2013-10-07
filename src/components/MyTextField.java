package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class MyTextField extends JTextField implements FocusListener, DocumentListener {
	private JTextField jtf;
	private Icon icon;
	private Icon iconStored;
	private String hint;
	private Insets dummyInsets;
	private MyBorder border;

	public MyTextField(JTextField jtf, String icon, String hint) {
		this.jtf = jtf;
		this.hint = hint;
		this.setVisible(false);
		this.setHorizontalAlignment(JTextField.CENTER);
		this.setDocument(new DocumentLimiter(64)); // bokstav begrensning
		this.setOpaque(false);
		this.setForeground(Color.WHITE);
		this.setCaretColor(Color.WHITE);
		try {
			if (icon != null) {
				this.iconStored = new ImageIcon(ImageIO.read(this.getClass().getClassLoader()
						.getResource("images/" + icon + ".png")));
				this.icon = iconStored;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.border = new MyBorder(20);
		this.setBorder(border);
		this.dummyInsets = border.getBorderInsets(jtf);
		addFocusListener(this);
		this.getDocument().addDocumentListener(this);
	}

	public MyBorder getMyBorder() {
		return this.border;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int textX = 2;

		if (this.icon != null) {
			int iconWidth = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			int x = dummyInsets.left + 5;
			textX = x + iconWidth + 2;
			int y = (this.getHeight() - iconHeight) / 2;
			icon.paintIcon(this, g, x, y);
		}

		setMargin(new Insets(2, textX, 2, 2));

		if (this.getText().equals("")) {
			int width = this.getWidth();
			int height = this.getHeight();
			Font prev = g.getFont();
			Font italic = prev.deriveFont(Font.ITALIC);
			Color prevColor = g.getColor();
			g.setFont(italic);
			g.setColor(UIManager.getColor("textInactiveText"));
			int h = g.getFontMetrics().getHeight();
			int textBottom = (height - h) / 2 + h - 4;
			// int x = this.getInsets().left + icon.getIconWidth() * 2;
			int x;
			if (icon != null) {
				x = (width - icon.getIconWidth()) / 2;
			} else {
				x = width / 6;
			}
			Graphics2D g2d = (Graphics2D) g;
			RenderingHints hints = g2d.getRenderingHints();
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.drawString(hint, x, textBottom);
			g2d.setRenderingHints(hints);
			g.setFont(prev);
			g.setColor(prevColor);
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		this.repaint();
		border.changeColor(Color.BLUE);
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		this.repaint();
		border.changeColor(Color.WHITE);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		checkLength(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		checkLength(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		checkLength(e);
	}

	private void checkLength(DocumentEvent documentEvent) {
		Document source = documentEvent.getDocument();
		int length = source.getLength();
		if (length >= 15) {
			this.icon = null;
		} else {
			this.icon = iconStored;
		}
	}
}