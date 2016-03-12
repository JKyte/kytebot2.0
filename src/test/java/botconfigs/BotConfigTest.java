package botconfigs;

import core.BotConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

public class BotConfigTest {

	@Test
	public void testBotConfigsAgainstDefaultProperties(){
		String configFilePath = BotConstants.TEST_DEFAULT;
		BotConfigs configs = BotConfigFactory.createBotConfigs(configFilePath);
		
		Assert.assertNotNull( configs );
		Assert.assertTrue( defaultConfigsAreValid(configs) );
	}
	
	private boolean defaultConfigsAreValid(BotConfigs configs) {
		//	Assert Core configs
		Assert.assertEquals("botnick", configs.getBotnick() );
		Assert.assertEquals("passwd", configs.getBotpasswd() );

		Assert.assertEquals(120000, configs.getHeartbeat() );

		//	Assert IRC Connection Configs
		Assert.assertEquals(6667, configs.getIrcport() );
		Assert.assertEquals("irc.server.net", configs.getIrcserver() );

		//	Assert Starting channels and ajoins
		Assert.assertEquals("#startchan", configs.getStartChan() );
		Assert.assertEquals(true, assertAjoins(configs.getAjoins()) );

		//	Assert Security Configurations
		Assert.assertEquals("adminnick", configs.getAdmin() );
		Assert.assertEquals(true, assertTrustedUsers(configs.getTrustedUsers()) );

		//	Assert Action Configurations
		Assert.assertEquals(true, assertGreetingChannels(configs.getGreetingChans()) );
		Assert.assertEquals(true, assertFarewellChannels(configs.getFarewellChans()) );
		Assert.assertEquals(true, assertStoryChannels(configs.getStoryChans()) );
		return true;
	}

	private boolean assertAjoins( ArrayList<String> ajoins){
        return ajoins.contains("#chan1") &&
                ajoins.contains("#chan2") &&
                ajoins.contains("#chan3");
    }

	private boolean assertTrustedUsers( HashSet<String> trustedUsers ){
        return trustedUsers.contains("trusteduser1") &&
                trustedUsers.contains("trusteduser2");
    }

	
	private boolean assertStoryChannels( HashSet<String> storyChans ){
        return storyChans.contains("#chan1") &&
                storyChans.contains("#chan4");
    }

	private boolean assertGreetingChannels( HashSet<String> greetChans ){
        return greetChans.contains("#chan1") &&
                greetChans.contains("#chan2") &&
                greetChans.contains("#chan3");
    }

	private boolean assertFarewellChannels( HashSet<String> farewellChans ){
        return farewellChans.contains("#chan1") &&
                farewellChans.contains("#chan2") &&
                farewellChans.contains("#chan3");
    }
}
