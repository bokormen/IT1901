package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import components.MyBorder;
import components.MyButton;
import components.MyImagePanel;
import components.MyLabel;
import components.MyListRenderer;
import components.MyMap;
import components.MyPasswordField;
import components.MySheepButton;
import components.MyTextField;

import div.ClientConnection;
import div.MyPoint;
import div.Sheep;
import div.SheepLocation;
import div.User;
import div.UserRegistration;

/**
 * Klasse som setter sammen GUI
 * 
 * @author andreas
 * 
 */

@SuppressWarnings("serial")
public class GUI extends JFrame {

	public static void main(String args[]) {
		try {
			ClientConnection.open(null);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		GUI f = new GUI();
		f.setVisible(true);
	}

	private int width;
	private int height;

	private User user; // gitt bruker etter en har logget paa
	private User tUser = new User(); // test bruker
	private Sheep tSheep = new Sheep(); // test sau
	private Sheep editSheep; // gitt sau som skal endres
	private MySheepButton listSelected; // sau blitt trykket paa i listen
	private MySheepButton editSheepButton; // gitt saueknapp som skal endres
	private ArrayList<SheepLocation> sheepLocs; // historie lokasjonen til sau

	public static int START = 0, LOGIN = 1, REG = 2, FORGOT = 3, MAIN = 4, SEARCH = 5, EDIT = 6, LIST = 7,
			SHEEPREG = 8, LOG = 9;
	private int state; // hvor i programmet en er

	public final static Color valid = Color.green;
	public final static Color invalid = Color.red;

	private MyListener actionListener;
	private FocusListener focusListener;
	private JLayeredPane lp;

	public GUI() {
		super("Sheepy");
		width = 1200;
		height = 600;
		setBounds(0, 0, width, height + 20); // +20 for tittel bar'en
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		this.setLocationByPlatform(true);
		try {
			setIconImage(ImageIO.read(new File("src/resources/bluesheepicon2.png")));
		} catch (IOException e) {
		}

		actionListener = new MyListener();
		createFocusListener();

		this.lp = getLayeredPane();

		createLowerPanel();

		createLoginInterfaceComponents();
		createRegisterInterfaceComponents();
		createForgotInterfaceComponents();

		createMainInterfaceComponents();
		createSearchInterfaceComponents();
		createEditInterfaceComponents();
		createRegSheepInterfaceComponents();
		createLogInterfaceComponents();
		createListInterfaceComponents();

		createPanels();

		createComponentArrays();

		// Setter alle start komponentene synlige
		changeToStartInterface(true);

		repaintPanel();
	}

	/**
	 * Tegner om alle komponenter
	 */
	public void repaintPanel() {
		repaint();
		validate();
	}

	/**
	 * Lager to paneler fordelt paa: leftPanel (width/3) og rightPanel
	 * (2*width/3) som tegner bakgrunnen
	 * 
	 * @param lp
	 *            LayeredPane
	 */
	private void createPanels() {
		rightPanel = new MyImagePanel(2 * width / 3, height, "images/sheepbackground.jpeg");
		rightPanel.setBounds(width / 3, 0, 2 * width / 3, height);
		rightPanel.repaint();

		leftPanel = new MyImagePanel(width / 3, height, "images/blackcarbon.jpg");
		leftPanel.setBounds(0, 0, width / 3, height);
		leftPanel.repaint();

		lp.add(rightPanel);
		lp.add(leftPanel);
	}

	/**
	 * Lager alle komponeneter til login interfacet
	 */
	private void createLoginInterfaceComponents() {
		int cw = width / 6;
		int ch = 3 * height / 40;

		backButton = new MyButton("Back", null);
		backButton.setBounds(11 * width / 48, 21 * height / 25, cw / 2, ch);
		backButton.addActionListener(actionListener);

		exitButton = new MyButton("Exit", null);
		exitButton.setBounds(width / 48, 21 * height / 25, cw / 2, ch);
		exitButton.addActionListener(actionListener);

		loginButton = new MyButton("Login", "bluesheepicon2");
		loginButton.setBounds(width / 12, 7 * height / 20, cw, ch);
		loginButton.addActionListener(actionListener);

		registerButton = new MyButton("Register", "blueformicon");
		registerButton.setBounds(width / 12, 9 * height / 20, cw, ch);
		registerButton.addActionListener(actionListener);

		unField = new MyTextField("blueusericon", "Email", 8);
		unField.setBounds(width / 12, 35 * height / 200, width / 6, ch);
		unField.addActionListener(actionListener);
		unField.setName("unField");

		pwField = new MyPasswordField("bluekeyicon", "Password", 8);
		pwField.setBounds(width / 12, 55 * height / 200, width / 6, ch);
		pwField.addActionListener(actionListener);
		pwField.setName("pwField");

		loginInfo = new MyLabel("", null);
		loginInfo.setBounds(width / 12, 70 * height / 200, width / 6, ch);

		lp.add(backButton);
		lp.add(exitButton);
		lp.add(loginButton);
		lp.add(registerButton);
		lp.add(unField);
		lp.add(pwField);
		lp.add(loginInfo);
	}

	/**
	 * Lager alle komponeneter til registrering interfacet
	 */
	private void createRegisterInterfaceComponents() {
		int cw = width / 9 /* 4 * width / 30 */;
		int ch = 3 * height / 40;

		regNameField = new MyTextField("bluesheepicon", "Firstname", 8);
		regNameField.setBounds(width / 12, 20 * height / 200, 3 * cw / 2, ch);
		regNameField.addActionListener(actionListener);
		regNameField.addFocusListener(focusListener);
		regNameField.setName("regNameField");

		regLNameField = new MyTextField("bluesheepicon", "Lastname", 8);
		regLNameField.setBounds(width / 12, 35 * height / 200, 3 * cw / 2, ch);
		regLNameField.addActionListener(actionListener);
		regLNameField.addFocusListener(focusListener);
		regLNameField.setName("regLNameField");

		regEmailField = new MyTextField("bluemailicon", "Email", 8);
		regEmailField.setBounds(width / 12, 50 * height / 200, 3 * cw / 2, ch);
		regEmailField.addActionListener(actionListener);
		regEmailField.addFocusListener(focusListener);
		regEmailField.setName("regEmailField");

		regPhoneField = new MyTextField("bluephoneicon", "Phone", 8);
		regPhoneField.setBounds(width / 12, 65 * height / 200, 3 * cw / 2, ch);
		regPhoneField.addActionListener(actionListener);
		regPhoneField.addFocusListener(focusListener);
		regPhoneField.setName("regPhoneField");

		regLongitudeField = new MyTextField(null, "Longitude", 8);
		regLongitudeField.setBounds(width / 12 + 3 * cw / 4, 80 * height / 200, 3 * cw / 4, ch);
		regLongitudeField.addActionListener(actionListener);
		regLongitudeField.addFocusListener(focusListener);
		regLongitudeField.setName("regLongitudeField");

		regLatitudeField = new MyTextField(null, "Latitude", 8);
		regLatitudeField.setBounds(width / 12, 80 * height / 200, 3 * cw / 4, ch);
		regLatitudeField.addActionListener(actionListener);
		regLatitudeField.addFocusListener(focusListener);
		regLatitudeField.setName("regLatitudeField");

		regPwField = new MyPasswordField("bluekeyicon", "Password", 8);
		regPwField.setBounds(width / 12, 95 * height / 200, 3 * cw / 2, ch);
		regPwField.addActionListener(actionListener);
		regPwField.addFocusListener(focusListener);
		regPwField.setName("regPwField");

		regPwField2 = new MyPasswordField("bluekeyicon", "Password", 8);
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
	 * Lager alle komponeneter til glemt passord interfacet
	 */
	private void createForgotInterfaceComponents() {
		int cw = width / 6;
		int ch = 3 * height / 40;

		forgotButton = new MyButton("Forgot", "bluequestionicon");
		forgotButton.setBounds(width / 12, 125 * height / 200, cw, ch);
		forgotButton.addActionListener(actionListener);

		sendButton = new MyButton("Send", null);
		sendButton.setBounds(width / 12, 9 * height / 20, cw, ch);
		sendButton.addActionListener(actionListener);

		emailField = new MyTextField("bluemailicon", "Email", 8);
		emailField.setBounds(width / 12, 6 * height / 20, cw, ch);
		emailField.addActionListener(actionListener);
		emailField.setName("emailField");

		lp.add(forgotButton);
		lp.add(sendButton);
		lp.add(emailField);
	}

	/**
	 * Lager alle komponeneter til hovedmeny interfacet
	 */
	private void createMainInterfaceComponents() {
		int cw = width / 6;
		int ch = 3 * height / 40;

		sheepRegButton = new MyButton("Register sheep", "blueplusicon");
		sheepRegButton.setBounds(width / 12, 30 * height / 200, cw, ch);
		sheepRegButton.addActionListener(actionListener);

		listButton = new MyButton("Sheeplist", "bluelisticon");
		listButton.setBounds(width / 12, 50 * height / 200, cw, ch);
		listButton.addActionListener(actionListener);

		searchButton = new MyButton("Search for sheep", "bluesearchicon");
		searchButton.setBounds(width / 12, 70 * height / 200, cw, ch);
		searchButton.addActionListener(actionListener);

		editButton = new MyButton("Edit user", "blueediticon");
		editButton.setBounds(width / 12, 90 * height / 200, cw, ch);
		editButton.addActionListener(actionListener);

		homeButton = new MyButton("Home", "bluehouseicon");
		homeButton.setBounds(width / 12, 110 * height / 200, cw, ch);
		homeButton.addActionListener(actionListener);

		mainInfoLabel = new MyLabel("", null);
		mainInfoLabel.setBounds(width / 24, 15 * height / 200, 3 * cw / 2, ch);

		lp.add(sheepRegButton);
		lp.add(listButton);
		lp.add(searchButton);
		lp.add(editButton);
		lp.add(homeButton);
		lp.add(mainInfoLabel);
	}

	/**
	 * Lager alle komponeneter til søk etter sau interfacet
	 */
	private void createSearchInterfaceComponents() {
		int cw = width / 6;
		int ch = 3 * height / 40;

		searchLabel = new JLabel("");
		searchLabel.setBounds(width / 12, 80 * height / 200, cw, ch);
		searchLabel.setForeground(Color.RED);
		searchLabel.setHorizontalAlignment(JLabel.CENTER);
		searchLabel.setVisible(false);

		searchField = new MyTextField("bluesheepicon", "ID", 8);
		searchField.setBounds(width / 12, 65 * height / 200, cw, ch);
		searchField.addFocusListener(focusListener);
		searchField.setName("searchField");

		lp.add(searchLabel);
		lp.add(searchField);
	}

	/**
	 * Lager alle komponeneter til forandre saue informasjon interfacet
	 */
	private void createEditInterfaceComponents() {
		int cw = width / 6;
		int ch = 3 * height / 40;

		editPasswordField = new MyTextField("bluesheepicon", "New password", 8);
		editPasswordField.setBounds(width / 12, 25 * height / 200, cw, ch);
		editPasswordField.addFocusListener(focusListener);
		editPasswordField.setName("editPasswordField");

		editEmailField = new MyTextField("bluesheepicon", "new email", 8);
		editEmailField.setBounds(width / 12, 40 * height / 200, cw, ch);
		editEmailField.addFocusListener(focusListener);
		editEmailField.setName("editEmailField");

		editPhoneField = new MyTextField("bluesheepicon", "new number", 8);
		editPhoneField.setBounds(width / 12, 55 * height / 200, cw, ch);
		editPhoneField.addFocusListener(focusListener);
		editPhoneField.setName("editPhoneField");

		updateButton = new MyButton("Update", null);
		updateButton.setBounds(width / 12, 80 * height / 200, cw, ch);
		updateButton.addActionListener(actionListener);
		updateButton.setName("updateButton");

		lp.add(editPasswordField);
		lp.add(editEmailField);
		lp.add(editPhoneField);
		lp.add(updateButton);
	}

	/**
	 * Lager alle komponeneter til forandre saue informasjon interfacet
	 */
	private void createListInterfaceComponents() {
		int cw = width / 6;
		int ch = 3 * height / 40;

		sheepy = new MyButton("Sort by color", null);
		sheepy.setBounds(width / 6 - 7 * cw / 12, 12 * height / 200, cw / 2, ch);
		sheepy.addActionListener(actionListener);

		sheepyAttack = new MyButton("Attack sheep", null);
		sheepyAttack.setBounds(width / 6, 12 * height / 200, cw / 2, ch);
		sheepyAttack.addActionListener(actionListener);

		sheepyLog = new MyButton("History", null);
		sheepyLog.setBounds(width / 6, 30 * height / 200, cw / 2, ch);
		sheepyLog.addActionListener(actionListener);

		sheepyDel = new MyButton("Delete", null);
		sheepyDel.setBounds(width / 6 - 7 * cw / 12, 30 * height / 200, cw / 2, ch);
		sheepyDel.addActionListener(actionListener);

		sheepListModel = new DefaultListModel();

		sheepList = new JList(sheepListModel);
		sheepList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		sheepList.setCellRenderer(new MyListRenderer());

		sheepList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				int index = list.locationToIndex(evt.getPoint());
				Sheep s = null;
				String id = ((String) sheepListModel.getElementAt(index)).split(" ")[0];
				for (MySheepButton b : mySheepButtons) {
					if (b.getSheep().getId() == (Integer.parseInt(id))) {
						s = b.getSheep();
						listSelected = b;
						setLwEditSheep(s);
					}
				}

				if (evt.getClickCount() == 2) {
					MyPoint p = null;
					try {
						p = getLocationPoint(s.getLocation().getPosition());
					} catch (Exception e) {
						e.printStackTrace();
					}
					myMap.centerInOnSheep(p.getLatitude(), p.getLongitude());
					changeMySheepButtonColor(Color.WHITE);
					listSelected.setColor(Color.BLUE);
				} else if (evt.getClickCount() == 3) { // Triple-click
					index = list.locationToIndex(evt.getPoint());

				}
			}
		});

