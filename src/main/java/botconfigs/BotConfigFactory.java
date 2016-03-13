package botconfigs;

import core.BotConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

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
    public static BotConfigs createBotConfigs(boolean useProductionAsDefault) {
        if (useProductionAsDefault) {
            return createBotConfigs(BotConstants.PRODUCTION_DEFAULT);
        } else {
            return createBotConfigs(BotConstants.TEST_DEFAULT);
        }

	}

    private static BotConfigs createBotConfigs(String filePath) {

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

        configs.setHeadless(getPropertyAsBoolean("headless"));

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
        HashSet<String> propertySet = new HashSet<>();
        for (String property : propertyArray) {
            propertySet.add(property);
		}
		return propertySet;
	}
	
	public static ArrayList<String> getPropertyAsList( String propertyKey ){
		String[] propertyArray = properties.getProperty(propertyKey).split(",");
        ArrayList<String> propertyList = new ArrayList<>();
        for( String property : propertyArray ){
			propertyList.add(property);
		}
		return propertyList;
	}

    /**
     * @param propertyKey
     * @returns FALSE if property key does not match any existing properties
     */
    public static boolean getPropertyAsBoolean(String propertyKey) {
        String propertyValue = properties.getProperty(propertyKey);
        if (propertyValue.toLowerCase().equals("false")) {
            return false;
        } else if (propertyValue.toLowerCase().equals("true")) {
            return true;
        } else {
            System.err.println("Failure to load property with key: " + propertyKey + " and value: " + propertyValue);
            return false;
        }
    }

}
