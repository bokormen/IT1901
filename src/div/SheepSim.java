package div;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class SheepSim {
	
	private ArrayList<Sheep> sheepList;
	
	/**
	 * Legger til en ny posisjon til sauen, og oppdaterer puls og temperatur. 
	 */
	public static void updateSheep(Sheep s) {
		try {
			s.setHeartrate(newHeartRate(s.getHeartrate()));
			double oldLatitude = Double.parseDouble(s.getLocation().getLatitude());
			double oldLongitude = Double.parseDouble(s.getLocation().getLongitude());
			s.newLocation(newLocation(oldLatitude, oldLongitude),"07/10/2013");
			s.setHeartrate(newHeartRate(s.getHeartrate()));
			
		} catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	/**
	 * Returnerer en ny posisjon basert på den gamle som blir sendt inn. 
	 */
	public static String newLocation(double oldLatitude, double oldLongitude) {
		Random randomGen = new Random();
		double newLatitude = 91;
		double newLongitude = 181;
		while(newLatitude > 90 || newLatitude < -90 || newLongitude > 180 || newLongitude < -180) {
			newLatitude = oldLatitude + (randomGen.nextInt(5) * Math.pow(-1, randomGen.nextInt(1)) );
			newLongitude = oldLongitude + (randomGen.nextInt(5) * Math.pow(-1, randomGen.nextInt(1)));
		}
		return "" + newLatitude + "," + newLongitude;
	}
	
	/**
	 * Returnerer en ny temperatur basert på den gamle til sauen. 
	 */
	public static double newTemperature(double oldTemp) {
		Random randomGen = new Random();
		double newTemp = -20;
		while (newTemp > 50 || newTemp < -10) {
			newTemp = oldTemp + (randomGen.nextInt(1) * Math.pow(-1, randomGen.nextInt(1)));
		}
		return newTemp;
	}
	/**
	 * Returnerer ny puls basert på den gamle. 
	 */
	public static int newHeartRate(int oldHR) {
		Random randomGen = new Random();
		int newHR = -1;
		while (newHR <= 0 || newHR > 200 ) {
			newHR = oldHR + ((int) ((randomGen.nextInt(3) * Math.pow(-1, randomGen.nextInt(1)) )));
		}
		return newHR;
	}
	
}
