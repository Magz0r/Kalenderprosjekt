package Logic;

import java.util.ArrayList;

public class Calendar {

	private ArrayList<Appointment> list;
	
	public Calendar() {
		list = new ArrayList<Appointment>();
	}
	
	public void addAppointment(Appointment appointment) {
		list.add(appointment);
	}
	
	public void deleteAppointment(Appointment appointment) {
		list.remove(appointment);
	}

	
	
}
