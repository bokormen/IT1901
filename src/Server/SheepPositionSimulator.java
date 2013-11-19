package Server;

import database.RandomTestData;

/**
 * Sau posisjon simulering klasse, kjøres av server og opdaterer databasen med sau posisjoner hver time
 * For øyeblikket opdaterer den bare posisjoner innenfor en hvis rekkevidde i fra trondheim sentrum
 * @author Eivind
 */
public class SheepPositionSimulator extends Thread {

    public boolean running = true;
    private String sheepBoundariesLongitude;
    private String sheepBoundariesLatitude;

    /**
     * Konstruktør, setter hvor sauene kan være henn
     */
    public SheepPositionSimulator() {
        sheepBoundariesLongitude = "63.4259,63.4341";
        sheepBoundariesLatitude = "10.3808,10.3992";
    }

    /**
     * starter en ny tråd som opdaterer sau posisjoner hver time
     */
    public void run() {
        while (running) {
            System.out.println("Updating Database: Sheep Positions");
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
