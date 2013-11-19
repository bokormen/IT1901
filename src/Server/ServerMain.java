package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import database.DatabaseConnector;


/**
 * Server Main klasse, her starter server programmet
 * @author Eivind
 */
public class ServerMain {

    public static final int USERS = 256;
    public static final ServerLog sLog = new ServerLog();
    private static final ServerConsole sConsole = new ServerConsole();
    private static final SheepPositionSimulator sSimulator = new SheepPositionSimulator();
    private static ServerSocket serverSocket = null;
    //private static ServerClientThread[] user = new ServerClientThread[USERS];
    public static ArrayList<ServerClientThread> user = new ArrayList<ServerClientThread>();
    public static int currentUsers = 0;
    private static boolean listening = true;


    /**
     * main funksjon
     * @param args argument(ikke brukt)
     */
    public static void main(String[] args) {

        //open a server socket
        try {
            DatabaseConnector.open();

            sLog.addEntry("Server Start-Up");
            serverSocket = new ServerSocket(58339);
            sConsole.start();
            sSimulator.start();
        } catch (IOException e) {
            System.err.println("Could not listen on port: 58339.");
            System.exit(-1);
        }

        //start accepting client connections;
        while (true) {

            ServerClientThread sct;
            Socket clientSocket = null;
            Socket objectSocket = null;

            try {

                clientSocket = serverSocket.accept();
                objectSocket = serverSocket.accept();

                clientSocket.setSendBufferSize(1048576);
                objectSocket.setSendBufferSize(1048576);



            } catch (IOException e) {
                System.err.println("Client socket error");
                e.printStackTrace();
            }

            if (user.size() < USERS) {
                sct = new ServerClientThread(clientSocket, objectSocket, sLog);
                user.add(sct);
            } else {
                sct = new ServerClientThread(clientSocket, objectSocket, sLog, true);
            }

            currentUsers += 1;

            if (!listening) {   // break out if we executed the quit function
                break;
            } else


            sct.start();
        }

        close();
        System.exit(0);
    }

    /**
     * Avslutt program
     * @throws IOException
     */
    public static void quit() throws IOException {
        listening = false;
        int i = 0;
        int size = user.size();
        while (i < size) {  //disconnect all users
            if (user.get(i) != null) {
                user.get(i).closeNoLog();
            }
            i += 1;
        }
        new Socket(InetAddress.getLocalHost(), 58339).close(); //breaks the loop
        new Socket(InetAddress.getLocalHost(), 58339).close(); //breaks the loop
        sLog.addEntry("Server Shut-Down");
    }

    /**
     * Sørger for at alt lukkes riktig.
     */
    public static void close() {

        try {
            sLog.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
