package Server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;

import database.DatabaseConnector;
import div.Sheep;
import div.SheepAttackMail;

//handles the input from clients
public class ComProtocol {

    SecureRandom random = new SecureRandom();
    public boolean isLoggedIn = false;
    public String ClientIP = null;
    private ServerLog log = null;
    private String UserName = null;
    private DataOutputStream oout = null;

    private static final int WAIT = 1001;
    private static final int LOGIN = 1002;
    private static final int MAILPASSWORD = 1003;

    private static final int REGISTERUSER = 2001;
    private static final int GETUSER = 2002;
    private static final int CHANGEUSER = 2003;
    private static final int CHANGEPASSWORD = 2004;

    private static final int REGISTERSHEEP = 3001;
    private static final int DELETESHEEP = 3002;
    private static final int FINDSHEEP = 3003;
    private static final int GETOWNEDSHEEP = 3004;
    private static final int CHANGESHEEP = 3005;
    private static final int ATTACKSHEEP = 3006;
    private static final int GETSHEEPLOG = 3007;

    private static final int TESTING = 9999;

    private int state = WAIT;

    //constructor
    public ComProtocol(String IP, Socket osocket, boolean login, ServerLog clientlog) {

        try {
            oout = new DataOutputStream(osocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Could not open object streams");
        }
        ClientIP = IP;
        isLoggedIn = login;
        log = clientlog;
    }



    //Haandterer input fra bruker
    public String processInput(String theInput) throws IOException {
        String theOutput;

        //hvis state er WAIT saa venter serveren paa et signal om hva den skal gj0re
        if (state == WAIT) {
            if (theInput.equals("login")) { //bruker vil logge inn, gaa til state LOGIN
                state = LOGIN;
                theOutput = "done";

            } else if (theInput.equals("mailpassword")) {
                state = MAILPASSWORD;
                theOutput = "done";


            } else if (theInput.equals("registeruser")) { //bruker vil registrere seg
                state = REGISTERUSER;
                theOutput = "done";

            } else if (theInput.equals("getuser")) { //bruker vil registrere sau
                state = GETUSER;
                theOutput = "done";

            } else if (theInput.equals("changeuser")) { //bruker vil forandre paa profil sau
                state = CHANGEUSER;
                theOutput = "done";

            } else if (theInput.equals("changepassword")) { //bruker vil forandre passord
                state = CHANGEPASSWORD;
                theOutput = "done";


            } else if (theInput.equals("registersheep")) { //bruker vil registrere sau
                state = REGISTERSHEEP;
                theOutput = "done";

            } else if (theInput.equals("delsheep")) { //bruker vil slette sau
                state = DELETESHEEP;
                theOutput = "done";

            } else if (theInput.equals("findsheep")) { //bruker vil finne sau
                state = FINDSHEEP;
                theOutput = "done";

            } else if (theInput.equals("getownedsheep")) { //bruker vil ha en liste over alle sauer
                state = GETOWNEDSHEEP;
                theOutput = "done";

            } else if (theInput.equals("changesheep")) { //bruker vil forandre paa en sau
                state = CHANGESHEEP;
                theOutput = "done";

            } else if (theInput.equals("attacksheep")) { //bruker vil forandre paa en sau
                state = ATTACKSHEEP;
                theOutput = "done";

            } else if (theInput.equals("getsheeplog")) { //bruker vil ha log over posisjonene til en sau
                state = GETSHEEPLOG;
                theOutput = "done";
                
                
                
                
            } else if (theInput.equals("testing")) { //bruker vil forandre paa en sau
                state = TESTING;
                theOutput = "done";

            } else if (theInput.equals("quit")) { // bruker vil avslutte
                theOutput = "bye";
            } else {
                theOutput = "command no exists";
            }


        //ANDRE STATES

        //Venter paa login
        //Input: "email||password"
        } else if (state == LOGIN) {

            if (theInput != null) {
                theOutput = userLogin(theInput);
            } else {
                theOutput = "login null input";
            }

            state = WAIT;

        //Venter paa input for mailing av passord
        //Input: "email"
        } else if (state == MAILPASSWORD) {

            if (theInput != null) {
                theOutput = mailPassword(theInput);
            } else {
                theOutput = "mailpassword null input";
            }

            state = WAIT;




        // I denne staten venter server paa input fra bruker med info om registrering
        //Input: "email||firstName||lastName||phoneNumber||password||location"
        } else if (state == REGISTERUSER) {

            if (theInput != null) {
                theOutput = registerUser(theInput);
            } else {
                theOutput = "reguser null input";
            }

            state = WAIT;

        //Venter paa input for getuser
        //input: "email"
        } else if (state == GETUSER) {

            if (theInput != null) {
                if (isLoggedIn) {
                    theOutput = "object sending";
                    sendObject(DatabaseConnector.getUser(theInput));

                } else {
                    theOutput = "getuser no login";
                }
            } else {
                theOutput = "getuser null input";
            }

            state = WAIT;

        //Venter paa input for changeuser
        //Input: "email||firstName||lastName||phoneNumber||password||location"
        } else if (state == CHANGEUSER) {

            if (theInput != null) {
                if (isLoggedIn) {
                    theOutput = changeUser(theInput);
                } else {
                    theOutput = "changeuser no login";
                }
            } else {
                theOutput = "changeuser null input";
            }

            state = WAIT;

        //Venter paa input for changepassword
        //Input: "email||newpassword"
        } else if (state == CHANGEPASSWORD) {

            if (theInput != null) {
                if (isLoggedIn) {
                    theOutput = changePassword(theInput);
                } else {
                    theOutput = "changepassword no login";
                }
            } else {
                theOutput = "changepassword null input";
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

        //venter paa input for sletting av sheep
        // input: "id"
        } else if (state == DELETESHEEP) {

            if (theInput != null) {
                if (isLoggedIn) {
                    theOutput = delSheep(theInput);
                } else {
                    theOutput = "delsheep no login";
                }
            } else {
                theOutput = "delsheep null input";
            }

            state = WAIT;

        //venter paa input for aa finne sheep
        //input: "id"
        } else if (state == FINDSHEEP) {

            if (theInput != null) {
                if (isLoggedIn) {
                    theOutput = "object sending";
                    sendObject(DatabaseConnector.findSheep(UserName, theInput));
                    log.addEntry(ClientIP + "[" + UserName + "] Searched for sheep: " + theInput + ".");

                } else {
                    theOutput = "findsheep no login";
                }
            } else {
                theOutput = "findsheep null input";
            }

            state = WAIT;

        //venter paa input for aa faa liste av sauer
        //input: "email"
        } else if (state == GETOWNEDSHEEP) {

            if (theInput != null) {
                if (isLoggedIn) {
                    theOutput = "object sending";
                    try {
                        sendObject(DatabaseConnector.getAllSheepsToOwner(UserName));
                        log.addEntry(ClientIP + "[" + UserName + "] Requested Sheep List.");
                    } catch (Exception e) {
                        theOutput = "getownedsheep database error";
                    }

                } else {
                    theOutput = "getownedsheep no login";
                }
            } else {
                theOutput = "getownedsheep null input";
            }

            state = WAIT;

        //Venter paa input for changesheep
        //typisk input string er: "id||name||owner||shepherd||gender||weight||birthyear"
        //eksempel "101||Ole||frodo@hotmail.com||gandalf@hotmail.com||f||10||2003"
        } else if (state == CHANGESHEEP) {

            if (theInput != null) {
                if (isLoggedIn) {
                    theOutput = changeSheep(theInput);
                } else {
                    theOutput = "changesheep no login";
                }
            } else {
                theOutput = "changesheep null input";
            }

            state = WAIT;

        //venter paa input for aa angripe sheep
        //input: "id"
        //exempel: "1337"
        } else if (state == ATTACKSHEEP) {

            if (theInput != null) {

                if (isLoggedIn) {
                    Sheep s = DatabaseConnector.findSheep(UserName, theInput);
                    SheepAttackMail.sendMail(s.getOwner(), s.getId(), s.getLocation().getPosition());
                    log.addEntry(ClientIP + "[" + UserName + "] Sheep: " + theInput + " under attack.");
                    theOutput = "attacksheep success";
                } else {
                    theOutput = "attacksheep no login";
                }
            } else {
                theOutput = "attacksheep null input";
            }

            state = WAIT;


        //venter paa input for aa faa hente saue log
        //input: "id||mengde"
        //eksempel: "12345||200"
        } else if (state == GETSHEEPLOG) {

            if (theInput != null) {
                if (isLoggedIn) {
                    String[] temp = theInput.split("\\|\\|"); //splitter input ved ||
                    if (temp.length == 2) {
                        sendObject(DatabaseConnector.addNumberOfHistoricalLocationsToSheep(temp[0], temp[1]));
                        log.addEntry(ClientIP + "[" + UserName + "] Requested log of sheep: " + temp[0] + ".");
                        theOutput = "object sending";

                    } else {
                        theOutput = "getsheeplog bad input";
                    }
                } else {
                    theOutput = "getsheeplog no login";
                }
            } else {
                theOutput = "getsheeplog null input";
            }

            state = WAIT;


        //venter paa input for aa faa liste av sauer
        //input: "email"
        } else if (state == TESTING) {

            if (theInput != null) {
                theOutput = "object sending";
                try {
                    int size = 10000;
                    ArrayList<Sheep> sl = new ArrayList<Sheep>();
                    //Sheep[] sa = new Sheep[size];
                    for (int i = 0; i < size; i++) {
                        Sheep s = new Sheep(i, "testsheep", 2008, 20, 'm', "test0@test.test", "test2@test.test");
                        s.newLocation(10 + "," + 9, "00/00/0000");
                        sl.add(s);
                        //sa[i] = s;
                    }
                    sendObject(sl);
                    log.addEntry(ClientIP + "[" + UserName + "] Requested Sheep List.");
                } catch (Exception e) {
                    theOutput = "getownedsheep database error";
                }

            } else {
                theOutput = "getownedsheep null input";
            }

            state = WAIT;


        } else {
            theOutput = "err";
            state = WAIT;
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

    //Redigere info om bruker i database
    //typisk input string er: "email||firstName||lastName||phoneNumber||location"
    //eksempel "frodo@hotmail.com||frodo||baggins||12345678||19.2,19.3"

    private String changeUser(String theInput) {

        String[] temp = theInput.split("\\|\\|"); //splitter input ved ||
        if (temp.length == 5) {
            if (temp[0].equals(UserName)) {
                DatabaseConnector.changeUser(temp[0], temp[1], temp[2], temp[3], temp[4]);
                log.addEntry(ClientIP + "[" + UserName + "] Changed user info.");
                return "changeuser success";
            } else {
                return "changeuser different username";
            }
        } else {
            return "changeuser bad input";
        }

    }


    //Forandre passord
    //typisk input string er: "email||newpassword"
    //eksempel "frodo@hotmail.com||mittpassord2"

    private String changePassword(String theInput) {

        String[] temp = theInput.split("\\|\\|"); //splitter input ved ||
        if (temp.length == 2) {
            if (temp[0].equals(UserName)) {
                DatabaseConnector.changePassword(temp[0], temp[1]);
                log.addEntry(ClientIP + "[" + UserName + "] Changed user password.");
                return "changepassword success";
            } else {
                return "changepassword different username";
            }
        } else {
            return "changepassword bad input";
        }

    }






    //registrere en sau i databasen
    //typisk input string er: "id||Eiernavn||shepherd||weight||75||39||age"
    //eksempel "id||Eiernavn||shepherd||weight||75||39||age"

    private String registerSheep(String theInput) {

        String[] temp = theInput.split("\\|\\|"); //splitter input ved ||
        if (temp.length == 8) {
            if (!DatabaseConnector.doesSheepExsist(temp[0])) {
                try {
                    sendObject(DatabaseConnector.newSheep(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6], temp[7], temp[8]));
                } catch (IOException e) {
                    return "regsheep database error";
                }
                log.addEntry(ClientIP + "[" + UserName + "] registered new sheep (" + temp[0] + ").");
                return "object sending";
            } else {
                return "regsheep exists";
            }
        } else {
            return "regsheep bad input";
        }

    }

    //sletter en sau i databaen
    //typisk input er: "id"
    //eksempel "3"

    private String delSheep(String theInput) {
        if (DatabaseConnector.doesSheepExsist(theInput)) {
            DatabaseConnector.deleteSheep(theInput);
            log.addEntry(ClientIP + "[" + UserName + "] deleted sheep (" + theInput + ").");
            return "delsheep success";
        }
        return "delsheep no exists";
    }


    //Redigere info om bruker i database
    //typisk input string er: "id||name||owner||shepherd||gender||weight||birthyear"
    //eksempel "101||Ole||frodo@hotmail.com||gandalf@hotmail.com||f||10||2003"

    private String changeSheep(String theInput) {

        String[] temp = theInput.split("\\|\\|"); //splitter input ved ||
        if (temp.length == 7) {
            if (temp[0].equals(UserName)) {
                DatabaseConnector.changeBasicSheep(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6]);
                log.addEntry(ClientIP + "[" + UserName + "] Changed sheep info for sheep: " + temp[0] + ".");
                return "changesheep success";
            } else {
                return "changesheep different username";
            }
        } else {
            return "changesheep bad input";
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


    //Auto-genererer et nytt passord og sender paa mail
    //typisk input string er: "email"
    //eksempel "frodo@hotmail.com"
    private String mailPassword(String theInput) {

        if (DatabaseConnector.doesUserExsist(theInput)) {

            String newpassword = new BigInteger(80, random).toString(32);
            
            DatabaseConnector.changePassword(theInput, newpassword);

            try {
                div.SendMail.sendMailTo(theInput, newpassword);
                log.addEntry(ClientIP + "requested password reset on (" + theInput + ").");
            } catch (Exception e) {
                e.printStackTrace();
                return "mailpassword mail error";
            }
        } else {
            return "mailpassword no exists";
        }

        return "mailpassword success";
    }


    private void sendObject(Object o) throws IOException {

            byte[] b = Serialize(o);
            sendBytes(b);
    }

    public static byte[] Serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public void sendBytes(byte[] myByteArray) throws IOException {
        sendBytes(myByteArray, 0, myByteArray.length);
    }

    public void sendBytes(byte[] myByteArray, int start, int len) throws IOException {
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (start < 0 || start >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + start);
        // Other checks if needed.

        oout.writeInt(len);
        if (len > 0) {
            oout.write(myByteArray, start, len);
        }
    }

    //lukker streams
    public void close() {
        try {
            oout.close();
        } catch (IOException e) {
            System.err.println("Error closing object streams.");
        }
    }





}