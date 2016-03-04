package commands;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import botconfigs.IRCCommands;
import listeners.Listener;
import listeners.Listeners;
import msg.IRCMsg;

/**
 * 
 * @author JKyte
 * 
 * This class implements the Trigger interface. Where Triggers are set by authorized users, a
 * Command is loaded at runtime and is persistent.
 *
 * All commands extend this class for storage in a ConcurrentHashMap of type BaseCommand. The
 * isTriggered() and doAction() methods MUST be overridden by child classes.
 */
public abstract class BaseCommand implements Listener {

	protected String target = null;
	protected String admin = null;
	
	public Listeners timedTriggers;
	public Listeners eventTriggers;
	
	protected BotCommands botCommands;
	protected IRCCommands ircCommands;
	
	protected String botnick;

	public ConcurrentLinkedQueue<String> outboundMsgQ;
	
	public ArrayList<String> description;
	
	public BaseCommand(BotCommands botCommands, Listeners timedTriggers, Listeners eventTriggers, 
			ConcurrentLinkedQueue<String> outboundMsgQ){

		this.botCommands = botCommands;
		this.ircCommands = new IRCCommands();
		
		this.timedTriggers = timedTriggers;
		this.eventTriggers = eventTriggers;
		this.outboundMsgQ = outboundMsgQ;
		
		this.admin = this.botCommands.getAdminNick();
		this.botnick = this.botCommands.getBotnick();
		loadCommandDescription();
	}
	
	@Override
	public abstract boolean listen(IRCMsg msg);

	@Override
	public abstract void doAction();

	public abstract void loadCommandDescription();
	
	//	Commands are persistent listeners, always return false
	@Override
	public boolean listenerFinished() { return false; };
	
	public ArrayList<String> getCommandDescription() { 
		return description; 
	}

}
