package listenerFactories;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import listeners.Listeners;
import listeners.botFunctions.UserFarewellsBotListener;
import listeners.botFunctions.UserGreetsBotListener;
import listeners.botFunctions.UserJoinsChannelListener;

public class EventListenerFactory {
	
	public static Listeners createEventListeners(IRCBot ircbot, IRCCommands ircCommands){
		
		Listeners listeners = new Listeners();

        if (null != ircbot.getConfigs().getGreetingChans()) {
            listeners.put("GreetingListener", new UserJoinsChannelListener(ircbot, ircCommands));
        }

        if (null != ircbot.getConfigs().getGreetingChans()) {
            listeners.put("UserGreetsBotListener", new UserGreetsBotListener(ircbot, ircCommands));
        }

        if (null != ircbot.getConfigs().getFarewellChans()) {
            listeners.put("UserFarewellsBotListener", new UserFarewellsBotListener(ircbot, ircCommands));
        }

		return listeners;
	}
}
