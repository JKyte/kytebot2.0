package triggers;

import java.util.concurrent.ConcurrentLinkedQueue;

import botconfigs.IRCCommands;
import msg.IRCMsg;

public class TimerTrigger extends BaseTrigger {


	public TimerTrigger(IRCCommands ircCommands, Triggers timedTriggers,
			Triggers eventTriggers, ConcurrentLinkedQueue<String> outboundMsgQ) {
		super(ircCommands, timedTriggers, eventTriggers, outboundMsgQ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isTriggered(IRCMsg msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean triggerFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
