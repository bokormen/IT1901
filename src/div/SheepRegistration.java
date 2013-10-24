package div;

import java.io.Serializable;
import java.util.ArrayList;

import database.DatabaseConnector;

/**
 * Klassen handterer registrering, sletting og sok av sauer.
 * 
 * @author Ragnhild
 * 
 */
public class SheepRegistration implements Serializable {

	private ArrayList<Sheep> sheepList;

	public SheepRegistration(String user) throws Exception {
		//sheepList = updateSheepList(user);
		
	}

	public void registerSheep(int id, int birthyear, int weight, char gender, String owner, String shepherd) throws Exception {

		sheepList.add(new Sheep(id, birthyear, weight, gender, owner, shepherd));

		// Lager en foresp0rsel til server.
		// retiningslinjer for kommunikasjon med server vil til en hver tid
		// ligge i server.ComProtocol klassen
		String query = id + "||" + owner + "||" + shepherd + "||" + gender + "||" + weight + "||" + 75 + "||" + 39 + "||" + birthyear;

		// sender foresp0rselen til serveren og faar tilbake respons
		String serverRespons = ClientConnection.sendServerQuery("registersheep", query);

		// sjekke at melding ble mottatt
		if (serverRespons.equals("err")) {
			System.out.println("Error. Can't register sheep.");
		}

	}

	 public void editSheep(int id, String shepherd, int weight, int birthyear) throws Exception {
		 Sheep s = sheepSearch(id);
		 s.setShepherd(shepherd);
		 s.setWeight(weight);
		 s.setBirthyear(birthyear);
		 /*
		 String query = id + "||" + shepherd + "||" + age + "||" + weight;
		 String serverRespons = ClientConnection.sendServerQuery("editsheep",query); 
		 if (serverRespons.equals("err")) {
			 System.out.println("Error. Can't change sheep information"); } 
		  */
	 }
	public boolean deleteSheep(Sheep s) {
		String query = "" + s.getId();

		String serverRespons = ClientConnection.sendServerQuery("delsheep", query);
		
		if (serverRespons.equals("err")) {
			System.out.println("Error. Can't delete sheep");
			return false;
		}

		return sheepList.remove(s);
	}

	public Sheep sheepSearch(int id) {
		for (Sheep s : sheepList) {
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}


	 public Sheep findSheep(String user, String id) throws Exception { 
	 String query = user + "||" + id;
	  
	 Object serverRespons = ClientConnection.sendObjectQuery("findsheep", query);
	 
	 if(serverRespons instanceof Sheep) {
		 return (Sheep) serverRespons;
	 } else if(serverRespons instanceof String) {
		 if(serverRespons.equals("findsheep no login")) {
			 throw new Exception("Not logged in");
		 } else if(serverRespons.equals("findsheep null input")) {
			 throw new Exception("Null input");
		 }
	 }
	 return null;
	 }

	public void updateSheepList(String user) throws Exception {
		String query = user;
		  
		 Object serverRespons = ClientConnection.sendObjectQuery("getownedsheep", query);
		 
		 if(serverRespons instanceof ArrayList) {
             this.sheepList = (ArrayList<Sheep>) serverRespons;
         } else if(serverRespons instanceof String) {
			 if(serverRespons.equals("getownedsheep no login")) {
				 throw new Exception("Not logged in");
			 } else if(serverRespons.equals("getownedsheep null input")) {
				 throw new Exception("Null input");
			 }
		 }
	}
	
	public ArrayList<Sheep> getSheepList() {
		return sheepList;
	}

}
