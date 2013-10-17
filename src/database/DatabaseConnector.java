package database;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

//import com.mysql.jdbc.PreparedStatement;

import java.lang.String;
import div.*;

import java.util.*;

public class DatabaseConnector {
	public static Connection con;
	
	/**
	 * Denne koden aapner en tilkobling til databasen vi bruker i gruppe 10, hoesten 2013i faget IT1901 ved NTNU
	 * @author Oeyvind
	 */
	public static void open() {
		try {
			
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String url = "jdbc:mysql://mysql.stud.ntnu.no/oyvilund_sheep"; //adressen til databasen
			String user = "oyvilund_it1901"; //brukernavnet for aa koble til databasen
			String pw = "passord"; //passordet for aa koble til databasen
			con = DriverManager.getConnection(url,user,pw);
		} catch (ClassNotFoundException e) {
			System.out.println("Error " + e.getMessage()+ "ClassNotFound");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
		}
	}
	
	public static void close() {
		try {
			if (con != null) con.close();
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage());
		}
	}
	
//	public static void main(String[] args) {
//		open();
//		deleteTestSheeps();
//		deleteTestUsers();
//		RandomTestData.fillDatabaseWithUsers(3);
//		RandomTestData.sheepsForTestUsers(4);
//		
//		for (int i=0;i<10;i++) {
//			RandomTestData.moveSheeps("10.00000000,60.00000000", "10.0000000,60.00000000");
//		}
//		close();
//	}
	
	/**
	 * Denne funksjonen sjekker om det eksisterer en bruker i databasen med e-posten som sendes til funksjonen
	 * @param user
	 * @return
	 * @author Oeyvind
	 */
	public static boolean doesUserExsist(String user) {
		try {
			Statement st = con.createStatement();
			String query = "SELECT Email FROM User WHERE Email = '" + user + "'";
			ResultSet rs= st.executeQuery(query);
			int matchingUsers=0;
			while(rs.next()) {
				matchingUsers+=1;
			}
			if (matchingUsers>0) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Denne koden oppretter en ny bruker
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param phoneNumber
	 * @param password
	 * @param location
	 * @author Oeyvind
	 */
	public static void newUser(String email, String firstName, String lastName, String phoneNumber, String password, String location) {
		try {
			Statement st = con.createStatement();
			
			String linje = "INSERT INTO `User` (`Email`, `FirstName`, `LastName`, `Tlf`, `Password`, `Location`) VALUES "+
			String.format("(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\")", email,firstName,lastName,phoneNumber,password,location);
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Denne koden oppretteren en sau i databasen
	 * @param name
	 * @param owner
	 * @param shepherd
	 * @param gender
	 * @param weight
	 * @param heartrate
	 * @param temperature
	 * @param birthyear
	 * @author Oeyvind
	 */
	public static void newSheep(String name, String owner, String shepherd, String gender, String weight, String heartrate, String temperature, String birthyear) {
		try {
			Statement st = con.createStatement();
			
			String linje ="INSERT INTO `Sheep` (`Name`, `Owner`, `Shepherd`, `Gender`, `Weight`,`Heartrate`,`Temperature`,`Age`) VALUES "+
			String.format("(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\")", name,owner,shepherd,gender,weight,heartrate,temperature,birthyear);
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returnerer en ArrayList med Sheep elementer som inneholder alle sauene til oppgitt eier 
	 * @param owner
	 * @return
	 * @author Oeyvind
	 * @throws Exception 
	 */
	public static ArrayList<Sheep> getAllSheepsToOwner(String owner) throws Exception {
		
		ArrayList<Sheep> Sheeps = new ArrayList<Sheep>( );
		
		try {
			Statement st = con.createStatement();
			
			String query = "Select S.ID, S.Gender, S.Shepherd, S.Weight, S.Heartrate, S.Temperature, S.Age, S.Shepherd From Sheep as S WHERE S.Owner="+owner+";"; //spoer etter all informasjonen om sauen med untak av eier(Owner) og gjeter(Shepherd)
			
			ResultSet rs = st.executeQuery(query);
			int i=0;
			while(rs.next()) {
				
				Sheep sau = new Sheep(rs.getInt(0),rs.getInt(6),rs.getInt(3),rs.getString(1).charAt(0),owner, rs.getString(7));
				
				Sheeps.add(sau); //Maa sansynligvis endres litt da constructoren ikke ser ut til aa ta hensyn til all infoen
				Statement st2 = con.createStatement();
				String query2 = "Select Date, Position From Location as L INNER JOIN Sheep as S ON (S.ID="+rs.getInt(1)+");";
				ResultSet rs2 = st2.executeQuery(query2);
				Sheeps.get(i).setHeartrate(rs.getInt(4));
				Sheeps.get(i).setTemperature(rs.getInt(5));
				while(rs2.next()) {
					Sheeps.get(0).newLocation(rs2.getString(0), rs2.getString(1));
				}
				i++;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Sheeps;
	}
	
	/**
	 * Denne funksjonen tar inn en e-post adresse og et passord, foerst sjekker den om e-posten er registrert paa en bruker, saa sjekker den om passordet som er gitt stemmer med brukerens passord, hvis brukernavnet eksisterer og passordet er riktig, saa returneres true, hvis ikke returneres false
	 * @param user
	 * @param password
	 * @return
	 * @author Oeyvind
	 */
	public static boolean login(String user, String password) {
		try {
			if (!doesUserExsist(user)) {
				return false;
			}
			Statement st = con.createStatement();
			
			String query = "Select U.Email, U.Password From User as U WHERE U.Email=\""+user+"\";";
			
			ResultSet rs = st.executeQuery(query);
			
			while (rs.next()) {
				if (rs.getString(2).equals(password)) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.fillInStackTrace();
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Denne funksjonen tar inn en e-postadresse og et passord og sjekker om brukeren eksisterer og at passordet til brukeren er riktig, hvis alt er der og alt stemmer, saa returners brukeren
	 * @param user
	 * @param password
	 * @return
	 */
	public static User loginUser(String user, String password) {
		User farmer=null;
		try {
			if (!doesUserExsist(user)) {
				return null;
			}
			Statement st = con.createStatement();
			
			String query = "Select U.Email, U.Password, U.FirstName, U.LastName, U.TLF, U.Location From User as U WHERE U.Email="+user+";";
			
			ResultSet rs = st.executeQuery(query);
			
			while (rs.next()) {
				if (rs.getString(2).equals(password)) {
					try {
						farmer=new User(rs.getString(3),rs.getString(4),rs.getString(1),rs.getString(5),rs.getString(6));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
			
			return farmer;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return farmer;
	}

	/**
	 * Returnerer et brukerobjekt av brukeren som etterspoer
	 * @param user
	 * @return
	 */
	public static User getUser(String user) {
		User farmer=null;
		try {

			Statement st = con.createStatement();
			
			String query = "Select U.FirstName, U.LastName, U.Email, U.TLF, U.Location From User as U WHERE U.Email=\""+user+"\";";
			
			ResultSet rs = st.executeQuery(query);
			
			if (rs.next()) {
				try {
					farmer=new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
                    return farmer;
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return farmer;
	}
	
	/**
	 * Tar inn en saue id og sjekker om sauen eksister i databasen
	 * @param id
	 * @return
	 * @author Oeyvind
	 */
	public static boolean doesSheepExsist(String id) {
		try {
			Statement st = con.createStatement();
			String query = "SELECT ID FROM Sheep WHERE ID = '" + id + "'";
			ResultSet rs= st.executeQuery(query);
			int matchingSheeps=0;
			while(rs.next()) {
				matchingSheeps+=1;
			}
			if (matchingSheeps>0) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Denne funksjonen tar inn en e-postadresse og en saue id og sjekker foerst om sauen eksisterer og saa om sauen er eid av e-postadressen som blir sent med. Returnerer true om sauen eksisterer og e-postadressen som er oppgitt er satt som owner hos sauen
	 * @param user
	 * @param ID
	 * @return
	 */
	public static boolean doesUserOwnSheep(String user,String ID) {
		if (!doesSheepExsist(ID)) {
			return false;
		}
		try {
			Statement st = con.createStatement();
			String query = "SELECT S.ID FROM Sheep AS S WHERE S.Owner = '" + user + "' AND S.ID = "+ID;
			ResultSet rs= st.executeQuery(query);
			int matchingSheeps=0;
			while(rs.next()) {
				matchingSheeps+=1;
			}
			if (matchingSheeps>0) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Denne funksjonen tar inn en e-postadresse og en saue id og returnerer sauen om sauen eksisterer og brukeren eier den.
	 * @param user
	 * @param ID
	 * @return
	 * @author Oeyvind
	 */
	public static Sheep findSheep(String user,String ID) {
		Sheep sheep = null;
		try {
			Statement st = con.createStatement();
			String query = "SELECT S.ID, S.Gender, S.Shepherd, S.Weight, S.Heartrate, S.Temperature, S.Age, S.Shepherd FROM Sheep AS S WHERE S.Owner = '" + user + "' AND S.ID = "+ID;
			ResultSet rs= st.executeQuery(query);
			while(rs.next()) {
				try {
					sheep = new Sheep(rs.getInt(0),rs.getInt(6),rs.getInt(3),rs.getString(1).charAt(0),user, rs.getString(7));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sheep;
	}
	
	/**
	 * Denne funksjonen tar inn en sau, og skriver de nye verdiene til databasen
	 * @param id
	 * @param name
	 * @param owner
	 * @param shepherd
	 * @param weight
	 * @param heartrate
	 * @param temperature
	 * @param birthyear
	 * @author Oeyvind
	 */
	public static void changeSheep(String id, String name, String owner, String shepherd, String gender, String weight, String heartrate, String temperature, String birthyear) {
		try {
			Statement st = con.createStatement();
			
			String linje ="UPDATE Sheep SET Sheep.Name = \""+name+"\", Sheep.Owner =\""+owner+"\", Sheep.Shepherd=\""+shepherd+"\", Sheep.Gender=\""+gender+"\", Sheep.Weight="+weight+", Sheep.Heartrate="+heartrate+", Sheep.Temperature="+temperature+", Sheep.Age="+birthyear+" WHERE Sheep.ID = "+id+";";
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Endrer informasjonen om brukeren i databasen
	 * @param user
	 * @param firstName
	 * @param lastName
	 * @param phoneNumber
	 * @param location
	 * @return
	 * @author Oeyvind
	 */
	public static void changeUser(String user, String firstName, String lastName, String phoneNumber, String location) {
		try {
			Statement st = con.createStatement();
			
			String linje ="UPDATE User SET User.FirstName = \""+firstName+"\", User.LastName = \""+lastName+"\", User.TLF = \""+phoneNumber+"\", User.Location = \""+location+"\" WHERE User.Email = \""+user+"\";";
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Denne funksjonen endrer passordet til en bruker
	 * @param user
	 * @param password
	 * @author Oeyvind
	 */
	public static void changePassword(String user, String password) {
		try {
			Statement st = con.createStatement();
			
			String linje ="UPDATE User SET User.Password = \""+password+"\" WHERE User.Email = \""+user+"\";";
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Lagerer en ny posisjon til sauen
	 * @param id
	 * @param date
	 * @param location
	 */
	public static void newPosition(String id,String date, String location) {
		try {
			Statement st = con.createStatement();
			
			String linje = "INSERT INTO `Location` (`SheepID`, `Date`, `Position`) VALUES "+
			String.format("(\"%s\", \"%s\", \"%s\")", id,date,location);
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Sletter oppgitt bruker fra databasen
	 * @param user
	 * @author Oeyvind
	 */
	public static void deleteUser(String user) {
		try {
			Statement st = con.createStatement();
			
			String linje ="DELETE FROM `oyvilund_sheep`.`User` WHERE `Email`=\""+user+"\";";
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Sletter oppgitt sau fra databasen
	 * @param id
	 * @author Oeyvind
	 */
	public static void deleteSheep(String id) {
		try {
			Statement st = con.createStatement();
			
			String linje ="DELETE FROM `oyvilund_sheep`.`Sheep` WHERE `ID`="+id+";";
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Sletter test sauer fra databasen
	 * @author Oeyvind
	 */
	public static void deleteTestSheeps() {
		try {
			Statement st = con.createStatement();
			
			String linje ="DELETE FROM `oyvilund_sheep`.`Sheep` WHERE `Name`=\"testSheep\";";
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Sletter test brukere fra databasen, innebaerer ogsaa at alle testsauer tilknyttet disse brukerne blir slettet
	 * @author Oeyvind
	 */
	public static void deleteTestUsers() {
		try {
			Statement st = con.createStatement();
			
			String linje ="DELETE FROM `oyvilund_sheep`.`User` WHERE `FirstName`=\"testuser\";";
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returnerer en liste med e-postadressen til alle brukerene med navnet "testuser"
	 * @return
	 * @author Oeyvind
	 */
	public static ArrayList<String> getAllTestUserEmail() {
		ArrayList<String> testUsers = new ArrayList<String>( );
		
		try {
			Statement st = con.createStatement();
			
			String query = "Select U.Email From User as U WHERE U.FirstName=\"testuser\";";
			
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				
				testUsers.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return testUsers;
	}
	
	/**
	 * Denne funkjsonen returnerer en liste med id'en til alle eksisterende sauer, uavhegig av eier
	 * @return
	 * @author Oeyvind
	 */
	public static ArrayList<String> getAllSheepIDs() {
		ArrayList<String> IDs = new ArrayList<String>( );
		
		try {
			Statement st = con.createStatement();
			
			String query = "Select S.ID From Sheep as S;";
			
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				
				IDs.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return IDs;
	}
	
	/**
	 * Denne funksjonen returnerer den seneste datoen som er lagt inn i databasen
	 * @return
	 * @author Oeyvind
	 */
	public static String getLatetsDate() {
		String date = null;
		try {
			Statement st = con.createStatement();
			String query = "Select max(Date) From Location;";
			ResultSet rs= st.executeQuery(query);
			while(rs.next()) {
				date = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date==null) {
			date="2013.04.01.00.00";
		}
		return date;
	}
	
}
