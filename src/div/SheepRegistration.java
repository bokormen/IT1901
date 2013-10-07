package div;

import java.util.ArrayList;

import database.DatabaseConnector;

/**
 * Klassen håndterer registrering, sletting og søk av sauer. 
 * @author Ragnhild
 *
 */
public class SheepRegistration {
	
	private ArrayList<Sheep> sheepList;
	private int sheepCounter;
	
	public SheepRegistration() {
		sheepList = new ArrayList<Sheep>();
	}

	public void registerSheep(int id, int age, int weight, char gender, String shepherd) throws Exception {
		
		//Lager en foresp0rsel til server.
        //retiningslinjer for kommunikasjon med server vil til en hver tid ligge i server.ComProtocol klassen
		String query = id + "||" + "Eiernavn" + "||" + shepherd + "||" + weight + "||" + 75 + "||" + 39  + "||" + age;

        //sender foresp0rselen til serveren og faar tilbake respons
        String serverRespons = ClientConnection.sendServerQuery("registersheep", query);

        //sjekke at melding ble mottatt
        if (serverRespons.equals("err")) {
            System.out.println("Error. Can't register user.");
        }
        
        sheepList.add(new Sheep(id, age, weight, gender, shepherd));
		
	}

	
	public boolean deleteSheep(Sheep s) {
		return sheepList.remove(s);
	}
	
	public Sheep sheepSearch(int id) {
		return null;
	}

	public ArrayList<Sheep> getSheepList() {
		return sheepList;
	}
	

}
