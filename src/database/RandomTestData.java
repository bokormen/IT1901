package database;

import java.util.*;

//import div.Sheep;

public class RandomTestData {
	
	/**
	 * En funksjon for aa generere tilfeldige verdier
	 */
	static Random generator = new Random();
	
	/**
	 * Fuller databasen med en oppgitt mengde brukere, det kan eksistere testbrukere i databasen fra foer av
	 * @param numberOfNewUsers
	 * @author Oeyvind
	 */
	public static void fillDatabaseWithUsers(int numberOfNewUsers) {
		int exsistingUsers = DatabaseConnector.getLatestTestUser()+1;
		for (int i=0;i<numberOfNewUsers;i++) {
			int lastname = i+exsistingUsers;
			String user = "test"+lastname+"@test.test";
			String firstName = "testuser";
			String lastName = uppercaseFirstLetters(EnglishNumberToWords.convert(lastname)).replaceAll("\\W", "");
			String tlf = Integer.toString(generator.nextInt(89999999)+10000000);
			String password = "passord";
			String location = "63.43,10.39";//Double.toString(90*generator.nextDouble())+","+Double.toString(90*generator.nextDouble());
			DatabaseConnector.newUser(user, firstName, lastName, tlf, password, location);
		}
	}
	
	/**
	 * Gir alle testbrukerene en vilkaarlig mengde sauer som ikke overstiger maxNumberOfSheepsPrUser
	 * @param maxNumberOfSheepsPrUser
	 * @author Oeyvind
	 */
	public static void randomSheepsForTestUsers (int maxNumberOfSheepsPrUser) {
		ArrayList<String> testUsers = DatabaseConnector.getAllTestUserEmail();
		for (String s : testUsers) {
			int numberOfSheeps= maxNumberOfSheepsPrUser; //generator.nextInt(maxNumberOfSheepsPrUser);
			for (int i=0;i<numberOfSheeps;i++) {
				String name = "testSheep";
				String owner = s;
				String gender = Gender.getRandomGender();
				int weight = generator.nextInt(10)+20;
				int heartrate = generator.nextInt(30)+60;
				int birthyear = generator.nextInt(12)+2001;
				int temperature = generator.nextInt(2)+38;
				DatabaseConnector.newSheep(name, owner, "", gender, Integer.toString(weight), Integer.toString(heartrate), Integer.toString(temperature), Integer.toString(birthyear));
			}
		}
	}
	
	/**
	 * Fyller databsesn med antall sauer
	 * @param numberOfSheep
	 * @author Oeyvind
	 */
	public static void sheepsForTestUsers (int numberOfSheep) {
		ArrayList<String> testUsers = DatabaseConnector.getAllTestUserEmail();
		testUsers.remove(0);
		for (String s : testUsers) {
			for (int i=0;i<numberOfSheep;i++) {
				String name = "testSheep";
				String owner = s;
				String gender = Gender.getRandomGender();
				int weight = generator.nextInt(10)+20;
				int heartrate = generator.nextInt(30)+60;
				int birthyear = generator.nextInt(12)+2001;
				int temperature = generator.nextInt(2)+38;
				DatabaseConnector.newSheep(name, owner, "", gender, Integer.toString(weight), Integer.toString(heartrate), Integer.toString(temperature), Integer.toString(birthyear));
			}
		}
	}
	
	/**
	 * Tar inn en e-post og en int og generer det antall sauer som inten angir til brukeren av e-posten
	 * @param owner
	 * @param numberOfSheep
	 * @author Oeyvind
	 */
	public static void sheepsForTestUser (String owner, int numberOfSheep) {
			for (int i=0;i<numberOfSheep;i++) {
				String name = "testSheep";
				String gender = Gender.getRandomGender();
				int weight = generator.nextInt(10)+20;
				int heartrate = generator.nextInt(30)+60;
				int birthyear = generator.nextInt(12)+2001;
				int temperature = generator.nextInt(2)+38;
				DatabaseConnector.newSheep(name, owner, "", gender, Integer.toString(weight), Integer.toString(heartrate), Integer.toString(temperature), Integer.toString(birthyear));
			}
	}
	
