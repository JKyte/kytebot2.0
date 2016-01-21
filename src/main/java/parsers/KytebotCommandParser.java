package parsers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import commands.BotCommands;
import commands.FlirtCommand;
import commands.HelpCommand;
import commands.JoinCommand;
import commands.ListCommand;
import commands.TradeCommand;
import responses.KytebotResponses;
import triggers.Triggers;
import msg.IRCMsg;
import botconfigs.IRCCommands;

public class KytebotCommandParser {

	private String botnick;
	private IRCCommands commands;
	private ConcurrentLinkedQueue<String> outboundMsgQ;

	private Triggers timedTriggers;
	private Triggers eventTriggers;
	
	private BotCommands botCommands;
	private String admin;
	
	private HashSet<String> greetingChans;
	private HashSet<String> farewellChans;
	
	HashMap<String, Integer> greetings = new HashMap<String, Integer>();
	HashMap<String, Integer> farewells = new HashMap<String, Integer>();

	KytebotResponses responses;

	public KytebotCommandParser(Properties configs, IRCCommands commands, ConcurrentLinkedQueue<String> outboundMsgQ, 
			Triggers timedTriggers, Triggers eventTriggers, String botnick){
		
		this.botnick = botnick;
		this.commands = commands;

		loadGreetingChans(configs.getProperty("greetingchans"));
		loadFarewellChans(configs.getProperty("farewellchans"));
		
		loadGreetings();
		loadFarewells();
		responses = new KytebotResponses();
		this.outboundMsgQ = outboundMsgQ;
		
		this.timedTriggers = timedTriggers;
		this.eventTriggers = eventTriggers;
		
		admin = configs.getProperty("admin");
		
		botCommands = new BotCommands(admin, botnick);
		loadKytebotCommands();
	}
	
	private void loadKytebotCommands() {
		botCommands.put("HELP", new HelpCommand(botCommands, timedTriggers, eventTriggers, outboundMsgQ));
		botCommands.put("LIST", new ListCommand(botCommands, timedTriggers, eventTriggers, outboundMsgQ));
		botCommands.put("TRADE", new TradeCommand(botCommands, timedTriggers, eventTriggers, outboundMsgQ));
		botCommands.put("JOIN", new JoinCommand(botCommands, timedTriggers, eventTriggers, outboundMsgQ));
		botCommands.put("FLIRT", new FlirtCommand(botCommands, timedTriggers, eventTriggers, outboundMsgQ));
	}

	private void loadGreetingChans(String property) {
		greetingChans = new HashSet<String>();
		String[] properties = property.split(",");
		for( String prop : properties ){
			greetingChans.add(prop);
		}
	}

	private void loadFarewellChans(String property) {
		farewellChans = new HashSet<String>();
		String[] properties = property.split(",");
		for( String prop : properties ){
			farewellChans.add(prop);
		}
	}
	
	private void loadFarewells() {
		farewells.put("GOODBYE", 4);
		farewells.put("GOOD", 2);
		farewells.put("BYE", 2);
		farewells.put("FAREWELL", 4);
		farewells.put("SEE", 1);
		farewells.put("YOU", 1);
		farewells.put("YA", 1);
		farewells.put("LATER", 1);
		farewells.put("LATERS", 1);
	}

	private void loadGreetings() {
		greetings.put("HELLO", 2);
		greetings.put("HI", 2);
		greetings.put("HEY", 2);
		greetings.put("GOOD", 1);
		greetings.put("MORNING", 2);
		greetings.put("EVENING", 2);
	}

	//	TODO prime candidate for pattern matching
	public void parseChannelMsg( IRCMsg msg ){
		System.out.println("CHANNEL message with our name in it!");
		
		if( isValidGreetingChan(msg.getArgs()[0]) && isGreeting(msg.getTrailing()) ){
			//	send response to greeting
			String response = responses.getKytebotGreeting(msg.getNickFromPrefix());
			String responseMsg = commands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
			
		}else if( isValidFairwellChan(msg.getArgs()[0]) && isFarewell(msg.getTrailing()) ){
			//	send response to greeting
			String response = responses.getKytebotFarewell(msg.getNickFromPrefix());
			String responseMsg = commands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
		}else if( isKytebotCommand(msg.getTrailing()) ){
			handleKytebotCommand(msg);
		}

	}

	private boolean isValidFairwellChan(String chan) {
		return greetingChans.contains(chan);
	}

	private boolean isValidGreetingChan(String chan) {
		return farewellChans.contains(chan);
	}

	/**
	 * Determines if a message body contains a greeting
	 * @param msgBody
	 */
	public boolean isGreeting( String msgBody ){
		int greetingScore = 0;
		List<String> msgs = Arrays.asList(msgBody.toUpperCase().split(" "));

		//	First word has to be part of our greeting matrix.
		if( !greetings.containsKey(msgs.get(0)) ){
			return false;
		}

		for( String str : msgs ){
			if( str.equalsIgnoreCase(botnick) ){
				greetingScore++;
				break;
			}else{
				greetingScore += getGreetingScore(str);
			}
		}

		//	do something here
		if( greetingScore >= 3 ){
			return true;
		}else{
			return false;
		}
	}

	public int getGreetingScore(String key){
		if( greetings.containsKey(key) ){
			return greetings.get(key);
		}
		return 0;
	}

	public boolean isFarewell( String msgBody ){
		int farewellScore = 0;
		List<String> msgs = Arrays.asList(msgBody.toUpperCase().split(" "));

		//	First word has to be part of our farewell matrix.
		if( !farewells.containsKey(msgs.get(0)) ){
			return false;
		}

		for( String str : msgs ){	
			if( str.equals(botnick) ){
				farewellScore++;
				break;
			}else{
				farewellScore += getFarewellScore(str);
			}
		}

		//	do something here
		if( farewellScore >= 3 ){
			return true;
		}else{
			return false;
		}
	}

	public int getFarewellScore(String key){
		if( farewells.containsKey(key) ){
			return farewells.get(key);
		}
		return 0;
	}

	//	TODO also prime candidate for pattern matching
	public void parsePrivateMsg( IRCMsg msg ){
		System.out.println("PRIVATE " + botnick + " command!");
		if( isGreeting(msg.getTrailing()) ){
			//	send response to greeting
			String response = responses.getKytebotGreeting(msg.getNickFromPrefix());
			String responseMsg = commands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
		}else if( isFarewell(msg.getTrailing()) ){
			//	send response to greeting
			String response = responses.getKytebotFarewell(msg.getNickFromPrefix());
			String responseMsg = commands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
		}else if( isKytebotCommand(msg.getTrailing()) ){
			handleKytebotCommand(msg);
		}
	}

	public boolean isKytebotCommand(String trailing) {
		if( trailing.startsWith("!"+botnick.charAt(0)) ){ 
			return true; 
		}else if( trailing.contains(botnick) ){
			return true;
		}
		return false;
	}

	//	Cleans the command indicator before passing the message
	public void handleKytebotCommand(IRCMsg msg) {
		msg.setTrailing( cleanKytebotCommand(msg.getTrailing()) );
		botCommands.iterateAcrossCommands(msg);
	}

	public String cleanKytebotCommand(String trailing) {
		return trailing.substring(trailing.indexOf(" ")+1);
	}

}
