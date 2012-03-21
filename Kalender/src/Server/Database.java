package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Logic.Appointment;
import Logic.User;
import Logic.Notification;

public class Database {
	private static Connection con;
	private static String start;
	private static String end;
	private static String title;
	private static String description;
	private static String user;
	private static String room_id;
	private static int privat;
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
		connect();
		setAppointmentVars(appointment);
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
	private static void setAppointmentVars(Appointment appointment) {
		start = appointment.getStart().getTimeString();
		end = appointment.getEnd().getTimeString();
		title = appointment.getTitle();
		description = appointment.getDescription();
		user = appointment.getOwner().getUsername();
		room_id = appointment.getRoom().getName();
		if(appointment.isHidden()) {
			privat = 1;
		}
		else {
			privat = 0;
		}
	}
	private static String getAppointmentId(Appointment appointment) throws SQLException {
		setAppointmentVars(appointment);
		Statement s = con.createStatement();
		ResultSet rs = s.executeQuery("SELECT id FROM appointment WHERE start='" + start + "' AND end='" + end + "' AND title='" + title + "' AND description='" + description + "' AND owner='" + user + "' AND room_id='" + room_id + "' AND private='" + privat + "'");
		
		if(rs.next()) {
			return rs.getString(1);
		}
		return null;
	}
	public static void editAppointment(Appointment oldAppointment, Appointment newAppointment) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		connect();
		setAppointmentVars(newAppointment);
		int newPrivate;
		if(newAppointment.isHidden()) {
			newPrivate = 1;
		}
		else {
			newPrivate = 0;
		}
		Statement s = con.createStatement();
		s.executeUpdate("UPDATE appointment SET start='" + start + "', end='" + end + "', title='" + title + "', description='" + description + "', owner='" + user + "', room_id='" + room_id + "', private='" + privat + "' WHERE start='" + oldAppointment.getStart().getTimeString() + "' AND end='" + oldAppointment.getEnd().getTimeString() + "' AND title='" + oldAppointment.getTitle() + "' AND description='" + oldAppointment.getDescription() + "' AND owner='" + oldAppointment.getOwner().getUsername() + "' AND room_id='" + oldAppointment.getRoom().getName() + "' AND private='" + newPrivate + "'");
		close();
	}
	public static void addNotification(User user, String notification) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		connect();
		addNotificationStatus(user, notification, 0);
		close();
	}
	public static void setNotificationRead(Notification notification, boolean read) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		connect();
		Statement s = con.createStatement();
		int readInt;
		if(read) {
			readInt = 1;
		}
		else {
			readInt = 0;
		}
		s.executeUpdate("DELETE FROM notification WHERE user_username='" + notification.getUser().getUsername() + "' AND text='" + notification.getText() + "'");
		addNotificationStatus(notification.getUser(), notification.getText(), readInt);
		close();
	}
	private static void addNotificationStatus(User user, String notification, int read) throws SQLException {
		Statement s = con.createStatement();
		s.executeUpdate("INSERT INTO notification (`user_username`, `text`, `read`) VALUES ('" + user.getUsername() + "', '" + notification + "', " + read + ")");
	}
	public static boolean login(String username, String passwd) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		connect();
		Statement s = con.createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM user WHERE username='" + username + "' AND password='" + passwd + "'");
		if(rs.next()) {
			return true;
		}
		else {
			return false;
		}
	}
}
