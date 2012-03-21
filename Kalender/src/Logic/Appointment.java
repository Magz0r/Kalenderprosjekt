package Logic;

import java.util.ArrayList;

public class Appointment {

	private Room room;
	private Date start, end;
	private User owner;
	private String title, description;
	private boolean hidden;
	private ArrayList<User> attendies;
	
	public Appointment(Room room, Date start, Date end, User owner,
			String title, String description, boolean hidden) {

		this.room = room;
		this.start = start;
		this.end = end;
		this.owner = owner;
		this.title = title;
		this.description = description;
		this.hidden = hidden;
		attendies = new ArrayList<User>();
	}
	
	public Appointment(String title, User owner) {
		this.title = title;
		this.owner = owner;
		hidden = true;
		attendies = new ArrayList<User>();
	}
	
	public Appointment() {
		
	}
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public ArrayList<User> getAttendies() {
		return attendies;
	}
	
	public void addAttending(User user) {
		attendies.add(user);
	}
	
	public void removeAttending(User user) {
		attendies.remove(user);
	}
	public void removeAttendings(){
		attendies.clear();
	}
	
}
