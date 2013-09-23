package database;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import com.mysql.jdbc.PreparedStatement;

import java.lang.String;

public class DatabaseConnector {
	public static Connection con;
	
	/**
	 * @author Oeyvind
	 * Denne koden åpner en tilkobling til databasen vi bruker i gruppe 10, hoesten 2013i faget IT1901 ved NTNU
	 */
	public static void open() {
		try {
			Class.forName("com.mysql.jdbcDrive");
			String url = "jdbc:mysql://mysql.stud.ntnu.no/oyvilund_sheep"; //adressen til databasen
			String user = "oyvilund_it1901"; //brukernavnet for aa koble til databasen
			String pw = "passord"; //passordet for aa koble til databasen
			con = DriverManager.getConnection(url,user,pw);
		} catch (ClassNotFoundException e) {
			System.out.println("Error " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage());
		}
	}
	
	public static void close() {
		try {
			if (con != null) con.close();
		} catch (SQLException e) {
			System.out.println("Error " + e.getMessage());
		}
	}
	
	/**
	 * Denne koden oppretter en ny bruker
	 * @param email
	 * @param name
	 * @param phoneNumber
	 * @param password
	 * @author Oeyvind
	 */
	public static void newUser(String email, String name, String phoneNumber, String password) {
		try {
			Statement st = con.createStatement();
			
			String linje = "INSERT INTO `oyvilund_sheep`.`User` (`Email`, `Name`, `Tlf`, `Password`) VALUES"+
			String.format("(\"%s\", \"%s\", \"%s\", \"%s\"", email,name,phoneNumber,password);
			
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
	 */
	public static void newSheep(String name, String owner, String shepherd, int weight, int heartrate, int temperature, int age) {
		try {
			Statement st = con.createStatement();
			
			String linje ="INSERT INTO `oyvilund_sheep`.`Sheep` (`Name`, `Owner`, `Shepherd`, `Weight`,`Heartrate`,`Temperature`,`Age`) VALUES"+
			String.format("(\"%s\", \"%s\", \"%s\",%s,%s,%s,%s)", name,owner,shepherd,weight,heartrate,temperature,age);
			
			st.executeUpdate(linje);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}