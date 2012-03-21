package Logic;

public class Notification {

	private User user;
	private String text;
	private boolean read;
	
	public Notification(User user, String text, boolean read) {
		this.user = user;
		this.text = text;
		this.read = read;
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
	
	
	
}
