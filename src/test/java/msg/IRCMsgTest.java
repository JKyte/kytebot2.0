package msg;

import org.junit.Assert;
import org.junit.Test;

public class IRCMsgTest {
	
	@Test
	public void fullIRCMsgTest(){
		String originalMsg = "prefix command arg1 arg2 trailing";
		String prefix = "prefix";
		String command = "command";
		String[] args = new String[]{ "arg1", "arg2" };
		String trailing = "trailing";
		
		IRCMsg msg = new IRCMsg(originalMsg, prefix, command, args, trailing);
		
		Assert.assertEquals(originalMsg, msg.getOriginalMsg());
		Assert.assertEquals(prefix, msg.getPrefix());
		Assert.assertEquals(command, msg.getCommand());
		Assert.assertNotNull(msg.getArgs());
		Assert.assertEquals(2, msg.getArgs().length);
		Assert.assertEquals(trailing, msg.getTrailing());
		Assert.assertNull(msg.getFromNick());
		Assert.assertFalse(msg.isFromAdmin());
		Assert.assertFalse(msg.isFromTrustedUser());
		
		//	These variables would be set by a decorator based on msg contents
		msg.setFromNick("nick");
		Assert.assertEquals("nick", msg.getFromNick());
		
		msg.setFromTrustedUser(true);
		Assert.assertFalse(msg.isFromAdmin());
		Assert.assertTrue(msg.isFromTrustedUser());
		
		msg.setFromAdmin(true);
		Assert.assertTrue(msg.isFromAdmin());
		Assert.assertTrue(msg.isFromTrustedUser());
		
	}
}
