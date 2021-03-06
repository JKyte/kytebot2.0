package listeners;

import botconfigs.IRCBot;
import msg.IRCMsgFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JKyte on 3/12/2016.
 */
public class ListenersTest {

    @Test
    public void testAddInterruptListener() throws InterruptedException {

        String channelMsg = ":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :hello botnick, channel msg";

        IRCBot mockBot = new IRCBot(false);
        InterruptListener interruptListener = new InterruptListener(mockBot, 0);
        Listeners listeners = new Listeners();

        listeners.put("InterruptForTests", interruptListener);

        //  Assert that the listener was added properly
        Assert.assertNotNull(listeners.get("InterruptForTests"));

        Thread.sleep(10);   //  Sleep, allowing the interrupt listener to expire

        listeners.iterateAcrossObjects(IRCMsgFactory.createIRCMsg(channelMsg));

        //  Assert that the listener was removed properly
        Assert.assertFalse(listeners.hasObject("InterruptForTests"));
        Assert.assertNull(listeners.get("InterruptForTests"));

    }
}
