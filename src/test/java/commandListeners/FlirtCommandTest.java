package commandListeners;

import botconfigs.IRCBot;
import msg.IRCMsg;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JKyte on 5/9/2016.
 */
public class FlirtCommandTest {

    @Test
    public void testMsgFromAdmin() {

        String channelMsg = ":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :!b flirt nick";
        String adminMsg = ":adminnick!adminnick@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :!botnick flirt nick";

        //  Set up bot and command
        IRCBot mockBot = new IRCBot(false);
        FlirtCommand flirtCommand = new FlirtCommand(mockBot);

        IRCMsg badFlirtMsg = mockBot.getIRCMsgHandler().createAndDecorateMsg(channelMsg);
        Assert.assertFalse(flirtCommand.listen(badFlirtMsg));
        String pollResult = mockBot.getOutboundMsgQ().poll();
        Assert.assertNull(pollResult);

        IRCMsg goodFlirt = mockBot.getIRCMsgHandler().createAndDecorateMsg(adminMsg);
        Assert.assertTrue(goodFlirt.isFromAdmin());

        mockBot.getIRCMsgHandler().handleMsg(adminMsg);
        pollResult = mockBot.getOutboundMsgQ().poll();
        Assert.assertNotNull(pollResult);
    }
}
