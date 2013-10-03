package div;

import java.util.ArrayList;

import database.DatabaseConnector;

/**
 * Klassen håndterer registrering, sletting og innlogging av brukere. 
 * @author Ragnhild
 *
 */
public class UserRegistration {
	
	private ArrayList<User> users = new ArrayList<User>();

	public void registerUser(String firstName, String lastName, String email, String password, String phoneNr) throws Exception {
		User user = new User(firstName, lastName, email, password, phoneNr);
		users.add(user);
		
		DatabaseConnector.open();
		DatabaseConnector.newUser(email, firstName + lastName, phoneNr, password, "15.0,7.4");
		DatabaseConnector.close();
	}
	
	public void deleteUser(User user) {
		users.remove(user);
	}	

	public User login(String email, String password) {
		for(User user : users) {
			if(email.equals(user.getEmail())) {
				if(password.equals(user.getPassword())) {
					return user;
				} else {
					System.out.println("Password not valid");
				}
			}
		}
		return null;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}
}
