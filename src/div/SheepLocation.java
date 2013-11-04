package div;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * SheepLocation representerer en sau sin posisjon pa et gitt tidspunkt. 
 * @author Ragnhild
 *
 */
public class SheepLocation implements Serializable {

	private final String position;
	private final String date;
	
	/**
	 * 
	 * @param position Posisjon på formen <latitude>,<longitude>
	 * @param date
	 */
	public SheepLocation(String position, String date) {
		this.position = position;
		this.date = date;
	}

	
	public String getDate() {
		return date;
	}

	public String getPosition() {
		return position;
	}
	
	public String toString() {
		return position + "-" + date;
	}
	
	public String getLatitude() {
		String[] positionA = position.split(",");
		return positionA[0];
	}
	
	public String getLongitude() {
		String[] positionA = position.split(",");
		return positionA[1];
	}
	


}
