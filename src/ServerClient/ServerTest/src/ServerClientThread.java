import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


//Thread that handles communication with a client
public class ServerClientThread extends Thread {

    //some variables
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    protected ServerLog log = null;
    protected boolean isLoggedIn = false;

    //start running a new thread.
    public void run() {

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;
            ComProtocol com = new ComProtocol();

            log.addEntry(getClientAddress() + " connected.");

            //read input from client and process
            while ((inputLine = in.readLine()) != null) {
                outputLine = com.processInput(inputLine, this);
                out.println(outputLine);
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

    public String getClientAddress() {
        return socket.getInetAddress().toString();
    }

    //close inputstreams and socket
    public void close() throws IOException {
        log.addEntry(getClientAddress() + " disconnected.");
        out.close();
        in.close();
        socket.close();
    }

    //close inputstreams and socket without logging
    public void closeNoLog() throws IOException {
        out.close();
        in.close();
        socket.close();
    }


    //constructor
    public ServerClientThread(Socket socket, ServerLog log) {
        super("KKMultiServerThread"); //invokes the thread constructor
        this.socket = socket;
        this.log = log;
    }
}