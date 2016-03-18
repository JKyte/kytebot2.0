package listenerFactories;

import botconfigs.IRCBot;
import commandListeners.*;
import listeners.Listeners;

public class BotCommandListenerFactory {

    public static Listeners createEventListeners(IRCBot ircbot) {
        Listeners listeners = new Listeners();

        listeners.put("HELP", new HelpCommand(ircbot));
        listeners.put("LIST", new ListCommand(ircbot));
        listeners.put("TRADE", new TradeCommand(ircbot));
        listeners.put("JOIN", new JoinCommand(ircbot));
        listeners.put("FLIRT", new FlirtCommand(ircbot));

        return listeners;
	}
}
