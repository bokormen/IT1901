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
	
	public SheepRegistration() {
		sheepList = new ArrayList<Sheep>();
	}

	public void registerSheep(int id, int age, int weight, char gender, String shepherd) throws Exception {
		
		sheepList.add(new Sheep(id, age, weight, gender, shepherd));
		
		//Lager en foresp0rsel til server.
        //retiningslinjer for kommunikasjon med server vil til en hver tid ligge i server.ComProtocol klassen
		String query = id + "||" + "Eiernavn" + "||" + shepherd + "||" + weight + "||" + 75 + "||" + 39  + "||" + age;

        //sender foresp0rselen til serveren og faar tilbake respons
        String serverRespons = ClientConnection.sendServerQuery("registersheep", query);

        //sjekke at melding ble mottatt
        if (serverRespons.equals("err")) {
            System.out.println("Error. Can't register user.");
        }
        
		
	}
/*
	public void editSheep(int id, String owner, String shepherd, String gender, int weight, int heartrate, int temperature) {
		String query = id + "||" + owner + "||" + shepherd + "||" + gender + "||" + weight + "||" + heartrate  + "||" + temperature;

        String serverRespons = ClientConnection.sendServerQuery("editsheep", query);

        if (serverRespons.equals("err")) {
            System.out.println("Error. Can't change sheep information");
        }
	}
	*/
	
	public boolean deleteSheep(Sheep s) {
		/*
		String query = "" + s.getId();

        String serverRespons = ClientConnection.sendServerQuery("deletesheep", query);

        if (serverRespons.equals("err")) {
            System.out.println("Error. Can't delete sheep");
        }*/
        
        return sheepList.remove(s);
	}
	
	public Sheep sheepSearch(int id) {
		for(Sheep s : sheepList) {
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	/*
	public Sheep findSheep(String user, String id) {
		String query = user + "||" + id;

        String serverRespons = ClientConnection.sendServerQuery("findsheep", query);

        if (serverRespons.equals("err")) {
            System.out.println("Error. Cannot find sheep");
        }
        return serverRespons;
	}*/

	public ArrayList<Sheep> getSheepList() {
		return sheepList;
	}

	

}
