package commands;

import io.JsonParser;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import msg.IRCMsg;
import triggers.Triggers;

public class TradeCommand extends BaseCommand {

	String resourceName;
	ArrayList<String> validResources;
	
	public TradeCommand(BotCommands botCommands, Triggers timedTriggers,
			Triggers eventTriggers, ConcurrentLinkedQueue<String> outboundMsgQ) {
		super(botCommands, timedTriggers, eventTriggers, outboundMsgQ);
		loadValidResources();
	}

	@Override
	public boolean isTriggered(IRCMsg msg) {

		target = msg.getArgs()[0];
		resourceName = null;
		String[] chunks = msg.getTrailing().split(" ");
		if( chunks.length == 2 && chunks[0].equalsIgnoreCase("TRADE") ){
			resourceName = chunks[1];
			return true;
		}else{
			return false;	
		}
	}

	@Override
	public void doAction() {
		//	Validate resource name first
		resourceName = resourceName.toLowerCase();

		if( isValidResource(resourceName) ){
			JsonParser parser = new JsonParser(resourceName);
			parser.fetchJson();
			
			ArrayList<String> lines = parser.buildResponse();
			for( String line : lines ){
				outboundMsgQ.add( ircCommands.privmsg(target, line));
			}
		}else{
			outboundMsgQ.add( ircCommands.privmsg(target, resourceName+" is not a valid resource."));			
		}

		target = null;
	}

	public boolean isValidResource( String resource ){
		return validResources.contains(resource);
	}
	
	@Override
	public void loadCommandDescription() {
		description = new ArrayList<String>();
		description.add( "TRADE - usage: ![botnick] TRADE [RESOURCE]");
		description.add( "  - returns the average trade prices for a resource on the Politics & War market.");
	}

	
	private void loadValidResources() {
		validResources = new ArrayList<String>();
		
		validResources.add("aluminum");
		validResources.add("bauxite");
		validResources.add("coal");
		validResources.add("credits");
		validResources.add("food");
		validResources.add("gasoline");
		validResources.add("iron");
		validResources.add("lead");
		validResources.add("munitions");
		validResources.add("oil");
		validResources.add("uranium");
		validResources.add("steel");
	}
}
