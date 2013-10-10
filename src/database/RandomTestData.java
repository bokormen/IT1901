package database;

import java.util.*;

public class RandomTestData {
	static Random generator = new Random();
	
	public static void fillDatabaseWithUsers(int numberOfNewUsers) {
		for (int i=0;i<numberOfNewUsers;i++) {
			String user = "test"+i+"@test.test";
			String firstName = "testuser";
			String lastName = Integer.toString(i);
			String tlf = Integer.toString(generator.nextInt(99999999)+10000000);
			String password = Integer.toString(generator.nextInt(99999999)+10000000);
			String location = Double.toString(90*generator.nextDouble())+","+Double.toString(90*generator.nextDouble());
			DatabaseConnector.newUser(user, firstName, lastName, tlf, password, location);
		}
	}
	
	public static void deleteAllTestUsers () {
		ArrayList<String> testUsers = DatabaseConnector.getAllTestUserEmail();
		for (String s : testUsers) {
			DatabaseConnector.deleteUser(s);
		}
	}
}
