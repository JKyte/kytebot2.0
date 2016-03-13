package commandListeners;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import msg.IRCMsg;

import java.util.ArrayList;

public class ListCommand extends BaseCommand {

	public ListCommand(IRCBot ircbot, IRCCommands ircCommands) {
		super(ircbot, ircCommands);

	}
	
	@Override
	public void loadCommandDescription() {
        description = new ArrayList<>();
        description.add( "LIST - usage: ![botnick] LIST");
		description.add( "  - prints a list of available bot commands");
	}

	@Override
	public boolean listen(IRCMsg msg) {
		if( msg.getTrailing().equalsIgnoreCase("LIST")){
			//	Set the appropriate target
            log.info("Prefix: " + msg.getPrefix());
            log.info("Command: " + msg.getCommand());
            log.info("Args[0]: " + msg.getArgs()[0]);
            log.info("Trailing: " + msg.getTrailing());

            target = msg.getArgs()[0];
			return true;
		}
		return false;
	}

	@Override
	public void doAction() {
//		HashMap<String, ArrayList<String>> commandsAndDescriptions = botCommands.getCommandsAndDescription();
//		Set<String> commandKeys = commandsAndDescriptions.keySet();
//		for( String key : commandKeys ){
//			ArrayList<String> descriptionLines = commandsAndDescriptions.get(key);
//			
//			for( String line : descriptionLines ){
//				outboundMsgQ.add( ircCommands.privmsg(target, line) );
//			}
//		}

		//	Reset target once command has executed
		target = null;
		
	}

}

