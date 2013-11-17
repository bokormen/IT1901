package div;

import java.io.Serializable;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * User-klassen representerer en brukers profil. Lagrer og validerer generell
 * info om brukeren, inkludert email og passord som er nodvendig for innlogging.
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

	public User(String firstName, String lastName, String email, String phoneNr, String location) throws Exception {
		sheepReg = new SheepRegistration();

		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setPhoneNr(phoneNr);
		setPosition(location);
	}

	public User() {

	}

	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setter fornavnet til brukeren. 
	 * @param name Fornavnet. Kan bare inneholde bokstaver og mellomrom. 
	 * @throws Exception Navnet er ikke valid
	 */
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

	/**
	 * Setter etternavnet til brukeren. 
	 * @param lastName Etternavnet. Kan bare inneholde bokstaver og mellomrom. 
	 * @throws Exception Navnet er ikke valid. 
	 */
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

	/**
	 * Setter epostaddressen til brukeren. 
	 * @param email Epostadressen som skal settes. Ma være på formen eksempel@eksempel.eks
	 * @throws Exception Epostadressen er ikke valid. 
	 */
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
	
	/**
	 * Setter passordet til brukeren. 
	 * @param password Passordet kan bare inneholde bokstaver og tall. 
	 * @throws Exception Passordet er ikke valid. 
	 */
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

	/**
	 * Setter telefonnummeret til brukeren.
	 * @param phoneNr Telefonnummeret ma vaere minst 8 tall. 
	 * @throws Exception Telefonnummeret er ikke valid. 
	 */
	public void setPhoneNr(String phoneNr) throws Exception {
		if (phoneNumberIsValid(phoneNr)) {
			this.phoneNr = phoneNr;
		} else {
			throw new Exception("Phone number not valid");
		}

	}

	/**
	 * Validerer navn.
	 * @param name Navnet som skal valideres. Kan bare besta av bokstaver og mellomrom. 
	 * @return True dersom navnet er valid, false ellers. 
	 */
	private boolean nameIsValid(String name) {
		if (name.matches("^[a-zA-Z ]+$")) {
			return true;
		}
		return false;
	}

	/**
	 * Validerer telefonnummer. 
	 * @param phoneNr Telefonnummeret ma vaere minst 8 tall. 
	 * @return Om telefonnummeret er valid eller ikke. 
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
	 * Validerer epostaddresse. 
	 * @param email Epostadressen ma vaere pa formen eksempel@eksempel.eks
	 * @return Om epostaddressen er valid eller ikke. 
	 */
	public boolean emailAddressIsValid(String email) {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return email.matches(emailPattern);

	}

	/**
	 * Validerer passord. 
	 * @param pass Passordet til brukeren. Kan bare besta av bokstaver, tall og mellomrom. 
	 * @return Om passordet er valid eller ikke. 
	 */
	private boolean passwordIsValid(String pass) {
		if (pass.matches("^[a-zA-Z0-9 ]+$")) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return Longitude-verdien til garden til brukeren. 
	 */
	public double getLongitudeDouble() {
		return longlatIsValid(this.longitude);
	}

	/**
	 * Setter longitude-verdien til brukeren. 
	 * @param longitude Longitude-verdien til garden. Ma være en double. 
	 * @throws Exception Longitude-verdien er ikke en double. 
	 */
	public void setLongitude(String longitude) throws Exception {

		if (longlatIsValid(longitude) != 0) {
			this.longitude = longitude;
		} else {
			throw new Exception("Coordinate is not valid");
		}
	}

	/**
	 * Gjor en string om til en double dersom det er mulig. 
	 * @param longlat Stringen som skal gjores om. 
	 * @return verdien longlat som en double. 0 dersom det ikke er mulig. 
	 */
	private double longlatIsValid(String longlat) {
		double tall = 0;
		try {
			tall = Double.parseDouble(longlat);
			return tall;
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * 
	 * @return Longlat-verdien til brukeren som en double. 
	 */
	public double getLatitudeDouble() {
		return longlatIsValid(this.latitude);

	}

	/**
	 * Setter latituden til garden til brukeren. 
	 * @param latitude Latitude-verdien må være et tall. 
	 * @throws Exception Latitude-verdien ikke valid. 
	 */
	public void setLatitude(String latitude) throws Exception {
		if (longlatIsValid(latitude) != 0) {
			this.latitude = latitude;
		} else {
			throw new Exception("Coordinate is not valid");
		}
	}

	/**
	 * 
	 * @return Posisjonen til garden til brukeren på formen latitude,longitude. 
	 */
	public String getPosition() {
		return latitude + "," + longitude;
	}

	/**
	 * Setter posisjonen til garden til brukeren. 
	 * @param position Posisjonen til garden ma være pa formen latitude,longitude. 
	 */
	public void setPosition(String position) {
		String[] positionA = position.split(",");
		latitude = positionA[0];
		longitude = positionA[1];
	}

	/**
	 * Registrerer en sau. Setter posisjonen til sauen til gardens posisjon. 
	 * @param name Navnet til sauen
	 * @param birthyear Aret sauen er fodt. Ma vaere over 1980.
	 * @param weight Vekten til sauen. Ma vaere positiv.
	 * @param gender Kjonnet til sauen. Ma begynne med m eller f.  
	 * @param owner Epostaddressen til eieren til sauen. 
	 * @param shepherd Epostaddressen til gjeteren til sauen. 
	 * @throws NumberFormatException Fodselsaret eller vekten er ikke et tall.  
	 * @throws Exception Feil under registrering. 
	 */
	public void registerSheep(String name, String birthyear, String weight, String gender, String owner, String shepherd)
			throws NumberFormatException, Exception {
		sheepReg.registerSheep(name, Integer.parseInt(birthyear), Integer.parseInt(weight), gender.charAt(0), owner,
				shepherd, latitude, longitude);
	}

	/**
	 * Endrer informasjonen til en sau. 
	 * @param id Id til sau. 
	 * @param name Navn sauen skal ha. 
	 * @param owner Epostaddressen til eieren av sauen
	 * @param shepherd Endret epostaddresse til gjeter
	 * @param gender Endret kjonn til sau
	 * @param weight Endret vekt til sau
	 * @param birthyear Endret fodselsar til sau.
	 * @throws Exception Sau kan ikke endres.
	 */
	public void editSheep(int id, String name, String owner, String shepherd, char gender, int weight, int birthyear)
			throws Exception {
		sheepReg.editSheep(id, name, owner, shepherd, gender, weight, birthyear);
	}

	/**
	 * Slett sau. 
	 * @param sheep Sheep-objektet som skal slettes.
	 * @return Om sauen kunne slettes eller ikke. 
	 */
	public boolean deleteSheep(Sheep sheep) {
		return sheepReg.deleteSheep(sheep);
	}

	/**
	 * Oppdaterer listen over sauene som brukeren har. 
	 * @throws Exception Kunne ikke oppdatere listen.
	 */
	public void updateSheepList() throws Exception {
		sheepReg.updateSheepList(this.email);
	}

	/**
	 * 
	 * @return Liste over sauene til bonden. 
	 */
	public ArrayList<Sheep> getSheepList() {
		return sheepReg.getSheepList();
	}

	/**
	 * Simulerer angrep på en sau. Oppdaterer database og sender mail til eier. 
	 * @param id Id til sau. 
	 * @param user Epostaddressen til eieren av sau.
	 */
	public void attackSheep(int id, String user) {
		sheepReg.attackSheep(id, user);
	}
	/**
	 * Får tak i de siste posisjonene til en sau. 
	 * @param id Id til sau
	 * @return De 5 siste posisjonene til en sau, eller alle dersom det er mindre enn 5. 
	 * @throws Exception
	 */
	public ArrayList<SheepLocation> getLastLocations(int id) throws Exception {
		return sheepReg.getLastLocations(id);
	}

}
