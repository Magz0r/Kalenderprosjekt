package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Logic.Appointment;

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
	public static void close() {
		try {
			con.close();
		} catch (SQLException e) {
			try {
				con.close();
			} catch (SQLException e1) {
				//Give up
			}
		}
	}
	public static void addAppointment(Appointment appointment) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String start = appointment.getStart().getTimeString();
		String end = appointment.getEnd().getTimeString();
		String title = appointment.getTitle();
		String description = appointment.getDescription();
		String user = appointment.getOwner().getUsername();
		String room_id = appointment.getRoom().getName();
		int privat;
		if(appointment.isHidden()) {
			privat = 1;
		}
		else {
			privat = 0;
		}
		connect();
		Statement s = con.createStatement();
		s.executeUpdate("INSERT INTO appointment (start,end,title,description, owner, room_id,private) VALUES ('" + start + "', '" + end + "', '" + title + "', '" + description + "', '" + user + "', '" + room_id + "', " + privat + ")");
		if(appointment.getAttendies().size() > 0) {
			ResultSet rs = s.executeQuery("SELECT id FROM appointment WHERE owner='" + user + "' ORDER BY id DESC");
			rs.first();
			int id = rs.getInt(1);
			for(int i = 0; i< appointment.getAttendies().size();i++) {
				s.executeUpdate("INSERT INTO user_has_appointment (user_username, appointment_id) VALUES ('" + appointment.getAttendies().get(i).getUsername() + "', " + id + ")");
			}
		}
		close();
	}
	public static void delAppointment(Appointment appointment) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
	connect();
	Statement s = con.createStatement();
	s.executeUpdate("DELETE FROM appointment WHERE id='" + getAppointmentId(appointment) + "'");
	
	close();
	}
	private static String getAppointmentId(Appointment appointment) throws SQLException {
		String start = appointment.getStart().getTimeString();
		String end = appointment.getEnd().getTimeString();
		String title = appointment.getTitle();
		String description = appointment.getDescription();
		String user = appointment.getOwner().getUsername();
		String room_id = appointment.getRoom().getName();
		int privat;
		if(appointment.isHidden()) {
			privat = 1;
		}
		else {
			privat = 0;
		}
		Statement s = con.createStatement();
		ResultSet rs = s.executeQuery("SELECT id FROM appointment WHERE start='" + start + "' AND end='" + end + "' AND title='" + title + "' AND description='" + description + "' AND owner='" + user + "' AND room_id='" + room_id + "' AND private='" + privat + "'");
		
		if(rs.next()) {
			return rs.getString(1);
		}
		return null;
	}
}
