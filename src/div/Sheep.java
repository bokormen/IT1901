package div;
import java.util.ArrayList;
import java.util.Date;


public class Sheep {

	private final String id;
	private int age;
	private int weight;
	private long temperature;
	private int heartrate;
	private ArrayList<SheepLocation> locations; 
	
	public Sheep(String id) {
		locations = new ArrayList<SheepLocation>();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public int getAge() {
		return age;
	}

	/**
	 * Setter alderen til en sau. Kan være fra 0 til 40. 
	 * @throws Exception 
	 */
	public void setAge(int age) throws Exception {
		if(age > 0 && age < 40) {
			this.age = age;			
		} else {
			throw new Exception("Age not valid");
		}
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) throws Exception {
		if(weight > 0) {
			this.weight = weight;			
		} else {
			throw new Exception("Weight not valid");
		}
	}

	public SheepLocation getLocation() {
		return locations.get(locations.size());
	}
	
	public void newLocation(long latitude, long longitude, Date date) {
		locations.add(new SheepLocation(latitude, longitude, date));
	}
	
	public ArrayList<SheepLocation> getLocationLog() {
		return locations;
	}

	public long getTemperature() {
		return temperature;
	}

	public void setTemperature(long temperature) {
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

	
	
}
