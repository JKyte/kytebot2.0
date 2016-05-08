package commandListeners;

import botconfigs.IRCBot;
import listeners.botFunctions.RedirectListener;
import msg.IRCMsg;

import java.util.ArrayList;

/**
 * Created by JKyte on 4/9/2016.
 * <p>
 * This command listener is actually a BotFunction listener
 * <p>
 * This listener will redirect output from one or more channels to a pre-determined channel.
 */
public class RedirectCommand extends AdminCommand {

    public RedirectCommand(IRCBot ircbot) {
        super(ircbot);
    }

    @Override
    public boolean listen(IRCMsg msg) {
        //	Ensure an admin is making this call
        if (!super.listen(msg)) {
            return false;
        }

        String sourceChannel = null;
        String redirectTarget = null;
        String[] chunks = msg.getTrailing().split(" ");
        if (chunks.length > 0 && chunks[0].equalsIgnoreCase("REDIRECT")) {

            /*
              Two ways to add redirect
                1) REDIRECT [#source]
                2) REDIRECT [#source] [#target]
               */
            if (chunks.length >= 2) {
                sourceChannel = chunks[1];
            }
            if (chunks.length == 3) {
                redirectTarget = chunks[2];
            }
        }

        //  Handle adding a new redirect
        if (chunks.length == 2) {
            ((RedirectListener) this.ircbot.getEventListeners().get("RedirectListener"))
                    .addNewRedirect(sourceChannel);
        } else if (chunks.length == 3) {
            ((RedirectListener) this.ircbot.getEventListeners().get("RedirectListener"))
                    .addNewRedirect(sourceChannel, redirectTarget);
        }

        return false;
    }

    @Override
    public void doAction() {
        //  Do nothing
    }

    @Override
    public void loadCommandDescription() {
        description = new ArrayList<>();
        description.add("REDIRECT - usage: ![botnick] REDIRECT [#source] [#channel]");
        description.add("  - bot redirects the output of #source to #channel.");
    }
}
