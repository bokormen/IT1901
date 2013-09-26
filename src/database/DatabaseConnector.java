package database;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

import com.mysql.jdbc.PreparedStatement;

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
	 * @author Oeyvind
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
	
	/**
	 * Returnerer en ArrayList med Sheep elementer som inneholder alle sauene til oppgitt eier 
	 * @param owner
	 * @return
	 * @author Oeyvind
	 */
	public static ArrayList<Sheep> getAllSheepsToOwner(String owner) {
		
		ArrayList<Sheep> Sheeps = new ArrayList<Sheep>( );
		
		try {
			Statement st = con.createStatement();
			
			String query = "Select S.ID, S.Gender, S.Shepherd, S.Weight, S.Heartrate, S.Temperature, S.Age, S.Shepherd From Sheep as S WHERE S.Owner="+owner+";"; //spoer etter all informasjonen om sauen med untak av eier(Owner) og gjeter(Shepherd)
			
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()) {
				
				//Sheeps.add(new Sheep(rs.getInt(0),rs.getInt(6),rs.getInt(3),rs.getString(1).charAt(0), rs.getString(7)),rs.getString(7)); //Maa sansynligvis endres litt da constructoren ikke ser ut til � ta hensyn til all infoen
				Statement st2 = con.createStatement();
				String query2 = "Select Date, Position From Location as L INNER JOIN Sheep as S ON (S.ID="+rs.getInt(1)+");";
				ResultSet rs2 = st.executeQuery(query2);
				while(rs2.next()) {
					//Sheeps.get(0).newLocation(rs2.getString(0), rs2.getString(1));
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Sheeps;
	}
}
