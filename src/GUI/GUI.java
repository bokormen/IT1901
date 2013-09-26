package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
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
import javax.swing.text.JTextComponent;

import div.User;

/**
 * A class that handles the GUI
 * 
 * @author andreas
 * 
 */
public class GUI extends JFrame {
	public static void main(String args[]) {
		GUI f = new GUI();
		f.setVisible(true);
	}

	// plasserer alt bedre! login specially

	private int width;
	private int height;

	private User user; // gitt bruker etter en har logget på
	private User tUser = new User(); // test bruker, for å sjekke om input er
										// gyldig

	public static int START = 0, LOGIN = 1, REG = 2, FORGOT = 3, MAIN = 4;
	private int state; // hvor i programmet en er

	// Font
	public final static String fontType = "Tahoma";
	public final static Font FONT = new Font(fontType, Font.BOLD, 12);

	public final static Color valid = Color.green;
	public final static Color invalid = new Color(255, 100, 15);

	private Listener actionListener;
	private FocusListener focusListener;
	private TimerListener tListener;
	private Timer timer;

	private GoogleStaticMap googleStaticMap;
	private KartverketStaticMap kartverketStaticMap;

	// private ClientConnection cc;

	public GUI() {
		super("Tittel");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		width = 1200;
		height = 600;
		setBounds(0, 0, width, height + 20); // +20 for tittel bar'en
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBackground(Color.BLUE);

		actionListener = new Listener();
		createFocusListener();
		tListener = new TimerListener();
		timer = new Timer(100, tListener);
		// timer.start();

		googleStaticMap = new GoogleStaticMap();
		kartverketStaticMap = new KartverketStaticMap();

		// cc = new ClientConnection();

		Server.addUsers(); // Legger til brukere til testing

		JLayeredPane lp = getLayeredPane();

		createComponentArrays(); // brukes i createComponents

		createButtons(lp);
		createLoginInterfaceComponents(lp);
		createRegisterInterfaceComponents(lp);
		createForgotInterfaceComponents(lp);
		createPanels(lp);

		createComponentArrays(); // igjen for å legge til objektene
		// setFocusListener(); // legger til focusListener til alle text
		// feltene.

		// cListCreate();

		// Setter alle start komponentene synlige
		changeToStartInterface(true);

		repaintPanel();
	}

	public void repaintPanel() {
		repaint();
		validate();

	}

	// for tull SLETT
	private Color randomColor() {
		Random r = new Random();
		return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
	}

	/**
	 * Lager to paneler fordelt på: leftPanel (width/3) og rightPanel
	 * (2*width/3)
	 * 
	 * @param lp
	 *            LayeredPane
	 */

