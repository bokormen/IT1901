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

	public void registerSheep(String id, int age, int weight, char gender) throws Exception {
		Sheep s = new Sheep(id, age, weight, gender);
		sheep.add(s);
		//DatabaseConnector.newSheep(id, null, null, weight, 0, 0, age);
		
	}
	
	public void deleteSheep(Sheep s) {
		sheep.remove(s);
	}
	
	public Sheep sheepSearch(String id) {
		for(Sheep s : sheep) {
			if(s.getId().equals(id)) {
				return s;
			}
		}
		return null;
	}
	

}
