package listeners;

import botconfigs.BotConfigFactory;
import botconfigs.BotConfigs;
import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by JKyte on 3/12/2016.
 */
public class InterruptListenersTest {

    private static BotConfigs configs;

    @BeforeClass
    public static void setup() {
        configs = BotConfigFactory.createBotConfigs(false);
    }

    @Test
    public void testInterruptListenerFinishes() throws InterruptedException {

        IRCCommands commands = new IRCCommands(configs);
        IRCBot mockBot = new IRCBot(false);

        InterruptListener interruptListener = new InterruptListener(mockBot, commands, 1);

        boolean listenerFinish = interruptListener.listenerFinished();
        Assert.assertFalse(listenerFinish);

        Thread.sleep(1200);

        listenerFinish = interruptListener.listenerFinished();
        Assert.assertTrue(listenerFinish);
    }


}
