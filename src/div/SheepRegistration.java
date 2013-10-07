package div;

import java.util.ArrayList;

import database.DatabaseConnector;

/**
 * Klassen h�ndterer registrering, sletting og s�k av sauer. 
 * @author Ragnhild
 *
 */
public class SheepRegistration {

	public void registerSheep(String name, int age, int weight, char gender, String shepherd) throws Exception {
		DatabaseConnector.open();
		DatabaseConnector.newSheep(name, "Per", shepherd, weight, 75, 39, age);
		DatabaseConnector.close();
		
	}
	

}
