package responses;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BotResponsesTest {
	
	static BotResponses responses;
	
	@BeforeClass
	public static void setup(){
		responses = new BotResponses();
	}
	
	@Test
	public void testGetRandomGreeting(){
		for( int ii = 0; ii < 25; ii++ ){
			Assert.assertNotNull(responses.getRandomGreeting());
		}
	}
	
	@Test
	public void testGetRandomFarewell(){
		for( int ii = 0; ii < 25; ii++ ){
			Assert.assertNotNull(responses.getRandomFarewell());
		}
	}
	
	@Test
	public void testGetJoinActions(){
		for( int ii = 0; ii < 25; ii++ ){
			String joinAction = responses.getRandomJoinAction();
			//System.out.println( joinAction );
			Assert.assertNotNull(joinAction);
		}
	}


}
