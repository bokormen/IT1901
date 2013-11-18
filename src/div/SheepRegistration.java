package div;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
	 * Registrerer en sau. 
	 * @param name Navnet til sauen
	 * @param birthyear Aret sauen er fodt. Ma vaere over 1980.
	 * @param weight Vekten til sauen. Ma vaere positiv.
	 * @param gender Kjonnet til sauen. Ma vaere 'f' eller 'm' 
	 * @param owner Epostaddressen til eieren til sauen. 
	 * @param shepherd Epostaddressen til gjeteren til sauen.  
	 * @param latitude Latitude til garden
	 * @param longitude Longitude til garden
	 * @throws Exception Feil under registrering. 
	 */
	public int registerSheep(String name, int birthyear, int weight, char gender, String owner, String shepherd, 
			String latitude, String longitude) throws Exception {

		// Lager en foresp0rsel til server.
		// retiningslinjer for kommunikasjon med server vil til en hver tid
		// ligge i server.ComProtocol klassen
		String query = name + "||" + owner + "||" + shepherd + "||" + gender
				+ "||" + weight + "||" + 75 + "||" + 39 + "||" + birthyear + "||" + latitude + "," + longitude;

		// sender foresp0rselen til serveren og faar tilbake respons
		Object serverRespons = ClientConnection.sendObjectQuery(
				"registersheep", query);

		int id = 0;
		// sjekke at melding ble mottatt
		if (serverRespons instanceof Integer) {
			id = (Integer) serverRespons;
		} else if (serverRespons instanceof String) {
			throw new Exception((String) serverRespons);
		}

		String timeStamp = new SimpleDateFormat("yyyy/MM/dd/HH-mm-ss")
				.format(Calendar.getInstance().getTime());
		Sheep tempSheep = new Sheep(id, name, birthyear, weight, gender, owner,
				shepherd);
		tempSheep.newLocation(latitude + "," + longitude, timeStamp);
		sheepList.add(tempSheep);

		return id;

	}

	/**
	 * Endrer informasjonen til en sau. 
	 * @param id Id til sau. 
	 * @param name Navn sauen skal ha. 
	 * @param owner Epostaddressen til eieren av sauen
	 * @param shepherd Endret epostaddresse til gjeter
	 * @param gender Endret kjonn til sau. 'm' eller 'f'. 
	 * @param weight Endret vekt til sau
	 * @param birthyear Endret fodselsar til sau.
	 * @throws Exception Sau kan ikke endres.
	 */
	public void editSheep(int id, String name, String owner, String shepherd, char gender, int weight, int birthyear)
			throws Exception {
		//validerer data
		Sheep s = sheepSearch(id);
		s.setName(name);
		s.setShepherd(shepherd);
		s.setGender(gender);
		s.setWeight(weight);
		s.setBirthyear(birthyear);
		
		//Sender data til server.
		String query = id + "||" + name + "||" + owner + "||" + shepherd + "||"
				+ gender + "||" + weight + "||" + birthyear;
		String serverRespons = ClientConnection.sendServerQuery("changesheep",
				query);
		if (!serverRespons.equals("changesheep success")) {
			System.out.println(serverRespons);
			System.out.println("Error. Can't change sheep information");
		}

	}

	/**
	 * Slett sau. 
	 * @param sheep Sheep-objektet som skal slettes.
	 * @return Om sauen kunne slettes eller ikke. 
	 */
	public boolean deleteSheep(Sheep sheep) {
		String query = "" + sheep.getId();

		String serverRespons = ClientConnection.sendServerQuery("delsheep",
				query);

		if (!serverRespons.equals("delsheep success")) {
			System.out.println("Error. Can't delete sheep");
			return false;
		}

		return sheepList.remove(sheep);
	}

	/**
	 * Soker lokalt etter en sau med en viss id. 
	 * @param id Id til sau
	 * @return Sheep-objektet som blir lett etter. Null dersom den ikke finnes i listen. 
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
	 * Finner en sau i databasen med en gitt eier og id til sau. 
	 * @param user Epostaddressen til brukeren. 
	 * @param id Id til sau
	 * @return Sheep-objektet dersom en sau med denne iden blir funnet, null ellers. 
	 * @throws Exception 
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
	 * @param user Epostaddressen til brukeren. 
	 * @throws Exception
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

	/**
	 * 
	 * @return Listen over sauer. 
	 */
	public ArrayList<Sheep> getSheepList() {
		return sheepList;
	}

	/**
	 * Far tak i listen over sauer som er angrepet fra databasen. 
	 * @param user Epostaddressen til brukeren. 
	 * @return listen over sauer som blir angrepet. Null om noe gar galt. 
	 */
	public ArrayList<String> getAttackedSheepList(String user) {
		String query = user;
		Object serverRespons = ClientConnection.sendObjectQuery("getattackedsheep", query);
		if (serverRespons instanceof ArrayList) {
			return (ArrayList<String>) serverRespons;
		} else if (serverRespons instanceof String) {
			System.out.println("Error. Can't get list of attacked sheep. ");
		}
		return null;

	}

	/**
	 * Sender informasjon om en angrepet sau til server. 
	 * @param id Id til sau
	 * @param user Epostaddressen til bruker. 
	 */
	public void attackSheep(int id, String user) {
		String query = Integer.toString(id);
		String serverRespons = ClientConnection.sendServerQuery("attacksheep",
				query);
		if (!serverRespons.equals("attacksheep success")) {
			System.out.println("Error. Can't add attack information");
		}
	}

	/**
	 * Returnerer de 5 siste posisjonene til en sau. 
	 * @param id Id til sau. 
	 * @return En liste med 5 eller mindre posisjoner. 
	 * @throws Exception
	 */
	public ArrayList<SheepLocation> getLastLocations(int id) throws Exception {
		String query = "" + id + "||" + 5;
		// F�r tilbake et Sheep-objekt
		Object serverRespons = ClientConnection.sendObjectQuery("getsheeplog",
				query);
		if (serverRespons instanceof Sheep) {
			// ((Sheep) serverRespons).getLocationLog();
			ArrayList<SheepLocation> ret = new ArrayList<SheepLocation>();
			for (SheepLocation s : ((Sheep) serverRespons).getLastLocations()) {
				System.out.println(s);
				ret.add(s);
			}
			return ret;
		}
		System.out.println(serverRespons);
		throw new Exception("Error. Can't get last locations");

	}

}
