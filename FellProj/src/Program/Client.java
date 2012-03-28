package Program;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.co.ConnectionImpl;

import GUI.Login;
import Logic.Appointment;
import Logic.Date;
import Logic.Notification;
import Logic.Room;
import Logic.User;
import Server.Server;
import Server.Database;

public class Client {

	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
//		new Login();
		/*Appointment appointment = new Appointment(new Room("Sentrum Bowling", 200),new Date(2006,2,2, 12, 00),new Date(2006,2,2,13,00),new User("Test2","owner@test.no","Test2"),"Testavtale2","Testinnhold2",false);
		appointment.addAttending(Database.getUser("vegaholt"));
		Appointment appointment2 = new Appointment(new Room("Sentrum Bowling", 200),new Date(2007,2,2, 12, 00),new Date(2007,2,2,13,00),new User("Name1","test@test.no","Test"),"Testavtale8","Testinnhold8",false);
		Notification notification = new Notification(Database.getUser("tandberg"), "tekst2");
		*/
		Log log = new Log();
	    log.setLogName("Client");
		
		ConnectionImpl conn = new ConnectionImpl(7877);
		
		try {
			conn.connect(InetAddress.getLocalHost(), 7878);
			String out = conn.receive();
			Log.writeToLog("RECEIVED THIS STRING + " + out, "CLIENT");
			Thread.sleep(150);
			conn.close();
			Log.writeToLog("CLOSING CLIENT", "CLIENT");
			
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		addappointment(appointment);
//		delappointment(appointment);
//		addappointment(appointment);
//		editappointment(appointment, appointment2);
//		setNotificationRead(notification, true);
//		getAppointmentsForUser(Database.getUser("tandberg"));
//		getUnansweredAppointmentsForUser(Database.getUser("tandberg"));
//		getAllUsers();
//		getAvailableRooms(1, Date.toDate("2012-09-03 08:00"), Date.toDate("2012-09-03 16:00"));
//		addUser(new User("Fjodor", "Fedor@gmail.com", "Fedja"), "123");
//		addRoom("F204", 10);
//		setAttending(Database.getUser("vegaholt"), appointment);
		//getAppointmentsByOwner(Database.getUser("tandberg"));
	}
	
	public static void addappointment(Appointment appointment) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		//lager string
		StringBuilder builder = new StringBuilder();
		builder.append("addappointment#");
		builder.append(appointment.getServerString());
		
