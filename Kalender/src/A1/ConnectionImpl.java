/*
 * Created on Oct 27, 2004
 */
package no.ntnu.fp.net.co;

import java.io.EOFException;

//import no.ntnu.fp.net.cl;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.cl.ClException;
import no.ntnu.fp.net.cl.ClSocket;
import no.ntnu.fp.net.cl.KtnDatagram;
import no.ntnu.fp.net.cl.KtnDatagram.Flag;

/**
 * Implementation of the Connection-interface. <br>
 * <br>
 * This class implements the behaviour in the methods specified in the interface
 * {@link Connection} over the unreliable, connectionless network realised in
 * {@link ClSocket}. The base class, {@link AbstractConnection} implements some
 * of the functionality, leaving message passing and error handling to this
 * implementation.
 * 
 * @author Sebjørn Birkeland and Stein Jakob Nordbø
 * @see no.ntnu.fp.net.co.Connection
 * @see no.ntnu.fp.net.cl.ClSocket
 */
public class ConnectionImpl extends AbstractConnection {

	int lastReceivedSeqNum = -1;
    /** Keeps track of the used ports for each server port. */
    private static Map<Integer, Boolean> usedPorts = Collections.synchronizedMap(new HashMap<Integer, Boolean>());

    /**
     * Initialise initial sequence number and setup state machine.
     * 
     * @param myPort
     *            - the local port to associate with this connection
     */
    public ConnectionImpl(int myPort) {
        //throw new NotImplementedException();
    	this.myPort = myPort;
    	this.myAddress = getIPv4Address();
    }

    private String getIPv4Address() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    /**
     * Establish a connection to a remote location.
     * 
     * @param remoteAddress
     *            - the remote IP-address to connect to
     * @param remotePort
     *            - the remote portnumber to connect to
     * @throws IOException
     *             If there's an I/O error.
     * @throws java.net.SocketTimeoutException
     *             If timeout expires before connection is completed.
     * @see Connection#connect(InetAddress, int)
     */
    public void connect(InetAddress remoteAddress, int remotePort) throws IOException,
            SocketTimeoutException {
    	Log.writeToLog("I was called", "ConnectionImpl - connect");
    	
    	//setting up IP addresses, ports
    	this.remoteAddress = remoteAddress.getHostAddress();
    	this.remotePort = remotePort;   	
    	
    	
    	//sending SYN-packet
    	KtnDatagram syn_packet = this.constructInternalPacket(Flag.SYN);
    	//Log.writeToLog(syn_packet, "sending this syn packet with SendTimer", "ConnectionImpl - connect");	
    	SendTimer sTimer = new SendTimer(new ClSocket(), syn_packet);
    	sTimer.run();
    	this.state = State.SYN_SENT;
    	
    	//receiving SYN_ACK-packet
    	KtnDatagram syn_ack_packet;
    	
    	long before = System.currentTimeMillis();
    	long after;
    	while (true){
    			after = System.currentTimeMillis();
    			if (after-before > this.TIMEOUT){
    				Log.writeToLog("timeout while w8ing for SYN_ACK", "ConnectionImpl - connect");
    				throw new SocketTimeoutException("Timeout while waiting for SYN_ACK, server might be down or perhaps network-failure");
    			}
    				
    			syn_ack_packet = this.receivePacket(true);
    			if (syn_ack_packet == null)
    				continue;
    			if (syn_ack_packet.getFlag() != Flag.SYN_ACK){
    				Log.writeToLog(syn_ack_packet, "got this pckg while w8ing for SYN_ACK", "ConnectionImpl - connect");
    			} else{
    				sTimer.cancel();
    				Log.writeToLog(syn_ack_packet, "received SYN_ACK ", "ConnectionImpl - connect");
    				break;
    			}
    	}
    	this.lastValidPacketReceived = syn_ack_packet;
    	
    	//waiting because the sender begins with listening 2 ack 50 ms after the package is sent. Sometimes ACK is sent before sender begins listening 2 it (happened under development stage)
			try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//ACK'ing the SYN_ACK packet
    	this.sendAck(syn_ack_packet, false);
    	//this.sendAck(syn_packet, false);
    	this.state = State.ESTABLISHED;
    	Log.writeToLog("connection established", "ConnectionImpl - connect");
    }

