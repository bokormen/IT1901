package div;

import java.util.ArrayList;


/**
 * Klassen handterer registrering, sletting, endring og innlogging av brukere. 
 * @author Ragnhild
 *
 */
public class UserRegistration {

	/**
     * Registrerer en bruker i databasen. 
     * @param firstName Fornavnet til brukeren
     * @param lastName Etternavnet til brukeren
     * @param email Epostaddressen til brukeren
     * @param password Passordet til brukeren. Kan bare inneholde bokstaver og tall. 
     * @param phoneNr Telefonnummeret til brukeren. Kan bare inneholde tall, og ma være minst 8 tegn. 
     * @param location Posisjonen til garden til bonden, på formen latitude,longitude
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
     * Logger inn brukeren dersom brukeren eksisterer og om passordet matcher. Brukerobjektet blir returnert. 
     * @param email
     * @param password
     * @return Brukeren som er logget inn. Null dersom brukeren ikke blir funnet. 
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

        //Henter User-objektet til brukeren som logges inn. 
        query = email;

        Object retobject = ClientConnection.sendObjectQuery("getuser", query);

        if (retobject instanceof User) {
            return (User) retobject;
        } else {
            return null; //retObject er en feilmelding. 
        }
    }
	

	/**
	 * Sletter en bruker. 
	 * @param user Epostaddressen til brukeren som skal slettes.
	 */
    public static void deleteUser(String user) {
    	String query = user;

        String serverRespons = ClientConnection.sendServerQuery("deleteuser", query);

        if (!serverRespons.equals("deleteuser success")) {
            System.out.println("Error. Cannot delete user");
        }
    }
    
    /**
     * Endrer informasjonene til en bruker
     * @param email Epostadressen til brukeren på formen eksempel@eksempel.com
     * @param firstName Fornavnet til brukeren
     * @param lastName Etternavnet til brukeren
     * @param phoneNumber Telefonnummeret til brukeren. Ma være minst 8 tall. 
     * @param location Posisjonen til garden til bonden på formen latitude,longitude
     */
    public static void editUser(String email, String firstName, String lastName, String phoneNumber, String location ) {
    	
    	
    	String query = email + "||" + firstName + "||" + lastName + "||" + phoneNumber + "||" + location;

        String serverRespons = ClientConnection.sendServerQuery("changeuser", query);

        if (!serverRespons.equals("changeuser success")) {
            System.out.println("Error. Can't change user information");
        }
    }
	
	/**
     * Endrer passordet til en bruker. 
     * @param user Epostadressen til brukeren. 
     * @param password Det nye passordet til brukeren. 
     */
	public static void changePassword(String user, String password) {
		String query = user + "||" + password;

        String serverRespons = ClientConnection.sendServerQuery("changepassword", query);

        if (!serverRespons.equals("changepassword success")) {
            System.out.println(serverRespons);
        }
	}
	
	/**
	 * Sender en mail til brukeren med et nytt passord. 
	 * @param user Epostadressen til brukeren. 
	 */
	public static void mailPassword(String user) {
		String query = user;

        String serverRespons = ClientConnection.sendServerQuery("mailpassword", query);

        if (!serverRespons.equals("mailpassword success")) {
            System.out.println(serverRespons);
        }
	}
}
