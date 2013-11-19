package Server;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Klasse for logføring av alle forespørsler fra klienter
 * @author Eivind
 */
public class ServerLog {

    private PrintWriter out = null;
    private static final int bufferLimit = 5;
    private int bufferCount = 0;
    private File logFile = null;


    /**
     * Legger til en ny log melding og dato stempler denne
     * @param entry log melding
     */
    public void addEntry(String entry) {

        Date d = new Date(); //gets time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //formats time
        String str = sdf.format(d) + ": " + entry + "\n"; // put everything together
        out.write(str); //write

        if (bufferCount > bufferLimit) { //flush the buffer and write to file when bufferLimit reached
            out.close();
            try {
                out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
            } catch (IOException e) {
                System.err.println("Error opening log file.");
            }
            bufferCount = 0;
        } else {
            bufferCount++;
        }
        System.out.print(str);
    }


    /**
     * sletter log filen
     */
    public void clearLog() {

        try {
            out.close();
            PrintWriter pw = new PrintWriter(logFile);
            pw.write("");
            pw.close();
            out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
            addEntry("Cleared Log.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * printer innhold av logfilen i konsoll
     */
    public void printLog() {
        try {
            out.close();
            out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));

            BufferedReader in = new BufferedReader(new FileReader(logFile));
            String str;
            while ((str = in.readLine()) != null) {
                System.out.println(str);
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * lukker ut strøm
     */
    public void close() {
        out.close();
    }

    /**
     * konstruktør for klassen
     */
    public ServerLog() {
        try {
            logFile = new File("log.txt");

            if (!logFile.exists()) {
                if (!logFile.createNewFile()) {
                    System.err.println("Error opening log file, no logging enabled");
                    return;
                }
            }

            //out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
            out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));

        } catch (FileNotFoundException e) {
            System.err.println("Could not open log file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
