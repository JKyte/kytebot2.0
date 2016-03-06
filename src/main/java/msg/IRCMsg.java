package msg;

public class IRCMsg {

	private String originalMsg;
	private String prefix;
	private String command;
	private String[] args;
	private String trailing;
	
	//	These fields set by decorator
	private String fromNick;
	private boolean fromAdmin;
	private boolean fromTrustedUser;
	
	public IRCMsg(String originalMsg, String prefix, String command, String[] args, String trailing){
		this.originalMsg = originalMsg;
		this.prefix = prefix;
		this.command = command;
		this.args = args;
		this.setTrailing(trailing);
		
		fromAdmin = false;
		fromTrustedUser = true;
	}
	
	public String getOriginalMsg() {
		return originalMsg;
	}

	public void setOriginalMsg(String originalMsg) {
		this.originalMsg = originalMsg;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public String getTrailing() {
		return trailing;
	}

	public void setTrailing(String trailing) {
		this.trailing = trailing;
	}
	
	public String getFromNick() {
		return fromNick;
	}

	public void setFromNick(String fromNick) {
		this.fromNick = fromNick;
	}

	public boolean isFromAdmin() {
		return fromAdmin;
	}

	public void setFromAdmin(boolean fromAdmin) {
		this.fromAdmin = fromAdmin;
	}

	public boolean isFromTrustedUser() {
		return fromTrustedUser;
	}

	public void setFromTrustedUser(boolean fromTrustedUser) {
		this.fromTrustedUser = fromTrustedUser;
	}
}
