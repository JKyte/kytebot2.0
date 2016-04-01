package core;

import msg.IRCMsg;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JKyte on 3/30/2016.
 */
public abstract class GenericHashMap {

    protected ConcurrentHashMap<String, Object> hashMap;

    public GenericHashMap() {
        hashMap = new ConcurrentHashMap<>();
        //new ConcurrentHashMap<String, ?>(8, 0.9f, 2);
    }

    public abstract void iterateAcrossObjects(IRCMsg msg);

    /**
     * Wrapper method
     *
     * @param objectName - The Key, i.e, the Name of the Listener, Command, or Pipeline
     * @param object     - The Value, i.e., the Listener, Command, or Pipeline
     * @returns - TRUE if the object added, FALSE if the object already exists
     */
    public synchronized boolean put(String objectName, Object object) {
        if (!hashMap.containsKey(objectName)) {
            hashMap.put(objectName, object);
            return true;
        } else {
            return false;
        }
    }

    public Object get(String listenerName) {
        return hashMap.get(listenerName);
    }

    public boolean hasObject(String listenerName) {
        return hashMap.containsKey(listenerName);
    }

}
