package div;

import java.io.Serializable;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * User-klassen representerer en brukers profil. Lagrer og validerer generell
 * info om brukeren, inkludert email og passord som er nodvendig for
 * innlogging.
 * 
 * @author Ragnhild
 * 
 */
public class User implements Serializable {

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String phoneNr;
	private String latitude;
	private String longitude;

	private SheepRegistration sheepReg;

	public User(String firstName, String lastName, String email,
			String phoneNr, String location) throws Exception {
		sheepReg = new SheepRegistration();

		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		// setPassword(password);
		setPhoneNr(phoneNr);
		setPosition(location);
	}

	public User() {

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) throws Exception {
		if (nameIsValid(name)) {
			this.firstName = name;
		} else {
			throw new Exception("First name is not valid");
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) throws Exception {
		if (nameIsValid(lastName)) {
			this.lastName = lastName;
		} else {
			throw new Exception("Last name is not valid");
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws Exception {
		if (emailAddressIsValid(email)) {
			this.email = email;
		} else {
			throw new Exception("Email is not valid");
		}

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws Exception {
		if (passwordIsValid(password)) {
			this.password = password;
		} else {
			throw new Exception("Password is not valid");
		}
	}

	public String getPhoneNr() {
		return phoneNr;
	}

	public void setPhoneNr(String phoneNr) throws Exception {
		if (phoneNumberIsValid(phoneNr)) {
			this.phoneNr = phoneNr;
		} else {
			throw new Exception("Phone number not valid");
		}

	}

	/**
	 * Validerer navn. True dersom det bare inneholder bokstaver og mellomrom. 
	 */
	private boolean nameIsValid(String name) {
		if (name.matches("^[a-zA-Z ]+$")) {
			return true;
		}
		return false;
	}

	/**
	 * Validerer telefonnummer. Må minst være 8 tall. 
	 */
	private boolean phoneNumberIsValid(String phoneNr) {
		if (phoneNr.length() < 8) {
			return false;
		}
		for (int i = 0; i < phoneNr.length(); i++) {
			Character c = phoneNr.charAt(i);
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Validerer emailadresse. 
	 */
	public boolean emailAddressIsValid(String email) {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return email.matches(emailPattern);

	}

	/**
	 * Validerer passord. Returnerer true dersom det bare inneholder bokstaver, tall og mellomrom. 
	 */
	private boolean passwordIsValid(String pass) {
		if (pass.matches("^[a-zA-Z0-9 ]+$")) {
			return true;
		}
		return false;
	}

	public double getLongitudeDouble() {
		return longlatIsValid(this.longitude);
	}

	public void setLongitude(String longitude) throws Exception {

		if (longlatIsValid(longitude) != 0) {
			this.longitude = longitude;
		} else {
			throw new Exception("Coordinate is not valid");
		}
	}

	private double longlatIsValid(String longlat) {
		double tall = 0;
		try {
			tall = Double.parseDouble(longlat);
			return tall;
		} catch (Exception e) {
			return 0;
		}

	}

	public double getLatitudeDouble() {
		return longlatIsValid(this.latitude);

	}

	public void setLatitude(String latitude) throws Exception {
		if (longlatIsValid(latitude) != 0) {
			this.latitude = latitude;
		} else {
			throw new Exception("Coordinate is not valid");
		}
	}

	public String getPosition() {
		return latitude + longitude;
	}

	public void setPosition(String position) {
		String[] positionA = position.split(",");
		latitude = positionA[0];
		longitude = positionA[1];
	}

	/**
	 * Registrer en sau på denne brukeren. 
	 */
	public void registerSheep(String name, String age, String weight,
			String gender, String owner, String shepherd)
			throws NumberFormatException, Exception {
		sheepReg.registerSheep(name, Integer.parseInt(age),
				Integer.parseInt(weight), gender.charAt(0), owner, shepherd, latitude, longitude);
	}
	
	public void editSheep(int id, String name, String owner, String shepherd, char gender, int weight, int birthyear) throws Exception {
		sheepReg.editSheep(id, name, owner, shepherd, gender, weight, birthyear);
	}

	public boolean deleteSheep(Sheep sheep) {
		return sheepReg.deleteSheep(sheep);
	}

	public void updateSheepList() throws Exception {
		sheepReg.updateSheepList(this.email);
	}

	public ArrayList<Sheep> getSheepList() {
		return sheepReg.getSheepList();
	}
	
	public void attackSheep(int id) {
		sheepReg.attackSheep(id);
	}
	
	public ArrayList<SheepLocation> getLastLocations(int id) throws Exception {
		return sheepReg.getLastLocations(id);
	}
	


}
