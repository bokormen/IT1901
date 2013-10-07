package div;

import java.util.ArrayList;
import database.DatabaseConnector;


/**
 * Klassen h�ndterer registrering, sletting og innlogging av brukere. 
 * @author Ragnhild
 *
 */
public class UserRegistration {
	
	private ArrayList<User> users = new ArrayList<User>();

	public void registerUser(String firstName, String lastName, String email, String password, String phoneNr) throws Exception {
		User user = new User(firstName, lastName, email, password, phoneNr);
		users.add(user);

        //Maa gaa gjennom server, her er ett eksempel.

        //Lager en foresp0rsel til server.
        //retiningslinjer for kommunikasjon med server vil til en hver tid ligge i server.ComProtocol klassen
        String query = email + "||" + firstName + "||" + lastName + "||" + phoneNr + "||" + password  + "||" + "15.0,7.4";

        //sender foresp0rselen til serveren og faar tilbake respons
        String serverRespons = ClientConnection.sendServerQuery("registeruser", query);

        //sjekke at melding ble mottatt
        if (serverRespons.equals("err")) {
            //hvis noe gikk galt gj0r dette
        }

        //ferdig


        /*
		DatabaseConnector.open();
		DatabaseConnector.newUser(email, firstName, lastName, phoneNr, password, "15.0,7.4");
		DatabaseConnector.close();
		*/
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
