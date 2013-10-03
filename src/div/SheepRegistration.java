package div;

import java.util.ArrayList;

import database.DatabaseConnector;

/**
 * Klassen håndterer registrering, sletting og søk av sauer. 
 * @author Ragnhild
 *
 */
public class SheepRegistration {
	private ArrayList<Sheep> sheep = new ArrayList<Sheep>();

	public void registerSheep(String name, int age, int weight, char gender, String shepherd) throws Exception {
		Sheep s = new Sheep(name, age, weight, gender, shepherd);
		sheep.add(s);
		DatabaseConnector.open();
		DatabaseConnector.newSheep(name, "Per", shepherd, weight, 75, 39, age);
		DatabaseConnector.close();
		
	}
	
	public void deleteSheep(Sheep s) {
		sheep.remove(s);
	}
	

	

	

}
