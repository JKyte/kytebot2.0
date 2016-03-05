package listeners;

import java.util.concurrent.ConcurrentLinkedQueue;

import botconfigs.IRCCommands;
import responses.BotResponses;
import msg.IRCMsg;

public class JoinChannelListener extends BaseListener {
	
	String targetChan;
	BotResponses responses;
	
	public JoinChannelListener(IRCCommands ircCommands, Listeners timedTriggers,
			Listeners eventTriggers, ConcurrentLinkedQueue<String> outboundMsgQ,
			String targetChan, BotResponses responses) {
		
		super(ircCommands, timedTriggers, eventTriggers, outboundMsgQ);
		this.targetChan = targetChan;
		this.responses = responses;
	}
	

	@Override
	public boolean listen(IRCMsg msg) {
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
	public boolean listenerFinished() {
		return false;
	}

}
