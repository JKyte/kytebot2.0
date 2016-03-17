package listeners.botFunctions;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import listeners.BaseListener;
import msg.IRCMsg;
import util.RandomList;

import java.util.*;

/**
 * Created by JKyte on 3/16/2016.
 */
public class UserGreetsBotListener extends BaseListener {

    HashMap<String, Integer> userGreetings = new HashMap<>();
    RandomList botGreetings;
    String userToGreet;
    private HashSet<String> greetingChannels;

    public UserGreetsBotListener(IRCBot ircbot, IRCCommands ircCommands) {
        super(ircbot, ircCommands);
        greetingChannels = ircbot.getConfigs().getGreetingChans();
        loadUserGreetings();
        botGreetings = new RandomList(loadBotGreetings());
    }

    @Override
    public boolean listen(IRCMsg msg) {

        /**
         * Must validate that the msg is...
         * 1) A greeting
         * AND
         * 2) Either
         *  a) Directed at the bot in a side chat
         *  OR
         *  b) Is greeting the bot in an approved channel
         */
        if (null != msg.getTrailing() && isGreeting(msg.getTrailing())) {

            //  Determine if channel msg or side chat
            if (msg.getArgs()[0].equals(botnick)) {

                //  Side chat
                log.info("Side chat: " + msg.getArgs()[0]);
            } else {

                //  Else should always be a channel
                log.info("Chan: " + msg.getArgs()[0].startsWith("#"));

                // So confirm that the channel is allowed.
                if (!isValidGreetingChan(msg.getArgs()[0])) {
                    return false;
                }
            }

            userToGreet = msg.getArgs()[0];

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void doAction() {
        String responseMsg = ircCommands.privmsg(userToGreet, botGreetings.getListEntry());
        outboundMsgQ.add(responseMsg);
        userToGreet = null; //  Null it out.
    }

    private boolean isValidGreetingChan(String chan) {
        return greetingChannels.contains(chan);
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
     * Determines if a message body contains a greeting. The criteria are such that the msg body must
     * 1. Start with one of any pre-defined greetings
     * 2. Aggregate score must pass a threshold
     * <p>
     * This enables us to respond to "hey botnick" or "good morning botnick" while not responding to
     * messages such as "hey dude, radical" or "good, good, it's all good botnick"
     *
     * @param msgBody - the trailing portion of an IRCMsg
     */
    public boolean isGreeting(String msgBody) {
        int greetingScore = 0;
        List<String> msgs = Arrays.asList(msgBody.toUpperCase().split(" "));

        //	First word has to be part of our greeting matrix.
        if (!userGreetings.containsKey(msgs.get(0))) {
            return false;
        }

        for (String str : msgs) {
            if (str.equalsIgnoreCase(botnick)) {
                greetingScore++;
                break;
            } else {
                greetingScore += getGreetingScore(str);
            }
        }

        //	Return the evaluation
        return greetingScore >= 3;
    }

    public int getGreetingScore(String key) {
        if (userGreetings.containsKey(key)) {
            return userGreetings.get(key);
        }
        return 0;
    }

    private void loadUserGreetings() {
        userGreetings = new HashMap<>();
        userGreetings.put("HELLO", 2);
        userGreetings.put("HI", 2);
        userGreetings.put("HEY", 2);
        userGreetings.put("GOOD", 1);
        userGreetings.put("MORNING", 2);
        userGreetings.put("EVENING", 2);
    }

    private ArrayList<String> loadBotGreetings() {
        ArrayList<String> botResponses = new ArrayList<>();
        botResponses.add("hello");
        botResponses.add("hey");
        botResponses.add("hi");
        botResponses.add("greetings");
        botResponses.add("good day");
        botResponses.add("'ello");
        return botResponses;
    }
}
