package div;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Sheep-klassen representerer en sau i systemet og lagrer og validerer generell
 * informasjon om den.
 * 
 * @author Ragnhild
 * 
 */
public class Sheep implements Serializable {

	private int id;
	private String name;
	private int birthyear;
	private int weight;
	private char gender;
	private double temperature;
	private int heartrate;
	private String owner;
	private String shepherd;
	private ArrayList<SheepLocation> locations;


	/**
	 * 
	 * @param id Id til sau
	 * @param name Navn til sau
	 * @param birthyear Fodselsaret til sau ma vaere minst 1980.
	 * @param weight Vekten til sau ma vaere positiv. 
	 * @param gender Kjonn til sau. Ma vaere 'f' eller 'm'.
	 * @param owner Epostaddressen til eieren av sauen. 
	 * @param shepherd Epostaddressen til gjeteren. 
	 * @throws Exception
	 */
	public Sheep(int id, String name, int birthyear, int weight, char gender, String owner, String shepherd) throws Exception {
		this.id = id;
		this.name = name;
		setBirthyear(birthyear);
		setWeight(weight);
		setOwner(owner);
		setShepherd(shepherd);
		locations = new ArrayList<SheepLocation>();
		setGender(gender);
	}

	public Sheep() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBirthyear() {
		return birthyear;
	}

	/**
	 * Setter fodselsaret til en sau. Ma være minst 1980, men ikke storren enn natid.
	 * @param birthyear Fodselsaret til sau.
	 * @throws Exception
	 */
	public void setBirthyear(int birthyear) throws Exception {
		Calendar cal = Calendar.getInstance();
		if (birthyear >= 1980 && birthyear <= cal.get(Calendar.YEAR)) {
			this.birthyear = birthyear;
		} else {
			throw new Exception("Age is not valid");
		}
	}

	public int getWeight() {
		return weight;
	}

	/**
	 * Setter vekten til en sau.
	 * @param weight Vekten må vaere positiv. 
	 * @throws Exception
	 */
	public void setWeight(int weight) throws Exception {
		if (weight >= 0) {
			this.weight = weight;
		} else {
			throw new Exception("Weight is not valid");
		}
	}

	public char getGender() {
		return gender;
	}

	public double getTemperature() {
		return temperature;
	}

	/**
	 * Setter temperaturen
	 * @param temperature Temperatur til sau.
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public int getHeartrate() {
		return heartrate;
	}

	/**
	 * Setter pulsen til en sau. Ma vaere positiv.
	 * @throws Exception
	 */
	public void setHeartrate(int heartrate) throws Exception {
		if (heartrate >= 0) {
			this.heartrate = heartrate;
		} else {
			throw new Exception("Heartrate not valid");
		}

	}

	public String getShepherd() {
		return shepherd;
	}

	/**
	 * Setter gjeteren til sauen dersom epostaddressen er valid. 
	 * @param shepherd Epostaddressen til gjeteren. Ma vaere på forme eksempel@eksempel.eks
	 * @throws Exception
	 */
	public void setShepherd(String shepherd) throws Exception {
		if (shepherd.equals("")) {
			this.shepherd = "";
		} else if (emailIsValid(shepherd)) {
			this.shepherd = shepherd;
		} else {
			throw new Exception("Shepherd not valid");
		}
	}

	public String getOwner() {
		return owner;
	}

	/**
	 * Setter eieren av sauen. 
	 * @param owner Epostaddressen til eieren av sauen. Ma vaere pa formen eksempel@eksempel.eks
	 * @throws Exception
	 */
	public void setOwner(String owner) throws Exception {
		if (emailIsValid(owner)) {
			this.owner = owner;
		} else {
			throw new Exception("Owner not valid");
		}
	}

	/**
	 * 
	 * @return Den siste posisjonen til sauen. 
	 */
	public SheepLocation getLocation() {
		if(locations.size() != 0) {
			return locations.get(locations.size()-1);
		} 
		return null;
	}

	/**
	 * 
	 * @return Returnerer de 5 siste posisjonene til sauen, eller sa mange den har. 
	 */
	public List<SheepLocation> getLastLocations() {
		int num = 5;
		if (locations.size() < 5) {
			num = locations.size() - 1;
		}
		return locations.subList(locations.size() - num, locations.size());
	}

	/**
	 * Legger til en ny posisjon til sauen. 
	 * @param position Den nye posisjonen
	 * @param date Datoen den blir lagt til
	 * @throws Exception
	 */
	public void newLocation(String position, String date) throws Exception {
		SheepLocation sl = new SheepLocation(position, date);
		locations.add(sl);
	}

	/**
	 * 
	 * @return Hele loggen over posisjonene til sauen. 
	 */
	public ArrayList<SheepLocation> getLocationLog() {
		return locations;
	}

	/**
	 * Validerer epostaddresser. 
	 * @param email Epostaddressen som skal valideres
	 * @return Om addressen er valid eller ikke. 
	 */
	public boolean emailIsValid(String email) {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		if (email.matches(emailPattern)) {
			return true;
		}
		return false;
	}

	/**
	 * Setter sauens kjonn. 
	 * @param gender Kjonnet ma vaere 'f' eller 'm'
	 * @throws Exception 
	 */
	public void setGender(char gender) throws Exception {
		if (gender == 'f' || gender == 'm') {
			this.gender = gender;
		} else {
			throw new Exception("Gender not valid");
		}

	}


}
