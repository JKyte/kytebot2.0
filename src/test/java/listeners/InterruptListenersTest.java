package listeners;

import botconfigs.IRCBot;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JKyte on 3/12/2016.
 */
public class InterruptListenersTest {

    @Test
    public void testInterruptListenerFinishes() throws InterruptedException {

        IRCBot mockBot = new IRCBot(false);

        InterruptListener interruptListener = new InterruptListener(mockBot, 1);

        boolean listenerFinish = interruptListener.listenerFinished();
        Assert.assertFalse(listenerFinish);

        Thread.sleep(1200);

        listenerFinish = interruptListener.listenerFinished();
        Assert.assertTrue(listenerFinish);
    }


}
