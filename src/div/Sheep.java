package div;
import java.util.ArrayList;
import java.util.Date;

/**
 * Sheep-klassen representerer en sau i systemet og lagrer og validerer generell informasjon om den. 
 * @author Ragnhild
 *
 */
public class Sheep {

	private final String id;
	private int age;
	private int weight;
	private char gender;
	private double temperature;
	private int heartrate;
	private ArrayList<SheepLocation> locations; 
	
	public Sheep(String id, int age, int weight, char gender) throws Exception {
		setAge(age);
		setWeight(weight);
		locations = new ArrayList<SheepLocation>();
		this.id = id;
		if(gender == 'f' || gender == 'm') {
			this.gender = gender;
		} else {
			throw new Exception("Gender not valid");			
		}
	}
	
	public String getId() {
		return id;
	}

	public int getAge() {
		return age;
	}

	/**
	 * Setter alderen til en sau. Kan v�re fra 0 til 40. 
	 * @throws Exception 
	 */
	public void setAge(int age) throws Exception {
		if(age > 0 && age < 40) {
			this.age = age;			
		} else {
			throw new Exception("Age is not valid");
		}
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) throws Exception {
		if(weight > 0) {
			this.weight = weight;			
		} else {
			throw new Exception("Weight is not valid");
		}
	}

	public SheepLocation getLocation() {
		return locations.get(locations.size());
	}
	
	public void newLocation(double latitude, double longitude, Date date) {
		locations.add(new SheepLocation(latitude, longitude, date));
	}
	
	public ArrayList<SheepLocation> getLocationLog() {
		return locations;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public int getHeartrate() {
		return heartrate;
	}

	public void setHeartrate(int heartrate) throws Exception {
		if(heartrate > 0) {
			this.heartrate = heartrate;			
		} else {
			throw new Exception("Heartrate not valid");
		}
	}

	public char getGender() {
		return gender;
	}


	
	
}
