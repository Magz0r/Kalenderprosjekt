package Logic;

import java.util.Calendar;

public class Date {

	private long timestamp;
	
	public Date(int year, int month, int day, int hour, int minutes) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minutes, 0);
		timestamp = cal.getTimeInMillis();
		
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	
}