    /**
     * Listen for, and accept, incoming connections.
     * 
     * @return A new ConnectionImpl-object representing the new connection.
     * @see Connection#accept()
     */
    public Connection accept() throws IOException, SocketTimeoutException {
    	
    	
    	
    	
        Log.writeToLog("I was called", "ConnectionImpl - accept");
        this.state = State.LISTEN;
        
        
        myAddress = getIPv4Address();
        ConnectionImpl returnedConnection = new ConnectionImpl(this.myPort);
        
        //receiving internal pckges until one with SYN flag comes
        KtnDatagram pckg_received = new KtnDatagram();
        pckg_received = this.receivePacketNotNull();
        
        while (true){       	
        		if (pckg_received.getFlag() != Flag.SYN){
        			Log.writeToLog(pckg_received, "received while w8ing for SYN pckg, ignoring", "ConnectionImpl - accept");
        		} else{
        			Log.writeToLog(pckg_received, "received SYN pckg, ->", "ConnectionImpl - accept");
        			this.state = State.SYN_RCVD;
        			break;
        		}
            pckg_received = this.receivePacketNotNull();
        }
        this.lastValidPacketReceived = pckg_received;
        
        //constructing and sending SYN_ACK pckg
        KtnDatagram syn_ack_pckg = this.constructInternalPacket(Flag.SYN_ACK);
        syn_ack_pckg.setDest_addr(pckg_received.getSrc_addr());
        syn_ack_pckg.setDest_port(pckg_received.getSrc_port());
        
        SendTimer sTimer = new SendTimer(new ClSocket(), syn_ack_pckg);
        sTimer.run();
        
        //Syn_ack sent, waiting for ack (infinitely may be, should be revised later)
        
        while (true){
        	pckg_received = this.receiveAck();
        	if (pckg_received == null)
        		continue;
        	if (pckg_received.getFlag() != Flag.ACK){
        		Log.writeToLog(pckg_received, "w8ed for ACK, got this, ignoring", "ConnectionImpl - accept");
        	} else{
        		
        		Log.writeToLog(pckg_received , "received this ACK ", "ConnectionImpl - accept");
        		sTimer.cancel();
        		
        		returnedConnection.lastValidPacketReceived = pckg_received;
        		returnedConnection.lastDataPacketSent = syn_ack_pckg;
        		break;
        	}
        }
        
        //returning configured connection in established state
        returnedConnection.lastReceivedSeqNum = pckg_received.getSeq_nr();
        returnedConnection.remoteAddress = pckg_received.getSrc_addr();
        returnedConnection.remotePort = pckg_received.getSrc_port();
        returnedConnection.state = State.ESTABLISHED;
        Log.writeToLog("returning new established connection", "ConnectionImpl - accept");
		return returnedConnection;
    }

