package div;
import java.util.Date;


public class SheepLocation {

	private long latitude;
	private long longitude;
	private Date date;
	
	public SheepLocation(long latitude, long longitude, Date date) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.date = date;
	}

	public long getLatitude() {
		return latitude;
	}

	public long getLongitude() {
		return longitude;
	}
	
	public Date getDate() {
		return date;
	}

}
