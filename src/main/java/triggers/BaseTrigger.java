package triggers;

import java.util.concurrent.ConcurrentLinkedQueue;

import botconfigs.IRCCommands;
import msg.IRCMsg;

/**
 * 
 * @author Kyte
 * 
 * A Trigger life cycle is outlined here
 * 1. Trigger.triggered(object o) is called. This checks to see if the triggers activating condition
 * 		is met. Returns true if it does.
 * 2. If a Trigger.isTriggered(), then the trigger action occurs.
 * 3. Finally, the trigger checks if it is at the end of it's life (1 time event vs recurring)
 * 
 * 
 * TODO -- triggers to fully add
 * 
 * - JoinTriggers
 * 
 * - TimerTriggers
 * 		- @ interval
 * 		- @ set time
 *
 */
public abstract class BaseTrigger implements Trigger {
	
	public Triggers timedTriggers;
	public Triggers eventTriggers;
	
	protected IRCCommands ircCommands;
	
	public BaseTrigger(IRCCommands ircCommands, Triggers timedTriggers, Triggers eventTriggers, 
			ConcurrentLinkedQueue<String> outboundMsgQ){
		
		this.ircCommands = ircCommands;
		this.ircCommands = new IRCCommands();
		
		this.timedTriggers = timedTriggers;
		this.eventTriggers = eventTriggers;
		this.outboundMsgQ = outboundMsgQ;
	}
	public ConcurrentLinkedQueue<String> outboundMsgQ;
	
	@Override
	public abstract boolean isTriggered(IRCMsg msg);

	@Override
	public abstract void doAction();

	@Override
	public abstract boolean triggerFinished();	
}
