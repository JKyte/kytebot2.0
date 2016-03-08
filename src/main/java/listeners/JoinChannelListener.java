package listeners;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import responses.BotResponses;
import msg.IRCMsg;

public class JoinChannelListener extends BaseListener {
	
	String targetChan;
	BotResponses responses;
	
	public JoinChannelListener(IRCBot ircbot, IRCCommands ircCommands,
			String targetChan, BotResponses responses) {
		super(ircbot, ircCommands);
		this.targetChan = targetChan;
		this.responses = responses;
	}
	

	@Override
	public boolean listen(IRCMsg msg) {
		if( msg.getCommand().equalsIgnoreCase("JOIN") 
				&& ( null != msg.getTrailing() && msg.getTrailing().equalsIgnoreCase(targetChan) )){
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
