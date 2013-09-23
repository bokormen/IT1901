package database;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import com.mysql.jdbc.PreparedStatement;

public class DatabaseConnector {
	public static Connection con;
	
	/**
	 * @author Oeyvind Klungland Lund
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
}