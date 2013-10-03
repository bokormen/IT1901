package div;

import java.util.Random;

public class SheepSim {

	
	public static String newLocation(double oldLatitude, double oldLongitude) {
		Random randomGen = new Random();
		double newLatitude = 91;
		double newLongitude = 181;
		while(newLatitude > 90 || newLatitude < -90 || newLongitude > 180 || newLongitude < -180) {
			newLatitude = oldLatitude + (randomGen.nextInt(10) * Math.pow(-1, randomGen.nextInt(1)) );
			newLongitude = oldLongitude + (randomGen.nextInt(10) * Math.pow(-1, randomGen.nextInt(1)));
		}
		return "" + newLatitude + "," + newLongitude;
	}
	
	public static double newTemperature(double oldTemp) {
		Random randomGen = new Random();
		double newTemp = -20;
		while (newTemp > 50 || newTemp < -10) {
			newTemp = oldTemp + (randomGen.nextInt(3) * Math.pow(-1, randomGen.nextInt(1)));
		}
		return newTemp;
	}
	
	public static int newHeartRate(int oldHR) {
		Random randomGen = new Random();
		int newHR = -1;
		while (newHR <= 0 || newHR > 200 ) {
			newHR = oldHR + ((int) ((randomGen.nextInt(10) * Math.pow(-1, randomGen.nextInt(1)) )));
		}
		return newHR;
	}
	
}
