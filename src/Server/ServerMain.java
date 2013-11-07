package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import database.DatabaseConnector;


//server applikasjon
public class ServerMain {

    private static final int USERS = 256;
    public static final ServerLog sLog = new ServerLog();
    private static final ServerConsole sConsole = new ServerConsole();
    private static ServerSocket serverSocket = null;
    private static ServerClientThread[] user = new ServerClientThread[USERS];
    private static int currentUser = 0;
    private static boolean listening = true;


    // Start Server
    public static void main(String[] args) {

        //open a server socket
        try {
            DatabaseConnector.open();

            sLog.addEntry("Server Start-Up");
            serverSocket = new ServerSocket(58339);
            sConsole.start();
        } catch (IOException e) {
            System.err.println("Could not listen on port: 58339.");
            System.exit(-1);
        }

        //start accepting client connections;
        while (currentUser < USERS) {


            Socket clientSocket = null;
            Socket objectSocket = null;

            try {

                clientSocket = serverSocket.accept();
                objectSocket = serverSocket.accept();

                clientSocket.setSendBufferSize(10485760);
                objectSocket.setSendBufferSize(10485760);

            } catch (IOException e) {
                System.err.println("Client socket error");
                e.printStackTrace();
            }


            user[currentUser] = new ServerClientThread(clientSocket, objectSocket, sLog);

            if (!listening) {   // break out if we executed the quit function
                break;
            }

            user[currentUser].start();
            currentUser += 1;
        }

        close();
        System.exit(0);
    }

    public static void quit() throws IOException {
        listening = false;
        int i = 0;
        while (i < USERS) {  //disconnect all users
            if (user[i] != null) {
                user[i].closeNoLog();
            }
            i += 1;
        }
        new Socket(InetAddress.getLocalHost(), 58339).close(); //breaks the loop
        new Socket(InetAddress.getLocalHost(), 58339).close(); //breaks the loop
        sLog.addEntry("Server Shut-Down");
    }

    public static void close() {

        try {
            sLog.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
