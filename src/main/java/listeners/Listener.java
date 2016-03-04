package listeners;

import msg.IRCMsg;

public interface Listener {
	
	public abstract boolean listen(IRCMsg msg);
	
	public abstract void doAction();
	
	public abstract boolean listenerFinished();
}
