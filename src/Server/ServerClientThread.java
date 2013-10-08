package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


//Traad som haandterer komunikasjon med klient
public class ServerClientThread extends Thread {

    //some variables
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    protected ServerLog log = null;

    //Starter en ny traad
    public void run() {

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;

            String ClientIP = getClientAddress();
            ComProtocol com = new ComProtocol(ClientIP, false, log); //opretter komunikasjons protokollen, all komunikasjon med klient blir prosessert her
            log.addEntry(ClientIP + " connected."); //loggf0rer

            //les input fra klient socket og prosesser input
            while ((inputLine = in.readLine()) != null) {
                outputLine = com.processInput(inputLine);
                out.println(outputLine);

                //litt ekstra prosessering
                if (outputLine.equals("quit"))
                    break;
            }

            close();

        } catch (IOException e) {
            try {
                close();
                log.addEntry("User Connection Error, Closed Connection.");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //finner klient ip
    public String getClientAddress() {
        return socket.getInetAddress().toString();
    }

    //lukker socket og inputstreams
    public void close() throws IOException {
        log.addEntry(getClientAddress() + " disconnected.");
        out.close();
        in.close();
        socket.close();
    }

    //lukker input strems uten logf0ring, fikser dobbel logf0ring i enkelte tilfeller
    public void closeNoLog() throws IOException {
        out.close();
        in.close();
        socket.close();
    }


    //konstrukt0r
    public ServerClientThread(Socket socket, ServerLog log) {
        super("MultiServerThread"); //invokes the thread constructor
        this.socket = socket;
        this.log = log;
    }
}