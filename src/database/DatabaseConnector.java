package database;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

//la til noe bare for aa gjoere en endring

//import com.mysql.jdbc.PreparedStatement;

import java.lang.String;
import div.*;

import java.util.*;

public class DatabaseConnector {
	public static Connection con;
	
	/**
	 * @author Oeyvind
	/**
	 * @author Oeyvind
	 * Denne koden aapner en tilkobling til databasen vi bruker i gruppe 10, hoesten 2013i faget IT1901 ved NTNU
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
//		newUser("test7@test.test","Olav Haraldsson","12345678","p","60,12345.50,4321");
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
			
			String linje = "INSERT INTO `User` (`Email`, `Name`, `Tlf`, `Password`, `Location`) VALUES "+
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
	 * @param weight
	 * @param heartrate
	 * @param temperature
	 * @param age
	 * @author Oeyvind
	 */
	public static void newSheep(String name, String owner, String shepherd, int weight, int heartrate, int temperature, int age) {
		try {
			Statement st = con.createStatement();
			
			String linje ="INSERT INTO `Sheep` (`Name`, `Owner`, `Shepherd`, `Weight`,`Heartrate`,`Temperature`,`Age`) VALUES "+
			String.format("(\"%s\", \"%s\", \"%s\",%s,%s,%s,%s)", name,owner,shepherd,weight,heartrate,temperature,age);
			
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
				
				Sheep sau = new Sheep(rs.getInt(0),rs.getInt(6),rs.getInt(3),rs.getString(1).charAt(0), rs.getString(7));
				
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
			
			String query = "Select U.Email, U.Password From User as U WHERE U.Email="+user+";";
			
			ResultSet rs = st.executeQuery(query);
			
			while (rs.next()) {
				if (rs.getString(1)==password) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
