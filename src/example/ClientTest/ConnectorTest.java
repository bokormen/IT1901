package example.ClientTest;

import database.DatabaseConnector;
import div.User;

/**
 * Created with IntelliJ IDEA.
 * User: Eivind
 * Date: 03.10.13
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class ConnectorTest {
    public static void main(String args[]) {
        DatabaseConnector.open();
        User suser = DatabaseConnector.getUser("lala@gmail.com");
        System.out.println(suser.getLastName());
    }
}
