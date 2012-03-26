package Server;

import java.sql.SQLException;
import java.util.ArrayList;

import Logic.Appointment;
import Logic.Notification;
import Logic.Room;
import Logic.Date;
import Logic.User;

public class TestDatabase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Appointment appointment = new Appointment(new Room("R40", 5),new Date(2003,2,2, 12, 00),new Date(2003,2,2,13,00),new User("Test2","owner@test.no","Test2"),"Testavtale2","Testinnhold2",false);
		Appointment appointment2 = new Appointment(new Room("R40", 4),new Date(2004,2,2, 12, 00),new Date(2004,2,2,13,00),new User("Name1","test@test.no","Test"),"Testavtale8","Testinnhold8",false);
		User user = new User("Ola Nordmann","ola@norge.no","OlaN");
		appointment.addAttending(new User("Ola Nordmann","ola@norge.no","OlaN"));
		appointment.addAttending(new User("Lise Nordmann","lise@norge.no","LiseN"));
		try {
			ArrayList<Appointment> al2 = Database.getUnansweredAppointmentsForUser("LiseN");
			ArrayList<User> al = Database.getUsersByAppointmentAndStatus(al2.get(0), 2);
			
			System.out.println(al2.get(0).getTitle());
			System.out.println(al.get(0).getUsername());
			//Database.editAppointment(appointment, appointment2);
			//Database.delAppointment(appointment);
			//Database.setAttending(user, appointment, "null");
		} catch (InstantiationException e1) {
		} catch (IllegalAccessException e1) {
		} catch (ClassNotFoundException e1) {
		} catch (SQLException e1) {
		}
	}

}
