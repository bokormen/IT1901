package database;

import java.util.*;

public class RandomTestData {
	static Random generator = new Random();
	
	public static void fillDatabaseWithUsers(int numberOfNewUsers) {
		for (int i=0;i<numberOfNewUsers;i++) {
			String user = "test"+i+"@test.test";
			String firstName = "testuser";
			String lastName = Integer.toString(i);
			String tlf = Integer.toString(generator.nextInt(89999999)+10000000);
			String password = Integer.toString(generator.nextInt(89999999)+10000000);
			String location = Double.toString(90*generator.nextDouble())+","+Double.toString(90*generator.nextDouble());
			DatabaseConnector.newUser(user, firstName, lastName, tlf, password, location);
		}
	}
	
	public static void sheepsForTestUsers (int maxNumberOfSheepsPrUser) {
		ArrayList<String> testUsers = DatabaseConnector.getAllTestUserEmail();
		for (String s : testUsers) {
			int numberOfSheeps=generator.nextInt(maxNumberOfSheepsPrUser);
			for (int i=0;i<numberOfSheeps;i++) {
				String name = "testSheep";
				String owner = s;
				String gender = Gender.getRandomGender();
				int weight = generator.nextInt(30)+40;
				int heartrate = generator.nextInt(100)+50;
				int birthyear = generator.nextInt(12)+2001;
				int temperature = generator.nextInt(7)+30;
				DatabaseConnector.newSheep(name, owner, "", gender, Integer.toString(weight), Integer.toString(heartrate), Integer.toString(temperature), Integer.toString(birthyear));
			}
		}
	}
	
	private enum Gender {       
		f,
		m;

		public static String getRandomGender() {            
			Random random = new Random();
			return values()[random.nextInt(values().length)].name();
			}
		}
	
}
