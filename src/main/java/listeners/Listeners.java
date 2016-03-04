package listeners;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import msg.IRCMsg;

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
		listeners = new ConcurrentHashMap<String, BaseListener>();
				//new ConcurrentHashMap<String, ?>(8, 0.9f, 2);
	}
	
	/**
	 * Wrapper method
	 * @param listenerName
	 * @param listener
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
	 * Wrapper method
	 * @param triggerName
	 * @return
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
