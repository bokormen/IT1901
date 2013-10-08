package div;

import java.io.*;
import java.net.*;

//opprette ny klient tilkoppling til server. Bruk getDataFromServer() for å sende/hente info fra server
public class ClientConnection {


    private static Socket ClientSocket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;
    public static boolean ConnectedToServer;



    //sender en foresp0rsel til database
    //retningslinjer for komunikasjon ligger i Server.ComProtocol klassen

    public static String sendServerQuery(String command, String query) throws IOException {

        String response;
        response = sendServerMsg(command);

        //check that nothing went wrong
        if (!response.equals("done")) {
            return "err";
        }

        return sendServerMsg(query);

    }

    //send en melding til server og få respons
    //bruk denne funksjonen til aa komunisere med server og database
    //retningslinjer for komunikasjon ligger i Server.ComProtocol klassen

    public static String sendServerMsg(String msg) {

        try {
            out.println(msg);
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Server I/O error");
            return "err";
        }
    }

    //haandterer feilmeldinger fra server her.
    public static void handleError(String err) {

        //exception error
        if (err.equals("err")) {
            System.out.println(err);

        //reguser errors
        } else if (err.equals("reguser null input")) {
            System.out.println(err);
        } else if (err.equals("reguser exists")) {
            System.out.println(err);
        } else if (err.equals("reguser bad input")) {
            System.out.println(err);

        //regsheep errors
        } else if (err.equals("regsheep no login")) {
            System.out.println(err);
        } else if (err.equals("regsheep null input")) {
            System.out.println(err);
        } else if (err.equals("regsheep exists")) {
            System.out.println(err);
        } else if (err.equals("regsheep bad input")) {
            System.out.println(err);

        //login errors
        } else if (err.equals("login null input")) {
            System.out.println(err);
        } else if (err.equals("login wrong password")) {
            System.out.println(err);
        } else if (err.equals("login no exists")) {
            System.out.println(err);
        } else if (err.equals("login bad input")) {
            System.out.println(err);
        }
    }


    //aapner en socket til server med gitt ip adresse
    public static void open(InetAddress ip) {
        try {
            if (ip == null) {
                ClientSocket = new Socket(InetAddress.getLocalHost(), 58339);
            } else {
                ClientSocket = new Socket(ip, 58339);
            }
            out = new PrintWriter(ClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            ConnectedToServer = true;
        } catch (UnknownHostException e) {
            System.err.println("Could not find host.");
            ConnectedToServer = false;
            //System.exit(1);
        } catch (IOException e) {
            ConnectedToServer = false;
            System.err.println("Couldn't get I/O for the connection to host.");
            //System.exit(1);
        }
    }



    //husk å lukke input og output streams når du er ferdig
    public static void close() throws IOException {
        out.close();
        in.close();
        ClientSocket.close();
    }
}