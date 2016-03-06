package listeners;

import java.util.concurrent.ConcurrentLinkedQueue;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import msg.IRCMsg;

/**
 * 
 * @author Kyte
 * 
 * A Listener is registered to a specific map of Listeners. It performs a check against
 * the object passed to it and makes a determination if an action should be performed.
 * 
 */
public abstract class BaseListener implements Listener {
	
	public Listeners timedListeners;
	public Listeners eventListeners;
	
	protected IRCCommands ircCommands;
	
	public BaseListener(IRCBot ircbot, IRCCommands ircCommands){
		
		this.ircCommands = ircCommands;
		this.ircCommands = new IRCCommands();
		
		this.timedListeners = ircbot.getTimedListeners();
		this.eventListeners = ircbot.getEventListeners();
		this.outboundMsgQ = ircbot.getOutboundMsgQ();
	}
	
	public ConcurrentLinkedQueue<String> outboundMsgQ;
	
	@Override
	public abstract boolean listen(IRCMsg msg);

	@Override
	public abstract void doAction();

	@Override
	public abstract boolean listenerFinished();	
}
