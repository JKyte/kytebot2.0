package msg;

public class IRCMsg {

	private String originalMsg;
	private String prefix;
	private String command;
	private String[] args;
	private String trailing;
	
	public IRCMsg(String originalMsg, String prefix, String command, String[] args, String trailing){
		this.originalMsg = originalMsg;
		this.prefix = prefix;
		this.command = command;
		this.args = args;
		this.setTrailing(trailing);
	}

	public String extractNameFromPrefix(){
		return prefix.substring(0,prefix.indexOf("!"));
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
	
	public String toPrivString(){
		return extractNameFromPrefix() + "\t" + args[0] + "\t" + trailing;
	}
}