		sheepScrollPane = new JScrollPane(sheepList);
		sheepScrollPane.setBounds(width / 24, 50 * height / 200, 3 * cw / 2, ch * 6);
		sheepScrollPane.setVisible(false);

		listInfo = new MyLabel("", null);
		listInfo.setBounds(width / 12, 140 * height / 200, cw, ch);

		lp.add(sheepScrollPane);
		lp.add(sheepy);
		lp.add(sheepyAttack);
		lp.add(sheepyLog);
		lp.add(sheepyDel);
		lp.add(listInfo);
	}

	/**
	 * Lager alle komponeneter til registrere sau interfacet
	 */
	private void createRegSheepInterfaceComponents() {
		int cw = width / 6;
		int ch = 3 * height / 40;
		regSheepNameField = new MyTextField("bluesheepicon", "Name", 8);
		regSheepNameField.setBounds(width / 12, 25 * height / 200, cw, ch);
		regSheepNameField.addFocusListener(focusListener);
		regSheepNameField.setName("regSheepNameField");

		regAgeField = new MyTextField("bluesheepicon", "Birthyear", 8);
		regAgeField.setBounds(width / 12, 45 * height / 200, cw, ch);
		regAgeField.addFocusListener(focusListener);
		regAgeField.setName("regAgeField");

		regWeightField = new MyTextField("bluesheepicon", "Weight", 8);
		regWeightField.setBounds(width / 12, 65 * height / 200, cw, ch);
		regWeightField.addFocusListener(focusListener);
		regWeightField.setName("regWeightField");

		regSexField = new MyTextField("bluesheepicon", "Sex", 8);
		regSexField.setBounds(width / 12, 85 * height / 200, cw, ch);
		regSexField.addFocusListener(focusListener);
		regSexField.setName("regSexField");

		regShepherdField = new MyTextField("bluesheepicon", "Shepard", 8);
		regShepherdField.setBounds(width / 12, 105 * height / 200, cw, ch);
		regShepherdField.addFocusListener(focusListener);
		regShepherdField.setName("regShepherdField");

		lp.add(regSheepNameField);
		lp.add(regAgeField);
		lp.add(regWeightField);
		lp.add(regSexField);
		lp.add(regShepherdField);
	}

	/**
	 * Lager alle komponeneter til log interfacet
	 */
	private void createLogInterfaceComponents() {
		int cw = width / 6;
		int ch = 3 * height / 40;
		logListModel = new DefaultListModel();

		logList = new JList(logListModel);
		logList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		logList.setCellRenderer(new MyListRenderer());

		logList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				int index = list.locationToIndex(evt.getPoint());
				setLwEditSheep(listSelected.getSheep());

				if (evt.getClickCount() == 2) {
					try {
						MyPoint p = getLocationPoint(sheepLocs.get(index).getPosition());
						myMap.centerInOnSheep(p.getLatitude(), p.getLongitude());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (evt.getClickCount() == 3) { // Triple-click
					index = list.locationToIndex(evt.getPoint());
				}
			}
		});

		logScrollPane = new JScrollPane(logList);
		logScrollPane.setBounds(width / 24, 50 * height / 200, 3 * cw / 2, ch * 6);
		logScrollPane.setVisible(false);

		logDateButton = new MyButton("Dates", null);
		logDateButton.setBounds(width / 9 - cw / 4, 40 * height / 200, cw / 2, ch / 2);
		logDateButton.addActionListener(actionListener);

		logCoordsButton = new MyButton("Coordinates", null);
		logCoordsButton.setBounds(width / 9 + cw / 2, 40 * height / 200, cw / 2, ch / 2);
		logCoordsButton.addActionListener(actionListener);

		logIdButton = new MyButton("Sheep ID: ", null);
		logIdButton.setBounds(width / 12, 18 * height / 200, cw, ch);
		logIdButton.addActionListener(actionListener);

		lp.add(logScrollPane);
		lp.add(logDateButton);
		lp.add(logIdButton);
		lp.add(logCoordsButton);
	}

	/**
	 * Lager 200 saueknapper der et gitt antall får posisjon, utifra listen av
	 * sauer fra brukeren.
	 */
	private void addMySheepButtons() {
		mySheepButtons = new ArrayList<MySheepButton>();
		mySheepButtons.clear();
		ArrayList<Sheep> list = user.getSheepList();
		if (!list.isEmpty()) {
			for (int i = 0; i < 200; i++) {
				Sheep s;
				try {
					s = list.get(i);
				} catch (Exception e) {
					s = null;
				}
				if (s != null) {
					mySheepButtons.add(new MySheepButton(new JButton(), s, 0, 0, 10, this));
				}
			}
			for (MySheepButton b : mySheepButtons) {
				lp.add(b);
			}
		}
	}

	/**
	 * Legger til et antall saueknapper til aa vise til historien til saue
	 * posisjonene
	 */
	private void addMySheepLogButtons() {
		logSheepButtons = new ArrayList<MySheepButton>();
		logSheepButtons.clear();

		for (int i = 0; i < 20; i++) {
			logSheepButtons.add(new MySheepButton(new JButton(), null, 0, 0, 10, this));
		}
		for (MySheepButton b : logSheepButtons) {
			lp.add(b);
		}

	}

	/**
	 * Lager et objekt av kartet
	 */
	private void createMap() {
		myMap = new MyMap(2 * width / 3, 85 * height / 100, user.getLatitudeDouble(), user.getLongitudeDouble(), this);
		myMap.setBounds(width / 3, 0, 2 * width / 3, 85 * height / 100);
		lp.add(myMap);
	}

	/**
	 * Lager en nedre meny som brukes til å vise informasjonen til sauene
	 */
	private void createLowerPanel() {
		try {
			lwEditLabel = new JLabel() {
				BufferedImage image = ImageIO.read(this.getClass().getClassLoader()
						.getResource("images/blackcarbon.jpg"));;

				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(image, 0, 0, this);
				}
			};

			int cw = width / 12;
			int ch = 2 * height / 40;

			lwEditLabel.setBounds(width / 3, 85 * height / 100, 2 * width / 3, 15 * height / 100);
			lwEditLabel.setVisible(false);

			lwEditIdField = new MyTextField(null, "", -1);
			lwEditIdField.setBounds(41 * width / 120, 9 * height / 10, 5 * cw / 8, ch);
			lwEditIdField.setEditable(false);

			lwEditNameField = new MyTextField(null, "", -1);
			lwEditNameField.setBounds(48 * width / 120, 9 * height / 10, 4 * cw / 5, ch);

			lwEditOwnerField = new MyTextField(null, "", -1);
			lwEditOwnerField.setBounds(57 * width / 120, 9 * height / 10, 23 * cw / 16, ch);

			lwEditShepherdField = new MyTextField(null, "", -1);
			lwEditShepherdField.setBounds(72 * width / 120, 9 * height / 10, 23 * cw / 16, ch);

			lwEditGenderField = new MyTextField(null, "", -1);
			lwEditGenderField.setBounds(87 * width / 120, 9 * height / 10, cw / 2, ch);

			lwEditWeightField = new MyTextField(null, "", -1);
			lwEditWeightField.setBounds(93 * width / 120, 9 * height / 10, cw / 2, ch);

			lwEditHeartrateField = new MyTextField(null, "", -1);
			lwEditHeartrateField.setBounds(99 * width / 120, 9 * height / 10, cw / 2, ch);
			lwEditHeartrateField.setEditable(false);

			lwEditTemperatureField = new MyTextField(null, "", -1);
			lwEditTemperatureField.setBounds(105 * width / 120, 9 * height / 10, cw / 2, ch);
			lwEditTemperatureField.setEditable(false);

			lwEditBirthyearField = new MyTextField(null, "", -1);
			lwEditBirthyearField.setBounds(111 * width / 120, 9 * height / 10, cw / 2, ch);

			lwEditIdLabel = new MyLabel("ID:", null);
			lwEditIdLabel.setBounds(41 * width / 120, 85 * height / 100, 5 * cw / 8, ch);

			lwEditNameLabel = new MyLabel("Name:", null);
			lwEditNameLabel.setBounds(48 * width / 120, 85 * height / 100, 5 * cw / 8, ch);

			lwEditOwnerLabel = new MyLabel("Owner:", null);
			lwEditOwnerLabel.setBounds(57 * width / 120, 85 * height / 100, 23 * cw / 16, ch);

			lwEditShepherdLabel = new MyLabel("Sheperd:", null);
			lwEditShepherdLabel.setBounds(72 * width / 120, 85 * height / 100, 23 * cw / 16, ch);

			lwEditGenderLabel = new MyLabel("Gender:", null);
			lwEditGenderLabel.setBounds(87 * width / 120, 85 * height / 100, cw / 2, ch);

			lwEditWeightLabel = new MyLabel("Weight:", null);
			lwEditWeightLabel.setBounds(93 * width / 120, 85 * height / 100, cw / 2, ch);

			lwEditHeartrateLabel = new MyLabel("", "redtempicon");
			lwEditHeartrateLabel.setBounds(211 * width / 240, 85 * height / 100, cw / 2, ch);

			lwEditTemperatureLabel = new MyLabel("", "redhearticon");
			lwEditTemperatureLabel.setBounds(199 * width / 240, 85 * height / 100, cw / 2, ch);

			lwEditBirthyearLabel = new MyLabel("Birth year:", null);
			lwEditBirthyearLabel.setBounds(108 * width / 120, 85 * height / 100, cw, ch);

			lwEditButton = new MyButton("", "redgearicon");
			lwEditButton.setBounds(231 * width / 240, 84 * height / 100, cw, cw);
			lwEditButton.setBorder(null);
			lwEditButton.setName("lwEditButton");
			lwEditButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editSheep();
				}
			});

			lp.add(lwEditButton);
			lp.add(lwEditIdField);
			lp.add(lwEditNameField);
			lp.add(lwEditWeightField);
			lp.add(lwEditOwnerField);
			lp.add(lwEditShepherdField);
			lp.add(lwEditGenderField);
			lp.add(lwEditHeartrateField);
			lp.add(lwEditTemperatureField);
			lp.add(lwEditBirthyearField);

			lp.add(lwEditIdLabel);
			lp.add(lwEditNameLabel);
			lp.add(lwEditWeightLabel);
			lp.add(lwEditOwnerLabel);
			lp.add(lwEditShepherdLabel);
			lp.add(lwEditGenderLabel);
			lp.add(lwEditHeartrateLabel);
			lp.add(lwEditTemperatureLabel);
			lp.add(lwEditBirthyearLabel);

			lp.add(lwEditLabel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Setter sammen de ulike listene av komponeneter for å enklere sette de
	 * synlige og motsatt
	 */
	private void createComponentArrays() {

		startComps = new ArrayList<JComponent>();
		startComps.add(loginButton);
		startComps.add(registerButton);
		startComps.add(exitButton);

		loginComps = new ArrayList<JComponent>();
		loginComps.add(pwField);
		loginComps.add(unField);
		loginComps.add(forgotButton);
		loginComps.add(loginButton);
		loginComps.add(loginInfo);
		loginComps.add(backButton);
		loginComps.add(exitButton);

		forgotComps = new ArrayList<JComponent>();
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
		mainComps.add(sheepRegButton);
		mainComps.add(listButton);
		mainComps.add(searchButton);
		mainComps.add(editButton);
		mainComps.add(homeButton);
		mainComps.add(mainInfoLabel);
		mainComps.add(backButton);
		mainComps.add(exitButton);

		searchComps = new ArrayList<JComponent>();
		searchComps.add(searchLabel);
		searchComps.add(searchField);
		searchComps.add(searchButton);
		searchComps.add(backButton);
		searchComps.add(exitButton);

		editComps = new ArrayList<JComponent>();
		editComps.add(editEmailField);
		editComps.add(editPhoneField);
		editComps.add(editPasswordField);
		editComps.add(updateButton);
		editComps.add(backButton);
		editComps.add(exitButton);

		listComps = new ArrayList<JComponent>();
		listComps.add(sheepy);
		listComps.add(sheepyAttack);
		listComps.add(sheepyLog);
		listComps.add(sheepyDel);
		listComps.add(sheepScrollPane);
		listComps.add(listInfo);
		listComps.add(backButton);
		listComps.add(exitButton);

		regSheepComps = new ArrayList<JComponent>();
		regSheepComps.add(regSheepNameField);
		regSheepComps.add(regAgeField);
		regSheepComps.add(regWeightField);
		regSheepComps.add(regSexField);
		regSheepComps.add(regShepherdField);
		regSheepComps.add(sheepRegButton);
		regSheepComps.add(exitButton);
		regSheepComps.add(backButton);

		logComps = new ArrayList<JComponent>();
		logComps.add(logScrollPane);
		logComps.add(logDateButton);
		logComps.add(logCoordsButton);
		logComps.add(logIdButton);
		logComps.add(exitButton);
		logComps.add(backButton);

		fieldComps = new ArrayList<JComponent>();
		fieldComps.add(regNameField);
		fieldComps.add(regLNameField);
		fieldComps.add(regEmailField);
		fieldComps.add(regPhoneField);
		fieldComps.add(regPwField);
		fieldComps.add(regPwField2);
		fieldComps.add(pwField);
		fieldComps.add(unField);
		fieldComps.add(emailField);

		lwEditComps = new ArrayList<JComponent>();
		lwEditComps.add(lwEditLabel);
		lwEditComps.add(lwEditNameField);
		lwEditComps.add(lwEditIdField);
		lwEditComps.add(lwEditWeightField);
		lwEditComps.add(lwEditOwnerField);
		lwEditComps.add(lwEditShepherdField);
		lwEditComps.add(lwEditGenderField);
		lwEditComps.add(lwEditHeartrateField);
		lwEditComps.add(lwEditTemperatureField);
		lwEditComps.add(lwEditBirthyearField);
		lwEditComps.add(lwEditIdLabel);
		lwEditComps.add(lwEditNameLabel);
		lwEditComps.add(lwEditWeightLabel);
		lwEditComps.add(lwEditOwnerLabel);
		lwEditComps.add(lwEditShepherdLabel);
		lwEditComps.add(lwEditGenderLabel);
		lwEditComps.add(lwEditHeartrateLabel);
		lwEditComps.add(lwEditTemperatureLabel);
		lwEditComps.add(lwEditBirthyearLabel);
		lwEditComps.add(lwEditButton);
	}

	/**
	 * Metode for aa bytte til start interface
	 * 
	 * @param bool
	 *            true for synlig
	 */
	private void changeToStartInterface(Boolean bool) {
		if (bool) {
			// endrer posisjon til gitte knapper
			loginButton.setBounds(width / 12, 6 * height / 20, width / 6, height / 10);
			registerButton.setBounds(width / 12, 9 * height / 20, width / 6, height / 10);
			state = 0;
		}
		for (Component c : startComps) {
			c.setVisible(bool);
		}
		repaintPanel();
	}

	/**
	 * Metode for aa bytte til login interface
	 * 
	 * @param bool
	 *            true for synlig
	 */
	private void changeToLoginInterface(boolean bool) {
		for (Component c : loginComps) {
			c.setVisible(bool);
		}
		if (bool) {
			// endrer posisjon til gitte knapper
			loginButton.setBounds(width / 12, 85 * height / 200, width / 6, 3 * height / 40);
			forgotButton.setBounds(width / 12, 105 * height / 200, width / 6, 3 * height / 40);
			registerButton.setVisible(!bool);
			backButton.setText("Back");
			unField.requestFocus();
			state = LOGIN;
		}
		repaintPanel();
	}

	/**
	 * Metode for aa bytte til register interface
	 * 
	 * @param bool
	 *            true for synlig
	 */
	private void changeToRegisterInterface(boolean bool) {
		if (bool) {
			registerButton.setBounds(width / 12, 130 * height / 200, width / 6, 3 * height / 40);
			backButton.setText("Back");
			state = REG;
		}
		for (Component c : registerComps) {
			c.setVisible(bool);
		}
		repaintPanel();
	}

	/**
	 * Metode for aa bytte til forgot interface
	 * 
	 * @param bool
	 *            true for synlig
	 */
	private void changeToForgotInterface(boolean bool) {
		if (bool) {
			sendButton.setBounds(width / 12, 85 * height / 200, width / 6, 3 * height / 40);
			backButton.setText("Back");
			state = FORGOT;
		}
		for (Component c : forgotComps) {
			c.setVisible(bool);
		}
		repaintPanel();
	}

	/**
	 * Metode for aa bytte til main interface
	 * 
	 * @param bool
	 *            true for synlig
	 */
	private void changeToMainInterface(boolean bool) {
		if (bool) {
			searchButton.setBounds(width / 12, 70 * height / 200, width / 6, 3 * height / 40);
			sheepRegButton.setBounds(width / 12, 30 * height / 200, width / 6, 3 * height / 40);
			backButton.setText("Log out");
			editButton.setVisible(false);
			if (editSheepButton != null) {
				((MyBorder) editSheepButton.getBorder()).setColor(Color.RED);
			}
			state = MAIN;
		}
		for (Component c : mainComps) {
			c.setVisible(bool);
		}
		repaintPanel();
	}

	/**
	 * Metode for aa bytte til search interface
	 * 
	 * @param bool
	 *            true for synlig
	 */
	private void changeToSearchInterface(boolean bool) {
		if (bool) {
			searchButton.setBounds(width / 12, 85 * height / 200, width / 6, 3 * height / 40);
			searchLabel.setText("");
			backButton.setText("Back");
			state = SEARCH;
		}
		for (Component c : searchComps) {
			c.setVisible(bool);
		}
	}

	/**
	 * Metode for aa bytte til edit interface
	 * 
	 * @param bool
	 *            true for synlig
	 */
	private void changeToEditInterface(boolean bool) {
		if (bool) {
			backButton.setText("Back");
			state = EDIT;
		}
		for (Component c : editComps) {
			c.setVisible(bool);
		}
	}

	/**
	 * Metode for aa bytte til list interface
	 * 
	 * @param bool
	 *            true for synlig
	 */
	private void changeToListInterface(boolean bool) {
		if (bool) {
			backButton.setText("Back");
			state = LIST;
		}
		for (Component c : listComps) {
			c.setVisible(bool);
		}
	}

	/**
	 * Metode for aa bytte til log interface
	 * 
	 * @param bool
	 *            true for synlig
	 */
	private void changeToLogInterface(boolean bool) {
		if (bool) {
			backButton.setText("Back");
			state = LOG;
		}
		for (Component c : logComps) {
			c.setVisible(bool);
		}
	}

	/**
	 * Metode for aa bytte til register sheep interface
	 * 
	 * @param bool
	 *            true for synlig
	 */
	private void changeToRegSheepInterface(boolean bool) {
		if (bool) {
			sheepRegButton.setBounds(width / 12, 130 * height / 200, width / 6, 3 * height / 40);
			backButton.setText("Back");
			state = SHEEPREG;
		}
		for (Component c : regSheepComps) {
			c.setVisible(bool);
		}
	}

	/**
	 * Metode som prover aa registrere bruker naar brukeren ber om det
	 */
	private boolean registerUser() {

		String firstName = regNameField.getText();
		String lastName = regLNameField.getText();
		String email = regEmailField.getText();
		String phoneNr = regPhoneField.getText();
		String latitude = regLatitudeField.getText();
		String longitude = regLongitudeField.getText();
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
		// Sjekker om passordene stemmer oversens
		if (password.equals(password2) && !password.equals("")) {
			try {
				// Registrer bruker
				UserRegistration.registerUser(firstName, lastName, email, password2, phoneNr, latitude + ","
						+ longitude);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Metode som prover aa logge inn med gitt email og passord
	 */
	private boolean login() {
		String email = unField.getText();
		String password = "";
		char[] c = pwField.getPassword();
		for (int i = 0; i < c.length; i++) {
			password += c[i];
		}

		// for testing, lett innlogging
		if (email.equals("")) {
			try {
				this.user = UserRegistration.login("test0@test.test", "passord");
				try {
					user.updateSheepList();
				} catch (Exception e) {
					e.printStackTrace();
				}
				changeToLoginInterface(false);
				changeToMainInterface(true);
				rightPanel.setVisible(false);

				addMySheepButtons();
				addMySheepLogButtons();
				createMap();
				myMap.setUser(user);
				myMap.home();
				updateSheepList();
				setAttackedSheeps();

				for (JComponent jc : lwEditComps) {
					jc.setVisible(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		User user = UserRegistration.login(email, password);
		if (user != null) {
			this.user = user;
			try {
				user.updateSheepList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			changeToLoginInterface(false);
			changeToMainInterface(true);
			rightPanel.setVisible(false);

			addMySheepButtons();
			createMap();
			myMap.setUser(user);
			updateSheepList();
			setAttackedSheeps();

			for (JComponent jc : lwEditComps) {
				jc.setVisible(true);
			}

			return true;
		}
		loginInfo.setForeground(Color.GREEN);
		loginInfo.setText("Password does not match!");
		return false;
	}

	/**
	 * Metode som vil prove aa sende nytt passord til email
	 */
	private boolean sendEmail() {
		boolean sendmail = true;
		if (sendmail) { // you know
			if (emailField.getText() != null) {
				UserRegistration.mailPassword(emailField.getText());
			}
		}
		return false;
	}

	/**
	 * Metode der en kan endre paa saue informasjonen
	 */
	private Sheep editSheep() {
		try {
			String id = lwEditIdField.getText();
			String name = lwEditNameField.getText();
			String weight = lwEditWeightField.getText();
			String owner = lwEditOwnerField.getText();
			String shepherd = lwEditShepherdField.getText();
			String gender = lwEditGenderField.getText();
			String birthyear = lwEditBirthyearField.getText();

			user.editSheep(Integer.parseInt(id), name, owner, shepherd, gender.charAt(0), Integer.parseInt(weight),
					Integer.parseInt(birthyear));
			mainInfoLabel.setText("Sheep " + id + " has been changed");
		} catch (Exception e) {
			e.printStackTrace();
			mainInfoLabel.setText("Failed to update sheep");
		}
		return null;
	}

	/**
	 * Registrerer sau med informasjon gitt i tekstboksene.
	 * 
	 * @return true Hvis sau er blitt registrert
	 */
	private boolean registerSheep() {
		String id = regNameField.getText();
		String age = regAgeField.getText();
		String weight = regWeightField.getText();
		String gender = regSexField.getText();
		String shepherd = regShepherdField.getText();

		try {
			user.registerSheep(id, age, weight, gender, user.getEmail(), shepherd);

			resetMap();
			for (JComponent c : regSheepComps) {
				if (c instanceof MyTextField) {
					((MyTextField) c).setText("");
					((MyBorder) ((MyTextField) c).getBorder()).setColor(Color.WHITE);
				}
			}

			changeToRegSheepInterface(false);
			changeToMainInterface(true);
			return true;
		} catch (NumberFormatException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Metode som logger ut ifra bruker
	 */
	private void logout() {
		myMap.setUser(null);
		rightPanel.setVisible(true);
		changeMySheepButtonDrawBool(mySheepButtons, false);
		changeMySheepButtonDrawBool(logSheepButtons, false);
		logSheepButtons.clear();
		for (JComponent jc : lwEditComps) {
			jc.setVisible(false);
		}
		this.user = null;
	}

	/**
	 * Soker etter riktig sau og zoomer inn pÔøΩ kartet til rett sau.
	 */
	private void searchSheep() {
		String input = searchField.getText();
		int id = -1;
		try {
			id = Integer.parseInt(input);
			searchButton.setBounds(width / 12, 85 * height / 200, width / 6, 3 * height / 40);
			searchLabel.setText("");
			Sheep sheep = null;
			for (MySheepButton b : mySheepButtons) {
				if (id == b.getSheep().getId()) {
					sheep = b.getSheep();
					editSheepButton = b;
					listSelected = b;
					b.setColor(Color.BLUE);
				}
			}
			if (sheep != null) {
				double latitude = Double.parseDouble(sheep.getLocation().getLatitude());
				double longitude = Double.parseDouble(sheep.getLocation().getLongitude());
				editSheep = sheep;
				setLwEditSheep(sheep);
				myMap.centerInOnSheep(latitude, longitude);

				sheepyLog.setVisible(true);
			} else {
				searchButton.setBounds(width / 12, 95 * height / 200, width / 6, 3 * height / 40);
				searchLabel.setText("No sheep by that ID, (" + input + ").");
			}
		} catch (NumberFormatException e) {
			// feil verdier for input
			searchButton.setBounds(width / 12, 95 * height / 200, width / 6, 3 * height / 40);
			searchLabel.setText("ID input is not a valid number.");
		}
	}

	/**
	 * Metode som prover aa endre infoen til brukeren
	 */
	private boolean editUser() {
		try {
			String pw = editPasswordField.getText();
			String phone = editPhoneField.getText();
			String email = editEmailField.getText();
			tUser.setEmail(email);
			tUser.setPassword(pw);
			tUser.setPhoneNr(phone);
			this.user.setEmail(email);
			this.user.setPhoneNr(phone);
			this.user.setPassword(pw);
			UserRegistration.editUser(email, user.getFirstName(), user.getLastName(), phone, user.getPosition());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Endrer posisjonen til saueknappene på kartet
	 * 
	 * @param lat
	 *            hoydegraden til senter av kartet
	 * @param lon
	 *            breddegraden til senter av kartet
	 * @param zoom
	 *            verdien av zoom
	 * @param x
	 *            antall pixler alt skal flyttes i x retning
	 * @param y
	 *            antall pixler alt skal flyttes i y retning
	 */
	public void changeMySheepButtonBounds(double lat, double lon, int zoom, int x, int y, int imageLength) {
		double numw = 0.0275;
		double numh = 0.0123;

		ArrayList<MySheepButton> list;
		if (state == LOG) {
			list = logSheepButtons;
		} else {
			list = mySheepButtons;
		}
		for (MySheepButton b : list) {
			MyPoint p = null;
			try {
				if (state == LOG) {
					p = b.getMyPoint();
				} else {
					if (b.getSheep() != null) {
						p = getLocationPoint(b.getSheep().getLocation().getPosition());
					} else {
						continue;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (p != null) {
				int dx = (int) (-(lon - p.getLongitude()) * imageLength / numw) - x;
				int dy = (int) ((lat - p.getLatitude()) * imageLength / numh) - y;
				b.setLocation(width / 3 + dx + imageLength / 2, dy + imageLength / 2);
				b.repaint();
			}
		}
	}

	/**
	 * Metode som setter gitt liste av sauer synlige/usynlige.
	 * 
	 * @param list
	 *            listen av MySheepButton som skal settes synlige/usynlige
	 * @param bool
	 *            true for synlig
	 */
	private void changeMySheepButtonDrawBool(ArrayList<MySheepButton> list, Boolean bool) {
		for (MySheepButton b : list) {
			b.setDraw(bool);
			b.repaint();
		}
		myMap.repaint();
	}

	/**
	 * Metode som setter alle mySheepButtons til en gitt farge
	 * 
	 * @param color
	 *            ny farge til knappene
	 */
	public void changeMySheepButtonColor(Color color) {
		for (MySheepButton b : mySheepButtons) {
			b.setColor(color);
		}
	}

	/**
	 * Endre fargen paa saue knappene
	 * 
	 * @param num
	 *            Tall for forskjellige farger
	 */
	private void changeMySheepButtonColors(int num) {
		if (num == 1) {
			for (MySheepButton b : mySheepButtons) {
				Sheep s = b.getSheep();
				if (s.getGender() == 'm') {
					b.setColor(Color.BLUE); // m
				} else {
					b.setColor(Color.MAGENTA); // f
				}
			}
		} else if (num == 2) {
			for (MySheepButton b : mySheepButtons) {
				b.setColor(Color.WHITE);
			}
		}
	}

	/**
	 * Setter inn informasjon fra en sau inn i lwEdit feltene. Setter fargen til
	 * gitt sau lik blaa og de andre hvite, og setter gitt sau trykket paa i
	 * sauelisten.
	 * 
	 * @param s
	 *            Sheep
	 */
	public void setLwEditSheep(Sheep s) {
		if (s == null) {

		} else {
			lwEditIdField.setText("" + s.getId());
			lwEditNameField.setText("" + s.getName());
			lwEditWeightField.setText("" + s.getWeight());
			lwEditOwnerField.setText("" + user.getEmail());
			lwEditShepherdField.setText("" + s.getShepherd());
			lwEditGenderField.setText("" + s.getGender());
			lwEditHeartrateField.setText("" + s.getHeartrate());
			lwEditTemperatureField.setText("" + s.getTemperature());
			lwEditBirthyearField.setText("" + s.getBirthyear());
			for (MySheepButton b : mySheepButtons) {
				if (b.getSheep().equals(s)) {
					b.setColor(Color.BLUE);
				} else {
					if (!b.isAttacked()) {
						b.setColor(Color.WHITE);
					}
				}
			}
			for (int i = 0; i < sheepListModel.size(); i++) {
				if (Integer.parseInt(((String) sheepListModel.getElementAt(i)).split(" ")[0]) == s.getId()) {
					sheepList.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	/**
	 * Setter inn informasjon fra brukeren inn i editUser feltene.
	 */
	private void setEditUser() {
		editPhoneField.setText(user.getPhoneNr());
		editEmailField.setText(user.getEmail());
		((MyBorder) editPasswordField.getBorder()).changeColor(valid);
		((MyBorder) editPhoneField.getBorder()).changeColor(valid);
		((MyBorder) editEmailField.getBorder()).changeColor(valid);
	}

	/**
	 * Setter et objekt i sheepList
	 * 
	 * @param sheep
	 */
	public void setListSelection(Sheep sheep) {
		for (int i = 0; i < sheepListModel.size(); i++) {
			if (Integer.parseInt(((String) sheepListModel.getElementAt(i)).split(" ")[0]) == sheep.getId()) {
				sheepList.setSelectedIndex(i);
				break;
			}
		}
	}

	/**
	 * Oppdaterer listen over sauer
	 */
	private void updateSheepList() {
		sheepListModel.clear();
		for (MySheepButton s : mySheepButtons) {
			sheepListModel.addElement(s.toString());
		}
	}

	/**
	 * Oppdaterer log listen fra en sau
	 * 
	 * @param sheep
	 */
	private void updateLogList(Sheep sheep) {
		try {
			logListModel.clear();
			logIdButton.setText("Sheep ID: " + sheep.getId());
			sheepLocs = user.getLastLocations(sheep.getId());
			int counter = 0;
			for (SheepLocation s : sheepLocs) {
				logSheepButtons.get(counter).setMyPoint(getLocationPoint(s.getPosition()));
				counter++;
			}

			for (SheepLocation s : sheepLocs) {
				String lat = String.format("%.6g%n", Double.parseDouble(s.getLatitude()));
				String lon = String.format("%.6g%n", Double.parseDouble(s.getLongitude()));
				String date = s.getDate();
				logListModel.addElement(date + " - " + lat.substring(0, 7) + "," + lon.substring(0, 7));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prover å gjore om en gitt string til et objekt MyPoint
	 * 
	 * @param arg
	 *            String
	 * @return myPoint
	 * @throws Exception
	 *             mislykket omgjoring fra String til double
	 */
	private MyPoint getLocationPoint(String arg) throws Exception {
		String[] list = arg.split(",");
		try {
			return new MyPoint(Double.parseDouble(list[0]), Double.parseDouble(list[1]));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Tegner alle sauene paa nytt
	 */
	public void repaintMySheepButtons() {
		if (state == LOG) {
			for (MySheepButton b : logSheepButtons) {
				b.repaint();
			}
		} else {
			for (MySheepButton b : mySheepButtons) {
				b.repaint();
			}
		}
	}

	/**
	 * Resetter kartet og lager nye sauer utifra nye lister
	 */
	private void resetMap() {
		myMap.setUser(null);
		changeMySheepButtonDrawBool(mySheepButtons, false);
		changeMySheepButtonDrawBool(logSheepButtons, false);

		addMySheepButtons();
		addMySheepLogButtons();
		createMap();
		myMap.setUser(user);
		myMap.home();
		updateSheepList();
		setAttackedSheeps();
	}

	/**
	 * Setter alle saueknappene angrepet hvis de er det fra før av.
	 */
	private void setAttackedSheeps() {
		try {
			ArrayList<String> list = user.getAttackedSheep();
			if (!list.isEmpty()) {
				for (MySheepButton s : mySheepButtons) {
					for (String t : list) {
						if (s.getSheep().getId() == Integer.parseInt(t)) {
							s.attackSheep(true);
						}
					}
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Lager en FocusListener som lytter til naar en bruker trykker paa
	 * tekstbokser eller forlater dem
	 */
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
				// sjekker om input i feltene stemmer og setter farge deretter
				if (component instanceof JTextField) {
					JTextField fc = (JTextField) component;
					String name = fc.getName();
					String input = null;
					if (name.equals("regNameField")) {
						try {
							input = regNameField.getText();
							if (!input.equals("")) {
								tUser.setFirstName(input);
								regNameField.setText(input.substring(0, 1).toUpperCase()
										+ input.substring(1).toLowerCase());
								((MyBorder) regNameField.getBorder()).changeColor(valid);
							}
						} catch (Exception exc) {
							((MyBorder) regNameField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("regLNameField")) {
						try {
							input = regLNameField.getText();
							if (!input.equals("")) {
								tUser.setLastName(input);
								regLNameField.setText(input.substring(0, 1).toUpperCase()
										+ input.substring(1).toLowerCase());
								((MyBorder) regLNameField.getBorder()).changeColor(valid);
							}
						} catch (Exception exc) {
							((MyBorder) regLNameField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("regEmailField")) {
						try {
							input = regEmailField.getText();
							if (!input.equals("")) {
								tUser.setEmail(input);
								((MyBorder) regEmailField.getBorder()).changeColor(valid);
							}
						} catch (Exception exc) {
							((MyBorder) regEmailField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("regPhoneField")) {
						try {
							input = regPhoneField.getText();
							if (!input.equals("")) {
								tUser.setPhoneNr(input);
								((MyBorder) regPhoneField.getBorder()).changeColor(valid);
							}
						} catch (Exception exc) {
							((MyBorder) regPhoneField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("regLatitudeField")) {
						try {
							input = regLatitudeField.getText();
							if (!input.equals("")) {
								int lat = Integer.parseInt(input);
								if (lat >= -90 && lat <= 90) {
									tUser.setLatitude(input);
									((MyBorder) regLatitudeField.getBorder()).changeColor(valid);
									if (((MyBorder) regLongitudeField.getBorder()).getColor().equals(valid)) {
										rightPanel.changeToGoogleImage(tUser.getLatitudeDouble(),
												tUser.getLongitudeDouble());
									}
								}
							}
						} catch (Exception exc) {
							((MyBorder) regLatitudeField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("regLongitudeField")) {
						try {
							input = regLongitudeField.getText();
							if (!input.equals("")) {
								int lon = Integer.parseInt(input);
								if (lon >= -180 && lon <= 180) {
									tUser.setLongitude(input);
									((MyBorder) regLongitudeField.getBorder()).changeColor(valid);
									if (((MyBorder) regLatitudeField.getBorder()).getColor().equals(valid)) {
										rightPanel.changeToGoogleImage(tUser.getLatitudeDouble(),
												tUser.getLongitudeDouble());
									}
								}
							}
						} catch (Exception exc) {
							((MyBorder) regLongitudeField.getBorder()).changeColor(invalid);
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
								((MyBorder) regPwField.getBorder()).changeColor(valid);
							}
						} catch (Exception exc) {
							((MyBorder) regPwField.getBorder()).changeColor(invalid);
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
								((MyBorder) regPwField2.getBorder()).changeColor(valid);
							} else {
								((MyBorder) regPwField2.getBorder()).changeColor(invalid);
								return;
							}
						} catch (Exception exc) {
							((MyBorder) regPwField.getBorder()).changeColor(invalid);
							((MyBorder) regPwField2.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("regSheepNameField")) {
						try {
							input = regSheepNameField.getText();
							tSheep.setName(input);
							((MyBorder) regSheepNameField.getBorder()).changeColor(valid);
						} catch (Exception exc) {
							((MyBorder) regSheepNameField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("regAgeField")) {
						try {
							input = regAgeField.getText();
							tSheep.setBirthyear(Integer.parseInt(input));
							((MyBorder) regAgeField.getBorder()).changeColor(valid);
						} catch (Exception exc) {
							((MyBorder) regAgeField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("regWeightField")) {
						try {
							input = regWeightField.getText();
							tSheep.setWeight(Integer.parseInt(input));
							((MyBorder) regWeightField.getBorder()).changeColor(valid);
						} catch (Exception exc) {
							((MyBorder) regWeightField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("regSexField")) {
						try {
							input = regSexField.getText();
							if (input.charAt(0) == 'm' || input.charAt(0) == 'f') {
								((MyBorder) regSexField.getBorder()).changeColor(valid);
							} else {
								((MyBorder) regSexField.getBorder()).changeColor(invalid);
							}
						} catch (Exception exc) {
							((MyBorder) regSexField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("regShepherdField")) {
						try {
							input = regShepherdField.getText();
							tSheep.setShepherd(input);
							((MyBorder) regShepherdField.getBorder()).changeColor(valid);
						} catch (Exception exc) {
							((MyBorder) regShepherdField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("editPasswordField")) {
						try {
							input = editPasswordField.getText();
							if (input.equals("")) {
								((MyBorder) editPasswordField.getBorder()).changeColor(valid);
								return;
							}
							tUser.setPassword(input);
							((MyBorder) editPasswordField.getBorder()).changeColor(valid);
						} catch (Exception exc) {
							((MyBorder) editPasswordField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("editPhoneField")) {
						try {
							input = editPhoneField.getText();
							tUser.setPhoneNr(input);
							((MyBorder) editPhoneField.getBorder()).changeColor(valid);
						} catch (Exception exc) {
							((MyBorder) editPhoneField.getBorder()).changeColor(invalid);
						}
					} else if (name.equals("editEmailField")) {
						try {
							input = editEmailField.getText();
							tUser.setEmail(input);
							((MyBorder) editEmailField.getBorder()).changeColor(valid);
						} catch (Exception exc) {
							((MyBorder) editEmailField.getBorder()).changeColor(invalid);
						}
					}
				}
			}
		};
	}

	/**
	 * Egen version av ActionListener klasse som haandterer knappetrykking og
	 * naar "enter" blir trykket paa mens en tekst boks er aktivert.
	 * 
	 * @author andreas
	 * 
	 */
	private class MyListener implements ActionListener {
		private int counter = 0;

		@Override
		public void actionPerformed(ActionEvent arg) {

			if (arg.getSource() instanceof JButton) {
				JButton pressed = (JButton) arg.getSource();
				String text = pressed.getText();
				// finner handling til hver knapp
				if (text.equals("Register")) {
					// sjekker om en er i register menyen og skal registrere seg
					if (state == 2) {
						loginInfo.setText("");
						if (registerUser()) {
							loginInfo.setForeground(Color.GREEN);
							loginInfo.setText("User registered!");
						} else {
							loginInfo.setForeground(Color.RED);
							loginInfo.setText("Failed to register user!");
						}
						changeToRegisterInterface(false);
						changeToLoginInterface(true);
					} else {
						changeToLoginInterface(false);
						changeToRegisterInterface(true);
					}
				} else if (text.equals("Login")) {
					// sjekker hvis man er i login interface og trykker login
					if (state == 1) {
						login();
						loginInfo.setText("");
					} else {
						changeToStartInterface(false);
						changeToLoginInterface(true);
					}
				} else if (text.equals("Exit")) {
					System.exit(0);
					ClientConnection.close();
				} else if (text.equals("Back")) {
					// state holder styr paa hvor man er, saa programet vet
					// hvor en skal sendes tilbake til
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
					} else if (state == LOG) {
						changeToLogInterface(false);
						changeToListInterface(true);
						changeMySheepButtonDrawBool(mySheepButtons, true);
						changeMySheepButtonDrawBool(logSheepButtons, false);
					}
				} else if (text.equals("Forgot")) {
					changeToLoginInterface(false);
					changeToForgotInterface(true);
				} else if (text.equals("Send")) {
					// sjekker om en er i forgot interface og trykker send ->
					if (state == 3) {
						if (sendEmail()) {
							changeToForgotInterface(false);
							changeToLoginInterface(true);
						} else {
						}
					}
				} else if (text.equals("Register sheep")) {
					if (state == SHEEPREG) {
						if (registerSheep()) {
							mainInfoLabel.setForeground(Color.GREEN);
							mainInfoLabel.setText("Sheep registred with ID: "
									+ user.getSheepList().get(user.getSheepList().size() - 1).getId());
						} else {
							mainInfoLabel.setForeground(Color.RED);
							mainInfoLabel.setText("Something went wrong with registering sheep.");
						}
					} else {
						changeToMainInterface(false);
						changeToRegSheepInterface(true);
					}
				} else if (text.equals("Sheeplist")) {
					changeToMainInterface(false);
					changeToListInterface(true);
				} else if (text.equals("Search for sheep")) {
					if (state == SEARCH) {
						searchSheep();
					} else {
						changeToMainInterface(false);
						changeToSearchInterface(true);
					}
				} else if (text.equals("Edit user")) {
					changeToMainInterface(false);
					changeToEditInterface(true);
					setEditUser();
				} else if (text.equals("Logs")) {
					changeToMainInterface(false);
					changeToLogInterface(true);
				} else if (text.equals("Home")) {
					myMap.home();
				} else if (text.equals("Log out")) {
					changeToMainInterface(false);
					changeToStartInterface(true);
					logout();
				} else if (text.equals("Update")) {
					if (editUser()) {
						mainInfoLabel.setForeground(Color.GREEN);
						mainInfoLabel.setText("User edited successfully");
					} else {
						mainInfoLabel.setForeground(Color.RED);
						mainInfoLabel.setText("Failed to edit user");
					}
					changeToEditInterface(false);
					changeToMainInterface(true);
				} else if (text.equals("Sort by color")) {
					counter++;
					if (counter % 2 == 1) {
						changeMySheepButtonColors(1);
					} else if (counter % 2 == 0) {
						changeMySheepButtonColors(2);
					}
				} else if (text.equals("Attack sheep")) {
					if (listSelected != null) {
						listInfo.setText("");
						for (MySheepButton b : mySheepButtons) {
							if (b.equals(listSelected)) {
								b.attackSheep(true);
								user.attackSheep(listSelected.getSheep().getId(), user.getEmail());
								try {
									MyPoint p = getLocationPoint(listSelected.getSheep().getLocation().getPosition());
									myMap.centerInOnSheep(p.getLatitude(), p.getLongitude());
									listInfo.setForeground(Color.GREEN);
									listInfo.setText(listSelected.getSheep().getId() + " has been attacked!");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} else {
						listInfo.setForeground(Color.RED);
						listInfo.setText("Select a sheep.");
					}
				} else if (text.equals("History")) {
					if (state == LIST) {
						listInfo.setText("");
						if (listSelected != null) {
							for (MySheepButton b : mySheepButtons) {
								if (b.equals(listSelected)) {
									updateLogList(b.getSheep());
									changeToListInterface(false);
									changeToLogInterface(true);
									changeMySheepButtonDrawBool(mySheepButtons, false);
									changeMySheepButtonDrawBool(logSheepButtons, true);
								}
							}
						} else {
							listInfo.setForeground(Color.RED);
							listInfo.setText("Select a sheep.");
						}
					} else if (state == SEARCH) {
						updateLogList(editSheep);
						changeToSearchInterface(false);
						changeToLogInterface(true);
						changeMySheepButtonDrawBool(mySheepButtons, false);
						changeMySheepButtonDrawBool(logSheepButtons, true);
						sheepyLog.setVisible(false);
					}
				} else if (text.equals("Delete")) {
					if (listSelected != null) {
						listInfo.setText("");
						Sheep var = listSelected.getSheep();
						try {
							user.deleteSheep(listSelected.getSheep());
							resetMap();
						} catch (Exception e) {
							listInfo.setForeground(Color.GREEN);
							listInfo.setText(var.getId() + " has been deleted.");
						}
					} else {
						listInfo.setForeground(Color.RED);
						listInfo.setText("Select a sheep.");
					}
				}
			} else if (arg.getSource() instanceof JTextField) {
				JTextField pressed = (JTextField) arg.getSource();
				String text = pressed.getName();
				if (state == 1) {
					if (text.equals("unField")) {
						pwField.requestFocus();
					} else if (text.equals("pwField")) {
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

	// Variabler for swing komponenter
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
	private JLabel loginInfo;

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
	private JLabel mainInfoLabel;
	private MyMap myMap;

	// search components
	private JLabel searchLabel;
	private JTextField searchField;

	// edit components
	private JTextField editPasswordField;
	private JTextField editPhoneField;
	private JTextField editEmailField;
	private JButton updateButton;

	// list components
	private JButton sheepy;
	private JButton sheepyAttack;
	private JButton sheepyLog;
	private JButton sheepyDel;
	private JList sheepList;
	private JScrollPane sheepScrollPane;
	private DefaultListModel sheepListModel;
	private JLabel listInfo;

	// register sheep components
	private JTextField regSheepNameField;
	private JTextField regAgeField;
	private JTextField regWeightField;
	private JTextField regSexField;
	private JTextField regShepherdField;

	// log over sauene
	private JList logList;
	private JScrollPane logScrollPane;
	private DefaultListModel logListModel;
	private JButton logDateButton;
	private JButton logCoordsButton;
	private JButton logIdButton;

	// Sheep button list
	private ArrayList<MySheepButton> mySheepButtons;
	private ArrayList<MySheepButton> logSheepButtons;

	// lower panel
	private JButton lwEditButton;

	private JLabel lwEditLabel;
	private JTextField lwEditIdField;
	private JTextField lwEditNameField;
	private JTextField lwEditWeightField;
	private JTextField lwEditOwnerField;
	private JTextField lwEditShepherdField;
	private JTextField lwEditGenderField;
	private JTextField lwEditHeartrateField;
	private JTextField lwEditTemperatureField;
	private JTextField lwEditBirthyearField;

	private JLabel lwEditIdLabel;
	private JLabel lwEditNameLabel;
	private JLabel lwEditWeightLabel;
	private JLabel lwEditOwnerLabel;
	private JLabel lwEditShepherdLabel;
	private JLabel lwEditGenderLabel;
	private JLabel lwEditHeartrateLabel;
	private JLabel lwEditTemperatureLabel;
	private JLabel lwEditBirthyearLabel;

	// Liste over de ulike komponentene
	private ArrayList<JComponent> startComps;
	private ArrayList<JComponent> loginComps;
	private ArrayList<JComponent> registerComps;
	private ArrayList<JComponent> forgotComps;
	private ArrayList<JComponent> mainComps;
	private ArrayList<JComponent> searchComps;
	private ArrayList<JComponent> editComps;
	private ArrayList<JComponent> listComps;
	private ArrayList<JComponent> regSheepComps;
	private ArrayList<JComponent> lwEditComps;
	private ArrayList<JComponent> logComps;

	private ArrayList<JComponent> fieldComps;
}
