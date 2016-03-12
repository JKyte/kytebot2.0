package listeners;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import msg.IRCMsg;
import responses.BotResponses;

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
        return msg.getCommand().equalsIgnoreCase("JOIN")
                && (null != msg.getTrailing() && msg.getTrailing().equalsIgnoreCase(targetChan));
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