	/**
	 * Oppretter en ny posisjon til en sau innenfor et kvadratisk omraade
	 * @param confinementsLongitude
	 * @param confinementsLatitude
	 * @author Oeyvind
	 */
	public static void moveSheeps(String confinementsLongitude, String confinementsLatitude)  {
		String date = nextDate();
		double maxLongitude;
		double minLongitude;
		double maxLatitude;
		double minLatitude;
		
		String[] longitude = confinementsLongitude.split(",");
		maxLongitude = Double.parseDouble(longitude[1]);
		minLongitude = Double.parseDouble(longitude[0]);
		
		String[] latitude = confinementsLatitude.split(",");
		maxLatitude = Double.parseDouble(latitude[1]);
		minLatitude = Double.parseDouble(latitude[0]);
		
		ArrayList<String> IDs = DatabaseConnector.getAllSheepIDs();
		
		for (String s : IDs) {
			double sheepLongitude = generator.nextDouble()*(maxLongitude-minLongitude)+minLongitude;
			double sheepLatitude = generator.nextDouble()*(maxLatitude-minLatitude)+minLatitude;
			String posistion = sheepLongitude+","+sheepLatitude;
			DatabaseConnector.newPosition(s, date, posistion);
		}
	}
	
	/**
	 * Returner en dato 8 timer senere enn den siste datoen i databasen;
	 * @return
	 * @author Oeyvind
	 */
	public static String nextDate() {
		String date = DatabaseConnector.getLatetsDate();
		String punktum = "\\.";
		String[] splitDate = date.split(punktum);
		int time = Integer.parseInt(splitDate[3])+8;
		int day = Integer.parseInt(splitDate[2]);
		int month = Integer.parseInt(splitDate[1]);
		int year = Integer.parseInt(splitDate[0]);
		if (time>23) {
			time = 00;
			day++;
		}
		if (day>daysInMonth(month, year)) {
			day = 01;
			month+=1;
		}
		if (month>12) {
			month = 01;
			year++;
		}
		String format = String.format("%%0%dd", 2);
		String timeFinished = String.format(format, time);
		String dayFinished = String.format(format, day);
		String monthFinished = String.format(format, month);
		String yearFinished = Integer.toString(year);
		date=yearFinished+"."+monthFinished+"."+dayFinished+"."+timeFinished+".00";
		return date;
	}
	
	/**
	 * En enum med verdier for kjoenn, med en funksjon for aa generere et tilfeldig kjoenn
	 * @author Oeyvind
	 *
	 */
	private enum Gender {       
		f,
		m;
		
		/**
		 * Denne funksjonen genrerere et tilfeldig kjoenn som det returnerer
		 * @return
		 * @author Oeyvind
		 */
		public static String getRandomGender() {            
			Random random = new Random();
			return values()[random.nextInt(values().length)].name();
			}
		}
	
	/**
	 * Sjekker om det er skuddaar, returnerer true hvis det er skuddaar
	 * @param year
	 * @return
	 * @author Oeyvind
	 */
	public static boolean isLeapYear ( int year){
	    boolean leapYear = false;
	    if (year%4==0){
	        leapYear = true;
	        if(year>1582){
	            if (year%100==0&&year%400!=0){
	                leapYear=false;
	            }
	        }
	    }
	    return leapYear;
	}
	
	/**
	 * Sjekker hvor mange dager det er i en den insendte maanden og returnerer en int med antall dager.
	 * @param month
	 * @param year
	 * @return
	 * @author Oeyvind
	 */
	public static int daysInMonth(int month, int year) {
		int days = 0;
		
		if (month == 2 && isLeapYear(year)) {
			days = 29;
		} else if (month == 2) {
			days = 28;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			days = 30;
		} else {
			days = 31;
		}
		
		return days;
	}
	
	/**
	 * Gjoer alle de foerste bokstavene i hvert ord en en string til en stor bokstav
	 * @param str
	 * @return
	 * @author Oeyvind
	 */
	public static String uppercaseFirstLetters(String str) 
	{
	    boolean prevWasWhiteSp = true;
	    char[] chars = str.toCharArray();
	    for (int i = 0; i < chars.length; i++) {
	        if (Character.isLetter(chars[i])) {
	            if (prevWasWhiteSp) {
	                chars[i] = Character.toUpperCase(chars[i]);    
	            }
	            prevWasWhiteSp = false;
	        } else {
	            prevWasWhiteSp = Character.isWhitespace(chars[i]);
	        }
	    }
	    return new String(chars);
	}
	
}
