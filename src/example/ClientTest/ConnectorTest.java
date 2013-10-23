package example.ClientTest;

import database.DatabaseConnector;
import div.User;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created with IntelliJ IDEA.
 * User: Eivind
 * Date: 03.10.13
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class ConnectorTest {

    private static SecureRandom random = new SecureRandom();

    public static void main(String args[]) {
        //DatabaseConnector.open();
        //User suser = DatabaseConnector.getUser("lala@gmail.com");
        //System.out.println(suser.getLastName());

        System.out.println(genRandomPassword());
        System.out.println(genRandomPassword());
        System.out.println(genRandomPassword());
        System.out.println(genRandomPassword());
        System.out.println(genRandomPassword());
        System.out.println(genRandomPassword());

    }

    public static String genRandomPassword()
    {
        return new BigInteger(80, random).toString(32);
    }
}
