package Logic;

import java.util.Calendar;

public class Date {

	private long timestamp;
	private String timeString;
	private String date;
	private String clock;
	
	
	public Date(int year, int month, int day, int hour, int minutes) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minutes, 0);
		timestamp = cal.getTimeInMillis();
		timeString = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":00";
		date = day + ". " + month + ". " + year;
		clock = hour+":"+minutes;
		if(minutes == 0) {
			clock += "0";
		}
	}

	public long getTimestamp() {
		return timestamp;
	}
	public String getTimeString() {
		return timeString;
	}
	public String getDate() {
		return date;
	}
	public String getClock() {
		return clock;
	}
	
	public static Date toDate(String dateString) {
		String[] ar = dateString.split(" ");
		String[] d = ar[0].split("-");
		String[] t = ar[1].split(":");
		int year = Integer.parseInt(d[0]);
		int month = Integer.parseInt(d[1]);
		int day = Integer.parseInt(d[2]);
		int hour = Integer.parseInt(t[0]);
		int minute = Integer.parseInt(t[1]);
		Date output = new Date(year, month, day, hour, minute);
		return output;
	}

	
}
