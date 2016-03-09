package commandListeners;

import java.util.ArrayList;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import msg.IRCMsg;

public class JoinCommand extends AdminCommand {

	private String targetChannel;

	public JoinCommand(IRCBot ircbot, IRCCommands ircCommands) {
		super(ircbot, ircCommands);
	}

	@Override
	public boolean listen(IRCMsg msg) {
		//	Ensure an admin is making this call
		if( !super.listen(msg) ){
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
