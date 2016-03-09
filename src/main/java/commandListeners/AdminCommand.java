package commandListeners;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import msg.IRCMsg;

/**
 * 
 * @author JKyte
 * 
 * This listener is only available to the admin of the bot.
 *
 */
public abstract class AdminCommand extends BaseCommand {

	public AdminCommand( IRCBot ircbot, IRCCommands ircCommands) {
		super(ircbot, ircCommands);
		//	Pass-through constructor
	}

	@Override
	public boolean listen(IRCMsg msg) {
		return msg.isFromAdmin();
	}
}
