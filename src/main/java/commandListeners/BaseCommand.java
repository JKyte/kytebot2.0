package commandListeners;

import java.util.ArrayList;
import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import listeners.BaseListener;
import msg.IRCMsg;

/**
 * 
 * @author JKyte
 * 
 * This class extends the BaseListener class and adds a command description
 * which will be exposed to users via a HELP command.
 */
public abstract class BaseCommand extends BaseListener {

	protected String target;
	protected ArrayList<String> description;
	
	public BaseCommand(IRCBot ircbot, 
				IRCCommands ircCommands){

		super(ircbot, ircCommands);
		loadCommandDescription();
	}
	
	@Override
	public abstract boolean listen(IRCMsg msg);

	@Override
	public abstract void doAction();

	public abstract void loadCommandDescription();
	
	/**
	 * Always returns false, bot commands are persistent
	 */
	@Override
	public boolean listenerFinished() { return false; };
	
	public ArrayList<String> getCommandDescription() { 
		return description; 
	}

}
