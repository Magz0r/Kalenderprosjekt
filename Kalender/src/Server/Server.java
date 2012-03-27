package Server;

import java.sql.SQLException;
import java.util.ArrayList; 
import java.util.List;    

import Logic.Appointment;
import Logic.Date;
import Logic.Notification;
import Logic.Room;
import Logic.User;
import Server.Database;


public class Server {
	
	static List<String> commandList;
	
	
	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		commandList = new ArrayList<String>();
		commandList.add("login");
		commandList.add("addappointment");
		commandList.add("delappointment");
		commandList.add("editappointment");
		commandList.add("setNotificationRead");
		commandList.add("getAppointmentsForUser");
		commandList.add("getUnansweredAppointmentsForUser"); //
		commandList.add("getAllUsers");
		commandList.add("getAvailableRooms");
		commandList.add("addUser");
		commandList.add("addRoom");
		commandList.add("setAttending");
		
//		interpretInput("login#Tandberg,123");
//		interpretInput("addappointment#2012-09-03 08:00,2012-09-03 16:00,Styremøte,beskrivelse av møte,Vegard-vegard.holter@gmail.com-vegaholt,Ola Nordmann-ola@norge.no-OlaN>Lise Nordmann-lise@norge.no-LiseN,F1-200,0");
//		interpretInput("delappointment#2012-09-03 08:00,2012-09-03 16:00,Styremøte,beskrivelse av møte,Vegard-vegard.holter@gmail.com-vegaholt,Ola Nordmann-ola@norge.no-OlaN>Lise Nordmann-lise@norge.no-LiseN,F1-200,0");
		interpretInput("editappointment#2012-09-03 08:00,2012-09-03 16:00,Styremøte,beskrivelse av møte,Vegard-vegard.holter@gmail.com-vegaholt,Ola Nordmann-ola@norge.no-OlaN>Lise Nordmann-lise@norge.no-LiseN,F1-200,0,2012-09-03 15:00,2012-09-03 20:00,Bespisning,Mat,Vegard-vegard.holter@gmail.com-vegaholt,Lise Nordmann-lise@norge.no-LiseN,Kjel-200,0");
//		interpretInput("setNotificationRead#Øystein Tandberg-tandeey@gmail.com-tandberg,halla");
//		interpretInput("getAppointmentsForUser#OlaN");
//		interpretInput("getUnansweredAppointmentsForUser#LiseN");
//		interpretInput("getAllUsers");
//		interpretInput("getAvailableRooms#1,2012-09-03 08:00,2012-09-03 16:00");
//		interpretInput("addUser#Vegard-vegard.holter@gmail.com-vegaholt,123");
//		interpretInput("addRoom#R3-300");
//		interpretInput("setAttending#Vegard-vegard.holter@gmail.com-vegaholt,2012-09-03 08:00,2012-09-03 16:00,Styremøte,beskrivelse av møte,Vegard-vegard.holter@gmail.com-vegaholt,F1-200,0,1");
	}
	static void interpretInput(String input) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		//Splitter command til string og args til string[]
		String[] args = input.split("#");
		String command = args[0];
		if(args.length!=1){
			args = args[1].split(",");
		}
		print(command, args); //for testing
		
		
		//Finner metodeindex
		int commandIndex = 0;
		for(int i = 0; i < commandList.size(); i++){
			if(command.equals(commandList.get(i))) commandIndex = i;
		}
