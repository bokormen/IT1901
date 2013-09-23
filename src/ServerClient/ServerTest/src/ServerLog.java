import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ServerLog {

    private BufferedReader in = null;
    private PrintWriter out = null;
    private static final int bufferLimit = 5;
    private int bufferCount = 0;
    private File logFile = null;


    //Prints a log entry to file
    public void addEntry(String entry) throws IOException {

        Date d = new Date(); //gets time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //formats time
        String str = sdf.format(d) + ": " + entry + "\n"; // put everything together
        out.write(str); //write

        if (bufferCount > bufferLimit) { //flush the buffer and write to file when bufferLimit reached
            out.close();
            out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
            bufferCount = 0;
        } else {
            bufferCount++;
        }
        System.out.print(str);
    }

    public void printLog() {
        try {
            in.mark(52428800);
            String str;
            while ((str = in.readLine()) != null) {
                System.out.println(str);
            }
            in.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void close() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    // constructor
    public ServerLog() {
        try {
            logFile = new File("log.txt");

            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            //out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
            out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
            in = new BufferedReader(new FileReader("log.txt"));

        } catch (FileNotFoundException e) {
            System.err.println("Could not open log file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
