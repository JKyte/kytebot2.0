package parsers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import core.BotConstants;
import parsers.KytebotCommandParser;
import botconfigs.BotConfigFactory;
import botconfigs.BotConfigs;
import botconfigs.IRCBot;
import botconfigs.IRCCommands;

public class KytebotCommandParserTest {
	
	private static BotConfigs configs;
	
	@BeforeClass
	public static void setup(){
		configs = BotConfigFactory.createBotConfigs(BotConstants.TEST_DEFAULT);
	}
	
	@Test
	public void testGetGreetingScore(){
		
		IRCCommands commands = new IRCCommands(configs);
		IRCBot mockBot = new IRCBot(BotConstants.TEST_DEFAULT);
		KytebotCommandParser kcp = new KytebotCommandParser(mockBot, commands);
		
		Assert.assertEquals(2, kcp.getGreetingScore("HELLO"));
		Assert.assertEquals(2, kcp.getGreetingScore("HI"));
		Assert.assertEquals(1, kcp.getGreetingScore("GOOD"));
		Assert.assertEquals(2, kcp.getGreetingScore("MORNING"));
		Assert.assertEquals(2, kcp.getGreetingScore("EVENING"));
	}

	@Test
	public void testIsGreeting(){
		IRCCommands commands = new IRCCommands(configs);
		IRCBot mockBot = new IRCBot(BotConstants.TEST_DEFAULT);
		KytebotCommandParser kcp = new KytebotCommandParser(mockBot, commands);
		
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
		
		IRCCommands commands = new IRCCommands(configs);
		IRCBot mockBot = new IRCBot(BotConstants.TEST_DEFAULT);
		KytebotCommandParser kcp = new KytebotCommandParser(mockBot, commands);
		String kytebotCommand_v1 = "!botnick command";
		String kytebotCommand_v2 = "!b command";
		
		Assert.assertEquals("command", kcp.cleanKytebotCommand(kytebotCommand_v1));
		Assert.assertEquals("command", kcp.cleanKytebotCommand(kytebotCommand_v2));
		
		Assert.assertEquals(true, kcp.isKytebotCommand(kytebotCommand_v1));
		Assert.assertEquals(true, kcp.isKytebotCommand(kytebotCommand_v2));
		Assert.assertFalse(kcp.isKytebotCommand("random string"));
	}
}
