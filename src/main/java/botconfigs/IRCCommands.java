package botconfigs;

import core.BotConstants;

public class IRCCommands {

	private final String homeChannel;
	private final String password;
	
	public IRCCommands(){
		homeChannel = null;
		password = null;
	}
	
	public IRCCommands(BotConfigs configs){
		homeChannel = configs.getStartChan();
		password = configs.getBotpasswd();
	}

    /*
        CHANSERV commands
     */

    public static String quit(){
        return quit(BotConstants.DEFAULT_QUIT_REASON);
    }
	
    public static String quit( String reason ){
        return "QUIT " + reason;
    }

	public String chanservInvite(){
		return "chanserv invite " + homeChannel;
	}

	public String chanservInvite( String channel ){
		return "chanserv invite " + channel;
	}

    /*
        Some CHANSERV commands for creating channels and setting permissions
     */
    public String chanservInfo(String channel) {
        return "chanserv info " + channel;
    }


    //  TODO -- http://irchelp.org/irchelp/changuide.html

    public String chanservRegister(String channel, String password, String description) {
        return chanservRegister(channel, password) + description;
    }
	
    public String chanservRegister(String channel, String password) {
        return "chanserv register " + channel + " " + password;
    }

    public String joinHomeChannel() {
        return "join " + homeChannel;
	}

	public String joinChannel( String channel ){
		return "join " + channel;
	}


    /*
        NICKSERV commands
     */

    public String setNick(String nick) {
        return "nick " + nick;
    }

    public String userIdent(String nick) {
        return "USER " + nick + " 0 * :" + nick;
    }

    /*
        Other commands
     */

	public String nickservIdentify(){
		return "nickserv identify " + password;
	}
	
    public String nickservInfo(String targetNick) {
        return "nickserv info " + targetNick;
    }

    public String action( String target, String action ){
		return "PRIVMSG " + target + " :\001ACTION " + action + "\001";
	}

	public String privmsg( String target, String message ){
		return "PRIVMSG " + target + " :" + message;
	}
}
