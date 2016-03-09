package commandListeners;

import java.util.ArrayList;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import listeners.Listeners;
import msg.IRCMsg;

public class HelpCommand extends BaseCommand {
	
	private String helpKey;
	private Listeners commandListeners;
	
	public HelpCommand( IRCBot ircbot, IRCCommands ircCommands ) {
		super(ircbot, ircCommands);
		this.commandListeners = ircbot.getBotCommandListeners();
	}

	@Override
	public void loadCommandDescription() {
		description = new ArrayList<String>();
		description.add( "HELP - usage: ![botnick] HELP [COMMAND]");
		description.add( "  - for a list of commands run ![botnick] LIST");
	}

	@Override
	public boolean listen(IRCMsg msg) {
		
		System.out.println("Prefix: " + msg.getPrefix());
		System.out.println("Command: " + msg.getCommand());
		System.out.println("Args[0]: " + msg.getArgs()[0]);
		System.out.println("Trailing: " + msg.getTrailing());
	
		String target = msg.getArgs()[0];
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
		
//		helpKey = helpKey.toUpperCase();
//		ArrayList<String> tmpDescription = botCommands.getSingleCommandDescription(helpKey);
//		for(String line : tmpDescription ){
//			outboundMsgQ.add( ircCommands.privmsg(target, line) );			
//		}
		
		//	Reset target & helpKey once command has executed
		target = null;
		helpKey = null;
	}
}
