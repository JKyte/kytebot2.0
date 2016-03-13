package parsers;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import commandListeners.*;
import listeners.Listeners;
import msg.IRCMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.BotResponses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author JKyte
 * 
 * Legacy class sticking around waiting for it's usefulness to be refactored 
 * into other, newer, shinier classes.
 *
 */
public class BotCommandParser {

    private final Logger log = LogManager.getLogger(getClass());
    HashMap<String, Integer> greetings = new HashMap<>();
    HashMap<String, Integer> farewells = new HashMap<>();
    BotResponses responses;
    private String botnick;
	private IRCCommands ircCommands;
	private ConcurrentLinkedQueue<String> outboundMsgQ;
	private Listeners interruptListeners;
	private Listeners eventListeners;
	private IRCBot ircbot;
	private BotCommands botCommands;
	private String admin;
	private HashSet<String> greetingChans;
	private HashSet<String> farewellChans;

	public BotCommandParser( IRCBot ircbot, IRCCommands ircCommands ){
		
		this.ircbot = ircbot;
		
		this.botnick = ircbot.getConfigs().getBotnick();
		this.ircCommands = ircCommands;

		greetingChans = ircbot.getConfigs().getGreetingChans();
		farewellChans = ircbot.getConfigs().getFarewellChans();
		
		loadGreetings();
		loadFarewells();
		responses = new BotResponses();
		this.outboundMsgQ = ircbot.getOutboundMsgQ();
		
		this.interruptListeners = ircbot.getInterruptListeners();
		this.eventListeners = ircbot.getEventListeners();
		
		admin = ircbot.getConfigs().getAdmin();
		
		botCommands = new BotCommands(admin, botnick);
		loadBotCommands();
	}
	
	private void loadBotCommands() {
		botCommands.put("HELP", new HelpCommand(ircbot, ircCommands));
		botCommands.put("LIST", new ListCommand(ircbot, ircCommands));
		botCommands.put("TRADE", new TradeCommand(ircbot, ircCommands));
		botCommands.put("JOIN", new JoinCommand(ircbot, ircCommands));
		botCommands.put("FLIRT", new FlirtCommand(ircbot, ircCommands));
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
			String responseMsg = ircCommands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
			
		}else if( isValidFairwellChan(msg.getArgs()[0]) && isFarewell(msg.getTrailing()) ){
			//	send response to greeting
			String response = responses.getBotFarewell(msg.getFromNick());
			String responseMsg = ircCommands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
		}else if( isBotCommand(msg.getTrailing()) ){
			botCommands.iterateAcrossCommands(msg);
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
        return greetingScore >= 3;
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
        return farewellScore >= 3;
    }

	public int getFarewellScore(String key){
		if( farewells.containsKey(key) ){
			return farewells.get(key);
		}
		return 0;
	}

	//	TODO also prime candidate for pattern matching
	public void parsePrivateMsg( IRCMsg msg ){
        log.info("PRIVATE " + botnick + " command!");
        if( isGreeting(msg.getTrailing()) ){
			//	send response to greeting
			String response = responses.getBotGreeting(msg.getFromNick());
			String responseMsg = ircCommands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
		}else if( isFarewell(msg.getTrailing()) ){
			//	send response to greeting
			String response = responses.getBotFarewell(msg.getFromNick());
			String responseMsg = ircCommands.privmsg(msg.getArgs()[0], response);
			outboundMsgQ.add( responseMsg );
		}else if( isBotCommand(msg.getTrailing()) ){
			botCommands.iterateAcrossCommands(msg);
		}
	}

	//	This is too permissive, will likely be refactored out
	public boolean isBotCommand(String trailing) {
		if( trailing.startsWith("!"+botnick.charAt(0)) ){ 
			return true; 
		}else if( trailing.contains(botnick) ){
			return true;
		}
		return false;
	}

}
