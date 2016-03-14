package commandListeners;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import msg.IRCMsg;

import java.util.ArrayList;
import java.util.Random;

public class FlirtCommand extends AdminCommand {
	
	private String target = null;
	private String nickToBeFlirted = null;
	private ArrayList<String> flirts = null;
	
	private Random rand;

	public FlirtCommand( IRCBot ircbot, IRCCommands ircCommands) {
		super(ircbot, ircCommands);
		loadFlirts();
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
		return flirts.get( rand.nextInt(flirts.size())).replaceAll("NICKTOBEFLIRTED", nickToBeFlirted);
	}

	private void loadFlirts() {
        flirts = new ArrayList<>();

        flirts.add("snuggles NICKTOBEFLIRTED");
        flirts.add("nuzzles NICKTOBEFLIRTED");
        flirts.add("seduces NICKTOBEFLIRTED");
        flirts.add("gazes at NICKTOBEFLIRTED longingly");
        flirts.add("daydreams about NICKTOBEFLIRTED and tries not to lose himself");
        flirts.add("fantasizes about NICKTOBEFLIRTED");
        flirts.add("scoots closer to NICKTOBEFLIRTED");
        flirts.add("looks NICKTOBEFLIRTED up and down and likes what he sees");
        flirts.add("tries hard not to think naughty thoughts about NICKTOBEFLIRTED");
        flirts.add("wonders what life would be like with NICKTOBEFLIRTED");
        flirts.add("wants to give NICKTOBEFLIRTED a backrub");
        flirts.add("pours NICKTOBEFLIRTED a glass of red wine");
        flirts.add("poses seductively for NICKTOBEFLIRTED");
        flirts.add("wants NICKTOBEFLIRTED to jump in bed. For a pillow fight.");
        flirts.add("hugs NICKTOBEFLIRTED");
        flirts.add("thinks he and NICKTOBEFLIRTED should be together");
        flirts.add("writes a Taylor Swift song about himself and NICKTOBEFLIRTED");
        flirts.add("writes a love poem about NICKTOBEFLIRTED");
        flirts.add("thinks NICKTOBEFLIRTED is sexy");
        flirts.add("'s thoughts aren't PG-rated right now");
    }
}
