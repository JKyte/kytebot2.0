package listeners;

import msg.IRCMsg;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Kyte
 * 
 * This class acts as a wrapper around a ConcurrentHashMap of Listeners
 *
 */
public class Listeners {
	
	private ConcurrentHashMap<String, BaseListener> listeners;
	
	public Listeners(){
        listeners = new ConcurrentHashMap<>();
        //new ConcurrentHashMap<String, ?>(8, 0.9f, 2);
	}
	
	/**
	 * Wrapper method
     * @param listenerName - The Key, i.e, the Name of the Listener
     * @param listener - The Value, i.e., the Listener
     * @returns - TRUE if the trigger added, FALSE if the trigger already exists
	 */
	public synchronized boolean put(String listenerName, BaseListener listener){
		if( !listeners.containsKey(listenerName) ){
			listeners.put(listenerName, listener);
			return true;
		}else{
			return false;
		}
	}

	/**
     *
     * @param listenerName
     * @returns a listener for the provided key
     */
	public BaseListener get(String listenerName){
		return listeners.get(listenerName);
	}
	
	public void iterateAcrossListeners(IRCMsg msg){
		
		for( Entry<String, BaseListener> entry : listeners.entrySet() ){
			if( entry.getValue().listen(msg) ){
				entry.getValue().doAction();
				
				if( entry.getValue().listenerFinished() ){
					listeners.remove(entry.getKey());
				}
			}
		}
	}

	//	Will not be 100% accurate
	public long getNumListeners(){
		return listeners.mappingCount();
	}
}
