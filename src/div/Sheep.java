package div;
import java.util.ArrayList;
import java.util.Date;

/**
 * Sheep-klassen representerer en sau i systemet og lagrer og validerer generell informasjon om den. 
 * @author Ragnhild
 *
 */
public class Sheep {

	private String name;
	private int age;
	private int weight;
	private char gender;
	private double temperature;
	private int heartrate;
	private String shepherd;
	private ArrayList<SheepLocation> locations; 
	
	public Sheep(String name, int age, int weight, char gender, String shepherd) throws Exception {
		setAge(age);
		setWeight(weight);
		setShepherd(shepherd);
		locations = new ArrayList<SheepLocation>();
		this.name = name;
		if(gender == 'f' || gender == 'm') {
			this.gender = gender;
		} else {
			throw new Exception("Gender not valid");			
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	/**
	 * Setter alderen til en sau. Kan være fra 0 til 40. 
	 * @throws Exception 
	 */
	public void setAge(int age) throws Exception {
		if(age >= 0 && age < 40) {
			this.age = age;			
		} else {
			throw new Exception("Age is not valid");
		}
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) throws Exception {
		if(weight >= 0) {
			this.weight = weight;			
		} else {
			throw new Exception("Weight is not valid");
		}
	}

	public SheepLocation getLocation() {
		return locations.get(locations.size()-1);
	}
	
	/**
	 * Legger til ny posisjon til sau. Dato må være på formen dd/mm/yyyy. 
	 */
	public void newLocation(String position, String date) throws Exception {
		locations.add(new SheepLocation(position, date));
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

	public String getShepherd() {
		return shepherd;
	}

	public void setShepherd(String shepherd) throws Exception {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		if (shepherd.matches(emailPattern)) {
			this.shepherd = shepherd;
		} else {
			throw new Exception("Email not valid");
		}
	}


	
	
}
