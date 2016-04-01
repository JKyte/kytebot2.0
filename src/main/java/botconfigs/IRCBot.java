package botconfigs;

import commandListeners.CommandListeners;
import core.IRCMsgHandler;
import io.OutputThread;
import listenerFactories.BotCommandListenerFactory;
import listeners.Listeners;
import listeners.ircfunctions.AuthenticateInterrupt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author JKyte
 *
 * A basic implementation of an IRCBot inspired somewhat by the PIRCBot framework
 */
public class IRCBot extends Thread {

    Logger log = LogManager.getLogger(getClass());
    private BotConfigs configs;
	private Socket socket;
	private long heartBeatInMillis = -1;
	private ConcurrentLinkedQueue<String> inboundMsgQ;
	private ConcurrentLinkedQueue<String> outboundMsgQ;
	private Listeners interruptListeners;
	private Listeners eventListeners;
    private CommandListeners botCommandListeners;
    private OutputThread ot;
	private IRCMsgHandler msgHandler;

    private IRCCommands ircCommands;

	/**
	 * Only initialize the queues at first
	 */
    public IRCBot(boolean useProductionConfigs) {
        this.configs = BotConfigFactory.createBotConfigs(useProductionConfigs);
        this.setIrcCommands(new IRCCommands(configs));

        this.heartBeatInMillis = configs.getHeartbeat();
        inboundMsgQ = new ConcurrentLinkedQueue<>();
        outboundMsgQ = new ConcurrentLinkedQueue<>();

		interruptListeners = new Listeners();
		eventListeners = new Listeners();
        botCommandListeners = BotCommandListenerFactory.createCommandListeners(this);

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

            log.info("All threads started.");


            this.getIRCMsgHandler().getInterruptListeners().put("AuthListener", new AuthenticateInterrupt(this, 30));


            /**
			 * Sleep to let things initialize
			 */
			Thread.sleep(3000);

			String nick = configs.getBotnick();

            outboundMsgQ.add(getIrcCommands().setNick(nick));
            outboundMsgQ.add(getIrcCommands().userIdent(nick));

			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

            String msg;
            while ( true ){

				msg = br.readLine();
				
				if( msg != null ){
                    inboundMsgQ.add( msg );
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
            log.fatal("FATAL: InputThread has crashed.");
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

	public Listeners getInterruptListeners() {
		return interruptListeners;
	}

	public void setInterruptListeners(Listeners interruptListeners) {
		this.interruptListeners = interruptListeners;
	}
	
	public Listeners getEventListeners() {
		return eventListeners;
	}

	public void setEventListeners(Listeners eventListeners) {
		this.eventListeners = eventListeners;
	}

    public CommandListeners getBotCommandListeners() {
        return botCommandListeners;
	}

    public void setBotCommandListeners(CommandListeners botCommandListeners) {
        this.botCommandListeners = botCommandListeners;
	}

	public IRCMsgHandler getIRCMsgHandler() {
		return msgHandler;
	}

	public void setIRCMsgHandler(IRCMsgHandler msgHandler) {
		this.msgHandler = msgHandler;
	}

	public OutputThread getOt() {
		return ot;
	}

	public void setOt(OutputThread ot) {
		this.ot = ot;
	}

    public IRCCommands getIrcCommands() {
        return ircCommands;
    }

    public void setIrcCommands(IRCCommands ircCommands) {
        this.ircCommands = ircCommands;
    }
}
