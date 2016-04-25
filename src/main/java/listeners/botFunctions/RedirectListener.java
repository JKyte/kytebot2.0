package listeners.botFunctions;

import botconfigs.IRCBot;
import listeners.BaseListener;
import msg.IRCMsg;
import org.junit.Assert;

import java.util.HashMap;

/**
 * Created by JKyte on 4/16/2016.
 */
public class RedirectListener extends BaseListener {

    private HashMap<String, String> redirectMappings;
    private String REDIRECT_DEFAULT_TARGET;
    private String currentRedirectTarget;
    private String redirectedMsg;

    public RedirectListener(IRCBot ircbot) {
        super(ircbot);
        redirectMappings = new HashMap<>();
        REDIRECT_DEFAULT_TARGET = ircbot.getConfigs().getStartChan();

        String[] sourceSink = ircbot.getConfigs().getRedirectSourceSink().split(",");
        this.addNewRedirect(sourceSink[0], sourceSink[1]);
    }

    @Override
    public boolean listen(IRCMsg msg) {
        if (redirectMappings.keySet().contains(msg.getArgs()[0])) {
            redirectedMsg = msg.getArgs()[0] + " " + msg.getFromNick() + ": " + msg.getTrailing();
            currentRedirectTarget = redirectMappings.get(msg.getArgs()[0]);
            return true;
        } else {
            return false;
        }
    }

    public void addNewRedirect(String sourceChannel) {
        addNewRedirect(sourceChannel, REDIRECT_DEFAULT_TARGET);
    }

    public void addNewRedirect(String sourceChannel, String redirectTarget) {
        redirectMappings.put(sourceChannel, redirectTarget);
    }

    @Override
    public void doAction() {
        Assert.assertNotNull(currentRedirectTarget);
        Assert.assertNotNull(redirectedMsg);

        outboundMsgQ.add(ircCommands.privmsg(currentRedirectTarget, redirectedMsg));
        redirectedMsg = null;
        currentRedirectTarget = null;
    }

    @Override
    public boolean listenerFinished() {
        return false;
    }
}
