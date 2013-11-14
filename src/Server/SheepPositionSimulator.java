package Server;

import database.RandomTestData;

/**
 * Created with IntelliJ IDEA.
 * User: Eivind
 * Date: 14.11.13
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */
public class SheepPositionSimulator extends Thread {

    public boolean running = true;
    private String sheepBoundariesLongitude = "63.4259,63.4341";
    private String sheepBoundariesLatitude = "10.3808,10.3992";

    public void run() {
        while (running) {
            System.out.println("Updating Sheeps");
            RandomTestData.moveSheeps(sheepBoundariesLongitude, sheepBoundariesLatitude);
            try {
                Thread.sleep(3600000); //vent en time
            } catch (InterruptedException e) {
                System.out.println("Update Stopped");
                running = false;
            }

        }
    }
}
