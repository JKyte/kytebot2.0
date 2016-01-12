package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author JKyte
 *
 * Simple class that reads lines off a socket's input stream, wraps the line in an IRCMsg object, 
 * and adds the IRCMsg to a ConcurrentLinkedQueue for further processing
 */
public class InputThread extends Thread {

	private Socket socket;
	private ConcurrentLinkedQueue<String> inboundMsgQ;
	
	public InputThread( Socket socket, ConcurrentLinkedQueue<String> inboundMsgQ){
		
		this.socket = socket;
		this.inboundMsgQ = inboundMsgQ;
	}
	
	@Override
	public void run(){
		
		System.out.println("InputThread started.");
		String msg = null;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			
			while ( true ){
				
				msg = br.readLine();
				
				if( msg != null ){
					inboundMsgQ.add( msg );
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		//	log.error(msg);
		//	log.fatal("FATAL: InputThread crashed.");
			System.out.println("FATAL: InputThread has crashed.");
		}
	}
}