    /**
     * Send a message from the application.
     * 
     * @param msg
     *            - the String to be sent.
     * @throws ConnectException
     *             If no connection exists.
     * @throws IOException
     *             If no ACK was received.
     * @see AbstractConnection#sendDataPacketWithRetransmit(KtnDatagram)
     * @see no.ntnu.fp.net.co.Connection#send(String)
     */
    public void send(String msg) throws ConnectException, IOException {
    	
    	Log.writeToLog("I was called", "ConnectionImpl - send");
    	KtnDatagram data_pckg = this.constructDataPacket(msg);
    	SendTimer sTimer = new SendTimer(new ClSocket(), data_pckg);
    	this.lastDataPacketSent = data_pckg;
    	
    	sTimer.run();
    	KtnDatagram rcved_ack;
    	
    	long before = System.currentTimeMillis();
    	long after;
    	while (true){
    		after = System.currentTimeMillis();
    		if (after - before > this.TIMEOUT){
    			Log.writeToLog("TIMEOUT while w8ing for ACK", "ConnectionImpl - send");
    			throw new ConnectException();
    			
    		}
    		rcved_ack = this.receiveAck();
    		if (rcved_ack == null){
    			Log.writeToLog("got null-pck while w8ing for ACK", "ConnectionImpl - send");
    			continue;
    		} else
    			Log.writeToLog(rcved_ack, "received this pck while w8ing for ack", "ConnectionImpl - receive");
    		this.lastReceivedSeqNum = rcved_ack.getSeq_nr();
    		if (rcved_ack.getAck() == data_pckg.getSeq_nr()){
    			sTimer.cancel();
    			this.lastReceivedSeqNum = rcved_ack.getSeq_nr();
    			Log.writeToLog("package delivered", "ConnectionImpl - send");
    			break;
    		}
    		
    	}
    	
    	
    
    	
    	
    }
    

    /**
     * Wait for incoming data.
     * 
     * @return The received data's payload as a String.
     * @see Connection#receive()
     * @see AbstractConnection#receivePacket(boolean)
     * @see AbstractConnection#sendAck(KtnDatagram, boolean)
     */
    public String receive() throws ConnectException, IOException {
    	this.state = State.LISTEN;
    	Log.writeToLog("I was called" , "ConnectionImpl - receive");
    	KtnDatagram rcved_pckg = this.receivePacket(false);
    	
    	while (true){
    		if (rcved_pckg == null){
    			rcved_pckg = this.receivePacket(false);
    		} else{
    			
    			if (this.isValid(rcved_pckg)){
    				
    				if (rcved_pckg.getSeq_nr() == lastReceivedSeqNum + 1 || lastReceivedSeqNum == -1){
    				
    					//waiting because the sender begins with listening 2 ack 50 ms after the package is sent. Sometimes ACK is sent before sender begins listening 2 it (happened under development stage)
    					try {
    						Thread.sleep(150);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
     					Log.writeToLog(rcved_pckg, "received this pckg(valid CS), ACK'ing it", "ConnectionImpl - receive");
     					this.lastReceivedSeqNum = rcved_pckg.getSeq_nr();
     					this.sendAck(rcved_pckg, false);
    					this.lastValidPacketReceived = rcved_pckg;
    					break;
    				} else{
    					Log.writeToLog(rcved_pckg, "received this invalid package, had checksum : " + rcved_pckg.getChecksum() + " real checksum is : " + rcved_pckg.calculateChecksum(), "ConnectionImpl - receive");
    					this.sendAck(this.lastValidPacketReceived, false);
    				}
    			}
    		}
    	}
    	this.state = State.ESTABLISHED;
    	
    	
    	
    	
    	return rcved_pckg.getPayload().toString();
    }

    /**
     * Close the connection.
     * 
     * @see Connection#close()
     */
    public void close() throws IOException {
        throw new NotImplementedException();
    }

    /**
     * Test a packet for transmission errors. This function should only called
     * with data or ACK packets in the ESTABLISHED state.
     * 
     * @param packet
     *            Packet to test.
     * @return true if packet is free of errors, false otherwise.
     */
    protected boolean isValid(KtnDatagram packet) {
    	return packet.getChecksum() == packet.calculateChecksum();
    }
    
    private KtnDatagram receivePacketNotNull() throws EOFException, IOException{
    	KtnDatagram pckg_received = this.receivePacket(true);
    	
    	while (true){       	
        	if (pckg_received != null){
        			break;  		
        	} else{
        		Log.writeToLog("received null-pckg", "ConnectionImpl - receivePacketNotNull");
        	}
            pckg_received = this.receivePacket(true);
        }
    	return pckg_received;
    }
}
