package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * 
 * @author andreas
 * 
 */
@SuppressWarnings("serial")
public class MyTextField extends JTextField implements FocusListener, DocumentListener {
	private Icon icon;
	private Icon iconStored;
	private String hint;
	private Insets dummyInsets;
	private MyBorder border;
	private Timer timer;
	private int speed;

	public MyTextField(String icon, String hint, int speed) {
		this.hint = hint;
		this.speed = speed;
		this.timer = new Timer(speed, new DrawingTimer());
		setVisible(false);
		setHorizontalAlignment(JTextField.CENTER);
		setDocument(new MyDocumentLimiter(64)); // bokstav begrensning
		setOpaque(false);
		setForeground(Color.WHITE);
		setCaretColor(Color.WHITE);
		try {
			if (icon != null) {
				this.iconStored = new ImageIcon(ImageIO.read(this.getClass().getClassLoader()
						.getResource("images/" + icon + ".png")));
				this.icon = iconStored;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.border = new MyBorder(20, speed);
		setBorder(border);
		this.dummyInsets = border.getBorderInsets(this);
		addFocusListener(this);
		getDocument().addDocumentListener(this);
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
		border.changeColor(Color.BLUE);
		if (speed != -1) {
			border.setPaintAnimationVar();
			timer.start();
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		border.changeColor(Color.WHITE);
		if (speed != -1) {
			timer.stop();
		}
		this.repaint();
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

	/**
	 * Sjekker lengden paa teksten og fjerner iconet hvis teksten gaar over
	 * 
	 * @param documentEvent
	 *            DocumentEvent
	 */
	private void checkLength(DocumentEvent documentEvent) {
		Document source = documentEvent.getDocument();
		int length = source.getLength();
		if (length >= 15) {
			this.icon = null;
		} else {
			this.icon = iconStored;
		}
	}

	/**
	 * Klasse som tegner en paa nytt knapp hver gang den blir tilkalt.
	 * 
	 * @author andreas
	 * 
	 */

	private class DrawingTimer implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			border.setUpdateTrue();
			repaint();
		}
	}
}