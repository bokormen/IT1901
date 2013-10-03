package div;
import java.text.SimpleDateFormat;

/**
 * SheepLocation representerer en sau sin posisjon på et gitt tidspunkt. 
 * @author Ragnhild
 *
 */
public class SheepLocation {

	private final String position;
	private final String date;
	
	public SheepLocation(String position, String date) throws Exception {
		this.position = position;
		//Valider dato
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		format.setLenient(false);
		format.parse(date);
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
	


}
