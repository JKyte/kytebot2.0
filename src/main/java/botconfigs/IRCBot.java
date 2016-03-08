package botconfigs;

import io.OutputThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import core.IRCMsgHandler;
import listeners.Listeners;
import msg.IRCMsg;

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

	private Listeners timedListeners;
	private Listeners eventListeners;

	private OutputThread ot;
	private IRCMsgHandler msgHandler;

	/**
	 * Only initialize the queues at first
	 */
	public IRCBot( String configPath ){
		this.configs = BotConfigFactory.createBotConfigs(configPath);
		this.heartBeatInMillis = configs.getHeartbeat();
		inboundMsgQ = new ConcurrentLinkedQueue<String>();
		outboundMsgQ = new ConcurrentLinkedQueue<String>();

		timedListeners = new Listeners();
		eventListeners = new Listeners();

		msgHandler = new IRCMsgHandler(this);
	}

	/**
	 * Handles initializing all threads, initial authorization
	 */
	@Override
	public void run() {
		try {
			//	Connect to server
			socket = new Socket( configs.getIrcserver(), configs.getIrcport() );
			ot = new OutputThread(socket, outboundMsgQ);

			//	Setup threads
			Thread t1 = new Thread( ot );
			Thread t2 = new Thread( msgHandler );

			//	Start threads
			t1.start();
			t2.start();

			System.out.println("All threads started.");
			/**
			 * Sleep to let things initialize
			 */
			Thread.sleep(3000);

			String nick = configs.getBotnick();

			outboundMsgQ.add( "nick " + nick );
			outboundMsgQ.add( "USER "+nick+" 0 * :"+nick );

			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			String msg = null;
			while ( true ){

				msg = br.readLine();
				
				if( msg != null ){
					inboundMsgQ.add( msg );
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("FATAL: InputThread has crashed.");
		}
	}

	public BotConfigs getConfigs() {
		return configs;
	}

	public void setConfigs(BotConfigs configs) {
		this.configs = configs;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public long getHeartBeatInMillis() {
		return heartBeatInMillis;
	}

	public void setHeartBeatInMillis(long heartBeatInMillis) {
		this.heartBeatInMillis = heartBeatInMillis;
	}

	public ConcurrentLinkedQueue<String> getInboundMsgQ() {
		return inboundMsgQ;
	}

	public void setInboundMsgQ(ConcurrentLinkedQueue<String> inboundMsgQ) {
		this.inboundMsgQ = inboundMsgQ;
	}

	public ConcurrentLinkedQueue<String> getOutboundMsgQ() {
		return outboundMsgQ;
	}

	public void setOutboundMsgQ(ConcurrentLinkedQueue<String> outboundMsgQ) {
		this.outboundMsgQ = outboundMsgQ;
	}

	public Listeners getTimedListeners() {
		return timedListeners;
	}

	public void setTimedListeners(Listeners timedListeners) {
		this.timedListeners = timedListeners;
	}

	public Listeners getEventListeners() {
		return eventListeners;
	}

	public void setEventListeners(Listeners eventListeners) {
		this.eventListeners = eventListeners;
	}

	public OutputThread getOt() {
		return ot;
	}

	public void setOt(OutputThread ot) {
		this.ot = ot;
	}

	public IRCMsgHandler getIRCMsgHandler() {
		return msgHandler;
	}

	public void setIRCMsgHandler(IRCMsgHandler msgHandler) {
		this.msgHandler = msgHandler;
	}

}
