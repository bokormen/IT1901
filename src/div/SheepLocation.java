package div;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SheepLocation representerer en sau sin posisjon på et gitt tidspunkt. 
 * @author Ragnhild
 *
 */
public class SheepLocation {

	private final double latitude;
	private final double longitude;
	private final String date;
	
	public SheepLocation(double latitude, double longitude, String date) throws Exception {
		this.latitude = latitude;
		this.longitude = longitude;
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		format.setLenient(false);
		format.parse(date);
		this.date = date;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public String getDate() {
		return date;
	}
	


}
