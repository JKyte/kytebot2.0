package commandListeners;

import botconfigs.IRCBot;
import msg.IRCMsg;
import pipelines.Pipeline;
import pipelines.PipelineFactory;

import java.util.ArrayList;

/**
 * Created by JKyte on 3/30/2016.
 */
public class ChannelInfoListener extends AdminCommand {

    private String targetChannel;

    public ChannelInfoListener(IRCBot ircbot) {
        super(ircbot);
    }

    @Override
    public boolean listen(IRCMsg msg) {
        //	Ensure an admin is making this call
        if (!super.listen(msg)) {
            return false;
        }

        String[] chunks = msg.getTrailing().split(" ");
        if (chunks.length == 2 && chunks[0].equalsIgnoreCase("CHANINFO")) {
            targetChannel = chunks[1];
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void doAction() {
        String timestampAsString = "" + System.currentTimeMillis();
        Pipeline chanInfoPipeline = PipelineFactory.createChannelInfoPipeline(ircbot, targetChannel);
        this.ircbot.getIRCMsgHandler().getPipelines().put(timestampAsString, chanInfoPipeline);
    }

    @Override
    public void loadCommandDescription() {
        description = new ArrayList<>();
        description.add("CHANINFO - usage: ![botnick] CHANINFO [#channel]");
        description.add("  - orders the bot to run '/chanserv #channel'");
    }
}
