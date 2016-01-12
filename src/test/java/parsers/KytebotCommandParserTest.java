package parsers;

import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Assert;
import org.junit.Test;

import parsers.KytebotCommandParser;
import triggers.Triggers;
import botconfigs.IRCCommands;
import botconfigs.PropertyHandler;

public class KytebotCommandParserTest {
	
	@Test
	public void testGetGreetingScore(){
		
		String botnick = "botnick";
		PropertyHandler propHandler = new PropertyHandler();
		Properties configs = PropertyHandler.readPropertyFile(propHandler.TEST_DEFAULT);
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
		PropertyHandler propHandler = new PropertyHandler();
		Properties configs = PropertyHandler.readPropertyFile(propHandler.TEST_DEFAULT);
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
		PropertyHandler propHandler = new PropertyHandler();
		Properties configs = PropertyHandler.readPropertyFile(propHandler.TEST_DEFAULT);
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
