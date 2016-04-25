package listenerFactories;

import botconfigs.IRCBot;
import listeners.Listeners;
import listeners.botFunctions.*;

public class EventListenerFactory {

    public static Listeners createEventListeners(IRCBot ircbot) {

        Listeners listeners = new Listeners();

        if (null != ircbot.getConfigs().getGreetingChans()) {
            listeners.put("GreetingListener", new UserJoinsChannelListener(ircbot));
        }

        if (null != ircbot.getConfigs().getGreetingChans()) {
            listeners.put("UserGreetsBotListener", new UserGreetsBotListener(ircbot));
        }

        if (null != ircbot.getConfigs().getFarewellChans()) {
            listeners.put("UserFarewellsBotListener", new UserFarewellsBotListener(ircbot));
        }

        listeners.put("YoutubeDescriptionListener", new YoutubeDescriptionListener(ircbot));

        listeners.put("RedirectListener", new RedirectListener(ircbot));

		return listeners;
	}
}
