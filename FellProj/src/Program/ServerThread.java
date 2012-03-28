package Program;

import java.io.IOException;
import java.net.ConnectException;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.co.ConnectionImpl;

public class ServerThread extends Server implements Runnable{
	
ConnectionImpl conn;
	public ServerThread (ConnectionImpl connection){
		this.conn = connection;
	}
	@Override
	public void run() {
		Log log = new Log();
	    log.setLogName("ServerThread" + conn.getConnectionIp());
	    Log.writeToLog("was called", "ServerThread");
		// TODO Auto-generated method stub
		try {
			conn.send("Hello, client, i'm server");
			conn.receive();
			
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
