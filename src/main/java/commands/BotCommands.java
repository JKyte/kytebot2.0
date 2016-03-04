package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import msg.IRCMsg;

/**
 * 
 * @author JKyte
 * 
 * This class acts as a wrapper around a ConcurrentHashMap
 *
 */
public class BotCommands {
	
	private ConcurrentHashMap<String, BaseCommand> commands;
	
	private String admin;
	private String botnick;

	public BotCommands(String admin, String botnick){
		commands = new ConcurrentHashMap<String, BaseCommand>();
				//new ConcurrentHashMap<String, ?>(8, 0.9f, 2);
		this.admin = admin;
		this.botnick = botnick;
	}
	
	/**
	 * Wrapper method
	 * @param triggerName
	 * @param trigger
	 * @returns - TRUE if the trigger added, FALSE if the trigger already exists
	 */
	public boolean put(String triggerName, BaseCommand trigger){
		if( !commands.containsKey(triggerName) ){
			commands.put(triggerName, trigger);
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
	public BaseCommand get(String triggerName){
		return commands.get(triggerName);
	}
	
	/**
	 * Never remove the commands from the ConcurrentHashMap
	 * @param msg - msg to respond to
	 */
	public synchronized void iterateAcrossCommands(IRCMsg msg){	
		for( Entry<String, BaseCommand> entry : commands.entrySet() ){
			if( entry.getValue().listen(msg) ){
				entry.getValue().doAction();
			}
		}
	}
	
	public synchronized HashMap<String, ArrayList<String>> getCommandsAndDescription(){
		HashMap<String, ArrayList<String>> commandDescriptions = new HashMap<String, ArrayList<String>>();
		for( Entry<String, BaseCommand> entry : commands.entrySet() ){
			commandDescriptions.put(entry.getKey(), entry.getValue().getCommandDescription());
		}
		return commandDescriptions;
	}

	public synchronized ArrayList<String> getSingleCommandDescription(String commandKey){
		return commands.get(commandKey).getCommandDescription();
	}
	//	Will not be 100% accurate
	public long getNumTriggers(){
		return commands.mappingCount();
	}

	public String getBotnick() {
		return botnick;
	}
	
	public String getAdminNick(){
		return admin;
	}
}
