package io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private final static String CRLF = "\r\n";
    private final Logger log = LogManager.getLogger(getClass());
    private Socket socket;
	private ConcurrentLinkedQueue<String> outboundMsgQ;
	private BufferedWriter bw;

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

        log.info("OutputThread started.");
        try {

			String ircMsg = null;
			while( true ){

				ircMsg = outboundMsgQ.poll();

				if( ircMsg != null ){
                    if (!ircMsg.startsWith("PONG")) {
                        log.info("OUT: " + ircMsg);
                    }
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
            log.fatal("FATAL: OutputThread has stopped.");
        }
	}

	public void sendMsg( String msg ){
		try {

			msg += CRLF;
			bw.write( msg );
			bw.flush();

		} catch (IOException e) {
		//	log.fatal("WARN: OutputThread failed to send command: " + msg );
            log.warn("WARN: OutputThread failed to send command: " + msg);
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
