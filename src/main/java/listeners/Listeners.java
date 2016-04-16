package listeners;

import core.GenericHashMap;
import msg.IRCMsg;

import java.util.Map.Entry;

/**
 * @author Kyte
 */
public class Listeners extends GenericHashMap<BaseListener> {


    @Override
    public void iterateAcrossObjects(IRCMsg msg) {
        for (Entry<String, BaseListener> object : hashMap.entrySet()) {
            BaseListener entry = object.getValue();
            if (entry.listen(msg)) {
                entry.doAction();
            }

            //  Check to remove listener if applicable
            if (entry.listenerFinished()) {
                hashMap.remove(object.getKey());
            }
        }
    }
}
