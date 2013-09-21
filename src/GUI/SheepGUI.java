package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class SheepGUI extends JFrame {
	public static void main(String args[]) {
		SheepGUI f = new SheepGUI();
		f.setVisible(true);
	}

	private int width;
	private int height;

	private String url;
	private BufferedImage mapImg;
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

	public SheepGUI() {
		super("Tittel");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		width = 1200;
		height = 600;
		setBounds(0, 0, width, height + 20); // +20 for tittel bar'en
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBackground(Color.BLUE);

		listener = new Listener();
		tListener = new TimerListener();
		timer = new Timer(100, tListener);
		// timer.start();

		/**
		 * <p>
		 * Kan fikse på url linken: <br>
		 * center={latitude},{longitude} <br>
		 * zoom={ja..hvor mye zoom :D fra 0 til 21 (tror jeg)} <br>
		 * size={størrelsen på kartet, noen begrensninger for gratis versjonen}
		 * </p>
		 * 
		 * <p>
		 * mer info: <br>
		 * https://developers.google.com/maps/documentation/staticmaps/
		 * </p>
		 **/
		url = "http://maps.googleapis.com/maps/api/staticmap?center=63.4305149,10.3950528000000300008&zoom=12&size="
				+ (width / 3) * 10 / 10 // bredden setter bildet 10% fra sidene
				+ "x" + (height / 2) * 10 / 10 // høyden
				+ "&scale=2&sensor=false"; //
		try {
			mapImg = ImageIO.read(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

		ImageIcon rightImage = new ImageIcon(mapImg);
		rightPanel = new JPanel();
		rightPanel.setBounds(width / 3, 0, 2 * width / 3, height);
		JLabel label = new JLabel(rightImage);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label.setBounds(width / 3, 0, 2 * width / 3, height);
		rightPanel.add(label);
		// rightPanel.setBackground(Color.RED);
		lp.add(rightPanel);

		leftPanel = new JPanel();
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
		forgotButton.setBounds(width / 12, 125 * height / 200, bw, 3 * bh / 4);
		forgotButton.addActionListener(listener);
		forgotButton.setVisible(false);

		sendButton = new JButton("Send");
		sendButton.setBounds(width / 12, 6 * height / 20, bw, 3 * bh / 4);
		sendButton.addActionListener(listener);
		sendButton.setVisible(false);

		backButton = new JButton("          Back", new ImageIcon(this
				.getClass().getClassLoader().getResource("sheep.png")));
		backButton.setBounds(11 * width / 48, 19 * height / 25, bw / 2, bw / 2);
		backButton.addActionListener(listener);
		backButton.setBorder(BorderFactory.createEmptyBorder());
		backButton.setHorizontalTextPosition(SwingConstants.CENTER);
		backButton.setVisible(false);

		exitButton = new JButton("          Exit", new ImageIcon(this
				.getClass().getClassLoader().getResource("sheep.png")));
		exitButton.setBounds(width / 48, 19 * height / 25, bw / 2, bw / 2);
		exitButton.addActionListener(listener);
		exitButton.setBorder(BorderFactory.createEmptyBorder());
		exitButton.setHorizontalTextPosition(SwingConstants.CENTER);

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
		unLabel.setBounds(width / 60, 40 * height / 200, cw, ch);
		unLabel.setVisible(false);

		pwLabel = new JLabel("Password: ");
		pwLabel.setBounds(width / 60, 55 * height / 200, cw, ch);
		pwLabel.setVisible(false);

		unField = new JTextField();
		unField.setBounds(width / 12, 40 * height / 200, width / 6, ch);
		unField.addActionListener(listener);
		unField.setName("unField");
		// unField.setHorizontalAlignment(JTextField.CENTER);
		unField.setVisible(false);

		pwField = new JPasswordField();
		// mask formatter!
		pwField.setBounds(width / 12, 55 * height / 200, width / 6, ch);
		pwField.addActionListener(listener);
		pwField.setName("pwField");
		// pwField.setHorizontalAlignment(JTextField.RIGHT);
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
		int cw = width / 9 /* 4 * width / 30 */;
		int ch = 3 * height / 40;
		int hReg = 15 * height / 200;
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
		regNameLabel.setBounds(width / 60, 40 * height / 200 - hReg, cw, ch);
		regNameLabel.setVisible(false);

		regEmailLabel = new JLabel("Email:");
		regEmailLabel.setBounds(width / 60, 55 * height / 200 - hReg, cw, ch);
		regEmailLabel.setVisible(false);

		regPhoneLabel = new JLabel("Phone:");
		regPhoneLabel.setBounds(width / 60, 70 * height / 200 - hReg, cw, ch);
		regPhoneLabel.setVisible(false);

		regPasswordLabel = new JLabel("Password:");
		regPasswordLabel
				.setBounds(width / 60, 85 * height / 200 - hReg, cw, ch);
		regPasswordLabel.setVisible(false);

		regPasswordLabel2 = new JLabel("Password:");
		regPasswordLabel2.setBounds(width / 60, 100 * height / 200 - hReg, cw,
				ch);
		regPasswordLabel2.setVisible(false);

		regNameField = new JTextField();
		regNameField.setBounds(width / 12, 40 * height / 200 - hReg,
				3 * cw / 2, ch);
		regNameField.addActionListener(listener);
		regNameField.setName("regNameField");
		regNameField.setVisible(false);

		regEmailField = new JTextField();
		regEmailField.setBounds(width / 12, 55 * height / 200 - hReg,
				3 * cw / 2, ch);
		regEmailField.addActionListener(listener);
		regEmailField.setName("regEmailField");
		regEmailField.setVisible(false);

		regPhoneField = new JTextField();
		regPhoneField.setBounds(width / 12, 70 * height / 200 - hReg,
				3 * cw / 2, ch);
		regPhoneField.addActionListener(listener);
		regPhoneField.setName("regPhoneField");
		regPhoneField.setVisible(false);

		// JPasswordField isteden? ******
		regPasswordField = new JTextField();
		regPasswordField.setBounds(width / 12, 85 * height / 200 - hReg,
				3 * cw / 2, ch);
		regPasswordField.addActionListener(listener);
		regPasswordField.setName("regPasswordField");
		regPasswordField.setVisible(false);

		regPasswordField2 = new JTextField();
		regPasswordField2.setBounds(width / 12, 100 * height / 200 - hReg,
				3 * cw / 2, ch);
		regPasswordField2.setName("regPasswordField2");
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
		int cWidth = width / 6/* 4 * width / 30 */;
		int cHeight = 3 * height / 40;

		emailLabel = new JLabel("Email: ");
		emailLabel.setBounds(width / 60, 90 * height / 200, cWidth, cHeight);
		emailLabel.setVisible(false);

		emailField = new JTextField();
		emailField.setBounds(width / 12, 90 * height / 200, cWidth, cHeight);
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
		loginButton.setBounds(width / 12, 6 * height / 20, width / 6,
				height / 10);
		registerButton.setBounds(width / 12, 9 * height / 20, width / 6,
				height / 10);

		for (Component c : startComps) {
			c.setVisible(bool);
		}
		mode = 0;
	}

	// Setter alle login komponenter synlige
	private void changeToLoginInterface(boolean bool) {
		loginButton.setBounds(width / 12, 80 * height / 200, width / 6,
				3 * height / 40);
		forgotButton.setBounds(width / 12, 95 * height / 200, width / 6,
				3 * height / 40);

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
		registerButton.setBounds(width / 12, 105 * height / 200, width / 6,
				3 * height / 40);

		for (Component c : registerComps) {
			c.setVisible(bool);
		}
		mode = 2;
	}

	// Setter alle forgot komponenter synlige
	private void changeToForgotInterface(boolean bool) {
		sendButton.setBounds(width / 12, 6 * height / 20, width / 6,
				3 * height / 40);
		for (Component c : forgotComps) {
			c.setVisible(bool);
		}
		mode = 3;
	}

	// Henter informasjon fra tekst boksene og skriver dem ut.
	private void registerUser() {
		String out = "Register user: \n";
		out += regNameField.getText() + "\n";
		out += regEmailField.getText() + "\n";
		out += regPhoneField.getText() + "\n";
		if (regPasswordField.getText().equals(regPasswordField2.getText())) {
			out += regPasswordField.getText() + "\n";
		} else {
			out += "passwords do not match!";
		}
		System.out.println(out);
	}

	private void login() {
		String out = "Login information: \n";
		out += unField.getText() + "\n";
		char[] c = pwField.getPassword();
		for (int i = 0; i < c.length; i++) {
			out += c[i];
		}
		out += "\n";
		System.out.println(out);
	}

	private void sendEmail() {
		String out = "Email sendt to: \n";
		out += emailField.getText() + "\n";
		System.out.println(out);
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
					if (mode == 2) {
						registerUser();
					}
					changeToLoginInterface(false);
					changeToRegisterInterface(true);
				} else if (text.equals("Login")) {
					// sjekker: hvis man er i login interface og trykker login
					// ->
					if (mode == 1) {
						// metode for å sjekke om brukernavn og passord stemmer
						login();
					}
					changeToLoginInterface(true);
				} else if (text.equals("          Exit")) {
					System.exit(0);
				} else if (text.equals("          Back")) {
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
				// System.out.println(((JTextField) arg.getSource()).getName());
				JTextField pressed = (JTextField) arg.getSource();
				String text = pressed.getName();
				// Register user
				System.out.println(mode);
				if (mode == 1) {
					if (text.equals("unField")) {
						pwField.requestFocus();
					} else if (text.equals("pwField")) {
						// LOG IN!
						login();
					}
				} else if (mode == 2) {
					if (text.equals("regNameField")) {
						regEmailField.requestFocus();
					} else if (text.equals("regEmailField")) {
						regPhoneField.requestFocus();
					} else if (text.equals("regPhoneField")) {
						regPasswordField.requestFocus();
					} else if (text.equals("regPasswordField")) {
						regPasswordField2.requestFocus();
					} else if (text.equals("regPasswordField2")) {
						// REGISTER USER!
						registerUser();
					}
				} else if (mode == 3) {
					if (text.equals("emailField")) {
						// SEND EMAIL!
						sendEmail();
					}
				}
			}
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
