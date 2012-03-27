/*
 * Created on Oct 27, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.ntnu.fp.net.co;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.co.Connection;

/**
 * Simplest possible test application, server part.
 *
 * @author seb, steinjak
 *
 */
public class TestCoServer {

  /**
   * Empty.
   */
  public TestCoServer() {
  }

  /**
   * Program Entry Point.
   */
  public static void main (String args[]){

    // Create log
    Log log = new Log();
    log.setLogName("Server");

    // server connection instance, listen on port 7778
    Connection server = new ConnectionImpl(7878);
    // each new connection lives in its own instance
    Connection conn;
    try {
    	
    	conn = server.accept();
    	String sith = conn.receive();
    	Log.writeToLog("received this sith: " + sith, "TestServer");
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	conn.send("hi bro, i'm server");
    	sith = conn.receive();
    	Log.writeToLog("received this sith: " + sith, "TestServer");
    	
    	try {
    	sith = conn.receive();
    	}catch (ConnectException e){
    		Log.writeToLog("received following connect exception : " + e.getMessage(), "TestServer");
    	}
    

    	
    	System.out.println("SERVER TEST FINISHED");
    	Log.writeToLog("TEST SERVER FINISHED","TestServer");
    	
    }
    catch (IOException e){
      e.printStackTrace();
    }
  }
}
