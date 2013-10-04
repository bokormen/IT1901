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

    //Haandterer input fra bruker
    public String processInput(String theInput, ServerClientThread sct) throws IOException {
        String theOutput = "";

        //hvis state er WAIT saa venter serveren paa et signal om hva den skal gj0re
        if (state == WAIT) {
            if (theInput.equals("login")) { //bruker vil logge inn, gaa til state LOGIN
                state = LOGIN;
                theOutput = "done";

            } else if (theInput.equals("registeruser")) { //bruker vil registrere seg
                state = REGISTERUSER;
                theOutput = "done";

            } else if (theInput.equals("registersheep")) { //bruker vil registrere sau
                state = REGISTERSHEEP;
                theOutput = "done";

            } else if (theInput.equals("quit")) { // bruker vil avslutte
                theOutput = "bye";

            }

        // I denne staten venter server paa input fra bruker med info om registrering
        //Input eksempel: "email||firstName||lastName||phoneNumber||password||location"
        } else if (state == REGISTERUSER) {

            if (theInput != null) {
                regiserUser(theInput);
            }

            theOutput = "done";
            state = WAIT;

        //IKKE FERDIG
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

        //FOR TESTING
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

    private void regiserUser(String theInput) {

        String[] temp = theInput.split("\\|\\|"); //split the input string on ||
        if (temp.length == 5) {
            if (!DatabaseConnector.doesUserExist(temp[0])) {
                DatabaseConnector.newUser(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5]);
            }
        }

    }

    //ikke brukt
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