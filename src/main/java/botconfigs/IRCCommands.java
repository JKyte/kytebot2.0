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
	
	public String chanservInvite(){
		return "chanserv invite " + homeChannel;
	}
	
	public String chanservInvite( String channel ){
		return "chanserv invite " + channel;
	}
	
	public String joinChannel(){
		return "join " + homeChannel;
	}
	
	public String joinChannel( String channel ){
		return "join " + channel;
	}
	
	public String nickservIdentify(){
		return "nickserv identify " + password;
	}
	
	public String action( String target, String action ){
		return "PRIVMSG " + target + " :\001ACTION " + action + "\001";
	}
	
	public String privmsg( String target, String message ){
		return "PRIVMSG " + target + " :" + message;
	}
}
