package listeners.botFunctions;

import botconfigs.IRCBot;
import msg.IRCMessageDecorator;
import msg.IRCMsg;
import msg.IRCMsgFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JKyte on 4/16/2016.
 */
public class RedirectListenerTest {

    @Test
    public void testCommandListeners() {
        IRCBot mockBot = new IRCBot(false);
        RedirectListener redirectListener = new RedirectListener(mockBot);
        redirectListener.addNewRedirect("#sourceChan", "#homeChan");

        IRCMsg noop = IRCMsgFactory.createIRCMsg(":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :hello botnick, channel msg");
        IRCMsg msgForRedirect = IRCMsgFactory.createIRCMsg(":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG #sourceChan :this msg should be redirected");
        String expectedRedirect = "PRIVMSG #homeChan :#sourceChan User: this msg should be redirected";

        //  Decorate msgs
        noop = IRCMessageDecorator.decorateMessage(mockBot.getConfigs(), noop);
        msgForRedirect = IRCMessageDecorator.decorateMessage(mockBot.getConfigs(), msgForRedirect);

        //  Do the listen thing
        redirectListener.listen(noop);

        //  Assert no action on the NO OP
        String outMsg = mockBot.getOutboundMsgQ().poll();
        Assert.assertNull(outMsg);

        //  Do the listen thing with expected output
        Assert.assertTrue(redirectListener.listen(msgForRedirect));
        //  Since .list() is true, do the action
        redirectListener.doAction();

        //  Assert the action is correct
        outMsg = mockBot.getOutboundMsgQ().poll();
        Assert.assertNotNull(outMsg);
        Assert.assertEquals(expectedRedirect, outMsg);
    }
}
