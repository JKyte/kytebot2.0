package listenerFactories;

import responses.BotResponses;
import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import listeners.JoinChannelListener;
import listeners.Listeners;

public class EventListenerFactory {
	
	public static Listeners createEventListeners(IRCBot ircbot, IRCCommands ircCommands){
		
		Listeners listeners = new Listeners();
		
		//	Currently only handle GREETING listeners this way
		int curListener = 0;
		for( String greetingChan : ircbot.getConfigs().getGreetingChans() ){
			String greetingName = "GREET_" + ++curListener;
			listeners.put(greetingName, new JoinChannelListener(ircbot, ircCommands, greetingChan, new BotResponses()));		
		}
		
		return listeners;
	}
}
