package example.ClientTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import div.ClientConnection;
import div.Sheep;
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

        /*
        div.User suser;

        suser = div.UserRegistration.login("lala@gmail.com", "random");
        System.out.println(suser.getLastName());

        */

        div.Sheep ssheep;

        try {
            //div.UserRegistration.login("test2@test.test", "passord");
            ClientConnection.sendServerQuery("login", "test0@test.test||passord");
            Object user0 = ClientConnection.sendObjectQuery("getuser", "test0@test.test");

            System.out.println(((User) user0).getPhoneNr());
            Object sl = ClientConnection.sendObjectQuery("testing", "hei");

            if (sl instanceof ArrayList) {
                ArrayList<Sheep> sll = (ArrayList<Sheep>) sl;
                for (int i = 0; i < sll.size(); i++) {
                    System.out.println(sll.get(i).getId());
                }
            } else if (sl instanceof Sheep[]) {
                Sheep[] sll = (Sheep[]) sl;
                for (int i = 0; i < sll.length; i++) {
                    System.out.println(sll[i].getLocation().getPosition());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        ClientConnection.close();  //close connection
    }
}
