package parsers;

import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import responses.KytebotResponses;
import triggers.JoinChannelTrigger;
import triggers.Triggers;
import botconfigs.IRCCommands;
import msg.IRCMsg;

/**
 * 
 * @author JKyte
 * 
 * This class takes IRCMsgs off the internalMsgQ, interprets them, and then
 * decides on a course of action.
 *
 */
public class IRCMsgInterpreter implements Runnable {
	
	private HashSet<String> serverResponseCodesToIgnore;
	
	private String botnick;
	private String startchan;
	
	private ConcurrentLinkedQueue<IRCMsg> internalMsgQ;
	private ConcurrentLinkedQueue<String> outboundMsgQ;

	private Triggers timedTriggers;
	private Triggers eventTriggers;
	
	private IRCCommands commands;
	private KytebotCommandParser kytebotParser;
	
	public IRCMsgInterpreter(Properties configs, ConcurrentLinkedQueue<IRCMsg> internalMsgQ, 
			ConcurrentLinkedQueue<String> outboundMsgQ,
			Triggers timedTriggers, Triggers eventTriggers){
		
		botnick = configs.getProperty("nick");
		startchan = configs.getProperty("startchan");
		
		this.internalMsgQ = internalMsgQ;
		this.outboundMsgQ = outboundMsgQ;
		
		this.timedTriggers = timedTriggers;
		this.eventTriggers = eventTriggers;
		
		loadEventTriggers();
		
		loadServerResponseCodesToIgnore();
		commands = new IRCCommands(configs);
		kytebotParser = new KytebotCommandParser(configs, commands, this.outboundMsgQ, 
				this.timedTriggers, this.eventTriggers, botnick);
	}

	private void loadEventTriggers() {
		eventTriggers.put("GREET_1", new JoinChannelTrigger(commands, timedTriggers, eventTriggers, outboundMsgQ, "#kytebotlair", new KytebotResponses()));
		eventTriggers.put("GREET_2", new JoinChannelTrigger(commands, timedTriggers, eventTriggers, outboundMsgQ, "#whitewalkers", new KytebotResponses()));
		
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

		IRCMsg msg;
		try {	
			while ( true ){
				
				timedTriggers.iterateAcrossTriggers(null);
				
				msg = internalMsgQ.poll();
				if( null != msg ){
					try {
					//	long start = System.currentTimeMillis();
						interpretMsg(msg);
					//	updateInterpretStats( (System.currentTimeMillis() - start) );

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

	public void interpretMsg( IRCMsg msg ){
		
		//	Check event-driven triggers up front
		eventTriggers.iterateAcrossTriggers(msg);
		
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
		if( kytebotParser.isKytebotCommand(msg.getTrailing()) ){
			kytebotParser.parsePrivateMsg(msg);
		}else{
			System.out.println("PRIVATE message");
		}
	}



	private void handleChannelMsg(IRCMsg msg) {
		//	TODO extend this
		if( kytebotParser.isKytebotCommand(msg.getTrailing()) ){
			kytebotParser.parseChannelMsg(msg);
		}else{
			System.out.println("CHANNEL message");	
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
