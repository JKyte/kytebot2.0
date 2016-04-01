package listeners;

import core.GenericHashMap;
import msg.IRCMsg;

import java.util.Map.Entry;

/**
 * @author Kyte
 */
public class Listeners extends GenericHashMap {


    @Override
    public void iterateAcrossObjects(IRCMsg msg) {
        for (Entry<String, Object> object : hashMap.entrySet()) {
            BaseListener entry = (BaseListener) object.getValue();
            if (entry.listen(msg)) {
                entry.doAction();
            }

            //  This block is independent of any listen/actions to support InterruptListeners
            if (entry.listenerFinished()) {
                hashMap.remove(object.getKey());
            }
        }
    }
}
