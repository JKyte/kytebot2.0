package parsers;

import msg.IRCMsg;

import org.junit.Assert;
import org.junit.Test;

import botconfigs.IRCBot;
import core.BotConstants;

//	Keeping this class around for IRCMsgHandler unit tests
public class IRCMsgParserTest {
/*
	private final String CHANNEL = "#channelname";
	private final String BOTNICK = "botnick";
	
	@Test
	public void testParse_ChannelMsg(){
		String channelMsg = ":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :hello botnick, channel msg";
		
		IRCBot mockBot = new IRCBot(BotConstants.TEST_DEFAULT);
		IRCMsgParser parser = new IRCMsgParser(mockBot);
		parser.parseRawMsg(channelMsg);
		Assert.assertNotNull( parser.internalMsgQ.peek() );
		
		IRCMsg result = parser.internalMsgQ.poll();
		Assert.assertEquals( channelMsg, result.getOriginalMsg() );
		Assert.assertEquals( "User!User@server-ABCD1234.areacode.network.isp.net", result.getPrefix() );
		Assert.assertEquals( "PRIVMSG", result.getCommand() );
		Assert.assertEquals( 1, result.getArgs().length);
		Assert.assertEquals( CHANNEL, result.getArgs()[0] );
		Assert.assertEquals( "hello botnick, channel msg", result.getTrailing() );
	}
	
	@Test
	public void testParse_PrivateMsg(){
		String channelMsg = ":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG botnick :hello botnick, private msg";
		
		IRCBot mockBot = new IRCBot(BotConstants.TEST_DEFAULT);
		IRCMsgParser parser = new IRCMsgParser(mockBot);
		parser.parseRawMsg(channelMsg);
		Assert.assertNotNull( parser.internalMsgQ.peek() );
		
		IRCMsg result = parser.internalMsgQ.poll();
		Assert.assertEquals( channelMsg, result.getOriginalMsg() );
		Assert.assertEquals( "User!User@server-ABCD1234.areacode.network.isp.net", result.getPrefix() );
		Assert.assertEquals( "PRIVMSG", result.getCommand() );
		Assert.assertEquals( 1, result.getArgs().length);
		Assert.assertEquals( BOTNICK, result.getArgs()[0] );
		Assert.assertEquals( "hello botnick, private msg", result.getTrailing() );
	}
	
	@Test
	public void testParse_ChannelMsg2(){
		String channelMsg = ":User!User@server-ABCD1234.areacode.network.isp.net PRIVMSG #channelname :here's a link, http://www.website.com/some-random-page/";
		
		IRCBot mockBot = new IRCBot(BotConstants.TEST_DEFAULT);
		IRCMsgParser parser = new IRCMsgParser(mockBot);
		parser.parseRawMsg(channelMsg);
		Assert.assertNotNull( parser.internalMsgQ.peek() );
		
		IRCMsg result = parser.internalMsgQ.poll();
		Assert.assertEquals( channelMsg, result.getOriginalMsg() );
		Assert.assertEquals( "User!User@server-ABCD1234.areacode.network.isp.net", result.getPrefix() );
		Assert.assertEquals( "PRIVMSG", result.getCommand() );
		Assert.assertEquals( 1, result.getArgs().length);
		Assert.assertEquals( CHANNEL, result.getArgs()[0] );
		Assert.assertEquals( "here's a link, http://www.website.com/some-random-page/", result.getTrailing() );
	}
	
	@Test
	public void testParse_Join(){
		String channelMsg = ":User!User@server-ABCD1234.areacode.network.isp.net JOIN #channelname";
		
		IRCBot mockBot = new IRCBot(BotConstants.TEST_DEFAULT);
		IRCMsgParser parser = new IRCMsgParser(mockBot);
		parser.parseRawMsg(channelMsg);
		Assert.assertNotNull( parser.internalMsgQ.peek() );
		
		IRCMsg result = parser.internalMsgQ.poll();
		Assert.assertEquals( channelMsg, result.getOriginalMsg() );
		Assert.assertEquals( "User!User@server-ABCD1234.areacode.network.isp.net", result.getPrefix() );
		Assert.assertEquals( "JOIN", result.getCommand() );
		Assert.assertEquals( 1, result.getArgs().length );
		Assert.assertEquals( CHANNEL, result.getArgs()[0] );
		Assert.assertEquals( null, result.getTrailing() );
	}
	
	@Test
	public void testParse_userWithIPv6(){
		String channelMsg = ":User!uid123456@ABCD123:1234ABCD:ABCD1234:IP PRIVMSG #channelname :Look! A UUID";
		
		IRCBot mockBot = new IRCBot(BotConstants.TEST_DEFAULT);
		IRCMsgParser parser = new IRCMsgParser(mockBot);
		parser.parseRawMsg(channelMsg);
		Assert.assertNotNull( parser.internalMsgQ.peek() );
		
		IRCMsg result = parser.internalMsgQ.poll();
		Assert.assertEquals( channelMsg, result.getOriginalMsg() );
		Assert.assertEquals( "User!uid123456@ABCD123:1234ABCD:ABCD1234:IP", result.getPrefix() );
		Assert.assertEquals( "PRIVMSG", result.getCommand() );
		Assert.assertEquals( 1, result.getArgs().length);
		Assert.assertEquals( CHANNEL, result.getArgs()[0] );
		Assert.assertEquals( "Look! A UUID", result.getTrailing() );
	}
	
	@Test
	public void testParse_serverCode(){
		String channelMsg = ":server.name.net 451 null :You have not registered";
		
		IRCBot mockBot = new IRCBot(BotConstants.TEST_DEFAULT);
		IRCMsgParser parser = new IRCMsgParser(mockBot);
		parser.parseRawMsg(channelMsg);
		Assert.assertNotNull( parser.internalMsgQ.peek() );
		
		IRCMsg result = parser.internalMsgQ.poll();
		Assert.assertEquals( channelMsg, result.getOriginalMsg() );
		Assert.assertEquals( "server.name.net", result.getPrefix() );
		Assert.assertEquals( "451", result.getCommand() );
		Assert.assertEquals( 1, result.getArgs().length);
		Assert.assertEquals( "null", result.getArgs()[0] );
		Assert.assertEquals( "You have not registered", result.getTrailing() );
	}	*/
}
