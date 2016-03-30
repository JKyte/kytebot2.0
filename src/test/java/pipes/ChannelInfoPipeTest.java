package pipes;

import botconfigs.IRCBot;
import msg.IRCMsg;
import msg.IRCMsgFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JKyte on 3/23/2016.
 */
public class ChannelInfoPipeTest {

    @Test
    public void testPipe() {
        IRCBot mockBot = new IRCBot(false);
        ChannelInfoPipe pipe = new ChannelInfoPipe(mockBot, "#targetChan");

        IRCMsg negativeMsg = IRCMsgFactory.createIRCMsg(":ChanServ!services@ircserver.net NOTICE mockbot :Information for channel \u0002#alreadyexists\u0002:");
        IRCMsg positiveMsg = IRCMsgFactory.createIRCMsg(":ChanServ!services@ircserver.net NOTICE mockbot :Channel \u0002#targetChan\u0002 isn't registered.");

        Assert.assertFalse(pipe.isActivePipe());
        pipe.setIsActivePipe(true);
        Assert.assertTrue(pipe.isActivePipe());
        Assert.assertFalse(pipe.isActionSent());
        Assert.assertFalse(pipe.isActionSuccess());
        Assert.assertFalse(pipe.isActionFailure());


        //  Run any message through to trigger a send action
        pipe.executePipe(negativeMsg);

        Assert.assertTrue(pipe.isActionSent());
        Assert.assertEquals(1, mockBot.getOutboundMsgQ().size());
        Assert.assertEquals("chanserv info #targetChan", mockBot.getOutboundMsgQ().poll());
        Assert.assertTrue(pipe.isActionSent());
        Assert.assertFalse(pipe.isActionSuccess());
        Assert.assertFalse(pipe.isActionFailure());


        //  Run negative msg through and confirm state
        pipe.executePipe(negativeMsg);

        Assert.assertTrue(pipe.isActionSent());
        Assert.assertEquals(1, mockBot.getOutboundMsgQ().size());
        Assert.assertEquals("PRIVMSG #startchan :Channel #targetChan is already registered.", mockBot.getOutboundMsgQ().poll());
        Assert.assertTrue(pipe.isActionSent());
        Assert.assertFalse(pipe.isActionSuccess());
        Assert.assertTrue(pipe.isActionFailure());

        //  Reset isActionFailure to false
        pipe.setActionFailure(false);

        //  Run positive msg through and confirm state
        pipe.executePipe(positiveMsg);
        Assert.assertEquals(1, mockBot.getOutboundMsgQ().size());
        Assert.assertEquals("PRIVMSG #startchan :Channel #targetChan is not registered.", mockBot.getOutboundMsgQ().poll());
        Assert.assertTrue(pipe.isActionSuccess());
        Assert.assertFalse(pipe.isActionFailure());

    }
}
