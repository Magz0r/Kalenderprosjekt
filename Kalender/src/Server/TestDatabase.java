package Server;

import java.sql.SQLException;

import Logic.Appointment;
import Logic.Room;
import Logic.Date;
import Logic.User;

public class TestDatabase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Appointment appointment = new Appointment(new Room("R41", 5),new Date(2000,1,1, 12, 00),new Date(2000,1,1,13,00),new User("Name1","test@test.no","Test"),"Testavtale","Testinnhold",false);
		appointment.addAttending(new User("Ola Nordmann","ola@norge.no","OlaN"));
		appointment.addAttending(new User("Lise Nordmann","lise@norge.no","LiseN"));
		try {
			Database.delAppointment(appointment);
		} catch (SQLException e) {
			System.out.println("Crash");
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.out.println("Crash");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("Crash");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Crash");
			e.printStackTrace();
		}
	}

}
