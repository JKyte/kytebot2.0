package botconfigs;

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


    public String chanservRegister(String channel, String password, String description) {
        return chanservRegister(channel, password) + description;
    }

    public String chanservRegister(String channel, String password) {
        return "chanserv register " + channel + " " + password;
    }


    //  TODO -- http://irchelp.org/irchelp/changuide.html



    public String joinHomeChannel() {
        return "join " + homeChannel;
	}
	
	public String joinChannel( String channel ){
		return "join " + channel;
	}

    public String setNick(String nick) {
        return "nick " + nick;
    }

    public String userIdent(String nick) {
        return "USER " + nick + " 0 * :" + nick;
    }


    /*
        NICKSERV commands
     */

	public String nickservIdentify(){
		return "nickserv identify " + password;
	}

    public String nickservInfo(String targetNick) {
        return "nickserv info " + targetNick;
    }

    /*
        Other commands
     */

    public String action( String target, String action ){
		return "PRIVMSG " + target + " :\001ACTION " + action + "\001";
	}
	
	public String privmsg( String target, String message ){
		return "PRIVMSG " + target + " :" + message;
	}
}
