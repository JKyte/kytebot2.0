package listeners.botFunctions;

import botconfigs.IRCBot;
import listeners.BaseListener;
import msg.IRCMsg;
import util.RandomList;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This class allows the bot to perform an action when a user joins a channel.
 */
public class UserJoinsChannelListener extends BaseListener {

    private HashSet<String> channels;
    private RandomList joinActions;
    private String targetChan;

    public UserJoinsChannelListener(IRCBot ircbot) {
        super(ircbot);
        this.targetChan = null;
        this.channels = ircbot.getConfigs().getGreetingChans();
        this.joinActions = new RandomList(loadJoinActions());
    }

    @Override
    public boolean listen(IRCMsg msg) {

        //  Up front check, fail fast
        if (!msg.getCommand().equalsIgnoreCase("JOIN")) {
            return false;
        }

        if (null != msg.getTrailing() && channels.contains(msg.getTrailing())) {
            targetChan = msg.getTrailing();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void doAction() {
        this.outboundMsgQ.add(ircCommands.action(targetChan, joinActions.getListEntry()));
        targetChan = null;  //  Null out the target, we don't want to cross wires
    }

    @Override
    public boolean listenerFinished() {
        return false;
    }

    private ArrayList<String> loadJoinActions() {
        ArrayList<String> actionOnJoin = new ArrayList<>();
        actionOnJoin.add("glances up from his newspaper disapprovingly");
        actionOnJoin.add("looks up from drafting a writ of execution");
        actionOnJoin.add("goes back to play Europa Universalis IV");
        actionOnJoin.add("stirs a glass of scotch");
        actionOnJoin.add("wonders where the whiskey is");
        actionOnJoin.add("yearns for a Master of Coin");
        actionOnJoin.add("wonders where torchbot is");
        actionOnJoin.add("makes a note of the last time Torchwood was seen");
        actionOnJoin.add("turns over in his sleep");
        actionOnJoin.add("holds out a piece of dead code");
        actionOnJoin.add("wishes for more features");
        actionOnJoin.add("ponders existence");
        actionOnJoin.add("adjusts his bowtie");
        actionOnJoin.add("puts away his foil");
        actionOnJoin.add("makes faces in a mirror");
        actionOnJoin.add("practices his microexpressions");
        return actionOnJoin;
    }
}
