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
public class UserFarewellsBotListenerTest {

    private static BotConfigs configs;
    private static IRCBot mockBot;
    private static IRCCommands commands;
    private static UserFarewellsBotListener listener;

    @BeforeClass
    public static void setup() {

        configs = BotConfigFactory.createBotConfigs(false);

        mockBot = new IRCBot(false);
        commands = new IRCCommands(configs);
        listener = new UserFarewellsBotListener(mockBot, commands);
    }

    @Test
    public void testGetFarewellScore() {
        Assert.assertEquals(4, listener.getFarewellScore("GOODBYE"));
        Assert.assertEquals(2, listener.getFarewellScore("GOOD"));
        Assert.assertEquals(2, listener.getFarewellScore("BYE"));
        Assert.assertEquals(4, listener.getFarewellScore("FAREWELL"));
        Assert.assertEquals(1, listener.getFarewellScore("SEE"));
        Assert.assertEquals(1, listener.getFarewellScore("YOU"));
        Assert.assertEquals(1, listener.getFarewellScore("YA"));
        Assert.assertEquals(1, listener.getFarewellScore("LATER"));
    }

    @Test
    public void testIsFarewell() {

        String[] validGreetings = {"goodbye botnick", "good bye botnick", "farewell botnick",
                "see you botnick", "see ya botnick"};

        String[] invalidGreetings = {"hello there", "hi there", "morn' botnick", "even' botnick", "botnick", "laters botnick", "see you", "see ya"};

        for (String greeting : validGreetings) {
            Assert.assertTrue("Failed on: " + greeting, listener.isFarewell(greeting));
        }

        for (String greeting : invalidGreetings) {
            Assert.assertFalse(listener.isFarewell(greeting));
        }
    }
}
