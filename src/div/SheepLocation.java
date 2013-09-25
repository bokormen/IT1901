package div;
import java.util.Date;

/**
 * SheepLocation representerer en sau sin posisjon på et gitt tidspunkt. 
 * @author Ragnhild
 *
 */
public class SheepLocation {

	private final double latitude;
	private final double longitude;
	private Date date;
	
	public SheepLocation(double latitude, double longitude, Date date) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.date = date;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public Date getDate() {
		return date;
	}

}