	/**
	 * @param lp
	 */
	private void createPanels(JLayeredPane lp) {
		boolean googleMap = false;
		boolean maps = false;
		BufferedImage rightImage;
		if (googleMap) {
			// rightImage = new ImageIcon(googleStaticMap.getImage(63.4305149,
			// 10.3950528, 12, width / 3, height / 2, 2));
		}
		// "topo2", "toporaster2", "norges_grunnkart", "kartdata2"
		if (maps) {
			String layer = "topo2";
			BufferedImage img1 = (BufferedImage) (kartverketStaticMap.getImage(
					layer, 4332, 2244, 13));
			BufferedImage img2 = (BufferedImage) (kartverketStaticMap.getImage(
					layer, 4333, 2244, 13));
			BufferedImage img3 = (BufferedImage) (kartverketStaticMap.getImage(
					layer, 4332, 2245, 13));
			BufferedImage img4 = (BufferedImage) (kartverketStaticMap.getImage(
					layer, 4333, 2245, 13));

			BufferedImage[] imgs = { img1, img2, img3, img4 };
			int[] xs = { 0, 256, 0, 256 };
			int[] ys = { 0, 0, 256, 256 };

			rightPanel = new MyImagePanel(imgs, xs, ys); //
			rightPanel.setBounds(width / 3, 0, 2 * width / 3, height);

			// rightPanel.setBackground(Color.RED);
			lp.add(rightPanel);
		}

		BufferedImage img1 = null;
		try {
			img1 = ImageIO.read(this.getClass().getClassLoader()
					.getResource("blackcarbon.jpg"));
			System.out.println(img1.getHeight());
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		BufferedImage[] imgs = { img1 };
		int[] xs = { 0 };
		int[] ys = { 0 };

		leftPanel = new MyImagePanel(imgs, xs, ys);
		leftPanel.repaint();
		leftPanel.setBounds(0, 0, width / 3, height);
		// leftPanel.setBackground(Color.ORANGE);
		lp.add(leftPanel);

	}

	/**
	 * En egen klasse for JPanel som har en egen verson av painComponent
	 * metoden.
	 * 
	 * @author andreas
	 * 
	 */
	class MyImagePanel extends JPanel {
		private BufferedImage[] img;
		private int[] x;
		private int[] y;

		public MyImagePanel(BufferedImage[] img, int[] x, int[] y) {
			this.img = img;
			this.x = x;
			this.y = y;
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (img != null) {
				for (int i = 0; i < img.length; i++) {
					g.drawImage(img[i], x[i], y[i], this);
				}
			}
		}
	}

	/**
	 * Creates all the buttons and places them at their start position
	 * 
	 * @param lp
	 *            LayeredPane
	 */
	private void createButtons(JLayeredPane lp) {
		int bw = width / 6;
		int bh = height / 10;

		loginButton = new JButton("Login");
		loginButton.setBounds(width / 12, 6 * height / 20, bw, bh);
		loginButton.addActionListener(actionListener);
		loginButton.setVisible(false);

		registerButton = new JButton("Register");
		registerButton.setBounds(width / 12, 9 * height / 20, bw, bh);
		registerButton.addActionListener(actionListener);
		registerButton.setVisible(false);

		forgotButton = new JButton("Forgot");
		forgotButton.setBounds(width / 12, 125 * height / 200, bw, 3 * bh / 4);
		forgotButton.addActionListener(actionListener);
		forgotButton.setVisible(false);

		sendButton = new JButton("Send");
		sendButton.setBounds(width / 12, 9 * height / 20, bw, 3 * bh / 4);
		sendButton.addActionListener(actionListener);
		sendButton.setVisible(false);

		backButton = new JButton("          Back", new ImageIcon(this
				.getClass().getClassLoader().getResource("sheep.png")));
		backButton.setBounds(11 * width / 48, 19 * height / 25, bw / 2, bw / 2);
		backButton.addActionListener(actionListener);
		backButton.setBorder(BorderFactory.createEmptyBorder());
		backButton.setHorizontalTextPosition(SwingConstants.CENTER);
		backButton.setVisible(false);

		exitButton = new JButton("          Exit", new ImageIcon(this
				.getClass().getClassLoader().getResource("sheep.png")));
		exitButton.setBounds(width / 48, 19 * height / 25, bw / 2, bw / 2);
		exitButton.addActionListener(actionListener);
		exitButton.setBorder(BorderFactory.createEmptyBorder());
		exitButton.setHorizontalTextPosition(SwingConstants.CENTER);
		exitButton.setVisible(false);

		lp.add(loginButton);
		lp.add(registerButton);
		lp.add(forgotButton);
		lp.add(sendButton);
		lp.add(backButton);
		lp.add(exitButton);
	}

	/**
	 * Creates all the components to the login interface
	 * 
	 * @param lp
	 *            LayeredPane
	 */

	private void createLoginInterfaceComponents(JLayeredPane lp) {
		int cw = 4 * width / 30;
		int ch = 3 * height / 40;

		unLabel = new JLabel("Email: ");
		unLabel.setForeground(Color.WHITE);
		unLabel.setBounds(width / 60, 40 * height / 200, cw, ch);
		unLabel.setVisible(false);

		pwLabel = new JLabel("Password: ");
		pwLabel.setForeground(Color.WHITE);
		pwLabel.setBounds(width / 60, 55 * height / 200, cw, ch);
		pwLabel.setVisible(false);

		unField = new JTextField();
		unField.setBounds(width / 12, 40 * height / 200, width / 6, ch);
		unField.addActionListener(actionListener);
		unField.setName("unField");
		// unField.setHorizontalAlignment(JTextField.CENTER);
		unField.setVisible(false);

		pwField = new JPasswordField();
		// mask formatter!
		pwField.setBounds(width / 12, 55 * height / 200, width / 6, ch);
		pwField.addActionListener(actionListener);
		pwField.setName("pwField");
		// pwField.setHorizontalAlignment(JTextField.RIGHT);
		pwField.setVisible(false);

		lp.add(unLabel);
		lp.add(pwLabel);
		lp.add(unField);
		lp.add(pwField);
	}

	/**
	 * Creates all the components to the register interface
	 * 
	 * @param lp
	 *            LayeredPane
	 */

	private void createRegisterInterfaceComponents(JLayeredPane lp) {
		// Tenkte å lage en for løkke og gjøre alt litt mindre, men før jeg
		// finner en BRA måte å gjøre det på, så blir det sånn som det er nå.
		int cw = width / 9 /* 4 * width / 30 */;
		int ch = 3 * height / 40;
		int hReg = 15 * height / 200;

		regNameLabel = new JLabel("Name:");
		regNameLabel.setForeground(Color.WHITE);
		regNameLabel.setBounds(width / 60, 40 * height / 200 - hReg, cw, ch);
		regNameLabel.setVisible(false);

		regLNameLabel = new JLabel("Last name: ");
		regLNameLabel.setForeground(Color.WHITE);
		regLNameLabel.setBounds(width / 60, 55 * height / 200 - hReg, cw, ch);
		regLNameLabel.setVisible(false);

		regEmailLabel = new JLabel("Email:");
		regEmailLabel.setForeground(Color.WHITE);
		regEmailLabel.setBounds(width / 60, 70 * height / 200 - hReg, cw, ch);
		regEmailLabel.setVisible(false);

		regPhoneLabel = new JLabel("Phone:");
		regPhoneLabel.setForeground(Color.WHITE);
		regPhoneLabel.setBounds(width / 60, 85 * height / 200 - hReg, cw, ch);
		regPhoneLabel.setVisible(false);

		regPwLabel = new JLabel("Password:");
		regPwLabel.setForeground(Color.WHITE);
		regPwLabel.setBounds(width / 60, 100 * height / 200 - hReg, cw, ch);
		regPwLabel.setVisible(false);

		regPwLabel2 = new JLabel("Password:");
		regPwLabel2.setForeground(Color.WHITE);
		regPwLabel2.setBounds(width / 60, 115 * height / 200 - hReg, cw, ch);
		regPwLabel2.setVisible(false);

		regNameField = new JTextField();
		regNameField.setBounds(width / 12, 40 * height / 200 - hReg,
				3 * cw / 2, ch);
		regNameField.addActionListener(actionListener);
		regNameField.addFocusListener(focusListener);
		regNameField.setName("regNameField");
		regNameField.setVisible(false);

		regLNameField = new JTextField();
		regLNameField.setBounds(width / 12, 55 * height / 200 - hReg,
				3 * cw / 2, ch);
		regLNameField.addActionListener(actionListener);
		regLNameField.addFocusListener(focusListener);
		regLNameField.setName("regLNameField");
		regLNameField.setVisible(false);

		regEmailField = new JTextField();
		regEmailField.setBounds(width / 12, 70 * height / 200 - hReg,
				3 * cw / 2, ch);
		regEmailField.addActionListener(actionListener);
		regEmailField.addFocusListener(focusListener);
		regEmailField.setName("regEmailField");
		regEmailField.setVisible(false);

		regPhoneField = new JTextField();
		regPhoneField.setBounds(width / 12, 85 * height / 200 - hReg,
				3 * cw / 2, ch);
		regPhoneField.addActionListener(actionListener);
		regPhoneField.addFocusListener(focusListener);
		regPhoneField.setName("regPhoneField");
		regPhoneField.setVisible(false);

		// JPasswordField isteden? ******
		regPwField = new JPasswordField();
		regPwField.setBounds(width / 12, 100 * height / 200 - hReg, 3 * cw / 2,
				ch);
		regPwField.addActionListener(actionListener);
		regPwField.addFocusListener(focusListener);
		regPwField.setName("regPwField");
		regPwField.setVisible(false);

		regPwField2 = new JPasswordField();
		regPwField2.setBounds(width / 12, 115 * height / 200 - hReg,
				3 * cw / 2, ch);
		regPwField2.setName("regPwField2");
		regPwField2.addFocusListener(focusListener);
		regPwField2.addActionListener(actionListener);
		regPwField2.setVisible(false);

		lp.add(regNameLabel);
		lp.add(regLNameLabel);
		lp.add(regEmailLabel);
		lp.add(regPhoneLabel);
		lp.add(regPwLabel);
		lp.add(regPwLabel2);
		lp.add(regNameField);
		lp.add(regLNameField);
		lp.add(regEmailField);
		lp.add(regPhoneField);
		lp.add(regPwField);
		lp.add(regPwField2);
	}

	/**
	 * Creates all the components to the forgot interface
	 * 
	 * @param lp
	 *            LayeredPane
	 */

	private void createForgotInterfaceComponents(JLayeredPane lp) {
		int cw = width / 6/* 4 * width / 30 */;
		int ch = 3 * height / 40;

		emailLabel = new JLabel("Email: ");
		emailLabel.setForeground(Color.WHITE);
		emailLabel.setBounds(width / 60, 6 * height / 20, cw, ch);
		emailLabel.setVisible(false);

		emailField = new JTextField();
		emailField.setBounds(width / 12, 6 * height / 20, cw, ch);
		emailField.addActionListener(actionListener);
		emailField.setName("emailField");
		emailField.setVisible(false);

		lp.add(emailLabel);
		lp.add(emailField);
	}

	private void setFocusListener() {
		for (final Component c : fieldComps) {
			c.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					((JTextComponent) c).selectAll();
				}

				@Override
				public void focusLost(FocusEvent arg0) {

				}
			});
		}
	}

	// sette sammen en liste her for å slippe create***InterfaceComponents
	// metodene. tror ikke det var en god ide.
	private void cListCreate() {
		int cw = width / 6;
		int ch = height / 10;
		int hReg = 15 * height / 200;
		cList = new ArrayList<CompLoc>();
		cList.clear();

		cList.add(new CompLoc(regNameLabel, width / 60, 40 * height / 200
				- hReg, cw, ch, "regNameLabel", null, REG, -1));
		cList.add(new CompLoc(regEmailLabel, width / 60, 55 * height / 200
				- hReg, cw, ch, "regEmailLabel", null, REG, -1));
		cList.add(new CompLoc(regPhoneLabel, width / 60, 70 * height / 200
				- hReg, cw, ch, "regPhoneLabel", null, REG, -1));
		cList.add(new CompLoc(regPwLabel, width / 60, 85 * height / 200 - hReg,
				cw, ch, "regPwLabel", null, REG, -1));
		cList.add(new CompLoc(regPwLabel2, width / 60, 100 * height / 200
				- hReg, cw, ch, "regPwLabel2", null, REG, -1));
		cList.add(new CompLoc(regNameField, width / 12, 40 * height / 200
				- hReg, 3 * cw / 2, ch, "regNameField", "regNameField", REG, -1));
		cList.add(new CompLoc(regEmailField, width / 12, 55 * height / 200
				- hReg, 3 * cw / 2, ch, "regEmailField", null, REG, -1));
		cList.add(new CompLoc(regPhoneField, width / 12, 70 * height / 200
				- hReg, 3 * cw / 2, ch, "regPhoneField", null, REG, -1));
		cList.add(new CompLoc(regPwField, width / 12, 85 * height / 200 - hReg,
				3 * cw / 2, ch, "regPwField", null, REG, -1));
		cList.add(new CompLoc(regPwField2, width / 12, 100 * height / 200
				- hReg, 3 * cw / 2, ch, "regPwField2", null, REG, -1));
		cList.add(new CompLoc(unLabel, width / 60, 40 * height / 200, cw, ch,
				"unLabel", null, LOGIN, -1));
		cList.add(new CompLoc(pwLabel, width / 60, 55 * height / 200, cw, ch,
				"pwLabel", null, LOGIN, -1));
		cList.add(new CompLoc(unField, width / 12, 40 * height / 200,
				width / 6, ch, "unField", "unField", LOGIN, -1));
		cList.add(new CompLoc(pwField, width / 12, 55 * height / 200,
				width / 6, ch, "pwField", "pwField", LOGIN, -1));
		cList.add(new CompLoc(emailField, width / 12, 90 * height / 200, cw,
				ch, "emailField", "emailField", FORGOT, -1));
		cList.add(new CompLoc(emailLabel, width / 12, 90 * height / 200, cw,
				ch, "Email: ", null, FORGOT, -1));
		cList.add(new CompLoc(loginButton, width / 12, 60 * height / 200,
				width / 6, width / 10, "Login", "login", START, LOGIN));
		cList.add(new CompLoc(registerButton, width / 12, 90 * height / 200,
				width / 6, height / 10, "Register", "register", LOGIN, REG));
		cList.add(new CompLoc(sendButton, width / 12, 6 * height / 20,
				width / 6, 3 * height / 40, "Send", "send", FORGOT, -1));
		cList.add(new CompLoc(forgotButton, width / 12, 125 * height / 200,
				width / 6, 3 * height / 40, "Forgot", "forgot", LOGIN, -1));
	}

	// tror ikke det var en god ide.
	private void createComponents(ArrayList<CompLoc> list, JLayeredPane lp,
			Listener l) {
		for (CompLoc c : list) {
			c.getComponent().setBounds(c.getX(), c.getY(), c.getWidth(),
					c.getHeight());
			if (c.getComponent() instanceof JButton) {
				((JButton) c.getComponent()).addActionListener(l);
			} else if (c.getComponent() instanceof JTextField) {
				((JTextField) c.getComponent()).addActionListener(l);
			} else if (c.getComponent() instanceof JLabel) {
			}
			c.getComponent().setVisible(false);
			c.getComponent().setName(c.getName());
			lp.add(c.getComponent());
		}
	}

	// setter sammen listene med de ulike kompoentene for å da gå igjennom
	// løkker for å sette de riktige komponentene synlige og usynlige.
	/**
	 * Sets together the different lists of components that are showing in the
	 */
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
		registerComps.add(regLNameLabel);
		registerComps.add(regEmailLabel);
		registerComps.add(regPhoneLabel);
		registerComps.add(regPwLabel);
		registerComps.add(regPwLabel2);
		registerComps.add(regNameField);
		registerComps.add(regLNameField);
		registerComps.add(regEmailField);
		registerComps.add(regPhoneField);
		registerComps.add(regPwField);
		registerComps.add(regPwField2);
		registerComps.add(registerButton);
		registerComps.add(backButton);
		registerComps.add(exitButton);

		fieldComps = new ArrayList<Component>();
		fieldComps.clear();
		fieldComps.add(regNameField);
		fieldComps.add(regLNameField);
		fieldComps.add(regEmailField);
		fieldComps.add(regPhoneField);
		fieldComps.add(regPwField);
		fieldComps.add(regPwField2);
		fieldComps.add(pwField);
		fieldComps.add(unField);
		fieldComps.add(emailField);
	}

	// Lage en metode som plasserer de ulie komponentene fint auomatisk. Uten at
	// en trenger å sette bounds på alle.
	private void placeComponentsNeat(ArrayList<Component> list) {
		int cw = width / 6; // bredden på hver boks
		int ch = 3 * height / 40; // høyden på hver boks
		int rh = height * 8 / 10; // nederste 20% til back og exit button

		// liste over labels
		ArrayList<Component> labelList = new ArrayList<Component>();
		// liste over fields
		ArrayList<Component> fieldList = new ArrayList<Component>();
		// liste over knapper
		ArrayList<Component> buttonList = new ArrayList<Component>();

		// plasserer alle komponenter som skal plasseres i ulike lister
		for (Component c : list) {
			if (c instanceof JLabel) {
				labelList.add(c);
			} else if (c instanceof JTextField || c instanceof JPasswordField) {
				fieldList.add(c);
			} else if (c instanceof JButton) {
				buttonList.add(c);
			}
		}
		int ih = 1; // høyden for alle labels og feilds

		for (Component c : buttonList) {

		}

	}

	/**
	 * Method for setting all the start components visible or not.
	 * 
	 * @param bool
	 *            true for visible
	 */
	private void changeToStartInterface(Boolean bool) {
		loginButton.setBounds(width / 12, 6 * height / 20, width / 6,
				height / 10);
		registerButton.setBounds(width / 12, 9 * height / 20, width / 6,
				height / 10);

		for (Component c : startComps) {
			c.setVisible(bool);
		}
		repaintPanel();
		state = 0;
	}

	/**
	 * Method for setting all the login components visible or not.
	 * 
	 * @param bool
	 *            true for visible
	 */
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
		repaintPanel();
		state = 1;
	}

	/**
	 * Method for setting all the register components visible or not.
	 * 
	 * @param bool
	 *            true for visible
	 */
	private void changeToRegisterInterface(boolean bool) {
		registerButton.setBounds(width / 12, 120 * height / 200, width / 6,
				3 * height / 40);

		for (Component c : registerComps) {
			c.setVisible(bool);
		}
		repaintPanel();
		state = 2;
	}

	/**
	 * Method for setting all the forgot components visible or not.
	 * 
	 * @param bool
	 *            true for visible
	 */
	private void changeToForgotInterface(boolean bool) {
		sendButton.setBounds(width / 12, 9 * height / 20, width / 6,
				3 * height / 40);
		for (Component c : forgotComps) {
			c.setVisible(bool);
		}
		repaintPanel();
		state = 3;
	}

	/**
	 * Method that runs when the user requests to finish his registration and
	 * save a new user.
	 */
	// Henter informasjon fra tekst boksene og skriver dem ut.
	private boolean registerUser() {
		String firstName = regNameField.getText();
		String lastName = regLNameField.getText();
		String email = regEmailField.getText();
		String phoneNr = regPhoneField.getText();
		char[] pw = regPwField.getPassword();
		char[] pw2 = regPwField2.getPassword();

		String password = "";
		for (int i = 0; i < pw.length; i++) {
			password += pw[i];
		}
		String password2 = "";
		for (int i = 0; i < pw2.length; i++) {
			password2 += pw2[i];
		}

		if (password.equals(password2)) {
			int num = Server.sendInformationRegisterUser(firstName, lastName,
					email, password2, phoneNr);
			if (num == 1) { // fikk registrert bruker
				unField.setText(email);
				regNameField.setText("");
				regNameField.setBackground(Color.WHITE);
				regLNameField.setText("");
				regLNameField.setBackground(Color.WHITE);
				regEmailField.setText("");
				regEmailField.setBackground(Color.WHITE);
				regPhoneField.setText("");
				regPhoneField.setBackground(Color.WHITE);
				regPwField.setText("");
				regPwField.setBackground(Color.WHITE);
				regPwField2.setText("");
				regPwField2.setBackground(Color.WHITE);
				return true;
			} else if (num == -1) {
				regNameField.setBackground(invalid);
			} else if (num == -2) {
				regLNameField.setBackground(invalid);
			} else if (num == -3) {
				regEmailField.setBackground(invalid);
			} else if (num == -4) {
				regPwField.setBackground(invalid);
				regPwField2.setBackground(invalid);
			}
		} else {
			regPwField.setBackground(invalid);
			regPwField2.setBackground(invalid);
		}
		return false;
	}

	/**
	 * Method that runs when the user requests to login after entering email and
	 * password in the fields.
	 */
	private boolean login() {
		String email = unField.getText();
		String password = "";
		char[] c = pwField.getPassword();
		for (int i = 0; i < c.length; i++) {
			password += c[i];
		}

		User user = Server.sendInformationLogin(email, password);

		if (user != null) {
			// DO STUFF
			System.out.println("Logged in: " + user.getEmail());
			return true;
		}
		return false;

	}

	/**
	 * Method that runs when the user requests to get a new password sent to his
	 * email registered to his user.
	 */
	private boolean sendEmail() {
		boolean sendmail = true;
		if (sendmail) { // you know
			if (emailField.getText() != null) {
				return Server.sendInformationSendEmail(emailField.getText());
			}
		}
		return false;
	}

	private void createFocusListener() {
		focusListener = new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				Component comp = e.getComponent();
				if (comp instanceof JTextField) {
					((JTextField) comp).selectAll();
					comp.setBackground(Color.WHITE);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				Component component = e.getComponent();

				if (component instanceof JTextField) {
					JTextField fc = (JTextField) component;
					String name = fc.getName();
					String input = null;
					if (name.equals("regNameField")) {
						try {
							input = regNameField.getText();
							tUser.setFirstName(input);
							regNameField.setBackground(valid);
						} catch (Exception exc) {
							regNameField.setBackground(invalid);
						}
					} else if (name.equals("regLNameField")) {
						try {
							input = regLNameField.getText();
							tUser.setLastName(input);
							regLNameField.setBackground(valid);
						} catch (Exception exc) {
							regLNameField.setBackground(invalid);
						}
					} else if (name.equals("regEmailField")) {
						try {
							input = regEmailField.getText();
							tUser.setEmail(input);
							regEmailField.setBackground(valid);
						} catch (Exception exc) {
							regEmailField.setBackground(invalid);
						}
					} else if (name.equals("regPhoneField")) {
						try {
							input = regPhoneField.getText();
							System.out.println(input);
							tUser.setPhoneNr(input);
							regPhoneField.setBackground(valid);
						} catch (Exception exc) {
							regPhoneField.setBackground(invalid);
						}
					} else if (name.equals("regPwField")) {
						try {
							char[] list = regPwField.getPassword();
							String password1 = "";
							for (int i = 0; i < list.length; i++) {
								password1 += list[i];
							}
							tUser.setPassword(password1);
							regPwField.setBackground(valid);
						} catch (Exception exc) {
							regPwField.setBackground(invalid);
						}
					} else if (name.equals("regPwField2")) {
						try {
							char[] list = regPwField.getPassword();
							String password1 = "";
							for (int i = 0; i < list.length; i++) {
								password1 += list[i];
							}

							char[] list2 = regPwField2.getPassword();
							String password2 = "";
							for (int i = 0; i < list2.length; i++) {
								password2 += list[i];
							}
							if (password1.equals(password2)) {
								tUser.setPassword(password2);
								regPwField2.setBackground(valid);
							} else {
								// LEGGE TIL EN PASSORD MATCHER IKKE LABEL
								// ELLERNO
								System.out.println("Password does not match!");
								return;
							}
						} catch (Exception exc) {
							regPwField2.setBackground(invalid);
						}
					}
				}
			}
		};
	}

	/**
	 * A class that handles the button and text field listening.
	 * 
	 * @author andreas
	 * 
	 */
	private class Listener implements ActionListener {
		// bruke setActionCommand på alt som skal lyttes på og bruke det
		// istedenfor navn?
		@Override
		public void actionPerformed(ActionEvent arg) {
			// rightPanel.setBackground(randomColor());
			if (arg.getSource() instanceof JButton) {
				JButton pressed = (JButton) arg.getSource();
				String text = pressed.getText();

				if (text.equals("Register")) {
					if (state == 2) {
						if (registerUser()) {
							changeToRegisterInterface(false);
							changeToLoginInterface(true);
						} else {
							// istdenfor boolean fra registerUser() få tall for
							// å finne ut om hvilke verdier som er feil
							// Noe lignende med login!
						}
					} else {
						changeToLoginInterface(false);
						changeToRegisterInterface(true);
					}
				} else if (text.equals("Login")) {
					// sjekker: hvis man er i login interface og trykker login
					// ->
					if (state == 1) {
						// metode for å sjekke om brukernavn og passord stemmer
						login();
					}
					changeToLoginInterface(true);
				} else if (text.equals("          Exit")) {
					System.exit(0);
				} else if (text.equals("          Back")) {
					// state holder styr på hvor man er, så programet vet hvor
					// en
					// skal sendes tilbake til
					if (state == 1) {
						changeToLoginInterface(false);
						changeToStartInterface(true);
					} else if (state == 2) {
						changeToRegisterInterface(false);
						changeToStartInterface(true);
					} else if (state == 3) {
						changeToForgotInterface(false);
						changeToLoginInterface(true);
					}
				} else if (text.equals("Forgot")) {
					changeToLoginInterface(false);
					changeToForgotInterface(true);
				} else if (text.equals("Send")) {
					// sjekker: er i forgot interface og trykker send ->
					if (state == 3) {
						// SEND EMAIL!
						if (sendEmail()) {
							changeToForgotInterface(false);
							changeToLoginInterface(true);
						} else {
							// fikk ikke sendt mail av en eller annen grunn
						}
					}
				}
			} else if (arg.getSource() instanceof JTextField) {
				// System.out.println(((JTextField) arg.getSource()).getName());
				JTextField pressed = (JTextField) arg.getSource();
				String text = pressed.getName();
				// Register user
				System.out.println(text);
				if (state == 1) {
					if (text.equals("unField")) {
						pwField.requestFocus();
					} else if (text.equals("pwField")) {
						// LOG IN!
						login();
					}
				} else if (state == 2) {
					if (text.equals("regNameField")) {
						regLNameField.requestFocus();
					} else if (text.equals("regLNameField")) {
						regEmailField.requestFocus();
					} else if (text.equals("regEmailField")) {
						regPhoneField.requestFocus();
					} else if (text.equals("regPhoneField")) {
						regPwField.requestFocus();
					} else if (text.equals("regPwField")) {
						regPwField2.requestFocus();
					} else if (text.equals("regPwField2")) {
						// REGISTER USER!
						if (registerUser()) {
							changeToRegisterInterface(false);
							changeToLoginInterface(true);
						}
					}
				} else if (state == 3) {
					System.out.println("fuck");
					if (text.equals("emailField")) {
						// SEND EMAIL!
						if (sendEmail()) {
							changeToForgotInterface(false);
							changeToLoginInterface(true);
						} else {
							// fikk ikke sendt mail av en eller annen grunn
						}
					}
				}
			}
		}
	}

	// Lytter for å håndtere tid. Til komponenter som skal flyttes over tid.
	// Animasjoner!
	/**
	 * A class that handles time. Can be used for animations.
	 * 
	 * @author andreas
	 * 
	 */
	private class TimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			rightPanel.setBackground(randomColor());
		}
	}

	// Variables for swing
	private JButton loginButton;
	private JButton registerButton;
	private JButton forgotButton;
	private JButton backButton;
	private JButton exitButton;
	private JButton sendButton;

	private MyImagePanel rightPanel;
	private MyImagePanel leftPanel;

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
	private JLabel regLNameLabel;
	private JLabel regEmailLabel;
	private JLabel regPhoneLabel;
	private JLabel regPwLabel;
	private JLabel regPwLabel2;
	private JTextField regNameField;
	private JTextField regLNameField;
	private JTextField regEmailField;
	private JTextField regPhoneField;
	private JPasswordField regPwField;
	private JPasswordField regPwField2;

	private ArrayList<Component> startComps;
	private ArrayList<Component> loginComps;
	private ArrayList<Component> registerComps;
	private ArrayList<Component> forgotComps;

	private ArrayList<CompLoc> cList;
	private ArrayList<Component> fieldComps;
}
