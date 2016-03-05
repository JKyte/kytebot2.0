package core;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import listeners.JoinChannelListener;
import listeners.Listeners;
import msg.IRCMsg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import parsers.BotCommandParser;
import responses.BotResponses;
import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import gui.UserInputBox;

/**
 * 
 * @author JKyte
 * 
 * This class will combine the functionality of two legacy classes by both parsing raw IRC messages
 * and interpreting them.
 *
 */
public class IRCMsgHandler implements Runnable {

	public ConcurrentLinkedQueue<String> inboundMsgQ;
	public ConcurrentLinkedQueue<String> outboundMsgQ;
	public ConcurrentLinkedQueue<IRCMsg> internalMsgQ;
	
	private HashSet<String> serverResponseCodesToIgnore;
	
	private String botnick;
	private String startchan;

	private Listeners interruptListeners;
	
	//	generic and botCommand listeners unused for the moment
	private Listeners genericListeners;
	private Listeners botCommandListeners;
	
	private Listeners eventListeners;
	
	private IRCCommands commands;
	private BotCommandParser botCommandParser;

	private Logger log = LogManager.getLogger(IRCMsgHandler.class);
	
	public IRCMsgHandler( IRCBot bot ){
		
		botnick = bot.getConfigs().getBotnick();
		startchan = bot.getConfigs().getStartChan();
		
		this.inboundMsgQ = bot.getInboundMsgQ();
		this.outboundMsgQ = bot.getOutboundMsgQ();
		this.internalMsgQ = bot.getInternalMsgQ();
	
		this.interruptListeners = bot.getTimedListeners();
		this.eventListeners = bot.getEventListeners();
		
		loadEventListeners();
		loadServerResponseCodesToIgnore();
		
		commands = new IRCCommands( bot.getConfigs() );
		botCommandParser = new BotCommandParser( bot, commands );
		
		UserInputBox uib = new UserInputBox(outboundMsgQ);
	}
	
	private void loadEventListeners() {
		eventListeners.put("GREET_1", new JoinChannelListener(commands, interruptListeners, eventListeners, outboundMsgQ, "#kytebotlair", new BotResponses()));
		eventListeners.put("GREET_2", new JoinChannelListener(commands, interruptListeners, eventListeners, outboundMsgQ, "#whitewalkers", new BotResponses()));
		
	}

	private void loadServerResponseCodesToIgnore() {
		serverResponseCodesToIgnore = new HashSet<String>();
		serverResponseCodesToIgnore.add("372");
		
		serverResponseCodesToIgnore.add("001");
		serverResponseCodesToIgnore.add("002");
		serverResponseCodesToIgnore.add("003");
		serverResponseCodesToIgnore.add("004");
		serverResponseCodesToIgnore.add("005");
		serverResponseCodesToIgnore.add("251");
		serverResponseCodesToIgnore.add("252");
		serverResponseCodesToIgnore.add("253");
		serverResponseCodesToIgnore.add("254");
		serverResponseCodesToIgnore.add("255");
		serverResponseCodesToIgnore.add("256");
		serverResponseCodesToIgnore.add("265");	//	Current Local Users/Max Users
		serverResponseCodesToIgnore.add("266");	//	Current Global Users/Max Users
		
		serverResponseCodesToIgnore.add("332");	//	MOTD
		serverResponseCodesToIgnore.add("333");	//	
		serverResponseCodesToIgnore.add("353");	//	Chan owner
		serverResponseCodesToIgnore.add("366");	//	End of NAMES list
		
		serverResponseCodesToIgnore.add("375");	//	IRC MOTD
		serverResponseCodesToIgnore.add("376");	//	End of IRC MOTD
	}
	
