package div;
import java.util.ArrayList;
import java.util.Date;


public class Sheep {

	private final String id;
	private int age;
	private int weight;
	private int healthStatus;
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

	public void setAge(int age) {
		if(age > 0 && age < 40) {
			this.age = age;			
		} else {
			System.out.println("Age is not valid");
		}
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
		if(healthStatus > 0 && healthStatus <= 100) {
			this.healthStatus = healthStatus;			
		} else {
			System.out.println("Health status not valid");
		}
	}

	public SheepLocation getLocation() {
		return locations.get(locations.size());
	}
	
	public void newLocation(long latitude, long longitude, Date date) {
		locations.add(new SheepLocation(latitude, longitude, date));
	}

	
	
}