//		System.out.println("INDEX" + commandIndex); //for testing
		
		switch(commandIndex){
		case 0 :{ //login
			Database.login(args[0], args[1]);
			break;
		}
		case 1 :{ //addappointment ok
			Date start = Date.toDate(args[0]);
			Date end = Date.toDate(args[1]);
			String title = args[2];
			String description = args[3];
			User owner = User.toUser(args[4]);			
			Room room = Room.toRoom(args[6]);
			boolean hidden = Boolean.parseBoolean(args[7]);
			Appointment appointment = new Appointment(room, start, end, owner, title, description, hidden);
			//liste med attendies
			String[] users = args[5].split(">");
			for(int i = 0; i < users.length; i++){
				appointment.addAttending(User.toUser(users[i]));
			}
			
			
			Database.addAppointment(appointment);
			break;
		}
		case 2 :{ //delappointment ok
			Date start = Date.toDate(args[0]);
			Date end = Date.toDate(args[1]);
			String title = args[2];
			String description = args[3];
			User owner = User.toUser(args[4]);
			Room room = Room.toRoom(args[5]);
			boolean hidden = Boolean.parseBoolean(args[6]);
			Appointment appointment = new Appointment(room, start, end, owner, title, description, hidden);
			
			Database.delAppointment(appointment);
			break;
		}
		case 3: { //editAppointment ok
			Date OLDstart = Date.toDate(args[0]);
			Date OLDend = Date.toDate(args[1]);
			String OLDtitle = args[2];
			String OLDdescription = args[3];
			User OLDowner = User.toUser(args[4]);
			Room OLDroom = Room.toRoom(args[6]);
			boolean OLDhidden = Boolean.parseBoolean(args[7]);
			
			Appointment OLDappointment = new Appointment(OLDroom, OLDstart, OLDend, OLDowner, OLDtitle, OLDdescription, OLDhidden);
			
			String[] OLDusers = args[5].split(">");
			for(int i = 0; i < OLDusers.length; i++){
				OLDappointment.addAttending(User.toUser(OLDusers[i]));
			}
			
			
			Date start = Date.toDate(args[8]);
			Date end = Date.toDate(args[9]);
			String title = args[10];
			String description = args[11];
			User owner = User.toUser(args[12]);
			Room room = Room.toRoom(args[14]);
			boolean hidden = Boolean.parseBoolean(args[15]);
			Appointment appointment = new Appointment(room, start, end, owner, title, description, hidden);
			
			String[] users = args[13].split(">");
			System.out.println("NEW");
			for(int i = 0; i < users.length; i++){
				appointment.addAttending(User.toUser(users[i]));
			}
			
			Database.editAppointment(OLDappointment, appointment);
			break;
		}
		case 4: { //setNotificationRead ok
			User user = User.toUser(args[0]);
			String tekst = args[1];
			Notification notification = new Notification(user, tekst);
			Database.setNotificationRead(notification, true);
			break;
		}
		case 5: { //getAppointmentsForUser mangler å sende
			String username = args[0];
			ArrayList<Appointment> appointments = Database.getAppointmentsForUser(username);
			//lag format for sending til client
			StringBuilder builder = new StringBuilder();
			for(Appointment app : appointments){
				builder.append(app.getServerString() + "¤");
			}
			System.out.println(builder.toString());
			//sendes til client
			
			
			break;
		}
		case 6: { //getUnasweredAppointmentsForUser
			String username = args[0];
			ArrayList<Appointment> appointments = Database.getUnansweredAppointmentsForUser(username);
			
			StringBuilder builder = new StringBuilder();
			for(Appointment app : appointments){
				builder.append(app.getServerString() + "¤");
			}
			System.out.println(builder.toString());
			break;
		}
		case 7: { //getAllUsers må sendes
			ArrayList<User> users = Database.getAllUsers();
			
			StringBuilder builder = new StringBuilder();
			for(User user : users){
				builder.append(user.getServerString() + "¤");
			}
			System.out.println(builder.toString());
			//sendes til client
			
			
			break;
		}
		case 8: { //getAvailableRooms må sendes
			int capasity = Integer.parseInt(args[0]);
			Date start = Date.toDate(args[1]);
			Date end = Date.toDate(args[2]);
			ArrayList<Room> rooms = Database.getAvailableRooms(capasity, start, end);
			
			StringBuilder builder = new StringBuilder();
			for(Room room : rooms){
				builder.append(room.toString() + "¤");
			}
			System.out.println(builder.toString());
			//sendes til client
			
			
			break;
		}
		case 9: { //addUser ok
			User user = User.toUser(args[0]);
			String password = args[1];
			Database.addUser(user, password);
			break;
		}
		case 10: { //addRoom ok
			Room room = Room.toRoom(args[0]);
			Database.addRoom(room);
			break;
		}
		case 11: { //setAttending
		
			User user = User.toUser(args[0]);
			Date start = Date.toDate(args[1]);
			Date end = Date.toDate(args[2]);
			String title = args[3];
			String description = args[4];
			User owner = User.toUser(args[5]);
			Room room = Room.toRoom(args[6]);
			boolean hidden = Boolean.parseBoolean(args[7]);
			Appointment appointment = new Appointment(room, start, end, owner, title, description, hidden);			
			String attending = args[8];
			
			Database.setAttending(user, appointment, attending);
			break;
		}
		}
	}

	static void print(String command, String[] args){
		System.out.println("Command: " + command);
		System.out.println("Args: ");
		for(int i = 0; i < args.length; i++){
			System.out.println(args[i]);
		}
	}
}