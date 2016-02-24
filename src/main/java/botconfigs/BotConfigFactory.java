package botconfigs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

import core.BotConstants;

/**
 * 
 * @author JKyte
 * 
 * Class that handles creating and returning a full BotConfig object
 *
 */
public class BotConfigFactory {
	
	private static Properties properties;

	/**
	 * One more layer of indirection never hurt anyone, right?
	 * @returns BotConfigs loaded with PRODUCTION defaults
	 */
	public static BotConfigs createBotConfigs(){
		return createBotConfigs(BotConstants.PRODUCTION_DEFAULT);
	}
		
	public static BotConfigs createBotConfigs( String filePath ){
		
		//	Setup the BotConfigs and the PropertyHandler
		BotConfigs configs = new BotConfigs();
		properties = PropertyHandler.readPropertyFile(filePath);
		
		//	Core configs
		configs.setBotnick( properties.getProperty("nick") );
		configs.setBotpasswd( properties.getProperty("passwd") );
		
		configs.setHeartbeat( getPropertyAsInteger("heartbeat") );
		
		//	IRC Connection Configs
		configs.setIrcport( getPropertyAsInteger("ircport") );
		configs.setIrcserver( properties.getProperty("ircserver") );
		
		//	Starting channels and ajoins
		configs.setStartChan( properties.getProperty("startchan") );
		configs.setAjoins( getPropertyAsList("ajoins") );
		
		//	Security Configurations
		configs.setAdmin( properties.getProperty("admin") );
		configs.setTrustedUsers( getPropertyAsHashSet("trustedusers") );
		
		//	Action configurations
		configs.setGreetingChans( getPropertyAsHashSet("greetingchans") );
		configs.setFarewellChans( getPropertyAsHashSet("farewellchans") );
		configs.setStoryChans( getPropertyAsHashSet("storychans") );
		
		
		return configs;
	}
	
	public static int getPropertyAsInteger(String propertyKey){
		return Integer.parseInt(properties.getProperty( propertyKey ));
	}
	
	public static HashSet<String> getPropertyAsHashSet( String propertyKey ){
		
		String[] propertyArray = properties.getProperty(propertyKey).split(",");
		HashSet<String> propertySet = new HashSet<String>();
		for( String property : propertyArray ){
			propertySet.add(property);
		}
		return propertySet;
	}
	
	public static ArrayList<String> getPropertyAsList( String propertyKey ){
		String[] propertyArray = properties.getProperty(propertyKey).split(",");
		ArrayList<String> propertyList = new ArrayList<String>();
		for( String property : propertyArray ){
			propertyList.add(property);
		}
		return propertyList;
	}
	
}
