package commandListeners;

import core.GenericHashMap;
import listeners.BaseListener;
import msg.IRCMsg;

import java.util.Map;

/**
 * Created by JKyte on 4/1/2016.
 */
public class CommandListeners extends GenericHashMap<BaseCommand> {

    @Override
    public void iterateAcrossObjects(IRCMsg msg) {
        for (Map.Entry<String, BaseCommand> object : hashMap.entrySet()) {
            BaseCommand entry = object.getValue();
            if (entry.listen(msg)) {
                entry.doAction();
            }
        }
    }
}
