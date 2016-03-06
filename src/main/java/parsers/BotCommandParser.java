package parsers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import commands.BotCommands;
import commands.FlirtCommand;
import commands.HelpCommand;
import commands.JoinCommand;
import commands.ListCommand;
import commands.TradeCommand;
import responses.BotResponses;
import listeners.Listeners;
import msg.IRCMsg;
import botconfigs.IRCBot;
import botconfigs.IRCCommands;

public class BotCommandParser {

	private String botnick;
	private IRCCommands commands;
	private ConcurrentLinkedQueue<String> outboundMsgQ;

	private Listeners timedListeners;
	private Listeners eventListeners;
	
	private BotCommands botCommands;
	private String admin;
	
	private HashSet<String> greetingChans;
	private HashSet<String> farewellChans;
	
	HashMap<String, Integer> greetings = new HashMap<String, Integer>();
	HashMap<String, Integer> farewells = new HashMap<String, Integer>();

	BotResponses responses;

	public BotCommandParser( IRCBot bot, IRCCommands commands ){
		
		this.botnick = bot.getConfigs().getBotnick();
		this.commands = commands;

		greetingChans = bot.getConfigs().getGreetingChans();
		farewellChans = bot.getConfigs().getFarewellChans();
		
		loadGreetings();
		loadFarewells();
		responses = new BotResponses();
		this.outboundMsgQ = bot.getOutboundMsgQ();
		
		this.timedListeners = bot.getTimedListeners();
		this.eventListeners = bot.getEventListeners();
		
		admin = bot.getConfigs().getAdmin();
		
		botCommands = new BotCommands(admin, botnick);
		loadBotCommands();
	}
	
	private void loadBotCommands() {
		botCommands.put("HELP", new HelpCommand(botCommands, timedListeners, eventListeners, outboundMsgQ));
		botCommands.put("LIST", new ListCommand(botCommands, timedListeners, eventListeners, outboundMsgQ));
		botCommands.put("TRADE", new TradeCommand(botCommands, timedListeners, eventListeners, outboundMsgQ));
		botCommands.put("JOIN", new JoinCommand(botCommands, timedListeners, eventListeners, outboundMsgQ));
		botCommands.put("FLIRT", new FlirtCommand(botCommands, timedListeners, eventListeners, outboundMsgQ));
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
			String response = responses.getBotGreeting(msg.getFromNick());
			String responseMsg = commands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
			
		}else if( isValidFairwellChan(msg.getArgs()[0]) && isFarewell(msg.getTrailing()) ){
			//	send response to greeting
			String response = responses.getBotFarewell(msg.getFromNick());
			String responseMsg = commands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
		}else if( isBotCommand(msg.getTrailing()) ){
			handleBototCommand(msg);
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
			String response = responses.getBotGreeting(msg.getFromNick());
			String responseMsg = commands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
		}else if( isFarewell(msg.getTrailing()) ){
			//	send response to greeting
			String response = responses.getBotFarewell(msg.getFromNick());
			String responseMsg = commands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
		}else if( isBotCommand(msg.getTrailing()) ){
			handleBototCommand(msg);
		}
	}

	public boolean isBotCommand(String trailing) {
		if( trailing.startsWith("!"+botnick.charAt(0)) ){ 
			return true; 
		}else if( trailing.contains(botnick) ){
			return true;
		}
		return false;
	}

	//	Cleans the command indicator before passing the message
	public void handleBototCommand(IRCMsg msg) {
		msg.setTrailing( cleanBotCommand(msg.getTrailing()) );
		botCommands.iterateAcrossCommands(msg);
	}

	public String cleanBotCommand(String trailing) {
		return trailing.substring(trailing.indexOf(" ")+1);
	}

}
