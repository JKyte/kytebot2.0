package commandListeners;

import botconfigs.IRCBot;
import msg.IRCMsg;
import msg.IRCMsgFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JKyte on 4/1/2016.
 */
public class CommandListenersTest {

    @Test
    public void testCommandListeners() {
        IRCBot mockBot = new IRCBot(false);
        CommandListeners commandListeners = mockBot.getBotCommandListeners();

        IRCMsg randomMsg = IRCMsgFactory.createIRCMsg(":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :hello botnick, channel msg");
        commandListeners.iterateAcrossObjects(randomMsg);

        /*
            Now run a good command msg through the IRCMsgHandler and assert expected results
         */
        String rawCmdMsg = ":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :!botnick help join";
        mockBot.getIRCMsgHandler().handleMsg(rawCmdMsg);

        String expectedFirst = "PRIVMSG #channelname :JOIN - usage: ![botnick] JOIN [#channel]";
        String expectedSecond = "PRIVMSG #channelname :  - orders the bot to join a channel.";

        String outMsg = mockBot.getOutboundMsgQ().poll();
        Assert.assertNotNull(outMsg);
        Assert.assertEquals(expectedFirst, outMsg);
        outMsg = mockBot.getOutboundMsgQ().poll();
        Assert.assertNotNull(outMsg);
        Assert.assertEquals(expectedSecond, outMsg);
    }
}
