package triggers;

import java.util.concurrent.ConcurrentLinkedQueue;

import botconfigs.IRCCommands;
import responses.KytebotResponses;
import msg.IRCMsg;

public class JoinChannelTrigger extends BaseTrigger {
	
	String targetChan;
	KytebotResponses responses;
	
	public JoinChannelTrigger(IRCCommands ircCommands, Triggers timedTriggers,
			Triggers eventTriggers, ConcurrentLinkedQueue<String> outboundMsgQ,
			String targetChan, KytebotResponses responses) {
		
		super(ircCommands, timedTriggers, eventTriggers, outboundMsgQ);
		this.targetChan = targetChan;
		this.responses = responses;
	}
	

	@Override
	public boolean isTriggered(IRCMsg msg) {
		if( msg.getCommand().equalsIgnoreCase("JOIN") && msg.getTrailing().equalsIgnoreCase(targetChan)){
			return true;
		}
		return false;
	}

	@Override
	public void doAction() {
		this.outboundMsgQ.add( ircCommands.action(targetChan, responses.getRandomJoinAction()));
	}

	@Override
	public boolean triggerFinished() {
		return false;
	}

}
