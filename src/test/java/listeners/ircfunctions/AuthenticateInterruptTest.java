package listeners.ircfunctions;

import botconfigs.BotConfigFactory;
import botconfigs.BotConfigs;
import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import msg.IRCMsgFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by JKyte on 3/14/2016.
 */
public class AuthenticateInterruptTest {

    private static BotConfigs configs;

    @BeforeClass
    public static void setup() {
        configs = BotConfigFactory.createBotConfigs(false);
    }

    @Test
    public void testInterruptListenerFinishes() throws InterruptedException {

        String rawIrcMsg = ":NickServ!services@ircserver.net NOTICE botnick :This nickname is registered and protected.  If it is your nickname, type \u0002/msg NickServ IDENTIFY \u001Fpassword\u001F\u0002.  Otherwise, please choose a different nickname.";

        IRCCommands commands = new IRCCommands(configs);
        IRCBot mockBot = new IRCBot(false);

        AuthenticateInterrupt authenticateInterrupt = new AuthenticateInterrupt(mockBot, commands, 30);

        boolean listenerFinished = authenticateInterrupt.listenerFinished();
        Assert.assertFalse(listenerFinished);

        if (authenticateInterrupt.listen(IRCMsgFactory.createIRCMsg(rawIrcMsg))) {
            authenticateInterrupt.doAction();
        }

        listenerFinished = authenticateInterrupt.listenerFinished();
        Assert.assertTrue(listenerFinished);

        Assert.assertFalse(mockBot.getOutboundMsgQ().isEmpty());
    }
}
