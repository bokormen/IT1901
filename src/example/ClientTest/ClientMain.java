package example.ClientTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import div.ClientConnection;
import div.User;
import div.UserRegistration;

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
        ClientConnection.open(null);

        //input from console
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        //read input from console, send to server and get reponse
        /*
        while ((fromUser = stdIn.readLine()) != null) {

            fromServer = ClientConnection.sendServerMsg(fromUser);
            System.out.println(fromServer);
            if (fromServer.equals("bye")) {
                break;
            }
        }
        */

        div.User suser;

        suser = div.UserRegistration.login("lala@gmail.com", "random");
        System.out.println(suser.getLastName());
        ClientConnection.close();  //close connection
    }
}
