package msg;

public class IRCMsgFactory {

	public static IRCMsg createIRCMsg( String rawMsg ){
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
			trailing = rawMsg.substring(rawMsg.indexOf(":")+1).trim();
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
}
