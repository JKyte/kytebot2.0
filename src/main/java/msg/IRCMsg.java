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
		this.setOriginalMsg(originalMsg);
		this.setPrefix(prefix);
		this.setCommand(command);
		this.setArgs(args);
		this.setTrailing(trailing);
		
		fromAdmin = false;
		fromTrustedUser = false;
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

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Prefix: ").append(prefix).append("\n");
        sb.append("Command: ").append(command).append("\n");

        for( String arg : args ){
            sb.append("Arg[]: ").append(arg).append("\n");
        }

        sb.append("Trailing: ").append(trailing).append("\n");

        return sb.toString();
    }
}
