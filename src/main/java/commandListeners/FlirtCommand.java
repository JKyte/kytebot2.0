package commandListeners;

import botconfigs.IRCBot;
import msg.IRCMsg;
import util.RandomList;

import java.util.ArrayList;
import java.util.Random;

public class FlirtCommand extends AdminCommand {
	
	private String target = null;
	private String nickToBeFlirted = null;
    private RandomList flirts = null;

    private Random rand;

    public FlirtCommand(IRCBot ircbot) {
        super(ircbot);
        flirts = new RandomList(loadFlirts());
        rand = new Random();
    }

	@Override
	public boolean listen(IRCMsg msg){
		//	Ensure an admin is making this call
		if( !super.listen(msg) ){
			return false;
		}
		
		setTarget(msg);

		String[] chunks = msg.getTrailing().split(" ");
		if( chunks.length >= 2 && chunks[0].equalsIgnoreCase("FLIRT") ){
			nickToBeFlirted = chunks[chunks.length-1];
			return true;
		}else{
			return false;
		}
	}
	
	private void setTarget(IRCMsg msg) {
		target = msg.getArgs()[0];
		if( target.equals(ircbot.getConfigs().getBotnick()) ){
			target = msg.getFromNick();
		}
	}

	@Override
	public void doAction() {
		outboundMsgQ.add( ircCommands.action(target, getRandomFlirt()) );
		target = null;
	}

	@Override
	public void loadCommandDescription() {
        description = new ArrayList<>();
        description.add( "FLIRT - usage: ![botnick] FLIRT [nick]");
		description.add( "  - orders the bot to flirt with somebody.");	
	}
	
	private String getRandomFlirt() {
        String flirt = flirts.getListEntry();
        return flirt.replaceAll("NICKTOBEFLIRTED", nickToBeFlirted);
    }

    private ArrayList<String> loadFlirts() {
        ArrayList<String> flirtList = new ArrayList<>();
        flirtList.add("snuggles NICKTOBEFLIRTED");
        flirtList.add("nuzzles NICKTOBEFLIRTED");
        flirtList.add("seduces NICKTOBEFLIRTED");
        flirtList.add("gazes at NICKTOBEFLIRTED longingly");
        flirtList.add("daydreams about NICKTOBEFLIRTED and tries not to lose himself");
        flirtList.add("fantasizes about NICKTOBEFLIRTED");
        flirtList.add("scoots closer to NICKTOBEFLIRTED");
        flirtList.add("looks NICKTOBEFLIRTED up and down and likes what he sees");
        flirtList.add("tries hard not to think naughty thoughts about NICKTOBEFLIRTED");
        flirtList.add("wonders what life would be like with NICKTOBEFLIRTED");
        flirtList.add("wants to give NICKTOBEFLIRTED a backrub");
        flirtList.add("pours NICKTOBEFLIRTED a glass of red wine");
        flirtList.add("poses seductively for NICKTOBEFLIRTED");
        flirtList.add("wants NICKTOBEFLIRTED to jump in bed. For a pillow fight.");
        flirtList.add("hugs NICKTOBEFLIRTED");
        flirtList.add("thinks he and NICKTOBEFLIRTED should be together");
        flirtList.add("writes a Taylor Swift song about himself and NICKTOBEFLIRTED");
        flirtList.add("writes a love poem about NICKTOBEFLIRTED");
        flirtList.add("thinks NICKTOBEFLIRTED is sexy");
        flirtList.add("'s thoughts aren't PG-rated right now");
        return flirtList;
    }
}
