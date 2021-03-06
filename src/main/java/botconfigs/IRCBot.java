package botconfigs;

import commandListeners.CommandListeners;
import core.IRCMsgHandler;
import io.OutputThread;
import listenerFactories.BotCommandListenerFactory;
import listenerFactories.EventListenerFactory;
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
public class IRCBot implements Runnable {

    Logger log = LogManager.getLogger(getClass());

    private volatile boolean threadExecuting;
    private Thread t1;
    private Thread t2;

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
        this.setThreadExecuting(true);
        this.configs = BotConfigFactory.createBotConfigs(useProductionConfigs);
        this.setIrcCommands(new IRCCommands(configs));

        this.heartBeatInMillis = configs.getHeartbeat();
        inboundMsgQ = new ConcurrentLinkedQueue<>();
        outboundMsgQ = new ConcurrentLinkedQueue<>();

		interruptListeners = new Listeners();
        eventListeners = EventListenerFactory.createEventListeners(this);
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
            t1 = new Thread(ot);
            t2 = new Thread(msgHandler);

			//	Start threads
			t1.start();
			t2.start();

            log.info("All threads started.");

            //  Setup the authenticate listener
            this.getIRCMsgHandler().getInterruptListeners().put("AuthListener", new AuthenticateInterrupt(this, 30));

            //  Send auth messages to the server
            String nick = configs.getBotnick();
            outboundMsgQ.add(getIrcCommands().setNick(nick));
            outboundMsgQ.add(getIrcCommands().userIdent(nick));

			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

            String msg;
            while (isThreadExecuting()) {

				msg = br.readLine();
				
				if( msg != null ){
                    inboundMsgQ.add( msg );
				}
			}
        } catch (IOException e) {
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

    public boolean isThreadExecuting() {
        return threadExecuting;
    }

    public void setThreadExecuting(boolean threadExecuting) {
        this.threadExecuting = threadExecuting;
    }

    public void stopBot() {
        ot.stopThreadExecution();
        msgHandler.setThreadExecuting(false);
        t1.interrupt();
        t2.interrupt();

        try {
            //  You know, just to be sure
            this.socket.shutdownOutput();
            this.socket.shutdownInput();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setThreadExecuting(false);
    }
}
