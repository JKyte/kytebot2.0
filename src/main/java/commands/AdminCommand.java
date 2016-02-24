package commands;

import java.util.concurrent.ConcurrentLinkedQueue;

import triggers.Triggers;
import msg.IRCMsg;

public abstract class AdminCommand extends BaseCommand {

	public AdminCommand(BotCommands botCommands, Triggers timedTriggers,
			Triggers eventTriggers, ConcurrentLinkedQueue<String> outboundMsgQ) {
		super(botCommands, timedTriggers, eventTriggers, outboundMsgQ);
		//	Pass-through constructor
	}

	@Override
	public boolean isTriggered(IRCMsg msg) {
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