package div;

import java.util.ArrayList;

public class SheepRegistration {
	private ArrayList<Sheep> sheep;

	public void registerSheep(String id, int age, int weight) throws Exception {
		Sheep s = new Sheep(id, age, weight);
		sheep.add(s);
		
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
