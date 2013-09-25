package div;

/**
 * User-klassen representerer en brukers profil. 
 * Lagrer og validerer generell info om brukeren, inkludert email og passord som er nødvendig for innlogging. 
 * @author Ragnhild
 *
 */
public class User {

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String phoneNr;
	
	public User(String firstName, String lastName, String email,
			String password, String phoneNr) throws Exception {
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setPassword(password);
		setPhoneNr(phoneNr);
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String name) throws Exception {
		if(nameIsValid(name)) {
			this.firstName = name;
		} else {
			throw new Exception("First name is not valid");
		}
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) throws Exception {
		if(nameIsValid(lastName)) {
			this.lastName = lastName;
		} else {
			throw new Exception("Last name is not valid");
		}
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) throws Exception {
		if(emailAddressIsValid(email)) {
			this.email = email;
		}
		else {
			throw new Exception("Email is not valid");
		}
		
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) throws Exception {
		if(passwordIsValid(password)) {
			this.password = password;
		} else {
			throw new Exception("Password is not valid");
		}
	}
	public String getPhoneNr() {
		return phoneNr;
	}
	public void setPhoneNr(String phoneNr) throws Exception {
		if(PhoneNumberIsValid(phoneNr)) {
			this.phoneNr = phoneNr;
		} else {
			throw new Exception("Phone number not valid");
		}
		
	}
	
	private boolean nameIsValid(String name) {
		if(name.matches("^[a-zA-Z ]+$")) {
			return true;
		}
		return false;
	}
	
	private boolean PhoneNumberIsValid(String phoneNr) {
		if(phoneNr.length() < 8) {
			return false;
		}
		for (int i = 0; i < phoneNr.length(); i++) {
			Character c = phoneNr.charAt(i);
			if(!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean emailAddressIsValid(String email) {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return email.matches(emailPattern);
		
	}
	
	private boolean passwordIsValid(String pass) {
		if(pass.matches("^[a-zA-Z0-9 ]+$")) {
			return true;
		}
		return false;
	}
	



}
