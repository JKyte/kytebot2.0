package master;

import core.BotConstants;
import botconfigs.IRCBot;

/**
 * 
 * @author JKyte
 * 
 * This is the master class for an IRC Bot framework
 *
 */
public class KickOffMaster {

	public static void main(String[] args){

		IRCBot bot = new IRCBot(BotConstants.PRODUCTION_DEFAULT);
		Thread t0 = new Thread(bot);
		t0.start();	
	}
}
