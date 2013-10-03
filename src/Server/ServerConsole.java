package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Eivind
 * Date: 22.09.13
 * Time: 23:03
 * To change this template use File | Settings | File Templates.
 */

//Server consol for å administrere server
public class ServerConsole extends Thread {

    BufferedReader stdIn = null;

    //start en ny tråd
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

    public ServerConsole() {
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    }
}
