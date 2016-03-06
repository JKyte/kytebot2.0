package commands;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import listeners.Listeners;
import msg.IRCMsg;

public class FlirtCommand extends AdminCommand {
	
	private String target = null;
	private String nickToBeFlirted = null;
	private ArrayList<String> flirts = null;
	
	private Random rand;

	public FlirtCommand(BotCommands botCommands, Listeners timedTriggers,
			Listeners eventTriggers, ConcurrentLinkedQueue<String> outboundMsgQ) {
		super(botCommands, timedTriggers, eventTriggers, outboundMsgQ);
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
		if( target.equals(botCommands.getBotnick()) ){
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
		description = new ArrayList<String>();
		description.add( "FLIRT - usage: ![botnick] FLIRT [nick]");
		description.add( "  - orders the bot to flirt with somebody.");	
	}
	
	private String getRandomFlirt() {
		return flirts.get( rand.nextInt(flirts.size())).replaceAll("NICKTOBEFLIRTED", nickToBeFlirted);
	}

	private void loadFlirts() {
		flirts = new ArrayList<String>();
		
		flirts.add(this.botnick + " snuggles NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " nuzzles NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " seduces NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " gazes at NICKTOBEFLIRTED longingly" );
		flirts.add(this.botnick + " daydreams about NICKTOBEFLIRTED and tries not to lose himself" );
		flirts.add(this.botnick + " fantasizes about NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " scoots closer to NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " looks NICKTOBEFLIRTED up and down and likes what he sees" );
		flirts.add(this.botnick + " tries hard not to think naughty thoughts about NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " wonders what life would be like with NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " wants to give NICKTOBEFLIRTED a backrub" );
		flirts.add(this.botnick + " pours NICKTOBEFLIRTED a glass of red wine" );
		flirts.add(this.botnick + " poses seductively for NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " wants NICKTOBEFLIRTED to jump in bed. For a pillow fight." );
		flirts.add(this.botnick + " hugs NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " thinks he and NICKTOBEFLIRTED should be together" );
		flirts.add(this.botnick + " writes a Taylor Swift song about himself and NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " writes a love poem about NICKTOBEFLIRTED" );
		flirts.add(this.botnick + " thinks NICKTOBEFLIRTED is sexy" );
		flirts.add(this.botnick + "'s thoughts aren't PG-rated right now" );
	}
}
