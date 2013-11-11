package database;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.lang.String;
import div.*;

import java.util.*;

import java.sql.Timestamp;

@SuppressWarnings("unused")
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
				System.out.println("Error " + e.getMessage() + "InstantiationException");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.out.println("Error " + e.getMessage() + "IllegalAccessException");
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
	
	/**
	 * Denne funksjonen lukker tilkoblingen til databsen
	 * @author Oeyvind
	 */
	public static void close() {
		try {
			if (con != null) con.close();
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage());
		}
	}
	
	/**
	 * Laget for aa teste kode skrevet i denne klassen for feil
	 * @param args
	 */
	public static void main(String[] args) {
		open();
//		Sheep sheep = addNumberOfHistoricalLocationsToSheep(1012384, 3);
//		System.out.println(sheep.getLocationLog().get(2));
//		System.out.println(new Timestamp(System.currentTimeMillis()) + " Spørring etter alle sauene til test0@test.test startet");
//		try {
//			ArrayList<Sheep> Sheeps = getAllSheepsToOwner("test0@test.test");
//			System.out.println("Antall sauer: " + Sheeps.size());
//			for (int i=0;i<Sheeps.size();i++) {
//				System.out.println(Sheeps.get(i).getId() + " Lokasjon: " + Sheeps.get(i).getLocationLog().get(0));
//			}
//		} catch (Exception e) {
//			System.out.println("Error " + e.getMessage() + "Exception");
//			e.printStackTrace();
//		}
//		System.out.println(new Timestamp(System.currentTimeMillis()) + " Nå har alle sauene til test0@test.test blitt returnert");
//		ArrayList<String> testUsers = DatabaseConnector.getAllTestUserEmail();
//		for (String s : testUsers) {
//			System.out.println("testuser: " + s);
//		}
//		deleteTestUsers();
//		deleteTestSheeps();
//		RandomTestData.fillDatabaseWithUsers(200);
//		RandomTestData.sheepsForTestUser("bokormen-05@hotmail.com", 1);
		RandomTestData.maxSheepsForTestUser("bokormen-05@hotmail.com",7);
		String sheepBoundariesLongitude = "63.4259,63.4341";
		String sheepBoundariesLatitude = "10.3808,10.3992";
		for (int i=0;i<10;i++) {
			RandomTestData.moveSheeps(sheepBoundariesLongitude, sheepBoundariesLatitude);
		}
//		setSheepAttacked("1020");
//		setSheepSafe("1020");
//		ArrayList<String> Sheep = getAttackedSheep("test100@test.test");
//		for (String s : Sheep) {
//			System.out.println(s);
//		}
//		System.out.println(getLatestTestUser());
		
//		ArrayList<Integer> test = getNumberOfSheepForTestusers();
//		ArrayList<String> testEmails = getAllTestUserEmail();
//		
//		for (int i = 0; i < test.size(); i++) {
//			System.out.println(testEmails.get(i) + " har " + test.get(i) + " sauer");
//		}
		
		close();
	}
	
	/**
	 * Denne funksjonen sjekker om det eksisterer en bruker i databasen med e-posten som sendes til funksjonen
	 * @param user
	 * @return
	 * @author Oeyvind
	 */
	public static boolean doesUserExsist(String user) {
		try {
			String query = "SELECT Email FROM User WHERE Email = '" + user + "'";
//			Statement st = con.createStatement();
//			ResultSet rs= st.executeQuery(query);
			
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			int matchingUsers=0;
			while(rs.next()) {
				matchingUsers+=1;
			}
			if (matchingUsers>0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String linje = "INSERT INTO `User` (`Email`, `FirstName`, `LastName`, `Tlf`, `Password`, `Location`) VALUES "+
			String.format("(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\")", email,firstName,lastName,phoneNumber,password,location);
			
			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
	}
	
	/**
	 * Setter sauen i status angrepen
	 * @param id
	 */
	public static void setSheepAttacked(String id) {
		try {
			String linje ="UPDATE Sheep SET Sheep.Status = \"attacked\" WHERE ID = \"" + id + "\";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
	}
	
	/**
	 * Setter sauen i status angrepen
	 * @param id
	 */
	public static void setSheepSafe(String id) {
		try {
			String linje ="UPDATE Sheep SET Sheep.Status = \"safe\" WHERE ID = \"" + id + "\";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
	}
	
	/**
	 * tar inn en eier, og returnerer en liste med id'er til alle sauene som er under angrep
	 * @param owner
	 * @return
	 */
	public static ArrayList<String> getAttackedSheep(String owner) {
		ArrayList<String> Sheep = new ArrayList<String>( );

		try {
			String query = "SELECT S.ID FROM Sheep AS S WHERE S.Owner=\"" + owner + "\" AND S.Status = \"attacked\";";
			
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs;
			rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
					Sheep.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
		
		return Sheep;
	}
	
	/**
	 * Denne koden oppretteren en sau i databasen og returnerer id'en til denne sauen
	 * @param name
	 * @param owner
	 * @param shepherd
	 * @param gender
	 * @param weight
	 * @param heartrate
	 * @param temperature
	 * @param birthyear
	 * @author Oeyvind
	 * @return Returnerer id'en til den sist innsate sauen i databasen fra maskinen som brukes til aa kalle denne funksjonen
	 */
	public static int newSheep(String name, String owner, String shepherd, String gender, String weight, String heartrate, String temperature, String birthyear) {
		int i = -1;
		try {
			String linje ="INSERT INTO `Sheep` (`Name`, `Owner`, `Shepherd`, `Gender`, `Weight`,`Heartrate`,`Temperature`,`Age`) VALUES "+
			String.format("(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\");", name,owner,shepherd,gender,weight,heartrate,temperature,birthyear);

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
			String query = "SELECT LAST_INSERT_ID();";
			
			PreparedStatement ps2 = con.prepareStatement(query);
			ResultSet rs = ps2.executeQuery();
			
			
			
			while (rs.next()) {
				i=rs.getInt(1);
			}
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
		return i;
	}
	
	/**
	 * Returnerer en ArrayList med Sheep elementer som inneholder alle sauene til oppgitt eier, inkludert de siste fem posisjonene
	 * @param owner
	 * @return
	 * @author Oeyvind
	 * @throws Exception 
	 */
	public static ArrayList<Sheep> getAllSheepsToOwnerNoLongerInUse(String owner) throws Exception {
		
		ArrayList<Sheep> Sheeps = new ArrayList<Sheep>( );
		
		try {
			String query = "Select S.ID, S.Gender, S.Shepherd, S.Weight, S.Heartrate, S.Temperature, S.Age, S.Shepherd, S.Name From Sheep as S WHERE S.Owner=\""+owner+"\";"; //spoer etter all informasjonen om sauen med untak av eier(Owner) og gjeter(Shepherd)

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			int i=0;
			while(rs.next()) {
				Sheep sau = new Sheep(rs.getInt(1), rs.getString(9), rs.getInt(6), rs.getInt(3), rs.getString(2).charAt(0),owner, rs.getString(7));
				
				Sheeps.add(sau);
				
				String query2 = "Select Date, Position FROM Location WHERE SheepID = " + rs.getInt(1) + " ORDER BY Date DESC LIMIT 0,5;";

				PreparedStatement ps2 = con.prepareStatement(query2);
				ResultSet rs2 = ps2.executeQuery();
				
//				Statement st2 = con.createStatement();
//				ResultSet rs2 = st2.executeQuery(query2);
				
				Sheeps.get(i).setHeartrate(rs.getInt(4));
				Sheeps.get(i).setTemperature(rs.getInt(5));
				while(rs2.next()) {
					Sheeps.get(i).newLocation(rs2.getString(2), rs2.getString(1));
				}
				i++;
			}
			
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
		
		return Sheeps;
	}
	
	/**
	 * Returnerer en ArrayList med Sheep elementer som inneholder alle sauene til oppgitt eier, inkludert de siste antall etterspurte posisjonene
	 * @param owner
	 * @return
	 * @author Oeyvind
	 * @throws Exception 
	 */
	public static ArrayList<Sheep> getAllSheepsToOwnerWithHistory(String owner, int numberOfHistoryEvents) throws Exception {
		
		ArrayList<Sheep> Sheeps = new ArrayList<Sheep>( );
		
		try {
			String query = "SELECT S.ID, S.Gender, S.Weight, S.Heartrate, S.Temperature, S.Age, S.Shepherd, L.Date, L.Position, S.Name FROM Sheep AS S JOIN Location AS L ON (S.ID = L.SheepID) WHERE S.Owner = " + owner + " ORDER BY L.Date DESC;";
			
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			int j=0;
			
			
			int lastSheepID = -1;
			int storedLocations = 0;
			while(rs.next()) {
				if (rs.getInt(1) != lastSheepID) {
					Sheep sau = new Sheep(rs.getInt(1), rs.getString(10), rs.getInt(6), rs.getInt(3), rs.getString(2).charAt(0),owner, rs.getString(7));
					
					Sheeps.add(sau);
					
					Sheeps.get(j).setHeartrate(rs.getInt(4));
					Sheeps.get(j).setTemperature(rs.getInt(5));
					j++;
					Sheeps.get(j-1).newLocation(rs.getString(9), rs.getString(8));
					lastSheepID=rs.getInt(1);
					storedLocations=1;
				} else if (rs.getInt(1) == lastSheepID && storedLocations < numberOfHistoryEvents) {
					Sheeps.get(j-1).newLocation(rs.getString(9), rs.getString(8));
					storedLocations++;
				}
			}
			
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
		
		return Sheeps;
	}
	
	/**
	 * Returnerer en ArrayList med Sheep elementer som inneholder alle sauene til oppgitt eier, inkludert den siste posisjonen
	 * @param owner
	 * @return
	 * @author Oeyvind
	 * @throws Exception 
	 */
	public static ArrayList<Sheep> getAllSheepsToOwner(String owner) throws Exception {
		
		ArrayList<Sheep> Sheeps = new ArrayList<Sheep>( );
		int j=0;
		
		try {
			String query = "SELECT S.ID, S.Name, S.Owner, S.Shepherd, S.Gender, S.Weight, S.Heartrate, S.Temperature, S.Age, L.Date, L.Position FROM Sheep AS S INNER JOIN Location AS L ON S.ID = L.SheepID LEFT JOIN Location L2 ON L.SheepID = L2.SheepID AND L.Date < L2.Date WHERE L2.Date IS NULL AND S.Owner=\"" + owner + "\";";
			
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
					Sheep sau = new Sheep(rs.getInt(1),rs.getString(2),rs.getInt(9),rs.getInt(6),rs.getString(5).charAt(0),owner, rs.getString(4));
					
					Sheeps.add(sau);
					
					Sheeps.get(j).setHeartrate(rs.getInt(7));
					Sheeps.get(j).setTemperature(rs.getInt(8));
					Sheeps.get(j).newLocation(rs.getString(11), rs.getString(10));
					j++;
					
			}
			
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage() + "Exception");
			e.printStackTrace();
		}
		
		return Sheeps;
	}
	
	/**
	 * Legger til historik til sauen med opptil sizeOfHistory antall tidligere posisjoner, den foerste posisjonen som legges til er den siste registrerte posisjonen
	 * @param id
	 * @param sizeOfHistory
	 * @return
	 * @author Oeyvind
	 */
	public static Sheep addNumberOfHistoricalLocationsToSheep(String id, String sizeOfHistory) {
		Sheep sheep = null;
		try {
//			String query = "Select L.Date, L.Position From Location AS L WHERE L.SheepID = " + id + " ORDER BY L.Date DESC LIMIT 0," + sizeOfHistory + ";";
			
			String query = "SELECT S.ID, S.Gender, S.Weight, S.Heartrate, S.Temperature, S.Age, S.Shepherd, L.Date, L.Position, S.Name, S.Owner FROM Sheep AS S JOIN Location AS L ON (S.ID = L.SheepID) WHERE S.ID = " + id + " ORDER BY L.Date DESC LIMIT 0," + sizeOfHistory + ";";
			
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			boolean sheepAdded = false;
			while(rs.next()) {
				try {
					if (!sheepAdded){
						sheep = new Sheep(rs.getInt(1), rs.getString(10), rs.getInt(6), rs.getInt(3), rs.getString(2).charAt(0),rs.getString(11), rs.getString(7));
						sheep.setHeartrate(rs.getInt(4));
						sheep.setTemperature(rs.getInt(5));
						sheepAdded = true;
					}
					sheep.newLocation(rs.getString(9), rs.getString(8));
				} catch (Exception e) {
					System.out.println("Error " + e.getMessage() + "Exception");
					e.printStackTrace();
				}
			}
			
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
		
		return sheep; 
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
			String query = "Select U.Email, U.Password From User as U WHERE U.Email=\""+user+"\";";

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			
			while (rs.next()) {
				if (rs.getString(2).equals(password)) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.fillInStackTrace();
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Denne funksjonen tar inn en e-postadresse og et passord og sjekker om brukeren eksisterer og at passordet til brukeren er riktig, hvis alt er der og alt stemmer, saa returners brukeren
	 * @param user
	 * @param password
	 * @author Oeyvind
	 * @return
	 */
	public static User loginUser(String user, String password) {
		User farmer=null;
		try {
			if (!doesUserExsist(user)) {
				return null;
			}
			String query = "Select U.Email, U.Password, U.FirstName, U.LastName, U.TLF, U.Location From User as U WHERE U.Email="+user+";";

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			
			while (rs.next()) {
				if (rs.getString(2).equals(password)) {
					try {
						farmer=new User(rs.getString(3),rs.getString(4),rs.getString(1),rs.getString(5),rs.getString(6));
					} catch (Exception e) {
						System.out.println("Error " + e.getMessage() + "Exception");
						e.printStackTrace();
					}
				} 
			}
			
			return farmer;
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String query = "Select U.FirstName, U.LastName, U.Email, U.TLF, U.Location From User as U WHERE U.Email=\""+user+"\";";
			
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			
			if (rs.next()) {
				try {
					farmer=new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
                    return farmer;
                } catch (Exception e) {
                	System.out.println("Error " + e.getMessage() + "Exception");
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String query = "SELECT ID FROM Sheep WHERE ID = '" + id + "'";

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs= st.executeQuery(query);
			int matchingSheeps=0;
			while(rs.next()) {
				matchingSheeps+=1;
			}
			if (matchingSheeps>0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Denne funksjonen tar inn en e-postadresse og en saue id og sjekker foerst om sauen eksisterer og saa om sauen er eid av e-postadressen som blir sent med. Returnerer true om sauen eksisterer og e-postadressen som er oppgitt er satt som owner hos sauen
	 * @param user
	 * @param ID
	 * @author Oeyvind
	 * @return
	 */
	public static boolean doesUserOwnSheep(String user,String ID) {
		if (!doesSheepExsist(ID)) {
			return false;
		}
		try {
			String query = "SELECT S.ID FROM Sheep AS S WHERE S.Owner = '" + user + "' AND S.ID = "+ID;

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs= st.executeQuery(query);
			int matchingSheeps=0;
			while(rs.next()) {
				matchingSheeps+=1;
			}
			if (matchingSheeps>0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String query = "SELECT S.ID, S.Name, S.Gender, S.Shepherd, S.Weight, S.Heartrate, S.Temperature, S.Age FROM Sheep AS S WHERE S.Owner = '" + user + "' AND S.ID = "+ID;

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs= st.executeQuery(query);
			while(rs.next()) {
				try {
					sheep = new Sheep(rs.getInt(1), rs.getString(2), rs.getInt(8),rs.getInt(5),rs.getString(3).charAt(0),user, rs.getString(4));
					String query2 = "Select Date, Position FROM Location WHERE SheepID = " + rs.getInt(1) + " ORDER BY Date DESC LIMIT 0,5;";

					PreparedStatement ps2 = con.prepareStatement(query2);
					ResultSet rs2 = ps2.executeQuery();
					
//					Statement st2 = con.createStatement();
//					ResultSet rs2 = st2.executeQuery(query2);
					
					sheep.setHeartrate(rs.getInt(4));
					sheep.setTemperature(rs.getInt(5));
					while(rs2.next()) {
						sheep.newLocation(rs2.getString(0), rs2.getString(1));
					}
				} catch (Exception e) {
					System.out.println("Error " + e.getMessage() + "Exception");
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String linje ="UPDATE Sheep SET Sheep.Name = \""+name+"\", Sheep.Owner =\""+owner+"\", Sheep.Shepherd=\""+shepherd+"\", Sheep.Gender=\""+gender+"\", Sheep.Weight="+weight+", Sheep.Heartrate="+heartrate+", Sheep.Temperature="+temperature+", Sheep.Age="+birthyear+" WHERE Sheep.ID = "+id+";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
	}
	
	/**
	 * Denne funksjonen tar inn en sau, og skriver de nye verdiene til databasen
	 * @param id
	 * @param name
	 * @param owner
	 * @param shepherd
	 * @param weight
	 * @param birthyear
	 * @author Oeyvind
	 */
	public static void changeBasicSheep(String id, String name, String owner, String shepherd, String gender, String weight, String birthyear) {
		try {
			String linje ="UPDATE Sheep SET Sheep.Name = \""+name+"\", Sheep.Owner =\""+owner+"\", Sheep.Shepherd=\""+shepherd+"\", Sheep.Gender=\""+gender+"\", Sheep.Weight="+weight+", Sheep.Age="+birthyear+" WHERE Sheep.ID = "+id+";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String linje ="UPDATE User SET User.FirstName = \""+firstName+"\", User.LastName = \""+lastName+"\", User.TLF = \""+phoneNumber+"\", User.Location = \""+location+"\" WHERE User.Email = \""+user+"\";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String linje ="UPDATE User SET User.Password = \""+password+"\" WHERE User.Email = \""+user+"\";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String linje = "INSERT INTO `Location` (`SheepID`, `Date`, `Position`) VALUES "+
			String.format("(\"%s\", \"%s\", \"%s\")", id,date,location);

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String linje ="DELETE FROM `oyvilund_sheep`.`User` WHERE `Email`=\""+user+"\";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String linje ="DELETE FROM `oyvilund_sheep`.`Sheep` WHERE `ID`="+id+";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sletter all lokasjonsinformasjn tidligere enn oppgitt dato
	 * @param date
	 * @author Oeyvind
	 */
	public static void deleteLocatinDataEarlierThan(String date) {
		try {
			String linje ="DELETE FROM `oyvilund_sheep`.`Location` WHERE Date < \"" + date + "\";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sletter all lokasjnsdata for en brukers sauer som er eldre enn oppgitt dato
	 * @param date
	 * @param user
	 * author Oeyvind
	 */
	public static void deleteLocatinDataForUserEarlierThan(String date, String user) {
		try {
			String linje ="DELETE FROM Location WHERE SheepID IN (SELECT S.ID FROM Sheep AS S WHERE S.Owner = \"" +user+"\") AND Date < \"" + date + "\";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sletter test sauer fra databasen, sjekker paa navnet til sauen, og sletter alle med navnet "testsau"
	 * @author Oeyvind
	 */
	public static void deleteTestSheeps() {
		try {
			String linje ="DELETE FROM `oyvilund_sheep`.`Sheep` WHERE `Name`=\"testSheep\";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
			Statement st = con.createStatement();
			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sletter test brukere fra databasen, innebaerer ogsaa at alle testsauer tilknyttet disse brukerne blir slettet
	 * @author Oeyvind
	 */
	public static void deleteTestUsers() {
		try {
			String linje ="DELETE FROM `oyvilund_sheep`.`User` WHERE `FirstName`=\"testuser\";";

			PreparedStatement ps = con.prepareStatement(linje);
			ps.executeUpdate();
			
//			Statement st = con.createStatement();
//			st.executeUpdate(linje);
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returnerer en liste med e-postadressen til alle brukerene med navnet "testuser", sorteres paa samme maate som i funksjonen getNumberOfSheepForTestusers
	 * @return
	 * @author Oeyvind
	 */
	public static ArrayList<String> getAllTestUserEmail() {
		ArrayList<String> testUsers = new ArrayList<String>( );
		
		try {
			String query = "Select U.Email From User as U WHERE U.FirstName=\"testuser\" GROUP BY Email ASC;";
			
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()) {
				
				testUsers.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String query = "Select S.ID From Sheep as S;";

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				
				IDs.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
		
		return IDs;
	}
	
	/**
	 * Denne funkjsonen returnerer en liste med id'en til alle eksisterende sauer, avhegig av eier
	 * @return
	 * @author Oeyvind
	 */
	public static ArrayList<String> getAllSheepIDs(String owner) {
		ArrayList<String> IDs = new ArrayList<String>( );
		
		try {
			String query = "Select S.ID From Sheep as S WHERE S.Owner = \"" + owner + "\";";

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				
				IDs.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
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
			String query = "Select max(Date) From Location;";

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
//			Statement st = con.createStatement();
//			ResultSet rs= st.executeQuery(query);
			while(rs.next()) {
				date = rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
		if (date==null) {
			date="2013.04.01.00.00";
		}
		return date;
	}
	
	/**
	 * testbrukerene faar et tall som etternavn, denne funksjonen returnerer det etternavnet med stoerst verdi
	 * @return
	 * @author Oeyvind
	 */
	public static int getLatestTestUser() {
		
		ArrayList<String> emails = getAllTestUserEmail();
		
		List<Integer> list = new ArrayList<Integer>();
		
		for (String s : emails) {
			list.add(getTestUserNumberFromEmail(s));
		}
		
		Collections.sort( list );
		
		if (list.size()==0) {
			return -1;
		}
		return list.get(list.size()-1);
	}
	
	/**
	 * returnerer en arraylist med int verdier som angir hvor mange sauer testbrukerne har, verdien paa posisjon i tilhoerer testbruker i-1, listen sorteres paa samme maate som getAllTestUserEmail
	 * @return
	 */
	public static ArrayList<Integer> getNumberOfSheepForTestusers() {
		
		ArrayList<Integer> numberOfSheeps = new ArrayList<Integer>();
		
		try {
			String query = "SELECT Owner, COUNT(*) AS counted FROM ( Select S.*, U.* From Sheep AS S Join User AS U ON (U.Email=S.Owner)) AS Info WHERE Info.FirstName = \"testuser\" GROUP BY Owner ASC;";

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
//			
//			int count = 0;
//			while (rs.next()) {
//				count++;
//			}
//			
//			rs.beforeFirst();
//
//			numberOfSheeps = new ArrayList<Integer>(Collections.nCopies(count, 0));
//			
//			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				numberOfSheeps.add(rs.getInt(2));
//				numberOfSheeps.set(getTestUserNumberFromEmail(rs.getString(1)),rs.getInt(2));
			}
			
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage() + "SQLException");
			e.printStackTrace();
		}
		
		return numberOfSheeps;
	}
	
	/**
	 * Finner tallet i testuser mailen og returnerer det som en int
	 * @param email
	 * @return
	 */
	public static int getTestUserNumberFromEmail(String email) {
		int number;
		String temp = "";
		int i = 4;
		
		while (email.charAt(i) != '@') {
			temp+=Character.toString(email.charAt(i));
			i++;
		}
		
		number = Integer.parseInt(temp);
		
		return number;
	}
	
}