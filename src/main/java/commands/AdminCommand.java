package commands;

import java.util.concurrent.ConcurrentLinkedQueue;

import listeners.Listeners;
import msg.IRCMsg;

public abstract class AdminCommand extends BaseCommand {

	public AdminCommand(BotCommands botCommands, Listeners timedTriggers,
			Listeners eventTriggers, ConcurrentLinkedQueue<String> outboundMsgQ) {
		super(botCommands, timedTriggers, eventTriggers, outboundMsgQ);
		//	Pass-through constructor
	}

	@Override
	public boolean listen(IRCMsg msg) {
		return callerHasAdminPrivileges(msg);
	}

	public boolean callerHasAdminPrivileges(IRCMsg msg) {
		return msg.getNickFromPrefix().equalsIgnoreCase(admin);
	}

	@Override
	public abstract void doAction();

	@Override
	public abstract void loadCommandDescription();

}
