package io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author JKyte
 * 
 * This class reads messages off of a queue and sends them to a specified IRC server
 *
 */
public class OutputThread extends Thread{

	private Socket socket;
	private ConcurrentLinkedQueue<String> outboundMsgQ;
	private BufferedWriter bw;
	private final static String CRLF = "\r\n";

	public OutputThread( Socket socket, ConcurrentLinkedQueue<String> outboundMsgQ){

		this.setSocket(socket);
		this.outboundMsgQ = outboundMsgQ;

		try {
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		System.out.println("OutputThread started.");
		try {

			String ircMsg = null;
			while( true ){

				ircMsg = outboundMsgQ.poll();

				if( ircMsg != null ){
				//System.out.println("out: " + ircMsg );
					sendMsg(ircMsg);
				}

			//	Thread.sleep(1000);
			}
	//	} catch (InterruptedException e){
		//	log.error(e);
	//		System.err.println("EXCEPTION: " + e.getLocalizedMessage());
	//		e.printStackTrace();
		} finally {
		//	log.fatal("FATAL: OutputThread has stopped.");
			System.out.println("FATAL: OutputThread has stopped.");
		}
	}

	public void sendMsg( String msg ){
		try {

			msg += CRLF;
			bw.write( msg );
			bw.flush();

		} catch (IOException e) {
		//	log.fatal("WARN: OutputThread failed to send command: " + msg );
			System.out.println("WARN: OutputThread failed to send command: " + msg);
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
