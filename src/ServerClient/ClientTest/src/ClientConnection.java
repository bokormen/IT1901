
import java.io.*;
import java.net.*;

public class ClientConnection {

    Socket kkSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    public ClientConnection() throws IOException {


        try {
            kkSocket = new Socket(InetAddress.getLocalHost(), 58339);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Could not find host.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to host.");
            System.exit(1);
        }

    }

    public String getServerMessage() throws IOException {
        return in.readLine();
    }

    public void close() throws IOException {
        out.close();
        in.close();
        kkSocket.close();
    }

    public void sendClientMessage(String fromUser) {
        out.println(fromUser);
    }

    public String getDataFromServer(String query) throws IOException {
        out.println(query);
        return in.readLine();
    }
}