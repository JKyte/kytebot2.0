package pipes;

import botconfigs.IRCBot;
import msg.IRCMsg;

/**
 * Created by JKyte on 4/27/2016.
 * <p>
 * This Pipe is a single-stage pipeline
 * <p>
 * This Pipe is a wrapper for '/ns info <nick>'
 */
public class SeenNickPipe extends BasePipe {

    private String target;
    private String targetNick;

    private boolean isTargetNickOnline = false;
    private String nickOnline = " is currently online";
    private String nickOffline = " last seen:";

    private long ONE_POINT_FIVE_SECONDS = 1500;

    public SeenNickPipe(IRCBot ircBot, String target, String targetNick) {
        super(ircBot);
        this.setMAX_DURATION_IN_MILLIS(ONE_POINT_FIVE_SECONDS);
        this.target = target;
        this.targetNick = targetNick;

        nickOnline = targetNick + nickOnline;
        nickOffline = targetNick + nickOffline;
    }

    public boolean sendAction() {
        outboundMsgQ.add(ircCommands.nickservInfo(targetNick));
        return super.sendAction();
    }

    @Override
    protected void confirmNegativeResponse() {
        setActionFailure(true);
        outboundMsgQ.add(ircCommands.privmsg(target, targetNick + " isn't registered"));
    }

    @Override
    protected void confirmPositiveResponse() {
        setActionSuccess(true);
        setPipeComplete(true);

        if (isTargetNickOnline) {
            outboundMsgQ.add(ircCommands.privmsg(target, nickOnline));
        } else {
            outboundMsgQ.add(ircCommands.privmsg(target, nickOffline));
        }
    }

    @Override
    protected boolean isNegativeResponse(IRCMsg msg) {
        //  This should be a check for a nick that doesn't exist
        return msg.getTrailing().contains("isn't registered");
    }

    /**
     * @param msg
     * @returns a boolean if the nick is registered
     */
    @Override
    protected boolean isPositiveResponse(IRCMsg msg) {
        //  This is a dual check for (online|offline)

        //  Online check
        if (msg.getTrailing().contains("is currently online")) {
            isTargetNickOnline = true;
            return true;
        } else if (msg.getTrailing().contains("Last seen time:")) {
            System.out.println("SeenPipe.getTrailing() " + msg.getTrailing());

            nickOffline += msg.getTrailing().substring(msg.getTrailing().indexOf(":") + 1);

            return true;
        }

        return false;
    }
}
