package Logic;

public class Notification {

	private User user;
	private String text;
	private boolean read;
	
	public Notification(User user, String text) {
		this.user = user;
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
	public String toString() {
		if(text.length() > 25) {
			
			return text.substring(0, 25)+"...";
		} else {
			return text;
		}
	}
	
	
}
