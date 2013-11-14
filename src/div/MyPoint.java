package div;

public class MyPoint {
	private double latitude;
	private double longitude;

	public MyPoint(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setX(double latitude) {
		this.latitude = latitude;
	}

	public void setY(double longitude) {
		this.longitude = longitude;
	}

	public String toString() {
		return latitude + "," + longitude;
	}

}
