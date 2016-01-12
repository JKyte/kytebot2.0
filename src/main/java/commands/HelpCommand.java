package commands;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import triggers.Triggers;
import msg.IRCMsg;

public class HelpCommand extends BaseCommand {
	
	private String helpKey;
	
	public HelpCommand(BotCommands botCommands, Triggers timedTriggers, 
			Triggers eventTriggers, ConcurrentLinkedQueue<String> outboundMsgQ) {

		super(botCommands, timedTriggers, eventTriggers, outboundMsgQ);
	}

	@Override
	public void loadCommandDescription() {
		description = new ArrayList<String>();
		description.add( "HELP - usage: ![botnick] HELP [COMMAND]");
		description.add( "  - for a list of commands run ![botnick] LIST");
	}

	@Override
	public boolean isTriggered(IRCMsg msg) {
		
		System.out.println("Prefix: " + msg.getPrefix());
		System.out.println("Command: " + msg.getCommand());
		System.out.println("Args[0]: " + msg.getArgs()[0]);
		System.out.println("Trailing: " + msg.getTrailing());
	
		target = msg.getArgs()[0];
		helpKey = null;
		String[] chunks = msg.getTrailing().split(" ");
		if( chunks.length == 1 && chunks[0].equalsIgnoreCase("HELP") ){
			helpKey = "HELP";
			return true;
		}else if( chunks.length == 2 && chunks[0].equalsIgnoreCase("HELP") ){
			helpKey = chunks[1];
			return true;
		}else{
			return false;	
		} 
	}

	
	@Override
	public void doAction() {
		System.out.println("Help command called!");
		
		helpKey = helpKey.toUpperCase();
		ArrayList<String> tmpDescription = botCommands.getSingleCommandDescription(helpKey);
		for(String line : tmpDescription ){
			outboundMsgQ.add( ircCommands.privmsg(target, line) );			
		}
		
		//	Reset target & helpKey once command has executed
		target = null;
		helpKey = null;
	}
}