		String returnString = Server.interpretInput(builder.toString());
	}
	public static void delappointment(Appointment appointment) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		//lager string
		StringBuilder builder = new StringBuilder();
		builder.append("delappointment#");
		builder.append(appointment.getServerString());
		
		String returnString = Server.interpretInput(builder.toString());
	}
	public static void editappointment(Appointment OLDappointment, Appointment NEWappointment) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		//lager string
		StringBuilder builder = new StringBuilder();
		builder.append("editappointment#");
		builder.append(OLDappointment.getServerString());
		builder.append(",");
		builder.append(NEWappointment.getServerString());

		String returnString = Server.interpretInput(builder.toString());

	}
	public static void setNotificationRead(Notification notification, boolean read) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		User user = notification.getUser();
		
		StringBuilder builder = new StringBuilder();
		builder.append("setNotificationRead#");
		builder.append(user.getServerString());
		builder.append(",");
		builder.append(notification.getText());
		
		String returnString = Server.interpretInput(builder.toString());

	}
	public static ArrayList<Appointment> getAppointmentsForUser(User user) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		StringBuilder builder = new StringBuilder();
		builder.append("getAppointmentsForUser#");
		builder.append(user.getUsername());
		
		String returnString = Server.interpretInput(builder.toString());
		
		//lager appointments og legger de i ArrayList
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		String[] apps = returnString.split("�");
	
		//g�r gjennom hver appointmentString og lager appointment
		for(int i = 0; i < apps.length; i++){
			String[] args = apps[i].split(",");
			
			Date start = Date.toDate(args[0]);
			Date end = Date.toDate(args[1]);
			String title = args[2];
			String description = args[3];
			User owner = User.toUser(args[4]);			
			Room room = Room.toRoom(args[6]);
			boolean hidden = Boolean.parseBoolean(args[7]);
			Appointment appointment = new Appointment(room, start, end, owner, title, description, hidden);
			//legger til appointment i arraylist
			appointments.add(appointment);
			
			//liste med attendies
			String[] users = args[5].split(">");
			if(args[5].length()>0){
				for(int j = 0; j < users.length; j++){
					appointment.addAttending(User.toUser(users[j]));
				}
			}
		}
		return appointments;
	}
	public static ArrayList<Appointment> getUnansweredAppointmentsForUser(User user) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		StringBuilder builder = new StringBuilder();
		builder.append("getUnansweredAppointmentsForUser#");
		builder.append(user.getUsername());
		
		String returnString = Server.interpretInput(builder.toString());
		
		//lager appointments og legger de i ArrayList
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		String[] apps = returnString.split("�");
	
		//g�r gjennom hver appointmentString og lager appointment
		for(int i = 0; i < apps.length; i++){
			String[] args = apps[i].split(",");
			
			Date start = Date.toDate(args[0]);
			Date end = Date.toDate(args[1]);
			String title = args[2];
			String description = args[3];
			User owner = User.toUser(args[4]);			
			Room room = Room.toRoom(args[6]);
			boolean hidden = Boolean.parseBoolean(args[7]);
			Appointment appointment = new Appointment(room, start, end, owner, title, description, hidden);
			//legger til appointment i arraylist
			appointments.add(appointment);
			
			//liste med attendies
			String[] users = args[5].split(">");
			if(args[5].length()>0){
				for(int j = 0; j < users.length; j++){
					appointment.addAttending(User.toUser(users[j]));
				}
			}
		}
		return appointments;	
	}
	public static ArrayList<User> getAllUsers() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		String userString = Server.interpretInput("getAllUsers#");
		
		ArrayList<User> userlist = new ArrayList<User>();
		String[] users = userString.split("�");
		
		for(int i = 0; i < users.length; i++){
			userlist.add(User.toUser(users[i]));
		}
		return userlist;
	}
	public static ArrayList<Room> getAvailableRooms(int capasity, Date start, Date end) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		StringBuilder builder = new StringBuilder();
		builder.append("getAvailableRooms#");
		builder.append(capasity);
		builder.append(",");
		builder.append(start.getTimeString());
		builder.append(",");
		builder.append(end.getTimeString());
		
		String returnString = Server.interpretInput(builder.toString());
		
		ArrayList<Room> roomlist = new ArrayList<Room>();
		String[] rooms = returnString.split("�");
		
		for(int i = 0; i < rooms.length; i++){
			roomlist.add(Room.toRoom(rooms[i]));
		}
		return roomlist;
	}
	public static void addUser(User user, String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		StringBuilder builder = new StringBuilder();
		builder.append("addUser#");
		builder.append(user.getServerString());
		builder.append(",");
		builder.append(password);
		System.out.println(builder.toString());
		String returnString = Server.interpretInput(builder.toString());
		System.out.println(returnString);
	}
	public static void addRoom(String roomName, int capasity) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		StringBuilder builder = new StringBuilder();
		builder.append("addRoom#");
		builder.append(roomName);
		builder.append("-");
		builder.append(capasity);
		String returnString = Server.interpretInput(builder.toString());
		System.out.println(returnString);
	}
	public static void setAttending(User user, Appointment appointment) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		StringBuilder builder = new StringBuilder();
		builder.append("setAttending#");
		builder.append(user.getServerString());
		builder.append(",");
		builder.append(appointment.getServerString());
		builder.append(",");
		builder.append("1");
		
		System.out.println(builder.toString());
		String returnString = Server.interpretInput(builder.toString());
		
		System.out.println(returnString);
	}
	public static ArrayList<Appointment> getAppointmentsByOwner(User user) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		StringBuilder builder = new StringBuilder();
		builder.append("getAppointmentsByOwner#");
		builder.append(user.getServerString());
		
		String returnString = Server.interpretInput(builder.toString());
		
		//lager appointments og legger de i ArrayList
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		String[] apps = returnString.split("�");
	
		//g�r gjennom hver appointmentString og lager appointment
		for(int i = 0; i < apps.length; i++){
			String[] args = apps[i].split(",");
			
			Date start = Date.toDate(args[0]);
			Date end = Date.toDate(args[1]);
			String title = args[2];
			String description = args[3];
			User owner = User.toUser(args[4]);			
			Room room = Room.toRoom(args[6]);
			boolean hidden = Boolean.parseBoolean(args[7]);
			Appointment appointment = new Appointment(room, start, end, owner, title, description, hidden);
			//legger til appointment i arraylist
			appointments.add(appointment);
			
			//liste med attendies
			String[] users = args[5].split(">");
			if(args[5].length()>0){
				for(int j = 0; j < users.length; j++){
					appointment.addAttending(User.toUser(users[j]));
				}
			}
		}
		return appointments;
	}
	
}
