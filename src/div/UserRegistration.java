package div;

import java.io.IOException;
import java.util.ArrayList;
import database.DatabaseConnector;


/**
 * Klassen h�ndterer registrering, sletting og innlogging av brukere. 
 * @author Ragnhild
 *
 */
public class UserRegistration {
	
	private  static ArrayList<User> users = new ArrayList<User>();

	public static void registerUser(String firstName, String lastName, String email, String password, String phoneNr, String location) throws Exception {
		User user = new User(firstName, lastName, email, phoneNr, "69.10,10.30");
		users.add(user);

        //Maa gaa gjennom server, her er ett eksempel.

        //Lager en foresp0rsel til server.
        //retiningslinjer for kommunikasjon med server vil til en hver tid ligge i server.ComProtocol klassen
        String query = email + "||" + firstName + "||" + lastName + "||" + phoneNr + "||" + password  + "||" + "63.43,10.39";

        //sender foresp0rselen til serveren og faar tilbake respons
        String serverRespons = ClientConnection.sendServerQuery("registeruser", query);

        //sjekke at melding ble mottatt
        if (serverRespons.equals("err")) {
            System.out.println("Error. Can't register user");
        }

	}
	
	public void deleteUser(User user) {
		users.remove(user);
	}	

	public User login(String email, String password) {

        //Maa gaa gjennom server, her er ett eksempel.

        //Lager en foresp0rsel til server.
        //retiningslinjer for kommunikasjon med server vil til en hver tid ligge i server.ComProtocol klassen
        String query = email + "||" + password;

        //sender foresp0rselen til serveren og faar tilbake respons
        String serverRespons = null;
        try {
            serverRespons = ClientConnection.sendServerQuery("login", query);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sjekke at melding ble mottatt
        if (serverRespons.equals("err")) {
            System.out.println("Error. Can't register user");
        }
		return null;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}
}
