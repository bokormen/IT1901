package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Server konsoll for å administrere server program
 * @author Eivind
 */
public class ServerConsole extends Thread {

    BufferedReader stdIn = null;

    /**
     * Starter en ny tråd som kjører kode for konsoll i bakgrunnen
     */
    public void run() {
        String fromConsole;

        try {
            while ((fromConsole = stdIn.readLine()) != null) {

                if (fromConsole.equals("quit")) {
                    break;
                }
                processCommand(fromConsole);
            }
            processCommand("quit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * prosessere input fra konsoll
     * @param fromConsole Melding fra konsoll
     */
    private void processCommand(String fromConsole) {
        if (fromConsole.equalsIgnoreCase("quit")) {
            try {
                System.out.println("Quitting...");
                ServerMain.quit();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (fromConsole.equalsIgnoreCase("printlog")) {
            ServerMain.sLog.printLog();
        } else if (fromConsole.equalsIgnoreCase("help")) {
            String str = "Commands:\nprintlog\nclearlog\nquit";
            System.out.println(str);
        } else if (fromConsole.equalsIgnoreCase("clearlog")) {
            ServerMain.sLog.clearLog();
        } else {
            System.out.println("Unknown Command.");
        }
    }

    /**
     * Konstruktør for klassen
     */
    public ServerConsole() {
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    }
}
