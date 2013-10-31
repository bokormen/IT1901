package div;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

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
	private boolean attackStatus;

	public Sheep(String name, int birthyear, int weight, char gender, String owner,
			String shepherd) throws Exception {
		this.name = name;
		setBirthyear(birthyear);
		setWeight(weight);
		setOwner(owner);
		setShepherd(shepherd);
		locations = new ArrayList<SheepLocation>();
		if (gender == 'f' || gender == 'm') {
			this.gender = gender;
		} else {
			throw new Exception("Gender not valid");
		}
		attackStatus = false;
	}
	public Sheep(int id, String name, int birthyear, int weight, char gender, String owner,
			String shepherd) throws Exception {
		this.id = id;
		this.name = name;
		setBirthyear(birthyear);
		setWeight(weight);
		setOwner(owner);
		setShepherd(shepherd);
		locations = new ArrayList<SheepLocation>();
		if (gender == 'f' || gender == 'm') {
			this.gender = gender;
		} else {
			throw new Exception("Gender not valid");
		}
	}
	

	public Sheep() {

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
	 * Setter f�dsels�ret til en sau.
	 * 
	 * @throws Exception
	 */
	public void setBirthyear(int birthyear) throws Exception {
		if (birthyear >= 1900) {
			this.birthyear = birthyear;
		} else {
			throw new Exception("Age is not valid");
		}
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) throws Exception {
		if (weight >= 0) {
			this.weight = weight;
		} else {
			throw new Exception("Weight is not valid");
		}
	}

	public SheepLocation getLocation() {
        try {
		    return locations.get(locations.size() - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            //throw new Exception("No location");
            System.err.println("No location available on sheep");
            return new SheepLocation("0,0", "00/00/0000");
        }
    }

	/**
	 * Returnerer de siste posisjonene til sauen. Antall er avhengig av
	 * parameteren num.
	 */
	public ArrayList<SheepLocation> getLastLocations(int num) {
		return (ArrayList<SheepLocation>) locations.subList(locations.size()
				- num, locations.size());
	}

	/**
	 * Legger til ny posisjon til sau. Dato m� v�re p� formen dd/mm/yyyy.
	 */
	public void newLocation(String position, String date) throws Exception {
        SheepLocation sl = new SheepLocation(position, date);
        locations.add(sl);
    }

	public ArrayList<SheepLocation> getLocationLog() {
		return locations;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		if (temperature < 35 || temperature > 40) {
			attackSheep();
		}
		this.temperature = temperature;
	}


	public int getHeartrate() {
		return heartrate;
	}

	public void setHeartrate(int heartrate) throws Exception {
		if (heartrate > 0) {
			this.heartrate = heartrate;
		} else {
			throw new Exception("Heartrate not valid");
		}
		if (heartrate < 50 || heartrate > 100) {
			attackSheep();
		}
	}

	public char getGender() {
		return gender;
	}

	public String getShepherd() {
		return shepherd;
	}

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

	public void setOwner(String owner) throws Exception {
		if (emailIsValid(owner)) {
			this.owner = owner;
		} else {
			throw new Exception("Owner not valid");
		}
	}

	public boolean emailIsValid(String email) {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		if (email.matches(emailPattern)) {
			return true;
		}
		return false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void attackSheep() {
		SheepAttackMail.sendMail(owner, id, getLocation().getPosition());
		if(shepherd != "") {
			SheepAttackMail.sendMail(shepherd, id, getLocation().getPosition());
		}
		attackStatus = true;
	}
	
	public boolean getAttackStatus() {
		return attackStatus;
	}

}
