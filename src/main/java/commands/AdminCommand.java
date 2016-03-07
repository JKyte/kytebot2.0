package commands;

import botconfigs.IRCBot;
import msg.IRCMsg;

public abstract class AdminCommand extends BaseCommand {

	public AdminCommand( IRCBot ircbot, BotCommands botCommands) {
		super(ircbot, botCommands);
		//	Pass-through constructor
	}

	@Override
	public boolean listen(IRCMsg msg) {
		return msg.isFromAdmin();
	}

	@Override
	public abstract void doAction();

	@Override
	public abstract void loadCommandDescription();

}
