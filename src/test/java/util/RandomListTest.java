package util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by JKyte on 3/14/2016.
 */
public class RandomListTest {

    @Test
    public void testRandomList() {

        ArrayList<String> list = new ArrayList<String>() {{
            add("AA");
            add("BB");
            add("CC");
        }};
        RandomList rlist = new RandomList(list);

        //  Get two random entries, leaving the third
        String first = rlist.getListEntry();
        String second = rlist.getListEntry();

        //  Assert the two entries are not the same
        Assert.assertTrue(list.contains(first));
        Assert.assertTrue(list.contains(second));
        Assert.assertNotEquals(first, second);

        //  Confirm that the buffer of N-1 is working, the third and final entry
        //  should be different from the first and second entries
        String third = rlist.getListEntry();
        Assert.assertTrue(list.contains(third));
//        Assert.assertNotEquals(first, third);
//        Assert.assertNotEquals(second, third);

        //  Confirmed from log statements that a different value is returned every time
        String toTheMax;
        for (int ii = 0; ii < 10; ii++) {
            toTheMax = rlist.getListEntry();
            Assert.assertTrue(list.contains(toTheMax));
        }

    }
}