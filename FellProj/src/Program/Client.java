package Program;

import java.sql.SQLException;
import java.util.ArrayList;

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
		Appointment appointment = new Appointment(new Room("R40", 5),new Date(2003,2,2, 12, 00),new Date(2003,2,2,13,00),new User("Test2","owner@test.no","Test2"),"Testavtale2","Testinnhold2",false);
		appointment.addAttending(Database.getUser("vegaholt"));
		Appointment appointment2 = new Appointment(new Room("R40", 4),new Date(2004,2,2, 12, 00),new Date(2004,2,2,13,00),new User("Name1","test@test.no","Test"),"Testavtale8","Testinnhold8",false);
		
		getAppointmentsForUser(Database.getUser("tandberg"));
		
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
//	interpretInput("getAppointmentsForUser#OlaN");
	public static void getAppointmentsForUser(User user) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		StringBuilder builder = new StringBuilder();
		builder.append("getAppointmentsForUser#");
		builder.append(user.getUsername());
		
		String returnString = Server.interpretInput(builder.toString());
		
		//lager appointments og legger de i ArrayList
//		Date start = Date.toDate(args[0]);
//		Date end = Date.toDate(args[1]);
//		String title = args[2];
//		String description = args[3];
//		User owner = User.toUser(args[4]);			
//		Room room = Room.toRoom(args[6]);
//		boolean hidden = Boolean.parseBoolean(args[7]);
//		Appointment appointment = new Appointment(room, start, end, owner, title, description, hidden);
//		//liste med attendies
//		String[] users = args[5].split(">");
//		if(args[5].length()>0){
//			for(int i = 0; i < users.length; i++){
//				appointment.addAttending(User.toUser(users[i]));
//			}
//		}

	}
//	getUnansweredAppointmentsForUser
//	getAllUsers
//	getAvailableRooms
//	addUser
//	addRoom
//	setAttending
//	getAppointmentsByOwner
	
}
