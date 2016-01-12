package triggers;

import msg.IRCMsg;

public interface Trigger {
	
	public abstract boolean isTriggered(IRCMsg msg);
	
	public abstract void doAction();
	
	public abstract boolean triggerFinished();
}
