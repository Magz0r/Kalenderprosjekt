package Server;

import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.ArrayList;

import Logic.Appointment;
//import Logic.Notification;
import Logic.Room;
import Logic.Date;
import Logic.User;

public class TestDatabase {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Appointment appointment = new Appointment(new Room("R40", 5),new Date(2003,2,2, 12, 00),new Date(2003,2,2,13,00),Database.getUser("tandberg"),"Testavtale2","Testinnhold2",false);
		Appointment appointment2 = new Appointment(new Room("R40", 4),new Date(2004,2,2, 12, 00),new Date(2004,2,2,13,00),new User("Name1","test@test.no","Test"),"Testavtale8","Testinnhold8",false);
		//User user = new User("Ola Nordmann","ola@norge.no","OlaN");
		appointment.addAttending(new User("Ola Nordmann","ola@norge.no","OlaN"));
		appointment.addAttending(new User("Lise Nordmann","lise@norge.no","LiseN"));
		appointment2.addAttending(new User("Ola Nordmann","ola@norge.no","OlaN"));
		appointment2.addAttending(new User("Vegard","vegard.holter@gmail.com","vegaholt"));
		try {
			Database.addAppointment(appointment);
			//ArrayList<User> al = Database.getUsersByAppointmentAndStatus(al2.get(0), 2);
			//Database.addAppointment(appointment);
			//System.out.println(al2.get(0).getTitle());
			//System.out.println(al.get(0).getUsername());
			//Database.editAppointment(appointment, appointment2);
			//Database.delAppointment(appointment);
			//Database.setAttending(user, appointment, "null");
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
