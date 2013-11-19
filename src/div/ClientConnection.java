package div;

import java.io.*;
import java.net.*;


/**
 * opprette ny klient tilkoppling til server. Bruk sendServerQuery() eller sendObjectQuery for å sende/hente info fra server
 * @author Eivind
 */
public class ClientConnection {


    private static Socket ClientSocket = null;
    private static Socket ObjectSocket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;
    private static DataInputStream oin = null;
    public static boolean ConnectedToServer;


    /**
     * sender en foresp0rsel til database
     * retningslinjer for komunikasjon ligger i Server.ComProtocol klassen
     * @param command Hva som skal gjøres
     * @param query Parametre som trengs
     * @return svar streng
     */
    public static String sendServerQuery(String command, String query) {

        String response;
        response = sendServerMsg(command);

        //check that nothing went wrong
        if (!response.equals("done")) {
            return "err";
        }

        return sendServerMsg(query);

    }


    /**
     * Henter et objekt fra Serveren/databasen
     * retningslinjer for komunikasjon ligger i Server.ComProtocol klassen
     * @param command Hva som skal gjøres
     * @param query Parametre som trengs
     * @return svar objekt
     */
    public static Object sendObjectQuery(String command, String query) {

        String response;
        response = sendServerMsg(command);

        //check that nothing went wrong
        if (!response.equals("done")) {
            return "err";
        }

        response = sendServerMsg(query);
        if (response.equals("object sending")) {
            try {
                return getObject();
            } catch (IOException e) {
                System.err.println("Problem reading object from object stream.");
            } catch (ClassNotFoundException e) {
                System.err.println("Object stream contains object of unknown class.");
            }
        }

        return response;

    }

    /**
     *
     * @return Object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Object getObject() throws IOException, ClassNotFoundException {

        Object o;
        int length;


        length = oin.readInt();
        byte[] data = new byte[length];
        oin.readFully(data);
        o = deSerialize(data);

        return o;
    }

    /**
     *
     * @param data seriellisert objekt
     * @return deserielisert objekt
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deSerialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }


    /**
     * send en melding til server og få respons
     * @param msg Melding til server
     * @return Respons fra server
     */
    public static String sendServerMsg(String msg) {

        try {
            out.println(msg);
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Server I/O error");
            return "err";
        }
    }

    /**
     * haandterer feilmeldinger fra server her.
     * @param err Error streng
     */
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


    /**
     * aapner en socket til server med gitt ip adresse
     * @param ip Koble til server på denn ip'en
     * @throws Exception
     */
    public static void open(InetAddress ip) throws Exception {
        try {
            if (ip == null) {
                ClientSocket = new Socket(InetAddress.getLocalHost(), 58339);
                ObjectSocket = new Socket(InetAddress.getLocalHost(), 58339);
                ClientSocket.setSendBufferSize(1048576);
                ObjectSocket.setSendBufferSize(1048576);

            } else {
                ClientSocket = new Socket(ip, 58339);
                ObjectSocket = new Socket(ip, 58339);
                ClientSocket.setSendBufferSize(1048576);
                ObjectSocket.setSendBufferSize(1048576);
            }

            out = new PrintWriter(ClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            oin = new DataInputStream(ObjectSocket.getInputStream());

            String handshake = in.readLine();
            if (handshake.equals("Server Full")) {
                close();
                throw new Exception("Server Full Exception");
            }

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


    /**
     * husk å lukke input og output streams når du er ferdig
     */
    public static void close() {
        try {
            out.close();
            in.close();
            oin.close();
            ClientSocket.close();
            ObjectSocket.close();
        } catch (IOException e) {
            System.out.println("Error closing connection");
        } catch (NullPointerException e) {
            System.out.println("Error closing connection");
        }
    }
}