package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	private static Connection con;
	public static void connect() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		final String url = "jdbc:mysql://mysql.stud.ntnu.no/" +
		  		"thomagje_gruppe41";
		  final String username = "thomagje_bruker";
		  final String password = "Gruppe41";
		  Class.forName("com.mysql.jdbc.Driver").newInstance();
		  con = DriverManager.getConnection(url,username,password);
		  System.out.println("Connected");
	}
}
