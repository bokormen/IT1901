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

	public void registerSheep(int id, int age, int weight, char gender, String shepherd) throws Exception {
		Sheep s = new Sheep(id, age, weight, gender, shepherd);
		sheep.add(s);
		//DatabaseConnector.newSheep(id, null, shepherd, weight, 0, 0, age);
		
	}
	
	public void deleteSheep(Sheep s) {
		sheep.remove(s);
	}
	
	public Sheep sheepSearch(long id) {
		for(Sheep s : sheep) {
			if(s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	

	

}
