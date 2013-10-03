package div;

import java.io.*;
import java.net.*;

//opprette ny klient tilkoppling til server. Bruk getDataFromServer() for å sende/hente info fra server
public class ClientConnection {

    Socket ClientSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;


    //husk å lukke input og output streams når du er ferdig
    public void close() throws IOException {
        out.close();
        in.close();
        ClientSocket.close();
    }


    //send en melding til server og få respons
    public String getDataFromServer(String query) throws IOException {
        out.println(query);
        return in.readLine();
    }


    //constructor
    public ClientConnection() throws IOException {


        try {
            ClientSocket = new Socket(InetAddress.getLocalHost(), 58339);
            out = new PrintWriter(ClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Could not find host.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to host.");
            System.exit(1);
        }

    }
}