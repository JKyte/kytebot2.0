package msg;

import botconfigs.BotConfigs;

/**
 * 
 * @author JKyte
 * 
 * This class takes an IRC message and determines if it is from the Admin or
 * from a trusted user. 
 *
 */
public class IRCMessageDecorator {
	
	public static IRCMsg decorateMessage( BotConfigs configs, IRCMsg msg){
	//	System.out.println("DECORATOR: " + msg.getOriginalMsg() );
		String fromNick = extractNickFromPrefix( msg.getPrefix() );
		if( null == fromNick ){
			return msg;	//	Do nothing
		}else{
			msg.setFromNick(fromNick);
		}

        if (configs.getAdmin().equals(fromNick)) {
            msg.setFromAdmin(true);
		}else if( configs.getTrustedUsers().contains( fromNick ) ){
			msg.setFromTrustedUser(true);
		}
		
		return msg;
	}

	/**
	 * 
	 * @returns a NICK from the prefix
	 */
	public static String extractNickFromPrefix( String prefix ){
		if( prefix.indexOf("!") == -1 ){
			return null;
		}
		return prefix.substring(0,prefix.indexOf("!"));
	}
}
