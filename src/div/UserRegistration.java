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


        //Lager en foresp0rsel til server.
        //retiningslinjer for kommunikasjon med server vil til en hver tid ligge i server.ComProtocol klassen
        String query = email + "||" + firstName + "||" + lastName + "||" + phoneNr + "||" + password  + "||" + "63.43,10.39";

        //sender foresp0rselen til serveren og faar tilbake respons
        String serverResponse = ClientConnection.sendServerQuery("registeruser", query);

        //sjekke feilmeldinger
        if (!serverResponse.equals("reguser success")) {
            ClientConnection.handleError(serverResponse);
            System.out.println("Error. Can't register user");
        }

	}
	
	public void deleteUser(User user) {
		users.remove(user);
	}	

	public User login(String email, String password) {

        //Lager en foresp0rsel til server.
        String query = email + "||" + password;

        //sender foresp0rselen til serveren og faar tilbake respons
        String serverResponse;
        try {
            serverResponse = ClientConnection.sendServerQuery("login", query);
        } catch (IOException e) {
            serverResponse = "err";
        }

        //sjekke at melding ble mottatt
        if (!serverResponse.equals("login success")) {
            ClientConnection.handleError(serverResponse);
            return null;
        } else {
            return null;
        }
    }
	
	public ArrayList<User> getUsers() {
		return users;
	}
}
