package pipelines;

import botconfigs.IRCBot;
import pipes.ChannelInfoPipe;

/**
 * Created by JKyte on 3/30/2016.
 */
public class PipelineFactory {

    public static Pipeline createChannelInfoPipeline(IRCBot ircbot, String targetChannel) {
        ChannelInfoPipe channelInfoPipe = new ChannelInfoPipe(ircbot, targetChannel);
        channelInfoPipe.setIsActivePipe(true);
        channelInfoPipe.setIsLastPipe(true);

        //  Set up Pipeline
        Pipeline channelInfoPipeline = new Pipeline();
        channelInfoPipeline.pipes.add(channelInfoPipe);
        return channelInfoPipeline;
    }
}
