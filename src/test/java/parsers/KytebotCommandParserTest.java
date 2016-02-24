package parsers;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import core.BotConstants;
import parsers.KytebotCommandParser;
import triggers.Triggers;
import botconfigs.BotConfigFactory;
import botconfigs.BotConfigs;
import botconfigs.IRCCommands;

public class KytebotCommandParserTest {
	
	private static BotConfigs configs;
	
	@BeforeClass
	public static void setup(){
		configs = BotConfigFactory.createBotConfigs(BotConstants.TEST_DEFAULT);
	}
	
	@Test
	public void testGetGreetingScore(){
		
		String botnick = "botnick";
		IRCCommands commands = new IRCCommands(configs);
		ConcurrentLinkedQueue<String> outQ = new ConcurrentLinkedQueue<String>();
		Triggers timedTriggers = new Triggers();
		Triggers eventTriggers = new Triggers();
		
		KytebotCommandParser kcp = new KytebotCommandParser(configs, commands, outQ, timedTriggers, eventTriggers, botnick);
		
		Assert.assertEquals(2, kcp.getGreetingScore("HELLO"));
		Assert.assertEquals(2, kcp.getGreetingScore("HI"));
		Assert.assertEquals(1, kcp.getGreetingScore("GOOD"));
		Assert.assertEquals(2, kcp.getGreetingScore("MORNING"));
		Assert.assertEquals(2, kcp.getGreetingScore("EVENING"));
	}

	@Test
	public void testIsGreeting(){
		//	TODO -- case sensitivity matters for nicks
		String botnick = "botnick";
		IRCCommands commands = new IRCCommands(configs);
		ConcurrentLinkedQueue<String> outQ = new ConcurrentLinkedQueue<String>();
		Triggers timedTriggers = new Triggers();
		Triggers eventTriggers = new Triggers();
		
		KytebotCommandParser kcp = new KytebotCommandParser(configs, commands, outQ, timedTriggers, eventTriggers, botnick);
		
		String[] validGreetings = {"hello botnick", "hi botnick", "good morning botnick",
				"good evening botnick", "morning botnick", "evening botnick"};
		
		String[] invalidGreetings = {"hello there", "hi there", "morn' botnick", "even' botnick", "botnick"};

		
		for( String greeting : validGreetings ){
			Assert.assertTrue(kcp.isGreeting(greeting));
		}
		
		for( String greeting : invalidGreetings ){
			Assert.assertFalse(kcp.isGreeting(greeting));
		}
	}
	
	@Test
	public void testCleanKytebotCommand_isKytebotCommand(){
		
		String botnick = "botnick";
		IRCCommands commands = new IRCCommands(configs);
		ConcurrentLinkedQueue<String> outQ = new ConcurrentLinkedQueue<String>();
		Triggers timedTriggers = new Triggers();
		Triggers eventTriggers = new Triggers();
		
		KytebotCommandParser kcp = new KytebotCommandParser(configs, commands, outQ, timedTriggers, eventTriggers, botnick);
		String kytebotCommand_v1 = "!botnick command";
		String kytebotCommand_v2 = "!b command";
		
		Assert.assertEquals("command", kcp.cleanKytebotCommand(kytebotCommand_v1));
		Assert.assertEquals("command", kcp.cleanKytebotCommand(kytebotCommand_v2));
		
		Assert.assertEquals(true, kcp.isKytebotCommand(kytebotCommand_v1));
		Assert.assertEquals(true, kcp.isKytebotCommand(kytebotCommand_v2));
		Assert.assertFalse(kcp.isKytebotCommand("random string"));
	}
}
