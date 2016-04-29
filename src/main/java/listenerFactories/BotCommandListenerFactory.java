package listenerFactories;

import botconfigs.IRCBot;
import commandListeners.*;

public class BotCommandListenerFactory {

    public static CommandListeners createCommandListeners(IRCBot ircbot) {
        CommandListeners commandListeners = new CommandListeners();

        commandListeners.put("HELP", new HelpCommand(ircbot));
        commandListeners.put("LIST", new ListCommand(ircbot));
        commandListeners.put("TRADE", new TradeCommand(ircbot));
        commandListeners.put("JOIN", new JoinCommand(ircbot));
        commandListeners.put("FLIRT", new FlirtCommand(ircbot));
        commandListeners.put("CHANINFO", new ChannelInfoListener(ircbot));
        commandListeners.put("REDIRECT", new RedirectCommandListener(ircbot));
        commandListeners.put("SEEN", new SeenNickCommandListener(ircbot));

        return commandListeners;
    }
}
