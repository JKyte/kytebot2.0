package listeners.ircfunctions;

import botconfigs.IRCBot;
import listeners.InterruptListener;
import msg.IRCMsg;

/**
 * Created by JKyte on 3/14/2016.
 */
public class AuthenticateInterrupt extends InterruptListener {

    private boolean authenticationSent;

    /**
     * @param ircbot
     * @param duration    in SECONDS for this InterruptListener
     */
    public AuthenticateInterrupt(IRCBot ircbot, long duration) {
        super(ircbot, duration);
        this.authenticationSent = false;
    }

    @Override
    public boolean listen(IRCMsg msg) {
        return msg.getCommand().equals("NOTICE") &&
                msg.getTrailing().contains("This nickname is registered and protected.");
    }

    @Override
    public void doAction() {
        authenticationSent = true;
        outboundMsgQ.add(ircCommands.nickservIdentify());
    }

    @Override
    public boolean listenerFinished() {
        if (authenticationSent) {
            return true;
        } else {
            return super.listenerFinished();
        }
    }

}
