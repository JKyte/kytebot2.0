package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import msg.IRCMsg;
import triggers.Triggers;

public class ListCommand extends BaseCommand {

	public ListCommand(BotCommands botCommands, Triggers timedTriggers, Triggers eventTriggers,
			ConcurrentLinkedQueue<String> outboundMsgQ) {
		super(botCommands, timedTriggers, eventTriggers, outboundMsgQ);

	}
	
	@Override
	public void loadCommandDescription() {
		description = new ArrayList<String>();
		description.add( "LIST - usage: ![botnick] LIST");
		description.add( "  - prints a list of available bot commands");
	}

	@Override
	public boolean isTriggered(IRCMsg msg) {
		if( msg.getTrailing().equalsIgnoreCase("LIST")){
			//	Set the appropriate target
			System.out.println("Prefix: " + msg.getPrefix());
			System.out.println("Command: " + msg.getCommand());
			System.out.println("Args[0]: " + msg.getArgs()[0]);
			System.out.println("Trailing: " + msg.getTrailing());
			
			target = msg.getArgs()[0];
			return true;
		}
		return false;
	}

	@Override
	public void doAction() {
		HashMap<String, ArrayList<String>> commandsAndDescriptions = botCommands.getCommandsAndDescription();
		Set<String> commandKeys = commandsAndDescriptions.keySet();
		for( String key : commandKeys ){
			ArrayList<String> descriptionLines = commandsAndDescriptions.get(key);
			
			for( String line : descriptionLines ){
				outboundMsgQ.add( ircCommands.privmsg(target, line) );
			}
		}

		//	Reset target once command has executed
		target = null;
		
	}

}

