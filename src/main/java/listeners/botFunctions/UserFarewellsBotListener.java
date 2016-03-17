package listeners.botFunctions;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import listeners.BaseListener;
import msg.IRCMsg;
import util.RandomList;

import java.util.*;

/**
 * Created by JKyte on 3/17/2016.
 */
public class UserFarewellsBotListener extends BaseListener {

    HashMap<String, Integer> userFarewells = new HashMap<>();
    RandomList botFarewells;
    String userToFarewell;
    private HashSet<String> farewellChannels;

    public UserFarewellsBotListener(IRCBot ircbot, IRCCommands ircCommands) {
        super(ircbot, ircCommands);
        farewellChannels = ircbot.getConfigs().getFarewellChans();
        loadUserFarewells();
        botFarewells = new RandomList(loadBotFarewells());
    }

    @Override
    public boolean listen(IRCMsg msg) {

        /**
         * Must validate that the msg is...
         * 1) A farewell
         * AND
         * 2) Either
         *  a) Directed at the bot in a side chat
         *  OR
         *  b) Is saying farewell the bot in an approved channel
         */
        if (null != msg.getTrailing() && isFarewell(msg.getTrailing())) {

            //  Determine if channel msg or side chat
            if (msg.getArgs()[0].equals(botnick)) {

                //  Side chat
                log.info("Side chat: " + msg.getArgs()[0]);
            } else {

                //  Else should always be a channel
                log.info("Chan: " + msg.getArgs()[0].startsWith("#"));

                // So confirm that the channel is allowed.
                if (!isValidFarewellChan(msg.getArgs()[0])) {
                    return false;
                }
            }

            userToFarewell = msg.getArgs()[0];

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void doAction() {
        String responseMsg = ircCommands.privmsg(userToFarewell, botFarewells.getListEntry());
        outboundMsgQ.add(responseMsg);
        userToFarewell = null; //  For posterity, I suppose
    }

    private boolean isValidFarewellChan(String chan) {
        return farewellChannels.contains(chan);
    }

    /**
     * Persistent listener, always return false
     *
     * @returns false
     */
    @Override
    public boolean listenerFinished() {
        return false;
    }


    /**
     * Determines if a message body contains a farewell. The criteria are such that the msg body must
     * 1. Start with one of any pre-defined farewells
     * 2. Aggregate score must pass a threshold
     * <p>
     * This enables us to respond to "goodbye botnick" or "good night botnick" while not responding to
     * messages such as "goodbye losers" or "good, good, it's all good botnick"
     *
     * @param msgBody - the trailing portion of an IRCMsg
     */
    public boolean isFarewell(String msgBody) {
        int farewellScore = 0;
        List<String> msgs = Arrays.asList(msgBody.toUpperCase().split(" "));

        //	First word has to be part of our farewell matrix.
        if (!userFarewells.containsKey(msgs.get(0))) {
            return false;
        }

        for (String str : msgs) {
            if (str.equalsIgnoreCase(botnick)) {
                farewellScore++;
                break;
            } else {
                farewellScore += getFarewellScore(str);
            }
        }

        //	Return the evaluation
        return farewellScore >= 3;
    }

    public int getFarewellScore(String key) {
        if (userFarewells.containsKey(key)) {
            return userFarewells.get(key);
        }
        return 0;
    }

    private void loadUserFarewells() {
        userFarewells.put("GOODBYE", 4);
        userFarewells.put("GOOD", 2);
        userFarewells.put("BYE", 2);
        userFarewells.put("FAREWELL", 4);
        userFarewells.put("SEE", 1);
        userFarewells.put("YOU", 1);
        userFarewells.put("YA", 1);
        userFarewells.put("LATER", 1);
    }

    private ArrayList<String> loadBotFarewells() {
        ArrayList<String> botResponses = new ArrayList<>();
        botResponses.add("goodbye");
        botResponses.add("bye");
        botResponses.add("later");
        botResponses.add("laters");
        botResponses.add("have a good one");
        botResponses.add("cheers");
        botResponses.add("see ya");
        botResponses.add("see you later!");
        return botResponses;
    }
}
