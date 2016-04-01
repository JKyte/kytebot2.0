package pipelines;

import botconfigs.IRCBot;
import msg.IRCMsgFactory;
import org.junit.Assert;
import org.junit.Test;
import pipes.ChannelInfoPipe;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by JKyte on 3/28/2016.
 */
public class PipelinesTest {

    @Test
    public void testChannelInfoPipeline() {

        /*  MockBot                     IRC Server
            -> 'chanserv info #targetChan'
            <- 'PRIVMSG #startchan :Channel #targetChan is not registered.'
            -> *MockBot weeps for joy*
         */

        //  Set up Pipe
        IRCBot mockBot = new IRCBot(false);
        ChannelInfoPipe channelInfoPipe = new ChannelInfoPipe(mockBot, "#targetChan");
        channelInfoPipe.setIsActivePipe(true);
        channelInfoPipe.setIsLastPipe(true);

        //  Set up Pipeline
        Pipeline channelInfoPipeline = new Pipeline();
        channelInfoPipeline.pipes.add(channelInfoPipe);

        //  Add to Pipelines object
        Pipelines pipelines = new Pipelines();
        String pipelineKey = "PipelineForTests";
        pipelines.put(pipelineKey, channelInfoPipeline);

        //  Set up server responses -- channel is available
        String[] serverMsgs = new String[]{
                ":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :hello botnick, channel msg",
                ":ChanServ!services@ircserver.net NOTICE mockbot :Channel \u0002#targetChan\u0002 isn't registered.",
        };

        //  Set up expected pipeline responses
        ConcurrentLinkedQueue<String> expectedPipelineOutput = new ConcurrentLinkedQueue<>();
        expectedPipelineOutput.add("chanserv info #targetChan");
        expectedPipelineOutput.add("PRIVMSG #startchan :Channel #targetChan is not registered.");

        /*
          Iterate through messages, asserting as we go. Final pass though will successfully end
          the pipeline, prompting it to be removed.
          */
        for (String msg : serverMsgs) {

            Assert.assertTrue(pipelines.hasObject(pipelineKey));
            pipelines.iterateAcrossObjects(IRCMsgFactory.createIRCMsg(msg));

            Assert.assertEquals(expectedPipelineOutput.poll(), mockBot.getOutboundMsgQ().poll());
        }

        //  Finally, assert an empty pipeline
        Assert.assertFalse(pipelines.hasObject(pipelineKey));
    }
}
