package Logic;

import java.util.Calendar;

public class Date {

	private long timestamp;
	private String timeString;
	
	public Date(int year, int month, int day, int hour, int minutes) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minutes, 0);
		timestamp = cal.getTimeInMillis();
		timeString = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":00";
		
	}

	public long getTimestamp() {
		return timestamp;
	}
	public String getTimeString() {
		return timeString;
	}
	
	
}
