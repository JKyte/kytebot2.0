package io;

import botconfigs.IRCCommands;
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
public class OutputThread implements Runnable {

    private final static String CRLF = "\r\n";
    private final Logger log = LogManager.getLogger(getClass());
    private Socket socket;
	private ConcurrentLinkedQueue<String> outboundMsgQ;
	private BufferedWriter bw;

    private volatile boolean threadExecuting;

	public OutputThread( Socket socket, ConcurrentLinkedQueue<String> outboundMsgQ){

		this.setSocket(socket);
		this.outboundMsgQ = outboundMsgQ;
        setThreadExecuting(true);

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
            while (isThreadExecuting()) {

				ircMsg = outboundMsgQ.poll();

				if( ircMsg != null ){
                    if (!ircMsg.startsWith("PONG")) {
                        log.info("OUT: " + ircMsg);
                    }
                    sendMsg(ircMsg);
				}

			}
		} finally {
            log.fatal("FATAL: OutputThread has stopped.");
        }
	}

	public void sendMsg( String msg ){
		try {

			msg += CRLF;
			bw.write( msg );
			bw.flush();

		} catch (IOException e) {
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

    public boolean isThreadExecuting() {
        return threadExecuting;
    }

    public void setThreadExecuting(boolean threadExecuting) {
        this.threadExecuting = threadExecuting;
    }

    public void stopThreadExecution() {
        outboundMsgQ.add(IRCCommands.quit());
        setThreadExecuting(false);
    }
}
