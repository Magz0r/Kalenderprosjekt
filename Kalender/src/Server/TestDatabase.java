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
		Appointment appointment = new Appointment(new Room("R140", 5),new Date(2002,2,2, 12, 00),new Date(2002,2,2,13,00),new User("Owner1","owner@test.no","Owner"),"Notetest","TestinnholdNotetest",false);
		//Appointment appointment2 = new Appointment(new Room("R40", 4),new Date(2002,2,2, 12, 00),new Date(2002,2,2,13,00),new User("Name1","test@test.no","Test"),"Testavtale8","Testinnhold8",false);
		User user = new User("Navn2","navn@test.test2","Brukernavn2");
		appointment.addAttending(new User("Ola Nordmann","ola@norge.no","OlaN"));
		appointment.addAttending(new User("Lise Nordmann","lise@norge.no","LiseN"));
		try {
			Database.addAppointment(appointment);
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		try {
			ArrayList<Room> al = Database.getAvailableRooms(5, new Date(1999,1,1,12,00), new Date(1999,1,1,13,00));
			for(int i = 0;i<al.size();i++) {
				System.out.println(al.get(i).getName());
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		//try {
			//Database.editAppointment(appointment, appointment2);
			//Database.addAppointment(appointment);
			//Database.delAppointment(appointment);
			//Database.addNotification(user, "Test");
			//Database.setNotificationRead(new Notification(user, "Test"), true);
			//System.out.print(Database.login("tandberg", "1234"));
			/*ArrayList<Appointment> al = new ArrayList<Appointment>();
			al = Database.getUnansweredAppointmentsForUser("OlaN");
			for(int i = 0;i<al.size();i++) {
				System.out.println(al.get(i).getTitle());
				System.out.println(al.get(i).getDescription());
				System.out.println(al.get(i).getEnd().getTimeString());
				System.out.println(al.get(i).getRoom().getName());
				System.out.println(al.get(i).getStart().getTimeString());
				System.out.println(al.get(i).getOwner().getName());
				System.out.println("Test" + al.get(i).getAttendies().get(0).getName());
			}
			
			
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
		*/
	}

}
