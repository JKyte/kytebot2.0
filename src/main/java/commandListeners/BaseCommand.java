package commandListeners;

import botconfigs.IRCBot;
import listeners.BaseListener;
import msg.IRCMsg;

import java.util.ArrayList;

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

    public BaseCommand(IRCBot ircbot) {

        super(ircbot);
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
	public boolean listenerFinished() {
		return false;
	}

	public ArrayList<String> getCommandDescription() { 
		return description; 
	}

}
