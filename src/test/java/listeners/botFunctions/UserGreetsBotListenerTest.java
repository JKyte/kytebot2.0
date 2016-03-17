package listeners.botFunctions;

import botconfigs.BotConfigFactory;
import botconfigs.BotConfigs;
import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by JKyte on 3/17/2016.
 */
public class UserGreetsBotListenerTest {

    private static BotConfigs configs;
    private static IRCBot mockBot;
    private static IRCCommands commands;
    private static UserGreetsBotListener listener;

    @BeforeClass
    public static void setup() {

        configs = BotConfigFactory.createBotConfigs(false);

        mockBot = new IRCBot(false);
        commands = new IRCCommands(configs);
        listener = new UserGreetsBotListener(mockBot, commands);
    }

    @Test
    public void testGetFarewellScore() {
        Assert.assertEquals(2, listener.getGreetingScore("HELLO"));
        Assert.assertEquals(2, listener.getGreetingScore("HI"));
        Assert.assertEquals(1, listener.getGreetingScore("GOOD"));
        Assert.assertEquals(2, listener.getGreetingScore("MORNING"));
        Assert.assertEquals(2, listener.getGreetingScore("EVENING"));
    }

    @Test
    public void testIsFarewell() {

        String[] validGreetings = {"hello botnick", "hi botnick", "good morning botnick",
                "good evening botnick", "morning botnick", "evening botnick"};

        String[] invalidGreetings = {"hello there", "hi there", "morn' botnick", "even' botnick", "botnick"};

        for (String greeting : validGreetings) {
            Assert.assertTrue(listener.isGreeting(greeting));
        }

        for (String greeting : invalidGreetings) {
            Assert.assertFalse(listener.isGreeting(greeting));
        }
    }
}
