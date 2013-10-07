package gui;

import java.util.ArrayList;

import div.SendMail;
import div.Sheep;
import div.User;
import div.UserRegistration;

public class Server {
	private static UserRegistration userReg = new UserRegistration();
	private static SendMail sendMail = new SendMail();

	private static User currentUser;

	public static void addUsers() {
		String[] forNavn = { "Aragorn", "Arwen", "Bilbo", "Boromir", "Gimlo", "Elrond", "Faramir", "Frodo", "Gandalf",
				"Gollum" };
		String[] etterNavn = { "Legolas", "Pippin", "Merry", "Sam", "Saruman", "Treebeard", "Isidur", "Fili", "Kili",
				"Gorkil" };
		String[] email = { "Aragorn@hotmail.com", "Arwen@hotmail.com", "Bilbo@hotmail.com", "Boromir@hotmail.com",
				"Gimlo@hotmail.com", "Elrond@hotmail.com", "Faramir@hotmail.com", "Frodo@hotmail.com",
				"Gandalf@hotmail.com", "Gollum@hotmail.com" };
		String[] password = { "passord123", "passord123", "passord123", "passord123", "passord123", "passord123",
				"passord123", "passord123", "passord123", "passord123" };
		String[] phone = { "12345678", "12345678", "12345678", "12345678", "12345678", "12345678", "12345678",
				"12345678", "12345678", "12345678" };

		for (int i = 0; i < forNavn.length; i++) {
			try {
				//userReg.registerUser(forNavn[i], etterNavn[i], email[i], password[i], phone[i]);
			} catch (Exception e) {
				System.out.println("fuuuck");
				e.printStackTrace();
			}
		}
		try {
			//userReg.getUsers().get(7).setLatitude("63.43");
			//userReg.getUsers().get(7).setLongitude("10.39");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int sendInformationRegisterUser(String firstName, String lastName, String email, String password,
			String phoneNr) {
		try {
			userReg.registerUser(firstName, lastName, email, password, phoneNr, "0");
			System.out.println("Registred user: " + firstName + " " + lastName);
			return 1;
		} catch (Exception e) {
			if (e.getMessage().equals("First name is not valid")) {
				return -1;
			} else if (e.getMessage().equals("Last name is not valid")) {
				return -2;
			} else if (e.getMessage().equals("Email is not valid")) {
				return -3;
			} else if (e.getMessage().equals("Phone number not valid")) {
				return -4;
			} else if (e.getMessage().equals("Password is not valid")) {
				return -5;
			}

			System.out.println(e.getLocalizedMessage() + "\n" + e.getMessage());
			System.out.println("Could not register user.");
			return -1;
		}
	}

	public static User sendInformationLogin(String email, String password) {
		try {
			User u = userReg.login(email, password);
			currentUser = u;
			return u;
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			return null;
		}
	}

	public static boolean sendInformationSendEmail(String email) {
		for (User u : userReg.getUsers()) {
			if (email.equals(u.getEmail())) {
				System.out.println(u.getEmail());
				String newGeneratedPassword = "passord123";
				try {
					if (sendMail.sendMailTo(email, newGeneratedPassword)) {
						u.setPassword(newGeneratedPassword);
						System.out.println("Email sent to: " + email);
						return true;
					}
				} catch (Exception e) {
					System.out.println(e.getLocalizedMessage());
					return false;
				}
			}
		}
		System.out.println("That email is not in the system.");
		return false;
	}

	public static void sendInformationRegisterSheep(String id, String age, String weight, String gender, String shepherd)
			throws NumberFormatException, Exception {
		currentUser.registerSheep(id, age, weight, gender, shepherd);
	}

	public static ArrayList<Sheep> getInformationSheepList() {
		return currentUser.getSheepList();
	}
}
