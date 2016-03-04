package core;

import java.util.concurrent.ConcurrentLinkedQueue;

import msg.IRCMsg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import botconfigs.IRCBot;
import gui.UserInputBox;

/**
 * 
 * @author JKyte
 * 
 * This class will combine the functionality of two legacy classes by both parsing raw IRC messages
 * and interpreting them.
 *
 */
public class IRCMsgHandler implements Runnable {

	public ConcurrentLinkedQueue<String> inboundMsgQ;
	public ConcurrentLinkedQueue<String> outboundMsgQ;
	public ConcurrentLinkedQueue<IRCMsg> internalMsgQ;

	private Logger log = LogManager.getLogger(IRCMsgHandler.class);
	
	public IRCMsgHandler( IRCBot bot ){
		this.inboundMsgQ = bot.getInboundMsgQ();
		this.outboundMsgQ = bot.getOutboundMsgQ();
		this.internalMsgQ = bot.getInternalMsgQ();
	
		UserInputBox uib = new UserInputBox(outboundMsgQ);
	}
	
	@Override
	public void run() {

		String rawMsg;
		try {	
			while ( true ){

				rawMsg = inboundMsgQ.poll();
				if( null != rawMsg ){
					try {
						
					//	long start = System.currentTimeMillis();
						handleRawMsg(rawMsg);
					//	updateParseStats( (System.currentTimeMillis() - start) );
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			//	log.error(msg, e);
		} finally {
			//	log.fatal("FATAL:" + msg.toString());
			//	log.fatal("FATAL: InputThread crashed.");
		}
	}
	
	public void handleRawMsg(String rawMsg){
		if( !isPing(rawMsg) ){
			parseRawMsg(rawMsg);
		}
	}
	
	public boolean isPing(String rawMsg){
		if( rawMsg.startsWith("PING") ){
			String pingResponse = rawMsg.replaceAll("PING", "PONG");
			outboundMsgQ.add(pingResponse);		
			return true;
		}
		return false;
	}

	public void parseRawMsg( String rawMsg ){
		
		String copyOfRawMsg = rawMsg;
		String prefix = null;
		String command = null;
		String[] args = null;
		String trailing = null;
		
		if( rawMsg.charAt(0) == ':' ){
			rawMsg = rawMsg.substring(1);
			prefix = rawMsg.split(" ")[0];
			rawMsg = rawMsg.substring(rawMsg.indexOf(" ")+1);
		}
		
		if( rawMsg.indexOf(" :") != -1 ){
			trailing = rawMsg.substring(rawMsg.indexOf(":")+1);
			command = rawMsg.substring(0, rawMsg.indexOf(" "));
			
			rawMsg = rawMsg.substring(rawMsg.indexOf(" "), rawMsg.indexOf(":")).trim();
			args = rawMsg.split(" :")[0].split(" ");
		}else{
			command = rawMsg.substring(0, rawMsg.indexOf(" "));
			rawMsg = rawMsg.substring(rawMsg.indexOf(" ")).trim();
			args = rawMsg.split(" ");
		}
		
		internalMsgQ.add( new IRCMsg(copyOfRawMsg, prefix, command, args, trailing) );
	}
}
