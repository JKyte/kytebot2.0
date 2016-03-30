package pipes;

import msg.IRCMsg;

/**
 * Created by JKyte on 3/18/2016.
 */
public interface Pipe {

    boolean executePipe(IRCMsg msg);
}
