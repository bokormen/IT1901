package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

//import com.sun.deploy.util.SessionState;
import components.MyBorder;
import components.MyButton;
import components.MyImagePanel;
import components.MyMap;
import components.MyPasswordField;
import components.MyTextField;

import div.ClientConnection;
import div.Sheep;
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

        if (ClientConnection.ConnectedToServer) {
            try {
                ClientConnection.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

	// plasserer alt bedre! login specially

	private int width;
	private int height;

	private User user; // gitt bruker etter en har logget p�
	private User tUser = new User(); // test bruker

	public static int START = 0, LOGIN = 1, REG = 2, FORGOT = 3, MAIN = 4,
			SEARCH = 5, EDIT = 6, LIST = 7, SHEEPREG = 8;
	private int state; // hvor i programmet en er

	// Font
	public final static String fontType = "Tahoma";
	public final static Font FONT = new Font(fontType, Font.BOLD, 12);

	public final static Color valid = Color.green;
	public final static Color invalid = Color.red;

	private MyListener actionListener;
	private FocusListener focusListener;
	private TimerListener tListener;
	private Timer timer;

	private GoogleStaticMap googleStaticMap;
	private KartverketStaticMap kartverketStaticMap;

    //serverip
    private InetAddress serverIP;


	public GUI() {
		super("Tittel");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		width = 1200;
		height = 600;
		setBounds(0, 0, width, height + 20); // +20 for tittel bar'en
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBackground(Color.BLUE);

		actionListener = new MyListener();
		createFocusListener();
		tListener = new TimerListener();
		timer = new Timer(100, tListener);
		// timer.start();

		googleStaticMap = new GoogleStaticMap();
		kartverketStaticMap = new KartverketStaticMap();

        //aapne tilkoppling til server
        try {
            serverIP = InetAddress.getLocalHost(); //default satt til localhost
            ClientConnection.open(serverIP);
        } catch (UnknownHostException e) {
            System.err.println("Could not find address: " + serverIP.toString());
        }

		Server.addUsers(); // Legger til brukere til testing

		JLayeredPane lp = getLayeredPane();

		createComponentArrays(); // brukes i createComponents

		createButtons(lp);
		createLoginInterfaceComponents(lp);
		createRegisterInterfaceComponents(lp);
		createForgotInterfaceComponents(lp);

		createMainInterfaceComponents(lp);
		createSearchInterfaceComponents(lp);
		createEditInterfaceComponents(lp);
		createListInterfaceComponents(lp);
		createRegSheepInterfaceComponents(lp);

		createPanels(lp);

		createComponentArrays(); // igjen for � legge til objektene
		// setFocusListener(); // legger til focusListener til alle text
		// feltene.

		// cListCreate();

		// Setter alle start komponentene synlige
		changeToStartInterface(true);

		repaintPanel();
	}

	public ArrayList<JComponent> getComponentList(int arg) {
		if (arg == LOGIN) {
			return loginComps;
		} else if (arg == REG) {
			return registerComps;
		} else if (arg == START) {
			return startComps;
		} else if (arg == FORGOT) {
			return forgotComps;
		} else {
			return null;
		}
	}

	public void repaintPanel() {
		repaint();
		validate();
	}

	private BufferedImage getGoogleImage(double latitude, double longitude) {
		BufferedImage img = (BufferedImage) (googleStaticMap.getImage(latitude,
				longitude, 4, 2 * width / 3, height, 1));
		return img;
	}

	private void updateRightPanelMap(double latitude, double longitude) {
		rightPanel.changeImage(getGoogleImage(latitude, longitude), 0);
		rightPanel.repaint();
	}

	/**
	 * Lager to paneler fordelt p�: leftPanel (width/3) og rightPanel
	 * (2*width/3)
	 * 
	 * @param lp
	 *            LayeredPane
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

		BufferedImage img2 = null;
		try {
			img2 = ImageIO.read(this.getClass().getClassLoader()
					.getResource("images/sheepbackground.jpeg"));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		BufferedImage[] imgs2 = { img2 };
		int[] xs2 = { 0 };
		int[] ys2 = { 0 };

		rightPanel = new MyImagePanel(imgs2, xs2, ys2);
		rightPanel.repaint();
		rightPanel.setBounds(width / 3, 0, 2 * width / 3, height);

		BufferedImage img1 = null;
		try {
			img1 = ImageIO.read(this.getClass().getClassLoader()
					.getResource("images/blackcarbon.jpg"));
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
		lp.add(rightPanel);

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

		ImageIcon img = null;
		try {
			img = new ImageIcon(ImageIO.read(this.getClass().getClassLoader()
					.getResource("images/buttonimage.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		loginButton = new MyButton(new JButton(), "Login", "bluesheepicon2");
		loginButton.setBounds(width / 12, 6 * height / 20, bw, bh);
		loginButton.addActionListener(actionListener);

		registerButton = new MyButton(new JButton(), "Register", "blueformicon");
		registerButton.setBounds(width / 12, 9 * height / 20, bw, bh);
		registerButton.addActionListener(actionListener);

		forgotButton = new MyButton(new JButton(), "Forgot", "bluequestionicon");
		forgotButton.setBounds(width / 12, 125 * height / 200, bw, 3 * bh / 4);
		forgotButton.addActionListener(actionListener);

		sendButton = new MyButton(new JButton(), "Send", null);
		sendButton.setBounds(width / 12, 9 * height / 20, bw, 3 * bh / 4);
		sendButton.addActionListener(actionListener);

		backButton = new JButton("          Back", new ImageIcon(this
				.getClass().getClassLoader().getResource("images/sheep.png")));
		backButton.setBounds(11 * width / 48, 19 * height / 25, bw / 2, bw / 2);
		backButton.addActionListener(actionListener);
		backButton.setBorder(BorderFactory.createEmptyBorder());
		backButton.setHorizontalTextPosition(SwingConstants.CENTER);
		backButton.setVisible(false);
        backButton.setContentAreaFilled(false);

		exitButton = new JButton("          Exit", new ImageIcon(this
				.getClass().getClassLoader().getResource("images/sheep.png")));
		exitButton.setBounds(width / 48, 19 * height / 25, bw / 2, bw / 2);
		exitButton.addActionListener(actionListener);
		exitButton.setBorder(BorderFactory.createEmptyBorder());
		exitButton.setHorizontalTextPosition(SwingConstants.CENTER);
		exitButton.setVisible(false);
        exitButton.setContentAreaFilled(false);

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

		unField = new MyTextField(new JTextField(), "blueusericon", "Email");
		unField.setBounds(width / 12, 35 * height / 200, width / 6, ch);
		unField.addActionListener(actionListener);
		unField.setName("unField");

		pwField = new MyPasswordField(new JPasswordField(), "bluekeyicon",
				"Password");
		// mask formatter!
		pwField.setBounds(width / 12, 55 * height / 200, width / 6, ch);
		pwField.addActionListener(actionListener);
		pwField.setName("pwField");

		// lp.add(unLabel);
		// lp.add(pwLabel);
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
		int cw = width / 9 /* 4 * width / 30 */;
		int ch = 3 * height / 40;
		int hReg = 15 * height / 200;

		regNameField = new MyTextField(new JTextField(), "bluesheepicon",
				"Firstname");
		regNameField.setBounds(width / 12, 20 * height / 200, 3 * cw / 2, ch);
		regNameField.addActionListener(actionListener);
		regNameField.addFocusListener(focusListener);
		regNameField.setName("regNameField");

		regLNameField = new MyTextField(new JTextField(), "bluesheepicon",
				"Lastname");
		regLNameField.setBounds(width / 12, 35 * height / 200, 3 * cw / 2, ch);
		regLNameField.addActionListener(actionListener);
		regLNameField.addFocusListener(focusListener);
		regLNameField.setName("regLNameField");

		regEmailField = new MyTextField(new JTextField(), "bluemailicon",
				"Email");
		regEmailField.setBounds(width / 12, 50 * height / 200, 3 * cw / 2, ch);
		regEmailField.addActionListener(actionListener);
		regEmailField.addFocusListener(focusListener);
		regEmailField.setName("regEmailField");

		regPhoneField = new MyTextField(new JTextField(), "bluephoneicon",
				"Phone");
		regPhoneField.setBounds(width / 12, 65 * height / 200, 3 * cw / 2, ch);
		regPhoneField.addActionListener(actionListener);
		regPhoneField.addFocusListener(focusListener);
		regPhoneField.setName("regPhoneField");

		regLongitudeField = new MyTextField(new JTextField(), null, "Longitude");
		regLongitudeField.setBounds(width / 12 + 3 * cw / 4, 80 * height / 200,
				3 * cw / 4, ch);
		regLongitudeField.addActionListener(actionListener);
		regLongitudeField.addFocusListener(focusListener);
		regLongitudeField.setName("regLongitudeField");

		regLatitudeField = new MyTextField(new JTextField(), null, "Latitude");
		regLatitudeField.setBounds(width / 12, 80 * height / 200, 3 * cw / 4,
				ch);
		regLatitudeField.addActionListener(actionListener);
		regLatitudeField.addFocusListener(focusListener);
		regLatitudeField.setName("regLatitudeField");

		regPwField = new MyPasswordField(new JPasswordField(), "bluekeyicon",
				"Password");
		regPwField.setBounds(width / 12, 95 * height / 200, 3 * cw / 2, ch);
		regPwField.addActionListener(actionListener);
		regPwField.addFocusListener(focusListener);
		regPwField.setName("regPwField");

		regPwField2 = new MyPasswordField(new JPasswordField(), "bluekeyicon",
				"Password");
		regPwField2.setBounds(width / 12, 110 * height / 200, 3 * cw / 2, ch);
		regPwField2.setName("regPwField2");
		regPwField2.addFocusListener(focusListener);
		regPwField2.addActionListener(actionListener);

		lp.add(regNameField);
		lp.add(regLNameField);
		lp.add(regEmailField);
		lp.add(regPhoneField);
		lp.add(regLongitudeField);
		lp.add(regLatitudeField);
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

		emailField = new MyTextField(new JTextField(), "bluemailicon", "Email");
		emailField.setBounds(width / 12, 6 * height / 20, cw, ch);
		emailField.addActionListener(actionListener);
		emailField.setName("emailField");

		// lp.add(emailLabel);
		lp.add(emailField);
	}

	private void createMainInterfaceComponents(JLayeredPane lp) {
		// TODO
		int cw = width / 6;
		int ch = 3 * height / 40;

		sheepRegButton = new MyButton(new JButton(), "Register sheep", null);
		sheepRegButton.setBounds(width / 12, 30 * height / 200, cw, ch);
		sheepRegButton.addActionListener(actionListener);

		listButton = new MyButton(new JButton(), "List over sheeps", null);
		listButton.setBounds(width / 12, 50 * height / 200, cw, ch);
		listButton.addActionListener(actionListener);

		searchButton = new MyButton(new JButton(), "Search for sheep", null);
		searchButton.setBounds(width / 12, 70 * height / 200, cw, ch);
		searchButton.addActionListener(actionListener);

		editButton = new MyButton(new JButton(), "Edit sheep", null);
		editButton.setBounds(width / 12, 90 * height / 200, cw, ch);
		editButton.addActionListener(actionListener);

		homeButton = new MyButton(new JButton(), "Home", null);
		homeButton.setBounds(width / 12, 110 * height / 200, cw, ch);
		homeButton.addActionListener(actionListener);

		myMap = new MyMap(width, height);

		lp.add(myMap);
		lp.add(sheepRegButton);
		lp.add(listButton);
		lp.add(searchButton);
		lp.add(editButton);
		lp.add(homeButton);

	}

	private void createSearchInterfaceComponents(JLayeredPane lp) {
		// TODO
		int cw = width / 6;
		int ch = 3 * height / 40;

		searchLabel = new JLabel("Write the ID:");
		searchLabel.setBounds(width / 12, 60 * height / 200, cw, ch);
		searchLabel.setVisible(false);

		searchField = new MyTextField(new JTextField(), "bluesheepicon", "ID");
		searchField.setBounds(width / 12, 90 * height / 200, cw, ch);

		// searchButton = new MyButton(new JButton(), "Search");
		// searchButton.setBounds(width / 12, 80 * height / 200, cw, ch);

		lp.add(searchField);
		// lp.add(searchButton);
	}

	private void createEditInterfaceComponents(JLayeredPane lp) {
		// TODO
		int cw = width / 6;
		int ch = 3 * height / 40;

		editIdField = new MyTextField(new JTextField(), "bluesheepicon", "");
		editIdField.setBounds(width / 12, 30 * height / 200, cw, ch);

		editAgeField = new MyTextField(new JTextField(), "bluesheepicon", "");
		editAgeField.setBounds(width / 12, 50 * height / 200, cw, ch);

		editWeightField = new MyTextField(new JTextField(), "bluesheepicon", "");
		editWeightField.setBounds(width / 12, 70 * height / 200, cw, ch);

		editSexField = new MyTextField(new JTextField(), "bluesheepicon", "");
		editSexField.setBounds(width / 12, 90 * height / 200, cw, ch);

		editShepardField = new MyTextField(new JTextField(), "bluesheepicon",
				"");
		editShepardField.setBounds(width / 12, 110 * height / 200, cw, ch);

		clearAllButton = new MyButton(new JButton(), "Clear All", null);
		clearAllButton.setBounds(width / 12, 140 * height / 200, cw, ch);
		clearAllButton.addActionListener(actionListener);

		updateButton = new MyButton(new JButton(), "Update", null);
		updateButton.setBounds(width / 12, 160 * height / 200, cw, ch);
		updateButton.addActionListener(actionListener);

		lp.add(editIdField);
		lp.add(editAgeField);
		lp.add(editWeightField);
		lp.add(editSexField);
		lp.add(editShepardField);
		lp.add(clearAllButton);
		lp.add(updateButton);
	}

	private void createListInterfaceComponents(JLayeredPane lp) {
		// TODO
		int cw = width / 6;
		int ch = 3 * height / 40;

		sheepy = new MyButton(new JButton(), "DONT CLICK", "bluesheepicon");
		sheepy.setBounds(width / 12, 90 * height / 200, cw, ch);

		lp.add(sheepy);
	}

	private void createRegSheepInterfaceComponents(JLayeredPane lp) {
		// TODO
		int cw = width / 6;
		int ch = 3 * height / 40;
		regIDField = new MyTextField(new JTextField(), "bluesheepicon", "ID");
		regIDField.setBounds(width / 12, 30 * height / 200, cw, ch);

		regAgeField = new MyTextField(new JTextField(), "bluesheepicon", "Age");
		regAgeField.setBounds(width / 12, 50 * height / 200, cw, ch);

		regWeightField = new MyTextField(new JTextField(), "bluesheepicon",
				"Weight");
		regWeightField.setBounds(width / 12, 70 * height / 200, cw, ch);

		regSexField = new MyTextField(new JTextField(), "bluesheepicon", "Sex");
		regSexField.setBounds(width / 12, 90 * height / 200, cw, ch);

		regShepardField = new MyTextField(new JTextField(), "bluesheepicon",
				"Shepard");
		regShepardField.setBounds(width / 12, 110 * height / 200, cw, ch);

		lp.add(regIDField);
		lp.add(regAgeField);
		lp.add(regWeightField);
		lp.add(regSexField);
		lp.add(regShepardField);
	}

	// setter sammen listene med de ulike kompoentene for � da g� igjennom
	// l�kker for � sette de riktige komponentene synlige og usynlige.
	/**
	 * Sets together the different lists of components that are showing in the
	 */
	private void createComponentArrays() {

		startComps = new ArrayList<JComponent>();
		startComps.clear();
		startComps.add(loginButton);
		startComps.add(registerButton);
		startComps.add(exitButton);

		loginComps = new ArrayList<JComponent>();
		loginComps.add(pwField);
		loginComps.add(unField);
		loginComps.add(forgotButton);
		loginComps.add(loginButton);
		loginComps.add(backButton);
		loginComps.add(exitButton);

		forgotComps = new ArrayList<JComponent>();
		forgotComps.clear();
		forgotComps.add(emailField);
		forgotComps.add(backButton);
		forgotComps.add(exitButton);
		forgotComps.add(sendButton);

		registerComps = new ArrayList<JComponent>();
		registerComps.add(regNameField);
		registerComps.add(regLNameField);
		registerComps.add(regEmailField);
		registerComps.add(regPhoneField);
		registerComps.add(regLongitudeField);
		registerComps.add(regLatitudeField);
		registerComps.add(regPwField);
		registerComps.add(regPwField2);
		registerComps.add(registerButton);
		registerComps.add(backButton);
		registerComps.add(exitButton);

		mainComps = new ArrayList<JComponent>();
		mainComps.clear();
		mainComps.add(sheepRegButton);
		mainComps.add(listButton);
		mainComps.add(searchButton);
		mainComps.add(editButton);
		mainComps.add(homeButton);
		mainComps.add(backButton);
		mainComps.add(exitButton);

		searchComps = new ArrayList<JComponent>();
		searchComps.clear();
		searchComps.add(searchLabel);
		searchComps.add(searchField);
		searchComps.add(searchButton);
		searchComps.add(backButton);
		searchComps.add(exitButton);

		editComps = new ArrayList<JComponent>();
		editComps.clear();
		editComps.add(editIdField);
		editComps.add(editAgeField);
		editComps.add(editWeightField);
		editComps.add(editSexField);
		editComps.add(editShepardField);
		editComps.add(clearAllButton);
		editComps.add(updateButton);
		editComps.add(backButton);
		editComps.add(exitButton);

		listComps = new ArrayList<JComponent>();
		listComps.clear();
		listComps.add(sheepy);
		listComps.add(backButton);
		listComps.add(exitButton);

		regSheepComps = new ArrayList<JComponent>();
		regSheepComps.clear();
		regSheepComps.add(regIDField);
		regSheepComps.add(regAgeField);
		regSheepComps.add(regWeightField);
		regSheepComps.add(regSexField);
		regSheepComps.add(regShepardField);
		regSheepComps.add(exitButton);
		regSheepComps.add(backButton);

		fieldComps = new ArrayList<JComponent>();
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
	// en trenger � sette bounds p� alle.
	private void placeComponentsNeat(ArrayList<Component> list) {
		int cw = width / 6; // bredden p� hver boks
		int ch = 3 * height / 40; // h�yden p� hver boks
		int rh = height * 8 / 10; // nederste 20% til back og exit button

		ArrayList<Component> labelList = new ArrayList<Component>();
		ArrayList<Component> fieldList = new ArrayList<Component>();
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
		int ih = 1; // h�yden for alle labels og feilds

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
		if (bool) {
			loginButton.setBounds(width / 12, 6 * height / 20, width / 6,
					height / 10);
			registerButton.setBounds(width / 12, 9 * height / 20, width / 6,
					height / 10);
			state = 0;
		}
		for (Component c : startComps) {
			c.setVisible(bool);
		}
		repaintPanel();
	}

	/**
	 * Method for setting all the login components visible or not.
	 * 
	 * @param bool
	 *            true for visible
	 */
	private void changeToLoginInterface(boolean bool) {
		if (bool) {
			loginButton.setBounds(width / 12, 85 * height / 200, width / 6,
					3 * height / 40);
			forgotButton.setBounds(width / 12, 105 * height / 200, width / 6,
					3 * height / 40);
			registerButton.setVisible(!bool);
			state = LOGIN;
		}

		for (Component c : loginComps) {
			c.setVisible(bool);
		}
		repaintPanel();
	}

	/**
	 * Method for setting all the register components visible or not.
	 * 
	 * @param bool
	 *            true for visible
	 */
	private void changeToRegisterInterface(boolean bool) {
		if (bool) {
			registerButton.setBounds(width / 12, 130 * height / 200, width / 6,
					3 * height / 40);
			state = REG;
		}
		for (Component c : registerComps) {
			c.setVisible(bool);
		}
		repaintPanel();
	}

	/**
	 * Method for setting all the forgot components visible or not.
	 * 
	 * @param bool
	 *            true for visible
	 */
	private void changeToForgotInterface(boolean bool) {
		if (bool) {
			sendButton.setBounds(width / 12, 85 * height / 200, width / 6,
					3 * height / 40);
			state = FORGOT;
		}
		for (Component c : forgotComps) {
			c.setVisible(bool);
		}
		repaintPanel();
	}

	private void changeToMainInterface(boolean bool) {
		// TODO
		if (bool) {
			searchButton.setBounds(width / 12, 70 * height / 200, width / 6,
					3 * height / 40);
			state = MAIN;
		}
		for (Component c : mainComps) {
			c.setVisible(bool);
		}
		repaintPanel();
	}

	private void changeToSearchInterface(boolean bool) {
		// TODO
		if (bool) {
			searchButton.setBounds(width / 12, 110 * height / 200, width / 6,
					3 * height / 40);
			state = SEARCH;
		}
		for (Component c : searchComps) {
			c.setVisible(bool);
		}
	}

	private void changeToEditInterface(boolean bool) {
		// TODO
		if (bool) {
			state = EDIT;
		}
		for (Component c : editComps) {
			c.setVisible(bool);
		}
	}

	private void changeToListInterface(boolean bool) {
		// TODO
		if (bool) {
			state = LIST;
		}
		for (Component c : listComps) {
			c.setVisible(bool);
		}
	}

	private void changeToRegSheepInterface(boolean bool) {
		// TODO
		if (bool) {
			sheepRegButton.setBounds(width / 12, 30 * height / 200, width / 6,
					3 * height / 40);
			state = SHEEPREG;
		}
		for (Component c : regSheepComps) {
			c.setVisible(bool);
		}
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
				Color white = Color.white;
				unField.setText(email);
				for (JComponent c : registerComps) {
					if (c instanceof MyTextField
							|| c instanceof MyPasswordField) {
						((MyTextField) c).setText("");
						((MyBorder) c.getBorder()).setColor(white);
					}
				}
				return true;
			} else if (num == -1) {
				((MyBorder) regNameField.getBorder()).setColor(invalid);
			} else if (num == -2) {
				((MyBorder) regNameField.getBorder()).setColor(invalid);
			} else if (num == -3) {
				((MyBorder) regEmailField.getBorder()).setColor(invalid);
			} else if (num == -4) {
				((MyBorder) regPhoneField.getBorder()).setColor(invalid);
			} else if (num == -5) {
				((MyBorder) regPwField.getBorder()).setColor(invalid);
				((MyBorder) regPwField2.getBorder()).setColor(invalid);
			}
		} else {
			((MyBorder) regPwField.getBorder()).setColor(invalid);
			((MyBorder) regPwField2.getBorder()).setColor(invalid);
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
			// TODO
			changeToLoginInterface(false);
			changeToMainInterface(true);
			System.out.println("Logged in: " + user.getEmail());
			this.user = user;
			myMap.setUser(user);

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

	private Sheep editSheep() {
		// TODO
		// finner sauen som
		return null;
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
							System.out.println(input);
							if (!input.equals("")) {
								tUser.setFirstName(input);
								((MyBorder) regNameField.getBorder())
										.changeColor(valid);
							}
						} catch (Exception exc) {
							((MyBorder) regNameField.getBorder())
									.changeColor(invalid);
						}
					} else if (name.equals("regLNameField")) {
						try {
							input = regLNameField.getText();
							if (!input.equals("")) {
								tUser.setLastName(input);
								((MyBorder) regLNameField.getBorder())
										.changeColor(valid);
							}
						} catch (Exception exc) {
							((MyBorder) regLNameField.getBorder())
									.changeColor(invalid);
						}
					} else if (name.equals("regEmailField")) {
						try {
							input = regEmailField.getText();
							if (!input.equals("")) {
								tUser.setEmail(input);
								((MyBorder) regEmailField.getBorder())
										.changeColor(valid);
							}
						} catch (Exception exc) {
							((MyBorder) regEmailField.getBorder())
									.changeColor(invalid);
						}
					} else if (name.equals("regPhoneField")) {
						try {
							input = regPhoneField.getText();
							if (!input.equals("")) {
								tUser.setPhoneNr(input);
								((MyBorder) regPhoneField.getBorder())
										.changeColor(valid);
							}
						} catch (Exception exc) {
							((MyBorder) regPhoneField.getBorder())
									.changeColor(invalid);
						}
					} else if (name.equals("regLatitudeField")) {
						try {
							input = regLatitudeField.getText();
							if (!input.equals("")) {
								System.out.println(input);
								tUser.setLatitude(input);
								((MyBorder) regLatitudeField.getBorder())
										.changeColor(valid);
								System.out.println("hore");
								if (((MyBorder) regLongitudeField.getBorder())
										.getColor().equals(valid)) {
									updateRightPanelMap(
											tUser.getLatitudeDouble(),
											tUser.getLongitudeDouble());
								}
							}
						} catch (Exception exc) {
							((MyBorder) regLatitudeField.getBorder())
									.changeColor(invalid);
						}
					} else if (name.equals("regLongitudeField")) {
						try {
							input = regLongitudeField.getText();
							if (!input.equals("")) {
								tUser.setLongitude(input);
								((MyBorder) regLongitudeField.getBorder())
										.changeColor(valid);
								if (((MyBorder) regLatitudeField.getBorder())
										.getColor().equals(valid)) {
									updateRightPanelMap(
											tUser.getLatitudeDouble(),
											tUser.getLongitudeDouble());
								}
							}
						} catch (Exception exc) {
							((MyBorder) regLongitudeField.getBorder())
									.changeColor(invalid);
						}

					} else if (name.equals("regPwField")) {
						try {
							char[] list = regPwField.getPassword();
							String password1 = "";
							for (int i = 0; i < list.length; i++) {
								password1 += list[i];
							}
							if (!password1.equals("")) {
								tUser.setPassword(password1);
								((MyBorder) regPwField.getBorder())
										.changeColor(valid);
							}
						} catch (Exception exc) {
							((MyBorder) regPwField.getBorder())
									.changeColor(invalid);
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
								((MyBorder) regPwField2.getBorder())
										.changeColor(valid);
							} else {
								// LEGGE TIL EN PASSORD MATCHER IKKE LABEL
								// ELLERNO
								((MyBorder) regPwField2.getBorder())
										.changeColor(invalid);
								System.out.println("Password does not match!");
								return;
							}
						} catch (Exception exc) {
							((MyBorder) regPwField.getBorder())
									.changeColor(invalid);
							((MyBorder) regPwField2.getBorder())
									.changeColor(invalid);
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
	private class MyListener implements ActionListener {
		// bruke setActionCommand p� alt som skal lyttes p� og bruke det
		// istedenfor navn?
		@Override
		public void actionPerformed(ActionEvent arg) {
			// rightPanel.setBackground(randomColor());
			if (arg.getSource() instanceof JButton) {
				JButton pressed = (JButton) arg.getSource();
				String text = pressed.getText();
				// System.out.println(text);

				if (text.equals("Register")) {
					if (state == 2) {
						if (registerUser()) {
							changeToRegisterInterface(false);
							changeToLoginInterface(true);
						} else {
							// istdenfor boolean fra registerUser() f� tall for
							// � finne ut om hvilke verdier som er feil
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
						// metode for � sjekke om brukernavn og passord stemmer
						login();
					} else {
						changeToStartInterface(false);
						changeToLoginInterface(true);
					}
				} else if (text.equals("          Exit")) {
					System.exit(0);
				} else if (text.equals("          Back")) {
					// state holder styr p� hvor man er, s� programet vet hvor
					// en
					// skal sendes tilbake til
					if (state == LOGIN) {
						changeToLoginInterface(false);
						changeToStartInterface(true);
					} else if (state == REG) {
						changeToRegisterInterface(false);
						changeToStartInterface(true);
					} else if (state == FORGOT) {
						changeToForgotInterface(false);
						changeToLoginInterface(true);
					} else if (state == SEARCH) {
						changeToSearchInterface(false);
						changeToMainInterface(true);
					} else if (state == EDIT) {
						changeToEditInterface(false);
						changeToMainInterface(true);
					} else if (state == LIST) {
						changeToListInterface(false);
						changeToMainInterface(true);
					} else if (state == SHEEPREG) {
						changeToRegSheepInterface(false);
						changeToMainInterface(true);
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
				} else if (text.equals("Register sheep")) {
					System.out.println("sheep reg");
					changeToMainInterface(false);
					changeToRegSheepInterface(true);
				} else if (text.equals("List over sheeps")) {
					System.out.println("sheep list");
					changeToMainInterface(false);
					changeToListInterface(true);
				} else if (text.equals("Search for sheep")) {
					System.out.println("sheep search");
					changeToMainInterface(false);
					changeToSearchInterface(true);
					updateRightPanelMap(user.getLatitudeDouble(),
							user.getLongitudeDouble());

				} else if (text.equals("Edit sheep")) {
					System.out.println("sheep edit");
					changeToMainInterface(false);
					changeToEditInterface(true);
				} else if (text.equals("Home")) {
					changeToMainInterface(true);
					// TODO
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

	// Lytter for � h�ndtere tid. Til komponenter som skal flyttes over tid.
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
	private JTextField unField;
	private JPasswordField pwField;

	// forgot components
	private JTextField emailField;

	// register components
	private JTextField regNameField;
	private JTextField regLNameField;
	private JTextField regEmailField;
	private JTextField regPhoneField;
	private JTextField regLongitudeField;
	private JTextField regLatitudeField;
	private JPasswordField regPwField;
	private JPasswordField regPwField2;

	// main components
	private JButton sheepRegButton;
	private JButton listButton;
	private JButton searchButton;
	private JButton editButton;
	private JButton homeButton;
	private MyMap myMap;

	// search components
	private JLabel searchLabel;
	private JTextField searchField;

	// edit components
	private JTextField editIdField;
	private JTextField editAgeField;
	private JTextField editWeightField;
	private JTextField editSexField;
	private JTextField editShepardField;
	private JButton clearAllButton;
	private JButton updateButton;

	// list components
	private JButton sheepy;

	// register sheep components
	private JTextField regIDField;
	private JTextField regAgeField;
	private JTextField regWeightField;
	private JTextField regSexField;
	private JTextField regShepardField;

	// List over the different components
	private ArrayList<JComponent> buttonComps;
	private ArrayList<JComponent> startComps;
	private ArrayList<JComponent> loginComps;
	private ArrayList<JComponent> registerComps;
	private ArrayList<JComponent> forgotComps;
	private ArrayList<JComponent> mainComps;
	private ArrayList<JComponent> searchComps;
	private ArrayList<JComponent> editComps;
	private ArrayList<JComponent> listComps;
	private ArrayList<JComponent> regSheepComps;

	private ArrayList<JComponent> fieldComps;
}