	@Override
	public void run() {

		String rawMsg;
		try {	
			while ( true ){

				rawMsg = inboundMsgQ.poll();
				if( null != rawMsg ){
					try {
						
					//	long start = System.currentTimeMillis();
						handleMsg(rawMsg);
					//	updateParseStats( (System.currentTimeMillis() - start) );
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			//	log.error(msg, e);
		} finally {
			//	log.fatal("FATAL:" + msg.toString());
			//	log.fatal("FATAL: InputThread crashed.");
		}
	}
	
	public void handleMsg(String rawMsg){
		if( !isPing(rawMsg) ){
			IRCMsg msg = parseRawMsg(rawMsg);
			
			interruptListeners.iterateAcrossListeners(null);
			
			//	Check event-driven listeners up front
			eventListeners.iterateAcrossListeners(msg);
			
			interpretMsg(msg);
			
		}
	}
	
	public boolean isPing(String rawMsg){
		if( rawMsg.startsWith("PING") ){
			String pingResponse = rawMsg.replaceAll("PING", "PONG");
			outboundMsgQ.add(pingResponse);		
			return true;
		}
		return false;
	}

	public IRCMsg parseRawMsg( String rawMsg ){
		
		String copyOfRawMsg = rawMsg;
		String prefix = null;
		String command = null;
		String[] args = null;
		String trailing = null;
		
		if( rawMsg.charAt(0) == ':' ){
			rawMsg = rawMsg.substring(1);
			prefix = rawMsg.split(" ")[0];
			rawMsg = rawMsg.substring(rawMsg.indexOf(" ")+1);
		}
		
		if( rawMsg.indexOf(" :") != -1 ){
			trailing = rawMsg.substring(rawMsg.indexOf(":")+1);
			command = rawMsg.substring(0, rawMsg.indexOf(" "));
			
			rawMsg = rawMsg.substring(rawMsg.indexOf(" "), rawMsg.indexOf(":")).trim();
			args = rawMsg.split(" :")[0].split(" ");
		}else{
			command = rawMsg.substring(0, rawMsg.indexOf(" "));
			rawMsg = rawMsg.substring(rawMsg.indexOf(" ")).trim();
			args = rawMsg.split(" ");
		}
		
		IRCMsg msg = new IRCMsg(copyOfRawMsg, prefix, command, args, trailing);
		return msg;
	}
	
public void interpretMsg( IRCMsg msg ){
		
		//	Check event-driven listeners up front
		eventListeners.iterateAcrossListeners(msg);
		
		if( msg.getCommand().equals("PRIVMSG") ){
			handlePrivMsg(msg);
			
		}else if( msg.getCommand().equals("JOIN") ){
			handleJoin(msg);
			
		}else if( msg.getCommand().equals("MODE") ){
			handleMode(msg);
			
		}else if( msg.getCommand().equals("QUIT") ){
			handleQuit(msg);
			
		}else if( msg.getCommand().equals("NOTICE") ){
			handleNotice(msg);
			
		}else if( msg.getCommand().equals("NICK") ){
			handleNick(msg);
			
		}else if( msg.getCommand().equals("INVITE") ){
			handleInvite(msg);
			
		}else if( msg.getCommand().equals("473") ){
			handleErrorInviteOnly(msg);
			
		}else if( serverResponseCodesToIgnore.contains(msg.getCommand()) ){
			//	Don't do anything on purpose
			
		}else{
			System.out.println("\nATTENTION MINION!!\nUnhandled command: " + msg.getCommand());
			System.out.println("Prefix: " + msg.getPrefix());
			System.out.println("Command: " + msg.getCommand());
			System.out.println("Args[0]: " + msg.getArgs()[0]);
			System.out.println("Trailing: " + msg.getTrailing());
		}
	}
	
	private void handleNick(IRCMsg msg) {
		System.out.println( "NICK: " + msg.getNickFromPrefix() + " changed to " + msg.getTrailing());
	}

	private void handleQuit(IRCMsg msg) {
		System.out.println( msg.getNickFromPrefix() + " QUIT " + msg.getTrailing() );
	}

	private void handlePrivMsg(IRCMsg msg) {
		if( msg.getArgs()[0].startsWith("#") ){
			System.out.println( msg.toPrivString() );
			handleChannelMsg(msg);
		}else if( msg.getArgs()[0].equals(botnick) ){
			System.out.println( msg.toPrivString() );
			handlePrivateMsg(msg);
		}else{
			System.err.println("Something went very wrong parsing: " + msg.getOriginalMsg());
		}
	}

	private void handlePrivateMsg(IRCMsg msg) {
		// TODO extend this
		if( botCommandParser.isBotCommand(msg.getTrailing()) ){
			botCommandParser.parsePrivateMsg(msg);
		}else{
		//	System.out.println("PRIVATE message");
		}
	}

	private void handleChannelMsg(IRCMsg msg) {
		//	TODO extend this
		if( botCommandParser.isBotCommand(msg.getTrailing()) ){
			botCommandParser.parseChannelMsg(msg);
		}else{
		//	System.out.println("CHANNEL message");	
		}
	}

	private void handleMode(IRCMsg msg) {
		if( !msg.getPrefix().equals(msg.getArgs()[0]) ){
			if( msg.getArgs().length == 3 ){
				System.out.println( msg.getArgs()[0] + " gave " + msg.getArgs()[1] 
						+ " to " + msg.getArgs()[2] );
			}else{
				System.out.println( msg.getArgs()[0] + " gave " + msg.getArgs()[1]);
			}
		}
	}

	private void handleJoin(IRCMsg msg) {
		System.out.println( msg.getNickFromPrefix() + " joined " + msg.getTrailing());
	}

	private void handleInvite(IRCMsg msg) {
		// TODO nothing at the moment, will have to compare against list of valid channels
		//		before taking action. Currently the home channel requires an invite.
	}

	private void handleErrorInviteOnly(IRCMsg msg) {
		if( msg.getArgs()[1].equals(startchan) ){
			outboundMsgQ.add( commands.chanservInvite() );
		}
	}

	public void handleNotice(IRCMsg msg) {
		
		if( msg.getTrailing().contains( "This nickname is registered and protected.") ){
			outboundMsgQ.add( commands.nickservIdentify() );
		
		}else if( msg.getTrailing().contains("Password accepted -- you are now recognized.") ){
			outboundMsgQ.add( commands.joinChannel() );
			
		}else if( msg.getTrailing().contains("Inviting "+botnick+" to channel "+startchan+".") ){

			System.out.println("Single-vector join");
			outboundMsgQ.add( commands.joinChannel() );
			
		}else if( msg.getTrailing().contains("Inviting") && 
				msg.getTrailing().contains("to channel") && 
				msg.getTrailing().contains(startchan)){
			
			System.out.println("Multi-vector join");
			outboundMsgQ.add( commands.joinChannel() );
		}
	}
}
