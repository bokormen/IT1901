package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

public class LoginFrame extends JFrame {
	public static void main(String args[]) {
		LoginFrame f = new LoginFrame();
		f.setVisible(true);
	}

	private int width;
	private int height;

	// 0 = main, 1 = login, 2 = register, 3 = forgot?
	private int mode = 0;

	private Listener listener;
	private TimerListener tListener;
	private Timer timer;

	private JButton loginButton;
	private JButton registerButton;
	private JButton forgotButton;
	private JButton backButton;
	private JButton exitButton;
	private JButton sendButton;

	private JPanel rightPanel;
	private JPanel leftPanel;

	// login components
	private JLabel unLabel;
	private JLabel pwLabel;
	private JTextField unField;
	private JPasswordField pwField;

	// forgot components
	private JLabel emailLabel;
	private JTextField emailField;

	// register components
	private JLabel regNameLabel;
	private JLabel regEmailLabel;
	private JLabel regPhoneLabel;
	private JLabel regPasswordLabel;
	private JLabel regPasswordLabel2;
	private JTextField regNameField;
	private JTextField regEmailField;
	private JTextField regPhoneField;
	private JTextField regPasswordField;
	private JTextField regPasswordField2;

	private ArrayList<Component> startComps;
	private ArrayList<Component> loginComps;
	private ArrayList<Component> registerComps;
	private ArrayList<Component> forgotComps;

