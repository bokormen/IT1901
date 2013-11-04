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

	public SheepRegistration() {

	}
	
	/**
	 * Registrerer en sau i databasen med navn, fodselsar, vekt, kjonn, eier og gjeter. 
	 */
	public void registerSheep(String name, int birthyear, int weight, char gender,
			String owner, String shepherd) throws Exception {

		sheepList.add(new Sheep(name, birthyear, weight, gender, owner, shepherd));

		// Lager en foresp0rsel til server.
		// retiningslinjer for kommunikasjon med server vil til en hver tid
		// ligge i server.ComProtocol klassen
		String query = name + "||" + owner + "||" + shepherd + "||" + gender
				+ "||" + weight + "||" + 75 + "||" + 39 + "||" + birthyear;

		// sender foresp0rselen til serveren og faar tilbake respons
		String serverRespons = ClientConnection.sendServerQuery(
				"registersheep", query);

		// sjekke at melding ble mottatt
		if (serverRespons.equals("err")) {
			System.out.println("Error. Can't register sheep.");
		}

	}
	/**
	 * Endrer en sau sine data. Kan endre navn, gjeter, kj�nn og f�dsels�r. 
	 */
	public void editSheep(int id, String name, String owner, String shepherd, char gender, int weight, int birthyear)
			throws Exception {
		Sheep s = sheepSearch(id);
		s.setName(name);
		s.setShepherd(shepherd);
		s.setWeight(weight);
		s.setBirthyear(birthyear);
		 String query = id + "||" + name + "||" + owner + "||" + shepherd + "||" + gender + "||" + weight + "||" + birthyear;
		 String serverRespons = ClientConnection.sendServerQuery("changesheep",query);
		 if(!serverRespons.equals("changesheep success")) {
			 System.out.println("Error. Can't change sheep information"); 
		 }
		 
	}
	/**
	 * Seltter en sau fra databasen. Returnerer true dersom alt gikk bra, false ellers. 
	 */
	public boolean deleteSheep(Sheep s) {
		String query = "" + s.getId();

		String serverRespons = ClientConnection.sendServerQuery("delsheep",
				query);

		if (serverRespons.equals("err")) {
			System.out.println("Error. Can't delete sheep");
			return false;
		}

		return sheepList.remove(s);
	}
	/**
	 * Soker og returnerer sauen med en viss id. 
	 */
	public Sheep sheepSearch(int id) {
		for (Sheep s : sheepList) {
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	/**
	 * Finner en sau i databasen med en gitt eier og id p� sau. 
	 */
	public Sheep findSheep(String user, String id) throws Exception {
		String query = user + "||" + id;

		Object serverRespons = ClientConnection.sendObjectQuery("findsheep",
				query);

		if (serverRespons instanceof Sheep) {
			return (Sheep) serverRespons;
		} else if (serverRespons instanceof String) {
			if (serverRespons.equals("findsheep no login")) {
				throw new Exception("Not logged in");
			} else if (serverRespons.equals("findsheep null input")) {
				throw new Exception("Null input");
			}
		}
		return null;
	}
	/**
	 * Fyller opp sheepList med sauer fra databasen. 
	 */
	public void updateSheepList(String user) throws Exception {
		String query = user;

		Object serverRespons = ClientConnection.sendObjectQuery(
				"getownedsheep", query);

		if (serverRespons instanceof ArrayList) {
			this.sheepList = (ArrayList<Sheep>) serverRespons;
		} else if (serverRespons instanceof String) {
			if (serverRespons.equals("getownedsheep no login")) {
				throw new Exception("Not logged in");
			} else if (serverRespons.equals("getownedsheep null input")) {
				throw new Exception("Null input");
			}
		}
	}

	public ArrayList<Sheep> getSheepList() {
		return sheepList;
	}

}
