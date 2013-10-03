package Server;

import java.io.IOException;
import java.util.StringTokenizer;

import database.DatabaseConnector;

//handles the input from clients
public class ComProtocol {

    private static final int WAIT = 1001;
    private static final int REGISTERUSER = 1002;
    private static final int REGISTERSHEEP = 1003;

    private static final int LOGIN = 1004;

    private static final int TESTING = 9999;

    private int state = WAIT;

    //process some input depending on state
    public String processInput(String theInput, ServerClientThread sct) throws IOException {
        String theOutput = "";

        if (state == WAIT) {
            if (theInput.equals("login")) {
                state = LOGIN;
                theOutput = "done";

            } else if (theInput.equals("registeruser")) {
                state = REGISTERUSER;
                theOutput = "done";

            } else if (theInput.equals("registersheep")) {
                state = REGISTERSHEEP;
                theOutput = "done";

            } else if (theInput.equals("quit")) {
                theOutput = "bye";

            }

        } else if (state == REGISTERUSER) {

            if (theInput != null) {
                regiserUser(theInput);
            }

            theOutput = "done";
            state = WAIT;

            //make a login function for this to parse the string and check credentials
        } else if (state == LOGIN) {

            if (theInput.equals("user1||1234")) {
                state = WAIT;
                sct.isLoggedIn = true;
                sct.log.addEntry("user1 logged in");
                theOutput = "You are logged in.";
            } else if (theInput.equals("cancel")) {
                state = WAIT;
            } else {
                theOutput = "Wrong Username or Password";
            }
        }

        else if (state == TESTING) {
            if (theInput.equalsIgnoreCase("hello world!")) {
                theOutput = "Hello User";
            } else {
                theOutput = "exit";
            }
        }

        return theOutput;
    }

    //registers a user in the database
    //typical input string is: "email||firstName||lastName||phoneNumber||password||location"
    //example "frodo@hotmail.com||frodo||baggins||1234566||gandalf||19.2,19.3"
    //newUser(String email, String firstName, String lastName, String phoneNumber, String password, String location)

    private void regiserUser(String theInput) {

        String[] temp = theInput.split("\\|\\|"); //split the input string on ||
        if (temp.length == 5) {
            if (!DatabaseConnector.doesUserExist(temp[0])) {
                DatabaseConnector.newUser(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5]);
            }
        }

    }

    public void changeState(String newstate) {
        if (newstate.equals("wait")) {
            state = WAIT;

        } else if (newstate.equals("register")) {
            state = REGISTERUSER;

        } else if (newstate.equals("login")) {
            state = LOGIN;
        }
    }
}