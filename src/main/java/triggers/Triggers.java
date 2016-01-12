package triggers;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import msg.IRCMsg;

/**
 * 
 * @author Kyte
 * 
 * This class acts as a wrapper around a ConcurrentHashMap
 *
 */
public class Triggers {
	
	private ConcurrentHashMap<String, BaseTrigger> triggers;
	
	public Triggers(){
		triggers = new ConcurrentHashMap<String, BaseTrigger>();
				//new ConcurrentHashMap<String, ?>(8, 0.9f, 2);
	}
	
	/**
	 * Wrapper method
	 * @param triggerName
	 * @param trigger
	 * @returns - TRUE if the trigger added, FALSE if the trigger already exists
	 */
	public synchronized boolean put(String triggerName, BaseTrigger trigger){
		if( !triggers.containsKey(triggerName) ){
			triggers.put(triggerName, trigger);
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
	public BaseTrigger get(String triggerName){
		return triggers.get(triggerName);
	}
	
	public void iterateAcrossTriggers(IRCMsg msg){
		
		for( Entry<String, BaseTrigger> entry : triggers.entrySet() ){
			if( entry.getValue().isTriggered(msg) ){
				entry.getValue().doAction();
				
				if( entry.getValue().triggerFinished() ){
					triggers.remove(entry.getKey());
				}
			}
		}
	}

	//	Will not be 100% accurate
	public long getNumTriggers(){
		return triggers.mappingCount();
	}
}
