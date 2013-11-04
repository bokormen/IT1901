package div;

import java.util.ArrayList;


/**
 * Klassen handterer registrering, sletting og innlogging av brukere. 
 * @author Ragnhild
 *
 */
public class UserRegistration {

	/**
     * Registrerer en bruker i databasen. 
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param phoneNr
     * @param location Posisjonen til garden til bonden. 
     * @return True dersom registreringen gikk bra, false ellers. 
     */
	public static boolean registerUser(String firstName, String lastName, String email, String password, String phoneNr, String location)  {

        //Lager en foresp0rsel til server.
        //retiningslinjer for kommunikasjon med server vil til en hver tid ligge i server.ComProtocol klassen
        String query = email + "||" + firstName + "||" + lastName + "||" + phoneNr + "||" + password  + "||" + location;

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
	/**
     * Sjekker om brukeren eksisterer og om passordet matcher. 
     * @param email
     * @param password
     * @return Brukeren som er logget inn. 
     */
	public static User login(String email, String password) {

        //Lager en foresp0rsel til server.
        String query = email + "||" + password;

        //sender foresp0rselen til serveren og faar tilbake respons
        String serverResponse = ClientConnection.sendServerQuery("login", query);

        //sjekke at melding ble mottatt
        if (!serverResponse.equals("login success")) {
            ClientConnection.handleError(serverResponse);
        }

        query = email;

        Object retobject = ClientConnection.sendObjectQuery("getuser", query);

        if (retobject instanceof User) {
            return (User) retobject;
        } else {
            return null;
        }
    }
	
	public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
		return users;
	}

	/**
	 * Slett bruker fra databasen. 
	 * @param user
	 */
    public static void deleteUser(String user) {
    	String query = user;

        String serverRespons = ClientConnection.sendServerQuery("deleteuser", query);

        if (!serverRespons.equals("deleteuser success")) {
            System.out.println("Error. Cannot delete user");
        }
    }
    
    /**
     * Endre en bruker. 
     * @param email
     * @param firstName
     * @param lastName
     * @param phoneNumber
     * @param location
     */
    public static void editUser(String email, String firstName, String lastName, String phoneNumber, String location ) {
    	
    	
    	String query = email + "||" + firstName + "||" + lastName + "||" + phoneNumber + "||" + location;

        String serverRespons = ClientConnection.sendServerQuery("changeuser", query);

        if (!serverRespons.equals("changeuser success")) {
            System.out.println("Error. Can't change user information");
        }
    }
	
	/**
     * Endre passord. 
     * @param user
     * @param password
     */
	public static void changePassword(String user, String password) {
		String query = user + "||" + password;

        String serverRespons = ClientConnection.sendServerQuery("changepassword", query);

        if (!serverRespons.equals("changepassword success")) {
            System.out.println(serverRespons);
        }
	}
}
