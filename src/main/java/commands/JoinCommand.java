package commands;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import msg.IRCMsg;
import triggers.Triggers;

public class JoinCommand extends AdminCommand {

	private String targetChannel;

	public JoinCommand(BotCommands botCommands, Triggers timedTriggers,
			Triggers eventTriggers, ConcurrentLinkedQueue<String> outboundMsgQ) {
		super(botCommands, timedTriggers, eventTriggers, outboundMsgQ);
	}

	@Override
	public boolean isTriggered(IRCMsg msg) {
		//	Ensure an admin is making this call
		if( !super.isTriggered(msg) ){
			return false;
		}
		
		String[] chunks = msg.getTrailing().split(" ");
		if( chunks.length == 2 && chunks[0].equalsIgnoreCase("JOIN") ){
			targetChannel = chunks[1];
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void doAction() {
		outboundMsgQ.add( ircCommands.joinChannel(targetChannel));	
	}

	@Override
	public void loadCommandDescription() {
		description = new ArrayList<String>();
		description.add( "JOIN - usage: ![botnick] JOIN [#channel]");
		description.add( "  - orders the bot to join a channel.");	
	}
}
