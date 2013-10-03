package div;

import java.util.Random;

public class SheepSim {

	
	public static String newLocation(double latitude, double longitude) {
		Random randomGen = new Random();
		double newLatitude = latitude + (randomGen.nextInt(10) * Math.pow(-1, randomGen.nextInt(1)) );
		double newLongitude = longitude + (randomGen.nextInt(10) * Math.pow(-1, randomGen.nextInt(1)));
		return "" + newLatitude + "," + newLongitude;
	}
	
	public static void main(String[] args) {
		System.out.println(SheepSim.newLocation(10, 7));	
	}
}
