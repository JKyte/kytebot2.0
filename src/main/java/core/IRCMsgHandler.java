package core;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import gui.UserInputBox;
import listenerFactories.BotCommandListenerFactory;
import listenerFactories.EventListenerFactory;
import listeners.Listeners;
import msg.IRCMessageDecorator;
import msg.IRCMsg;
import msg.IRCMsgFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author JKyte
 * 
 * This class will combine the functionality of two legacy classes by both parsing raw IRC messages
 * and interpreting them.
 *
 */
public class IRCMsgHandler implements Runnable {

    private final Logger log = LogManager.getLogger(getClass());
    public ConcurrentLinkedQueue<String> inboundMsgQ;
	public ConcurrentLinkedQueue<String> outboundMsgQ;
	private HashSet<String> serverResponseCodesToIgnore;
	private IRCBot bot;
	private String botnick;
	private String startchan;
	private Listeners interruptListeners;
	private Listeners eventListeners;
	private Listeners botCommandListeners;
	private IRCCommands ircCommands;

	public IRCMsgHandler( IRCBot bot ){
		
		this.bot = bot;
		
		botnick = bot.getConfigs().getBotnick();
		startchan = bot.getConfigs().getStartChan();
		
		this.inboundMsgQ = bot.getInboundMsgQ();
		this.outboundMsgQ = bot.getOutboundMsgQ();
		
		loadServerResponseCodesToIgnore();

        ircCommands = bot.getIrcCommands();

        this.setInterruptListeners(bot.getInterruptListeners());
        this.eventListeners = EventListenerFactory.createEventListeners(bot);
        this.botCommandListeners = BotCommandListenerFactory.createEventListeners(bot);

        if (bot.getConfigs().isHeadless()) {
            UserInputBox uib = new UserInputBox(outboundMsgQ);
            uib.stub();
        }
    }

	private void loadServerResponseCodesToIgnore() {
        serverResponseCodesToIgnore = new HashSet<>();
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

						handleMsg(rawMsg);

                    } catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public IRCMsg handleMsg(String rawMsg){
		if( !isPing(rawMsg) ){

            log.info("IN: " + rawMsg);

			IRCMsg msg = createAndDecorateMsg(rawMsg);
			
			//	Check to see if this is an expected msg
            getInterruptListeners().iterateAcrossListeners(msg);

            //	Check to see if the bot should perform an action, like greet someone
			eventListeners.iterateAcrossListeners(msg);
			
			//	Check to see if this is a bot command
			if( msgIsCommand(msg) ){
				
				msg = stripLeadingWordFromTrailing(msg);
				botCommandListeners.iterateAcrossListeners(msg);				
			}

			
			return interpretMsg(msg);
		}else{
			//	Wrap the raw PING msg, mostly for unit test purposes
			return IRCMsgFactory.createIRCMsg(rawMsg);
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
	
	public IRCMsg createAndDecorateMsg( String rawMsg ){
		IRCMsg msg = IRCMsgFactory.createIRCMsg(rawMsg);
		return IRCMessageDecorator.decorateMessage(bot.getConfigs(), msg);
	}
	
	/**
	 * Valid bot commands for nick Botnick are...
	 * 1. !Botnick cmd
	 * 2. !botnick cmd
	 * 
	 * Not yet implemented
	 * 1. !cmd
	 * 2. !B cmd
	 * 3. !b cmd
	 * 
	 * @param msg
	 * @return
	 */
	public boolean msgIsCommand(IRCMsg msg){
		
		if( null == msg.getTrailing() ){
			return false;
		}
		
		String tmpTrailing = msg.getTrailing().toLowerCase();
		
		if( tmpTrailing.startsWith("!"+botnick.charAt(0)) ){ 
			return true; 
		}else if( tmpTrailing.startsWith("!"+botnick) ){
			return true;
		}
		
		return false;
	}
	
	public IRCMsg stripLeadingWordFromTrailing( IRCMsg msg ){
		String lesser = msg.getTrailing().substring( msg.getTrailing().indexOf(" ")+1 );
		msg.setTrailing(lesser);
		return msg;
	}

	public IRCMsg interpretMsg( IRCMsg msg ){
		
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
            log.info("\nATTENTION MINION!!\nUnhandled command: " + msg.getCommand());
            log.info("Prefix: " + msg.getPrefix());
            log.info("Command: " + msg.getCommand());
            log.info("Args[0]: " + msg.getArgs()[0]);
            log.info("Trailing: " + msg.getTrailing());
        }
		
		return msg;
	}
	
	private void handleNick(IRCMsg msg) {
        log.info("NICK: " + msg.getFromNick() + " changed to " + msg.getTrailing());
    }

	private void handleQuit(IRCMsg msg) {
        log.info(msg.getFromNick() + " QUIT " + msg.getTrailing());
    }

	private void handlePrivMsg(IRCMsg msg) {
		if( msg.getArgs()[0].startsWith("#") ){
            log.info(msg.getOriginalMsg());
            handleChannelMsg(msg);
		}else if( msg.getArgs()[0].equals(botnick) ){
            log.info(msg.getOriginalMsg());
            handlePrivateMsg(msg);
		}else{
            log.error("Something went very wrong parsing: " + msg.getOriginalMsg());
        }
	}

	private void handlePrivateMsg(IRCMsg msg) {
		// TODO extend this
	}

	private void handleChannelMsg(IRCMsg msg) {
		//	TODO extend this
	}

	private void handleMode(IRCMsg msg) {
		if( !msg.getPrefix().equals(msg.getArgs()[0]) ){
			if( msg.getArgs().length == 3 ){
                log.info(msg.getArgs()[0] + " gave " + msg.getArgs()[1]
                        + " to " + msg.getArgs()[2]);
            }else{
                log.info(msg.getArgs()[0] + " gave " + msg.getArgs()[1]);
            }
		}
	}

	private void handleJoin(IRCMsg msg) {
        log.info(msg.getFromNick() + " joined " + msg.getTrailing());
    }

	private void handleInvite(IRCMsg msg) {
		// TODO nothing at the moment, will have to compare against list of valid channels
		//		before taking action. Currently the home channel requires an invite.
	}

	private void handleErrorInviteOnly(IRCMsg msg) {
		if( msg.getArgs()[1].equals(startchan) ){
			outboundMsgQ.add( ircCommands.chanservInvite() );
		}
	}

	public void handleNotice(IRCMsg msg) {
		
		if( msg.getTrailing().contains( "This nickname is registered and protected.") ){
            //	outboundMsgQ.add( ircCommands.nickservIdentify() );

        }else if( msg.getTrailing().contains("Password accepted -- you are now recognized.") ){
            outboundMsgQ.add(ircCommands.joinHomeChannel());

        }else if( msg.getTrailing().contains("Inviting "+botnick+" to channel "+startchan+".") ){

            log.info("Single-vector join");
            outboundMsgQ.add(ircCommands.joinHomeChannel());

        }else if( msg.getTrailing().contains("Inviting") &&
				msg.getTrailing().contains("to channel") && 
				msg.getTrailing().contains(startchan)){

            log.info("Multi-vector join");
            outboundMsgQ.add(ircCommands.joinHomeChannel());
        }
	}

    public Listeners getInterruptListeners() {
        return interruptListeners;
    }

    public void setInterruptListeners(Listeners interruptListeners) {
        this.interruptListeners = interruptListeners;
    }
}
