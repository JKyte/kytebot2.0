package botconfigs;

import io.InputThread;
import io.OutputThread;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.BotConstants;
import msg.IRCMsg;
import parsers.IRCMsgInterpreter;
import parsers.IRCMsgParser;
import triggers.Triggers;

/**
 * 
 * @author JKyte
 *
 * A basic implementation of an IRCBot modeled closely after the PIRCBot framework
 */
public class IRCBot extends Thread {

	private BotConfigs configs;
	private Socket socket;
	private long heartBeatInMillis = -1;

	private ConcurrentLinkedQueue<String> inboundMsgQ;
	private ConcurrentLinkedQueue<String> outboundMsgQ;
	private ConcurrentLinkedQueue<IRCMsg> internalMsgQ;
	
	private Triggers timedTriggers;
	private Triggers eventTriggers;

	private InputThread it;
	private OutputThread ot;
	private IRCMsgParser parser;
	private IRCMsgInterpreter interpreter;
	
	/**
	 * Only initialize the queues at first
	 */
	public IRCBot( Properties prop ){
		this.configs = BotConfigFactory.createBotConfigs(BotConstants.PRODUCTION_DEFAULT);
		this.heartBeatInMillis = Long.parseLong(prop.getProperty("heartbeat"));
		inboundMsgQ = new ConcurrentLinkedQueue<String>();
		outboundMsgQ = new ConcurrentLinkedQueue<String>();
		internalMsgQ = new ConcurrentLinkedQueue<IRCMsg>();
	}


	/**
	 * Handles initializing all threads, initial authorization
	 */
	@Override
	public void run() {

		System.out.println("IRCBot is running");

		try {
			String host = configs.getIrcserver();
			int port = configs.getIrcport();
			socket = new Socket( host, port );
			
			it = new InputThread(socket, inboundMsgQ);
			ot = new OutputThread(socket, outboundMsgQ);
			
			timedTriggers = new Triggers();
			eventTriggers = new Triggers();
			parser = new IRCMsgParser(inboundMsgQ, outboundMsgQ, internalMsgQ);
			interpreter = new IRCMsgInterpreter(configs, internalMsgQ, outboundMsgQ, timedTriggers, eventTriggers);
			
			Thread t1 = new Thread( it );
			Thread t2 = new Thread( ot );
			Thread t3 = new Thread( parser );
			Thread t4 = new Thread( interpreter );

			t1.start();
			t2.start();
			t3.start();
			t4.start();
			
			System.out.println("All threads started.");
			/**
			 * Sleep to let things initialize
			 */
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			//	log.error(e.getMessage());
			}
			

			String nick = configs.getBotnick();

			outboundMsgQ.add( "nick " + nick );
			outboundMsgQ.add( "USER "+nick+" 0 * :"+nick );
			
			while( true ){

			//	log.info("IRCBot keep alive loop");
			//	System.out.println(nick + " keep alive loop");
				try {
					Thread.sleep(heartBeatInMillis);
				} catch (InterruptedException e) {
				//	log.error(e.getMessage());
				}
			}

		} catch (UnknownHostException e1) {
		//	log.error(e1.getStackTrace());
		} catch (IOException e2) {
		//	log.error(e2.getStackTrace());
		}
	}
	
}
