/*
 * Created on Oct 27, 2004
 *
 */
package no.ntnu.fp.net.co;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.cl.ClSocket;
import no.ntnu.fp.net.co.Connection;

/**
 * Simplest possible test application, client part.
 *
 * @author seb, steinjak
 */
public class TestCoClient {

  /**
   * Empty.
   */
  public TestCoClient() {
  }

  /**
   * Program Entry Point.
   */
  public static void main (String args[]){

    // Set up log
    Log log = new Log();
    log.setLogName("Client");

    // Connection object listening on 7877
    Connection conn = new ConnectionImpl(7877);
    InetAddress addr;  // will hold address of host to connect to
    try {
      // get address of local host and connect
      addr = InetAddress.getLocalHost();
      
      conn.connect(addr, 7878);
      try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
      String sith;
      conn.send("Hi, server, i'm client");
      sith = conn.receive();
      Log.writeToLog("recevied this sith: " + sith, "ClientTest");
      try {
    	  
    	  
    	  
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      conn.send("hi again, server");
      
      
      try {
  		Thread.sleep(1000);
  	} catch (InterruptedException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
      
      Log.writeToLog("Client is now closing the connection!",
		     "TestClient");
      conn.close();
       
      
    }

    catch (ConnectException e){
      Log.writeToLog("connectException: " + e.getMessage(),"ClientTest");
      e.printStackTrace();
    }
    catch (UnknownHostException e){
      Log.writeToLog("UnknownHostException: " + e.getMessage(),"ClientTest");
      e.printStackTrace();
    }
    catch (IOException e){
      Log.writeToLog("IOExceotion: " + e.getMessage(),"ClientTest");
      e.printStackTrace();
    }

    System.out.println("CLIENT TEST FINISHED");
    Log.writeToLog("CLIENT TEST FINISHED","ClientTest");
    
  }

}
