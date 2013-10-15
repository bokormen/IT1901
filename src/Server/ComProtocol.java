package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import database.DatabaseConnector;
import div.Sheep;
import div.User;

//handles the input from clients
public class ComProtocol {

    public boolean isLoggedIn = false;
    public String ClientIP = null;
    private ServerLog log = null;
    private String UserName = null;
    private ObjectOutputStream oout = null;
    private ObjectInputStream oin = null;

    private static final int WAIT = 1001;
    private static final int REGISTERUSER = 1002;
    private static final int REGISTERSHEEP = 1003;

    private static final int GETUSER = 2002;

    private static final int LOGIN = 1004;

    private static final int TESTING = 9999;

    private int state = WAIT;

    //constructor
    public ComProtocol(String IP, Socket osocket, boolean login, ServerLog clientlog) {

        try {
            oout = new ObjectOutputStream(osocket.getOutputStream());
            oin = new ObjectInputStream(osocket.getInputStream());
        } catch (IOException e) {
            System.err.println("Could not open object streams");
        }
        ClientIP = IP;
        isLoggedIn = login;
        log = clientlog;
    }



    //Haandterer input fra bruker
    public String processInput(String theInput) throws IOException {
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

            } else if (theInput.equals("getuser")) { //bruker vil registrere sau
                state = GETUSER;
                theOutput = "done";

            } else if (theInput.equals("quit")) { // bruker vil avslutte
                theOutput = "bye";
            } else {
                theOutput = "command no exists";
            }

        // I denne staten venter server paa input fra bruker med info om registrering
        //Input: "email||firstName||lastName||phoneNumber||password||location"
        } else if (state == REGISTERUSER) {

            if (theInput != null) {
                theOutput = registerUser(theInput);
            } else {
                theOutput = "reguser null input";
            }

            state = WAIT;

        // I denne staten venter server paa input fra bruker med info om registrering av sau
        //Input: "id||Eiernavn||shepherd||weight||75||39||age"
        } else if (state == REGISTERSHEEP) {

            if (theInput != null) {
                if (isLoggedIn) {
                    theOutput = registerSheep(theInput);
                } else {
                    theOutput = "regsheep no login";
                }
            } else {
                theOutput = "regsheep null input";
            }

            state = WAIT;

        } else if (state == GETUSER) {

                if (theInput != null) {
                    if (isLoggedIn) {
                        theOutput = "object sending";
                        oout.writeObject(DatabaseConnector.getUser(theInput));

                    } else {
                        theOutput = "getuser no login";
                    }
                } else {
                    theOutput = "getuser null input";
                }

                state = WAIT;

        //Venter paa login
        //Input: "email||password"
        } else if (state == LOGIN) {

            if (theInput != null) {
                theOutput = userLogin(theInput);
            } else {
                theOutput = "login null input";
            }

            state = WAIT;
        } else {
            theOutput = "err";
        }

        return theOutput;
    }



    // =============================================
    // ===============FUNKSJONER====================
    // =============================================


    //registrere en bruker i databasen
    //typisk input string er: "email||firstName||lastName||phoneNumber||password||location"
    //eksempel "frodo@hotmail.com||frodo||baggins||12345678||gandalf||19.2,19.3"

    private String registerUser(String theInput) {

        String[] temp = theInput.split("\\|\\|"); //splitter input ved ||
        if (temp.length == 6) {
            if (!DatabaseConnector.doesUserExsist(temp[0])) {
                DatabaseConnector.newUser(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5]);
                log.addEntry(ClientIP + " registered new user (" + temp[0] + ").");
                return "reguser success";
            } else {
                return "reguser exists";
            }
        } else {
            return "reguser bad input";
        }

    }


    //registrere en sau i databasen
    //typisk input string er: "id||Eiernavn||shepherd||weight||75||39||age"
    //eksempel "id||Eiernavn||shepherd||weight||75||39||age"

    private String registerSheep(String theInput) {

        String[] temp = theInput.split("\\|\\|"); //splitter input ved ||
        if (temp.length == 8) {
            if (!DatabaseConnector.doesSheepExsist(temp[0])) {
                DatabaseConnector.newSheep(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6], temp[7]);
                log.addEntry(ClientIP + "[" + UserName + "] registered new sheep (" + temp[0] + ").");
                return "regsheep success";
            } else {
                return "regsheep exists";
            }
        } else {
            return "regsheep bad input";
        }

    }


    //logger inn en bruker
    //typisk input string er: "email||passord"
    //eksempel "frodo@hotmail.com||gandalf"
    private String userLogin(String theInput) {
        String[] temp = theInput.split("\\|\\|"); //split the input string on ||
        if (temp.length == 2) {
            if (DatabaseConnector.doesUserExsist(temp[0])) {
                if (DatabaseConnector.login(temp[0], temp[1])) {
                    isLoggedIn = true;
                    UserName = temp[0];
                    log.addEntry(ClientIP + "[" + UserName + "] logged in.");
                    return "login success";
                } else {
                    return "login wrong password";
                }
            } else {
                return "login no exists";
            }
        } else {
            return "login bad input";
        }
    }

    public void close() {
        try {
            oin.close();
            oout.close();
        } catch (IOException e) {
            System.err.println("Error closing object streams.");
        }
    }

}