package GUI;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Slide extends JFrame {
	public static void main(String args[]) {
		Slide slide = new Slide();
		slide.setVisible(true);
	}

	private ArrayList<Button> buttons = new ArrayList<Button>();

	// New sheep panel variables
	private JPanel newSheepPanel;
	private boolean sheepPanelDown = false;
	private JTextField ID = new JTextField();
	private JTextField weight = new JTextField();
	private JTextField status = new JTextField();
	private JTextField owner = new JTextField();
	private JTextField shepard = new JTextField();
	private String[] texts = { "ID:", "Weight:", "Status:", "Owner:", "Shepard" };

	private Random random = new Random();
	private Listener listener = new Listener();
	private TimerListener tl = new TimerListener();
	private Timer timer = new Timer(2, tl);

	public Slide() {
		super("shit");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, dim.width, dim.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.CYAN);

		JLayeredPane lp = getLayeredPane();
		createButtons();
		for (Button b : buttons) {
			lp.add(b);
		}

		newSheepPanel = new JPanel();
		newSheepPanel.setBounds(0, 40, 400, 0);
		newSheepPanel.setLayout(new GridBagLayout());
		newSheepPanel
				.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lp.add(newSheepPanel);
	}

	private void createButtons() {
		String[] texts = { "wee", "woo", "yay", "noo" };
		for (int i = 0; i < texts.length; i++) {
			buttons.add(new Button(0, i * 40, 200, 40, i, texts[i], listener));
		}
	}

	// oppdaterer panelet med de ulike JTe
	private void updateSlidePanel(JPanel p) {
		Rectangle dim = p.getBounds();
		int speed = 2;
		int height = 40;
		if (!sheepPanelDown) {
			if (dim.height == height) {
				addComponentSlide(p, ID, texts, 0);
			} else if (dim.height == height * 2) {
				addComponentSlide(p, status, texts, 1);
			} else if (dim.height == height * 3) {
				addComponentSlide(p, owner, texts, 2);
			} else if (dim.height == height * 4) {
				addComponentSlide(p, weight, texts, 3);
			} else if (dim.height == height * 5) {
				addComponentSlide(p, shepard, texts, 4);
				timer.stop();
				sheepPanelDown = true;
			}
			p.setSize(dim.width, dim.height + speed);
			for (Button b : buttons) {
				if (b.getNum() > 0) {
					b.updateLocation(0, speed);
				}
			}
		}
		if (sheepPanelDown) {
			speed *= -1;
			if (dim.height == 0) {
				timer.stop();
				sheepPanelDown = false;
			} else if (dim.height == height) {
				p.remove(ID);
			} else if (dim.height == height * 2) {
				p.remove(status);
			} else if (dim.height == height * 3) {
				p.remove(owner);
			} else if (dim.height == height * 4) {
				p.remove(weight);
			} else if (dim.height == height * 5) {
				p.remove(shepard);
			}
			p.setSize(dim.width, dim.height + speed);
			for (Button b : buttons) {
				if (b.getNum() > 0) {
					b.updateLocation(0, speed);
				}
			}
		}
	}

	private void addComponentSlide(JPanel p, JComponent comp, String[] text,
			int i) {
		GridBagConstraints c = new GridBagConstraints();
		Rectangle dim = p.getBounds();
		JLabel label = new JLabel(text[i]);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = i;
		c.gridx = 0;
		label.setBounds(0, i * 50, 200, 40);
		p.add(label, c);
		c.gridx = 1;
		comp.setBounds(100, i * 50, 200, 40);
		p.add(comp, c);

	}

	private class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg) {
			if (arg.getSource() instanceof JButton) {
				if (((JButton) arg.getSource()).getText().equals("wee")) {
					if (!timer.isRunning()) {
						timer.start();
					}
				}
			}
		}
	}

	private class TimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			updateSlidePanel(newSheepPanel);
		}
	}

	private class Button extends JButton {
		private int x, y;
		private int height, weight;
		private int num;
		private String text;

		public Button(int x, int y, int height, int weight, int num,
				String text, ActionListener al) {
			this.x = x;
			this.y = y;
			this.height = height;
			this.weight = weight;
			this.num = num;
			setBounds(x, y, height, weight);
			setText(text);
			addActionListener(al);
			setBackground(Color.ORANGE);
		}

		public int getNum() {
			return num;
		}

		public void updateLocation(int dx, int dy) {
			x += dx;
			y += dy;
			setBounds(x, y, height, weight);
		}
	}
}
