package commandListeners;

import botconfigs.IRCBot;
import msg.IRCMsg;
import pipelines.Pipeline;
import pipelines.PipelineFactory;

import java.util.ArrayList;

/**
 * Created by JKyte on 4/27/2016.
 */
public class SeenNickCommandListener extends BaseCommand {

    private String targetNick;

    public SeenNickCommandListener(IRCBot ircbot) {
        super(ircbot);
    }

    @Override
    public boolean listen(IRCMsg msg) {
        String[] chunks = msg.getTrailing().split(" ");
        if (chunks.length == 2 && chunks[0].equalsIgnoreCase("SEEN")) {
            targetNick = chunks[1];
            target = msg.getArgs()[0];
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void doAction() {

        //  Add the seen nick pipe
        String timestampAsString = "" + System.currentTimeMillis();
        Pipeline seenNickPipeline = PipelineFactory.createSeenNickPipeline(ircbot, target, targetNick);
        this.ircbot.getIRCMsgHandler().getPipelines().put(timestampAsString, seenNickPipeline);

        targetNick = null;
        target = null;
    }

    @Override
    public void loadCommandDescription() {
        description = new ArrayList<>();
        description.add("SEEN - usage: ![botnick] SEEN <nick>");
        description.add("  - prints if the nick is (online|offline)");
    }
}
