package Server;


import java.io.*;
import java.net.Socket;


/**
 * Klasse som håndterer en klient tilkoblet til server, kjøres en egen tråd for hver bruker
 *
 * @author Eivind
 */
public class ServerClientThread extends Thread {

    //some variables
    private Socket socket = null;
    private Socket osocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private boolean sendserverfullmessage = false;
    protected ServerLog log = null;

    /**
     * Starter en ny klient tråd
     */
    public void run() {

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;

            if (sendserverfullmessage) {
                out.println("Server Full");
                close();
                return;
            }

            String ClientIP = getClientAddress();
            ComProtocol com = new ComProtocol(ClientIP, osocket, false, log); //opretter komunikasjons protokollen, all komunikasjon med klient blir prosessert her
            log.addEntry(ClientIP + " connected."); //loggf0rer
            out.println("Welcome");

            //les input fra klient socket og prosesser input
            while ((inputLine = in.readLine()) != null) {
                outputLine = com.processInput(inputLine);
                out.println(outputLine);

                //litt ekstra prosessering
                if (outputLine.equals("quit"))
                    break;
            }

            com.close();
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

    /**
     * Finner klient'ens IP
     * @return IP
     */
    public String getClientAddress() {
        return socket.getInetAddress().toString();
    }

    /**
     * Lukker socket, streams og fjerner bruker fra liste over tilkoblede brukere
     * @throws IOException
     */
    public void close() throws IOException {
        log.addEntry(getClientAddress() + " disconnected.");
        ServerMain.user.remove(this);
        ServerMain.currentUsers -= 1;
        out.close();
        in.close();
        socket.close();
    }

    /**
     * lukker input strems uten logf0ring, fikser dobbel logf0ring i enkelte tilfeller
     * @throws IOException
     */
    public void closeNoLog() throws IOException {
        out.close();
        in.close();
        socket.close();
    }


    /**
     * Konstruktør
     * @param clientSocket Klient socket for normal komunikasjon
     * @param objectSocket Klient socket for sending av objekt
     * @param sLog referanse til server log objekt
     */
    public ServerClientThread(Socket clientSocket, Socket objectSocket, ServerLog sLog) {
        super("MultiServerThread"); //invokes the thread constructor
        socket = clientSocket;
        osocket = objectSocket;
        log = sLog;
    }

    /**
     * Konstruktør
     * @param clientSocket Klient socket for normal komunikasjon
     * @param objectSocket Klient socket for sending av objekt
     * @param sLog referanse til server log objekt
     * @param serverfull om server er full
     */
    public ServerClientThread(Socket clientSocket, Socket objectSocket, ServerLog sLog, boolean serverfull) {
        super("MultiServerThread"); //invokes the thread constructor
        socket = clientSocket;
        osocket = objectSocket;
        log = sLog;
        sendserverfullmessage = serverfull;
    }
}