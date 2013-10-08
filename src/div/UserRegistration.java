package div;

import java.util.ArrayList;


/**
 * Klassen h�ndterer registrering, sletting og innlogging av brukere. 
 * @author Ragnhild
 *
 */
public class UserRegistration {


	public static boolean registerUser(String firstName, String lastName, String email, String password, String phoneNr, String location)  {

        //Lager en foresp0rsel til server.
        //retiningslinjer for kommunikasjon med server vil til en hver tid ligge i server.ComProtocol klassen
        String query = email + "||" + firstName + "||" + lastName + "||" + phoneNr + "||" + password  + "||" + "63.43,10.39";

        //sender foresp0rselen til serveren og faar tilbake respons
        String serverResponse = ClientConnection.sendServerQuery("registeruser", query);

        //sjekke feilmeldinger
        if (!serverResponse.equals("reguser success")) {
            ClientConnection.handleError(serverResponse);
            System.out.println("Error. Can't register user");
            return false;
        } else {
            return true;
        }
	}

	public User login(String email, String password) {

        //Lager en foresp0rsel til server.
        String query = email + "||" + password;

        //sender foresp0rselen til serveren og faar tilbake respons
        String serverResponse = ClientConnection.sendServerQuery("login", query);

        //sjekke at melding ble mottatt
        if (!serverResponse.equals("login success")) {
            ClientConnection.handleError(serverResponse);
            return null;
        } else {
            return null;
        }
    }
	
	public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
		return users;
	}

    public void deleteUser(User user) {
        //ikke ferdig
    }
}
