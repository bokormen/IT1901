package example.ClientTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Eivind
 * Date: 21.09.13
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */

public class ClientMain {
    public static void main(String args[]) throws IOException {

        //create connection to server
        ClientConnection ccon = new ClientConnection();

        //input from console
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        //read input from console, send to server and get reponse
        while ((fromUser = stdIn.readLine()) != null) {

            fromServer = ccon.getDataFromServer(fromUser);
            System.out.println(fromServer);
            if (fromServer.equals("bye")) {
                break;
            }
        }

        ccon.close();  //close connection
    }
}
