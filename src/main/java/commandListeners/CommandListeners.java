package commandListeners;

import core.GenericHashMap;
import msg.IRCMsg;

import java.util.Map;

/**
 * Created by JKyte on 4/1/2016.
 */
public class CommandListeners extends GenericHashMap {

    @Override
    public void iterateAcrossObjects(IRCMsg msg) {
        for (Map.Entry<String, Object> object : hashMap.entrySet()) {
            BaseCommand entry = (BaseCommand) object.getValue();
            if (entry.listen(msg)) {
                entry.doAction();
            }
        }
    }
}
