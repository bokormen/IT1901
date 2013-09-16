package GUI;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

public class GUI {

	private JFrame loginFrame;
	private JFrame forgotFrame;
	private JFrame mainFrame;
	private JFrame registerUserFrame;
	private JFrame registerSheepFrame;

	// Listener, lytter etter knappetrykk etc.
	private Listener listener = new Listener();

	// button
	private JButton button = new JButton();

	// label
	private JLabel label = new JLabel();

	// Forgot email text fields
	JTextField emailField1 = new JTextField();
	JTextField emailField2 = new JTextField();

	// Login information fields
	JPasswordField pwField = new JPasswordField();
	JTextField unField = new JTextField();

	// Register new user fields
	JTextField name = new JTextField(), email = new JTextField();
	JTextField phone = new JTextField();
	JTextField password = new JTextField(), password2 = new JTextField();

	// Register new sheep fields
	JTextField ID = new JTextField(), weight = new JTextField();
	JTextField status = new JTextField(), owner = new JTextField();
	JTextField shepard = new JTextField();

	// Lager nye Components og setter de på panelet (JPane);
	private void createComponents(Container pane, Component[] comps,
			String[] text, int[] gridwidths, int[] gridsx, int[] gridsy) {
		pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof JLabel) {
				comps[i] = new JLabel(text[i]);
			} else if (comps[i] instanceof JTextField) {
				comps[i] = new JTextField(20);
				((JTextField) comps[i]).setText(text[i]);
			} else if (comps[i] instanceof JButton) {
				comps[i] = new JButton(text[i]);
				((JButton) comps[i]).addActionListener(listener);
			}
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = gridwidths[i];
			c.gridx = gridsx[i];
			c.gridy = gridsy[i];
			pane.add(comps[i], c);
		}
	}

	//
	public void addMainFrameComponents(Container pane) {
		pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		mainFrame.setSize(1060, 540);
		// pane.setLayout(null);

		JLayeredPane lp = new JLayeredPane();
		pane.setSize(1060, 540);
		pane.add(lp);
		/*
		 * pane.setLayout(new GridBagLayout()); GridBagConstraints c = new
		 * GridBagConstraints(); c.insets = new Insets(5, 5, 5, 5);
		 * 
		 * c.anchor = GridBagConstraints.FIRST_LINE_END; c.gridx = 100; c.gridy
		 * = 540; pane.add(addMainFrameButtons(), c);
		 * 
		 * label = new JLabel(new ImageIcon(this.getClass().getClassLoader()
		 * .getResource("penguin.jpg"))); c.fill =
		 * GridBagConstraints.HORIZONTAL; c.gridx = 1100; c.gridy = 540;
		 * pane.add(label, c);
		 */
		JPanel buttons = addMainFrameButtons();
		buttons.setBounds(0, 0, 120, 540);
		lp.add(buttons);
		lp.setOpaque(true);

		label = new JLabel(new ImageIcon(this.getClass().getClassLoader()
				.getResource("penguin.jpg")));
		label.setBounds(120, 0, 960, 540);
		lp.add(label, new Integer(1));

		JLabel minions = new JLabel(new ImageIcon(this.getClass()
				.getClassLoader().getResource("minion.gif")));
		minions.setBounds(600, 180, 150, 300);
		lp.add(minions, new Integer(2));

		// adds a manubar

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Meny");
		menuBar.add(menu);

		JMenuItem restart = new JMenuItem("Start nytt spill");
		restart.addActionListener(listener);
		menu.add(restart);

		JMenuItem exit = new JMenuItem("Avslutt");
		exit.addActionListener(listener);
		menu.add(exit);

		// pane.add(menuBar);

	}

	private JPanel addMainFrameButtons() {
		JPanel pane = new JPanel();
		pane.setSize(100, 540);

		Component[] comps = { button, button, button, button, button };
		String[] text = { "Add Sheep", "Find Sheep", "Edit profile", "Log out",
				"Quit" };
		int[] gridwidths = { 3, 1, 2, 1, 2, 1, 1 };
		int[] gridxs = { 0, 0, 0, 0, 0 };
		int[] gridys = { 0, 1, 2, 3, 4 };

		createComponents(pane, comps, text, gridwidths, gridxs, gridys);

		return pane;
	}

	public void addForgotFrameComponents(Container pane) {

		Component[] comps = { label, label, emailField1, label, emailField2,
				button, button };
		String[] text = { "We will generate and email you a new password.",
				"Email: ", null, "Confirm email: ", null, "Back", "Continue" };
		int[] gridwidths = { 3, 1, 2, 1, 2, 1, 1 };
		int[] gridxs = { 0, 0, 1, 0, 1, 0, 1 };
		int[] gridys = { 0, 1, 1, 2, 2, 3, 3 };

		createComponents(pane, comps, text, gridwidths, gridxs, gridys);
	}

	public void addLoginFrameComponents(Container pane) {
		Component[] comps = { label, unField, label, pwField, button, button,
				button, button };
		String[] text = { "Enter username: ", null, "Enter password: ", null,
				"Login", "Recover", "Register", "Quit" };
		int[] gridwidths = { 1, 3, 1, 3, 1, 1, 1, 1 };
		int[] gridxs = { 0, 1, 0, 1, 0, 1, 2, 3 };
		int[] gridys = { 0, 0, 1, 1, 2, 2, 2, 2 };

		createComponents(pane, comps, text, gridwidths, gridxs, gridys);
	}

	public void addRegisterUserFrameComponents(Container pane) {
		Component[] comps = { label, name, label, email, label, phone, label,
				password, label, password2, button, button };
		String[] text = { "Name: ", null, "Email: ", null, "Phone: ", null,
				"Password: ", null, "Password: ", null, "Back",
				"Create Program" };
		int[] gridwidths = { 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2 };
		int[] gridxs = { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
		int[] gridys = { 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5 };

		createComponents(pane, comps, text, gridwidths, gridxs, gridys);
	}

	public void addRegisterSheepFrameComponents(Container pane) {
		Component[] comps = { label, ID, label, weight, label, status, label,
				owner, label, shepard, button, button };
		String[] text = { "ID: ", null, "Email: ", null, "Phone: ", null,
				"Password: ", null, "Password: ", "Confirm password", "Back",
				"Create Account" };
		int[] gridwidths = { 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 1 };
		int[] gridxs = { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
		int[] gridys = { 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6 };

		createComponents(pane, comps, text, gridwidths, gridxs, gridys);

	}

	// legge til hotkeys?
	private class Listener implements ActionListener {
		Timer timer = new Timer(1, this);

		@Override
		public void actionPerformed(ActionEvent action) {
			JButton pressed = (JButton) action.getSource();
			String text = pressed.getText();
			System.out.println(text);
			if (text.equals("Login")) {
				// sjekke om brukernavn og passord stemmer oversens
				mainFrame.setVisible(true);
				loginFrame.setVisible(false);
			} else if (text.equals("Recover")) {
				// Skriv inn email og få tilsendt pw og brukernavn
				loginFrame.setVisible(false);
				forgotFrame.setVisible(true);
			} else if (text.equals("Quit")) {
				System.exit(0);
			} else if (text.equals("Continue")) {
				// sjekke om emailene stemmer overens, og at det er en email i
				// systemet.
			} else if (text.equals("Back")) {
				forgotFrame.setVisible(false);
				registerUserFrame.setVisible(false);
				registerSheepFrame.setVisible(false);
				loginFrame.setVisible(true);
			} else if (text.equals("Log out")) {
				mainFrame.setVisible(false);
				loginFrame.setVisible(true);
			} else if (text.equals("Register")) {
				// sjekke alt ofc
				loginFrame.setVisible(false);
				registerUserFrame.setVisible(true);
			} else if (text.equals("Add Sheep")) {
				mainFrame.setVisible(false);
				registerSheepFrame.setVisible(true);
			}
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private GUI() {
		// Create and set up the window.

		loginFrame = new JFrame("Sign in interface");
		forgotFrame = new JFrame("Reset password interface");
		mainFrame = new JFrame("tittel");
		registerUserFrame = new JFrame("Register new user");
		registerSheepFrame = new JFrame("Register new sheep");

		JFrame[] list = { loginFrame, forgotFrame, mainFrame,
				registerUserFrame, registerSheepFrame };

		for (int i = 0; i < list.length; i++) {
			list[i].setLocationByPlatform(true);
			list[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		// Set up the content pane.
		addLoginFrameComponents(loginFrame.getContentPane());
		addForgotFrameComponents(forgotFrame.getContentPane());
		addMainFrameComponents(mainFrame.getContentPane());
		addRegisterUserFrameComponents(registerUserFrame.getContentPane());
		addRegisterSheepFrameComponents(registerSheepFrame.getContentPane());

		// Display the window.
		loginFrame.pack();
		loginFrame.setVisible(true);

		forgotFrame.pack();
		forgotFrame.setVisible(false);

		// mainFrame.pack();
		mainFrame.setVisible(false);

		registerUserFrame.pack();
		registerUserFrame.setVisible(false);

		registerSheepFrame.pack();
		registerUserFrame.setVisible(false);
	}

	public static void main(String[] args) {
		GUI gui = new GUI();
	}
}