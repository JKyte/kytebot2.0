package msg;

import botconfigs.IRCBot;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JKyte on 4/4/2016.
 */
public class IRCMessageDecoratorTest {

    @Test
    public void testMessageDecorator() {

        IRCBot mockbot = new IRCBot(false);
        Assert.assertEquals("adminnick", mockbot.getConfigs().getAdmin());

        IRCMsg randomMsg = IRCMsgFactory.createIRCMsg(":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :hello botnick, channel msg");
        IRCMsg adminMsg = IRCMsgFactory.createIRCMsg(":adminnick!adminnick@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :hello botnick, this is an admin msg");

        //  Assert normal message profile
        IRCMsg decoratedMsg = IRCMessageDecorator.decorateMessage(mockbot.getConfigs(), randomMsg);

        Assert.assertFalse(decoratedMsg.isFromAdmin());
        Assert.assertFalse(decoratedMsg.isFromTrustedUser());

        //  Assert admin message profile
        decoratedMsg = IRCMessageDecorator.decorateMessage(mockbot.getConfigs(), adminMsg);

        Assert.assertTrue(decoratedMsg.isFromAdmin());
        Assert.assertFalse(decoratedMsg.isFromTrustedUser());

        System.out.println(randomMsg.toString());
    }
}
