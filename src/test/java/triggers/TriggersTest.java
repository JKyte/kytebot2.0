package triggers;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TriggersTest {
	
	static Triggers triggers;
	
	@BeforeClass
	public static void setup(){
		triggers = new Triggers();
	}
	
	@Test
	public void testAddTimerTrigger(){
		
		String timerKey = "TimerForTests";
		TimerTrigger timer = new TimerTrigger(null, null, null, null);
		
	//	Assert.assertTrue(triggers.put(timerKey, timer));
	//	Assert.assertNotNull(triggers.get(timerKey));
		triggers.iterateAcrossTriggers(null);
	}

	@Test
	public void testTimeToIterateAcrossOneTrigger(){
		String timerKey = "TimerForTests";
		TimerTrigger timer = new TimerTrigger(null, null, null, null);
		Triggers triggers = new Triggers();
		triggers.put(timerKey, timer);		
		
		long start = System.currentTimeMillis();
		triggers.iterateAcrossTriggers(null);
		System.out.println("1: " + (System.currentTimeMillis()-start) );
	}
	
	@Test
	public void testTimeToIterateAcrossOneThousandTriggers(){
		String timerKey = "";
		TimerTrigger timer = new TimerTrigger(null, null, null, null);
		Triggers triggers = new Triggers();
		
		for( int ii = 0; ii < 100000; ii++ ){
			timerKey = ""+ii;
			triggers.put(timerKey, timer);			
		}
		
		long start = System.currentTimeMillis();
		triggers.iterateAcrossTriggers(null);
		System.out.println("1: " + (System.currentTimeMillis()-start) );
	}
}
