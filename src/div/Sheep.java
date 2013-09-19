package div;
import java.util.ArrayList;


public class Sheep {

	private final String id;
	private int age;
	private int weight;
	private int healthStatus;
	private ArrayList<String> locations;
	
	public Sheep(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getHealthStatus() {
		return healthStatus;
	}

	public void setHealthStatus(int healthStatus) {
		this.healthStatus = healthStatus;
	}

	public String getLocation() {
		return locations.get(locations.size());
	}
	public void newLocation(String location) {
		if(locationIsValid(location)) {
			locations.add(location);
		} else {
			System.out.println("Location is not valid");
		}
	}

	private boolean locationIsValid(String location) {
		return true;
	}
	
	
}
