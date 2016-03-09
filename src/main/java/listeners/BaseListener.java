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
	
	protected IRCBot ircbot;
	
	public Listeners interruptListeners;
	public Listeners eventListeners;
	
	//	Local copy of the botnick
	protected String botnick;
	
	protected IRCCommands ircCommands;
	
	public BaseListener(IRCBot ircbot, IRCCommands ircCommands){
		
		this.ircbot = ircbot;
		this.botnick = ircbot.getConfigs().getBotnick();
		
		this.ircCommands = ircCommands;
		
		this.interruptListeners = this.ircbot.getInterruptListeners();
		this.eventListeners = this.ircbot.getEventListeners();
		this.outboundMsgQ = this.ircbot.getOutboundMsgQ();
	}
	
	public ConcurrentLinkedQueue<String> outboundMsgQ;
	
	@Override
	public abstract boolean listen(IRCMsg msg);

	@Override
	public abstract void doAction();

	@Override
	public abstract boolean listenerFinished();	
}
