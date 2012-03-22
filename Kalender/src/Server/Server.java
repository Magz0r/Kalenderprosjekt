package Server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;  
import java.util.List;    

import Logic.Appointment;
import Logic.Date;
import Logic.Room;
import Logic.User;

public class Server {
	
	static List<String> commandList;
	
	
	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		commandList = new ArrayList<String>();
		commandList.add("login");
		commandList.add("addappointment");
		commandList.add("delappointment");
		commandList.add("editappointment");
		commandList.add("setNotificationRead");
		
		interpretInput("addappointment:starttime,endtime,title,description,owner,room_id,private");
	}
	static void interpretInput(String input) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		//Splitter command til string og args til string[]
		String[] args = input.split(":");
		String command = args[0];
		args = args[1].split(",");
		print(command, args);
		
		
		//Finner metodeindex
		int commandIndex = 0;
		for(int i = 0; i < commandList.size(); i++){
			if(command.equals(commandList.get(i))) commandIndex = i;
		}
		
		
		switch(commandIndex){
		case 0 :{ //login
			Database.login(args[0], args[1]);
			break;
		}
		case 1 :{ //addappointment
			Date start = Date.toDate(args[0]);
			Date end = Date.toDate(args[1]);
			String title = args[2];
			String description = args[3];
			User owner = User.toUser(args[4]);
			Room room = Room.toRoom(args[5]);
			boolean hidden = Boolean.parseBoolean(args[6]);
			Appointment appointment = new Appointment(room, start, end, owner, title, description, hidden);
			break;
		}
		case 2 :{
			break;
		}
		case 3: {
			break;
		}
		case 4: {
			
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