	public LoginFrame() {
		super("Tittel");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		width = 1200;
		height = 600;
		setBounds(0, 0, width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBackground(Color.BLUE);

		listener = new Listener();
		tListener = new TimerListener();
		timer = new Timer(100, tListener);
		timer.start();

		JLayeredPane lp = getLayeredPane();

		createComponentArrays(); // brukes i createComponents

		createButtons(lp);
		createLoginInterfaceComponents(lp);
		createRegisterInterfaceComponents(lp);
		createForgotInterfaceComponents(lp);
		createPanels(lp);

		createComponentArrays(); // igjen for å legge til objektene

	}

	// for tull SLETT
	private Color randomColor() {
		Random r = new Random();
		return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
	}

	// Lager to paneler fordelt på: left; width/3 & right; 2*width/3
	private void createPanels(JLayeredPane lp) {
		/*
		 * ImageIcon rightImage = new ImageIcon(this.getClass().getClassLoader()
		 * .getResource("penguin.jpg")); ImageIcon leftImage = new
		 * ImageIcon(this.getClass().getClassLoader()
		 * .getResource("minion.jpg"));
		 */

		rightPanel = new JPanel(/* rightImage */);
		rightPanel.setBounds(width / 3, 0, 2 * width / 3, height);
		rightPanel.setBackground(Color.RED);
		lp.add(rightPanel);

		leftPanel = new JPanel(/* leftImage */);
		leftPanel.setBounds(0, 0, width / 3, height);
		leftPanel.setBackground(Color.ORANGE);
		lp.add(leftPanel);
	}

	// Lager alle knappene og plasserer de der de skal være
	private void createButtons(JLayeredPane lp) {
		int bw = width / 6;
		int bh = height / 10;

		loginButton = new JButton("Login");
		loginButton.setBounds(width / 12, 6 * height / 20, bw, bh);
		loginButton.addActionListener(listener);

		registerButton = new JButton("Register");
		registerButton.setBounds(width / 12, 9 * height / 20, bw, bh);
		registerButton.addActionListener(listener);

		forgotButton = new JButton("Forgot");
		forgotButton.setBounds(3 * width / 24, /* 130 * height / 200 */
				21 * height / 25, bw / 2, 3 * bh / 4);
		forgotButton.addActionListener(listener);
		forgotButton.setVisible(false);

		sendButton = new JButton("Send");
		sendButton.setBounds(width / 12, 6 * height / 20, bw, bh);
		sendButton.addActionListener(listener);
		sendButton.setVisible(false);

		backButton = new JButton("Back");
		backButton.setBounds(11 * width / 48, 21 * height / 25, bw / 2,
				3 * bh / 4);
		backButton.addActionListener(listener);
		backButton.setVisible(false);

		exitButton = new JButton("Exit");
		exitButton.setBounds(width / 48, 21 * height / 25, bw / 2, 3 * bh / 4);
		exitButton.addActionListener(listener);

		lp.add(loginButton);
		lp.add(registerButton);
		lp.add(forgotButton);
		lp.add(sendButton);
		lp.add(backButton);
		lp.add(exitButton);
	}

	// Lager alle komponenene til login interfacen.
	private void createLoginInterfaceComponents(JLayeredPane lp) {
		int cw = 4 * width / 30;
		int ch = 3 * height / 40;

		unLabel = new JLabel("Email: ");
		unLabel.setBounds(width / 30, 65 * height / 200, cw, ch);
		unLabel.setVisible(false);

		pwLabel = new JLabel("Password: ");
		pwLabel.setBounds(width / 30, 80 * height / 200, cw, ch);
		pwLabel.setVisible(false);

		unField = new JTextField();
		unField.setBounds(width / 10, 65 * height / 200, 3 * cw / 2, ch);
		unField.addActionListener(listener);
		unField.setName("unField");
		unField.setVisible(false);

		pwField = new JPasswordField();
		pwField.setBounds(width / 10, 80 * height / 200, 3 * cw / 2, ch);
		pwField.addActionListener(listener);
		pwField.setName("pwField");
		pwField.setVisible(false);

		lp.add(unLabel);
		lp.add(pwLabel);
		lp.add(unField);
		lp.add(pwField);
	}

	// Lager alle komponenene til register interfacen.
	private void createRegisterInterfaceComponents(JLayeredPane lp) {
		// Tenkte å lage en for løkke og gjøre alt litt mindre, men før jeg
		// finner en BRA måte å gjøre det på, så blir det sånn som det ernå.
		int cw = 4 * width / 30;
		int ch = 3 * height / 40;
		// lister med alle verdiene til komponentene under. STEMMER IKKE
		/*
		 * int[] type = { 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 }; String[] texts = {
		 * "Name:", "Email:", "Phone:", "Password:", "Password:", null, null,
		 * null, null, null }; Boolean[] bools = { false, false, false, false,
		 * false, false, false, false, false, false }; int[] x = { 30, 30, 30,
		 * 30, 30, 10, 10, 10, 10, 10 }; int[] y = { 50, 62, 74, 86, 98, 50, 62,
		 * 74, 86, 98 }; int[] cWidthList = { cWidth, cWidth, cWidth, cWidth,
		 * cWidth, 3 * cWidth / 2, 3 * cWidth / 2, 3 * cWidth / 2, 3 * cWidth /
		 * 2, 3 * cWidth / 2, 3 * cWidth / 2 };
		 */

		regNameLabel = new JLabel("Name:");
		regNameLabel.setBounds(width / 30, 40 * height / 200, cw, ch);
		regNameLabel.setVisible(false);

		regEmailLabel = new JLabel("Email:");
		regEmailLabel.setBounds(width / 30, 55 * height / 200, cw, ch);
		regEmailLabel.setVisible(false);

		regPhoneLabel = new JLabel("Phone:");
		regPhoneLabel.setBounds(width / 30, 70 * height / 200, cw, ch);
		regPhoneLabel.setVisible(false);

		regPasswordLabel = new JLabel("Password:");
		regPasswordLabel.setBounds(width / 30, 85 * height / 200, cw, ch);
		regPasswordLabel.setVisible(false);

		regPasswordLabel2 = new JLabel("Password:");
		regPasswordLabel2.setBounds(width / 30, 100 * height / 200, cw, ch);
		regPasswordLabel2.setVisible(false);

		regNameField = new JTextField();
		regNameField.setBounds(width / 10, 40 * height / 200, 3 * cw / 2, ch);
		regNameField.addActionListener(listener);
		regNameField.setBackground(Color.ORANGE);
		regNameField.setVisible(false);

		regEmailField = new JTextField();
		regEmailField.setBounds(width / 10, 55 * height / 200, 3 * cw / 2, ch);
		regEmailField.addActionListener(listener);
		regEmailField.setBackground(Color.GREEN);
		regEmailField.setVisible(false);

		regPhoneField = new JTextField();
		regPhoneField.setBounds(width / 10, 70 * height / 200, 3 * cw / 2, ch);
		regPhoneField.addActionListener(listener);
		regPhoneField.setBackground(Color.RED);
		regPhoneField.setVisible(false);

		regPasswordField = new JTextField();
		regPasswordField.setBounds(width / 10, 85 * height / 200, 3 * cw / 2,
				ch);
		regPasswordField.addActionListener(listener);
		regPasswordField.setBackground(Color.YELLOW);
		regPasswordField.setVisible(false);

		regPasswordField2 = new JTextField();
		regPasswordField2.setBounds(width / 10, 100 * height / 200, 3 * cw / 2,
				ch);
		regPasswordField2.setBackground(Color.BLUE);
		regPasswordField2.addActionListener(listener);
		regPasswordField2.setVisible(false);

		lp.add(regNameLabel);
		lp.add(regEmailLabel);
		lp.add(regPhoneLabel);
		lp.add(regPasswordLabel);
		lp.add(regPasswordLabel2);
		lp.add(regNameField);
		lp.add(regEmailField);
		lp.add(regPhoneField);
		lp.add(regPasswordField);
		lp.add(regPasswordField2);
		/*
		 * Component[] comps = { regNameLabel, regEmailLabel, regPhoneLabel,
		 * regPasswordLabel, regPasswordLabel, regNameField, regEmailField,
		 * regPhoneField, regPasswordField, regPasswordField2 };
		 * 
		 * for (int i = 0; i < comps.length; i++) { comps[i].setBounds(width /
		 * x[i], y[i] * height / 200, cWidthList[i], cHeight);
		 * comps[i].setVisible(bools[i]);
		 * 
		 * lp.add(comps[i]); }
		 */

	}

	// Lager alle komponenene til forgot interfacen.
	private void createForgotInterfaceComponents(JLayeredPane lp) {
		int cWidth = 4 * width / 30;
		int cHeight = 3 * height / 40;

		emailLabel = new JLabel("Email: ");
		emailLabel.setBounds(width / 30, 90 * height / 200, cWidth, cHeight);
		emailLabel.setVisible(false);

		emailField = new JTextField();
		emailField.setBounds(cWidth / 2 + width / 30, 90 * height / 200,
				cWidth, cHeight);
		emailField.addActionListener(listener);
		emailField.setName("emailField");
		emailField.setVisible(false);

		lp.add(emailLabel);
		lp.add(emailField);
	}

	// setter sammen listene med de ulike kompoentene for å da gå igjennom
	// løkker for å sette de riktige komponentene synlige og usynlige.
	private void createComponentArrays() {
		startComps = new ArrayList<Component>();
		startComps.clear();
		startComps.add(loginButton);
		startComps.add(registerButton);
		startComps.add(exitButton);

		loginComps = new ArrayList<Component>();
		loginComps.clear();
		loginComps.add(unLabel);
		loginComps.add(pwLabel);
		loginComps.add(pwField);
		loginComps.add(unField);
		loginComps.add(forgotButton);
		loginComps.add(loginButton);
		loginComps.add(backButton);
		loginComps.add(exitButton);

		forgotComps = new ArrayList<Component>();
		forgotComps.clear();
		forgotComps.add(emailLabel);
		forgotComps.add(emailField);
		forgotComps.add(backButton);
		forgotComps.add(exitButton);
		forgotComps.add(sendButton);

		registerComps = new ArrayList<Component>();
		registerComps.clear();
		registerComps.add(regNameLabel);
		registerComps.add(regEmailLabel);
		registerComps.add(regPhoneLabel);
		registerComps.add(regPasswordLabel);
		registerComps.add(regPasswordLabel2);
		registerComps.add(regNameField);
		registerComps.add(regEmailField);
		registerComps.add(regPhoneField);
		registerComps.add(regPasswordField);
		registerComps.add(regPasswordField2);
		registerComps.add(registerButton);
		registerComps.add(backButton);
		registerComps.add(exitButton);

	}

	// Setter alle start komponenter synlige
	private void changeToStartInterface(Boolean bool) {
		loginButton.setLocation(width / 12, 6 * height / 20);
		registerButton.setLocation(width / 12, 9 * height / 20);

		for (Component c : startComps) {
			c.setVisible(bool);
		}
		mode = 0;
	}

	// Setter alle login komponenter synlige
	private void changeToLoginInterface(boolean bool) {
		loginButton.setLocation(width / 12, 105 * height / 200);
		// forgotButton.setLocation(width / 12, 11 * height / 20);

		if (bool) {
			registerButton.setVisible(!bool);
		}
		for (Component c : loginComps) {
			c.setVisible(bool);
		}
		mode = 1;
	}

	// Setter alle register komponenter synlige
	private void changeToRegisterInterface(boolean bool) {
		registerButton.setLocation(width / 12, height / 20);

		for (Component c : registerComps) {
			c.setVisible(bool);
		}
		mode = 2;
	}

	// Setter alle forgot komponenter synlige
	private void changeToForgotInterface(boolean bool) {
		if (bool) {
			registerButton.setVisible(!bool);
			loginButton.setVisible(!bool);
		}

		for (Component c : forgotComps) {
			c.setVisible(bool);
		}
		mode = 3;
	}

	// Lytteren til knappene, finner riktig handling til hver knapp
	private class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg) {
			// rightPanel.setBackground(randomColor());
			if (arg.getSource() instanceof JButton) {
				JButton pressed = (JButton) arg.getSource();
				String text = pressed.getText();

				if (text.equals("Register")) {
					changeToLoginInterface(false);
					changeToRegisterInterface(true);
				} else if (text.equals("Login")) {
					changeToLoginInterface(true);
					// sjekker: hvis man er i login interface og trykker login
					// ->
					if (mode == 1) {
						// metode for å sjekke om brukernavn og passord stemmer
						System.out.println(unField.getText());
						char[] pwch = pwField.getPassword();
						for (int i = 0; i < pwch.length; i++) {
							System.out.print(pwch[i]);
						}
					}
				} else if (text.equals("Exit")) {
					System.exit(0);
				} else if (text.equals("Back")) {
					// mode holder styr på hvor man er, så programet vet hvor en
					// skal sendes tilbake til
					if (mode == 1) {
						changeToLoginInterface(false);
						changeToStartInterface(true);
					} else if (mode == 2) {
						changeToRegisterInterface(false);
						changeToStartInterface(true);
					} else if (mode == 3) {
						changeToForgotInterface(false);
						changeToLoginInterface(true);
					}
				} else if (text.equals("Forgot")) {
					changeToLoginInterface(false);
					changeToForgotInterface(true);
				} else if (text.equals("Send")) {
					// sjekker: er i forgot interface og trykker send ->
					if (mode == 3) {
						// metode for å sjekke om email er i systemet, evt.
						// sende ut
						// nytt passord til mail?
						String email = emailField.getText();
						System.out.println(email);
						// sendMail(email);
						changeToForgotInterface(false);
						changeToLoginInterface(true);
					}
				}
			} else if (arg.getSource() instanceof JPasswordField) {
				((JPasswordField) arg.getSource()).getPassword();
			} else if (arg.getSource() instanceof JTextField) {
				System.out.println(((JTextField) arg.getSource()).getName());

			}
		}
	}

	private class TextListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

		}

	}

	// Lytter for å håndtere tid. Til komponenter som skal flyttes over tid.
	// Animasjoner!
	private class TimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			rightPanel.setBackground(randomColor());
		}
	}
}
