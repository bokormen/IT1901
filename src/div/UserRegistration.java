package div;

import java.util.ArrayList;

public class UserRegistration {
	
	private ArrayList<User> users;

	public void registerUser(String firstName, String lastName, String email, String password, String phoneNr) {
		User user = new User(firstName, lastName, email, password, phoneNr);
		users.add(user);
	}
	
	public void deleteUser(User user) {
		users.remove(user);
	}
	
	public void sendInfoToDatabase() {
		
	}
	

	public User login(String email, String password) {
		for(User user : users) {
			if(email.equals(user.getEmail())) {
				if(password.equals(user.getPassword())) {
					return user;
				} else {
					System.out.println("Password not valid");
				}
			}
		}
		return null;
	}
}
