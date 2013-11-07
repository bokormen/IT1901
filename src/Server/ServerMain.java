package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import database.DatabaseConnector;


//server applikasjon
public class ServerMain {

    public static final int USERS = 256;
    public static final ServerLog sLog = new ServerLog();
    private static final ServerConsole sConsole = new ServerConsole();
    private static ServerSocket serverSocket = null;
    //private static ServerClientThread[] user = new ServerClientThread[USERS];
    public static ArrayList<ServerClientThread> user = new ArrayList<ServerClientThread>();
    public static int currentUsers = 0;
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
        while (true) {

            ServerClientThread sct = null;
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

    public static void close() {

        try {
            sLog.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
