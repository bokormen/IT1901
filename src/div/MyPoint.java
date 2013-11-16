package div;

/**
 * Klasse som holder styr paa hoyde og breddegrad
 * 
 * @author andreas
 * 
 */
public class MyPoint {
	private double latitude;
	private double longitude;

	public MyPoint(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Returnerer objektets hoydegrad
	 * 
	 * @return latitude double
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Returnerer objektets breddegrad
	 * 
	 * @return longitude double
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Metode som setter ny hoydegrad
	 * 
	 * @param latitude
	 *            double
	 */
	public void setX(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Metode som setter ny breddegrad
	 * 
	 * @param longitude
	 *            double
	 */
	public void setY(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Returnerer hoydegrad og breddegrad i en string
	 */
	@Override
	public String toString() {
		return latitude + "," + longitude;
	}

}
