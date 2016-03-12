package listeners;

import msg.IRCMsg;

public interface Listener {

    boolean listen(IRCMsg msg);

    void doAction();

    boolean listenerFinished();
}
